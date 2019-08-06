package cc.mrbird.febs.system.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fasterxml.jackson.databind.ObjectMapper;

import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.authentication.JWTToken;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.domain.ActiveUser;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.properties.FebsProperties;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.common.utils.AddressUtil;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.IPUtil;
import cc.mrbird.febs.common.utils.JsonUtils;
import cc.mrbird.febs.common.utils.MD5Util;
import cc.mrbird.febs.common.utils.StateResultUtil;
import cc.mrbird.febs.system.dao.LoginLogMapper;
import cc.mrbird.febs.system.domain.Admin;
import cc.mrbird.febs.system.domain.LoginLog;
import cc.mrbird.febs.system.domain.Permission;
import cc.mrbird.febs.system.domain.Role;
import cc.mrbird.febs.system.domain.Authentication;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.domain.UserConfig;
import cc.mrbird.febs.system.manager.UserManager;
import cc.mrbird.febs.system.service.LoginLogService;
import cc.mrbird.febs.system.service.PermissionService;
import cc.mrbird.febs.system.service.RoleService;
import cc.mrbird.febs.system.service.UserService;

@Validated
@RestController
public class LoginController {
	String userId = null;
	private static final String TOKEN = "Authentication";
	@Autowired
	private RoleService roleService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private UserManager userManager;
	@Autowired
	private UserService userService;
	@Autowired
	private LoginLogService loginLogService;
	@Autowired
	private LoginLogMapper loginLogMapper;
	@Autowired
	private FebsProperties properties;
	@Autowired
	private ObjectMapper mapper;
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@PostMapping("/login")
	// @Limit(key = "login", period = 60, count = 20, name = "登录接口", prefix =
	// "limit")
	public String login(@RequestBody Admin admin, HttpServletRequest request) throws Exception {

		String username = StringUtils.lowerCase(admin.getUsername());

		String password = MD5Util.encrypt(username, admin.getPassword());

		final String errorMessage = "用户名或密码错误";
		User user = this.userManager.getUser(username);

		if (user == null)
			return JsonUtils.objectToJson(StateResultUtil.build(403, "用户名不能为空"));
		if (!StringUtils.equals(user.getPassword(), password))
			return JsonUtils.objectToJson(StateResultUtil.build(402, errorMessage));

		// 更新用户登录时间
		this.userService.updateLoginTime(username);
		// 保存登录记录
		LoginLog loginLog = new LoginLog();
		loginLog.setUsername(username);
		this.loginLogService.saveLoginLog(loginLog);

		String token = FebsUtil.encryptToken(JWTUtil.sign(username, password));

		return JsonUtils.objectToJson(StateResultUtil.ok(new Authentication(token)));
	}

	@GetMapping("user/loginUser")
	public String loginUser(HttpServletRequest request) throws Exception {
		// String token = (String) SecurityUtils.getSubject().getPrincipal();
		logger.debug(userId);
		String token = request.getHeader(TOKEN);

		String username = JWTUtil.getUsername(FebsUtil.decryptToken(token));

		User user = this.userService.findByName(username);

		LocalDateTime expireTime = LocalDateTime.now().plusSeconds(properties.getShiro().getJwtTimeOut());
		String expireTimeStr = DateUtil.formatFullTime(expireTime);
		JWTToken jwtToken = new JWTToken(token, expireTimeStr);

		String Id = this.saveTokenToRedis(user, jwtToken, request);
		user.setId(Id);

		Map<String, Object> userInfo = this.generateUserInfo(jwtToken, user);
		return JsonUtils.objectToJson(StateResultUtil.ok(userInfo));
	}

	@GetMapping("index/{username}")
	public FebsResponse index(@NotBlank(message = "{required}") @PathVariable String username) {
		Map<String, Object> data = new HashMap<>();
		// 获取系统访问记录
		Long totalVisitCount = loginLogMapper.findTotalVisitCount();
		data.put("totalVisitCount", totalVisitCount);
		Long todayVisitCount = loginLogMapper.findTodayVisitCount();
		data.put("todayVisitCount", todayVisitCount);
		Long todayIp = loginLogMapper.findTodayIp();
		data.put("todayIp", todayIp);
		// 获取近期系统访问记录
		List<Map<String, Object>> lastSevenVisitCount = loginLogMapper.findLastSevenDaysVisitCount(null);
		data.put("lastSevenVisitCount", lastSevenVisitCount);
		User param = new User();
		param.setUsername(username);
		List<Map<String, Object>> lastSevenUserVisitCount = loginLogMapper.findLastSevenDaysVisitCount(param);
		data.put("lastSevenUserVisitCount", lastSevenUserVisitCount);
		return new FebsResponse().data(data);
	}

	@RequiresPermissions("user:online")
	@GetMapping("online")
	public FebsResponse userOnline(String username) throws Exception {
		String now = DateUtil.formatFullTime(LocalDateTime.now());
		Set<String> userOnlineStringSet = redisService.zrangeByScore(FebsConstant.ACTIVE_USERS_ZSET_PREFIX, now,
				"+inf");
		List<ActiveUser> activeUsers = new ArrayList<>();
		for (String userOnlineString : userOnlineStringSet) {
			ActiveUser activeUser = mapper.readValue(userOnlineString, ActiveUser.class);
			activeUser.setToken(null);
			if (StringUtils.isNotBlank(username)) {
				if (StringUtils.equalsIgnoreCase(username, activeUser.getUsername()))
					activeUsers.add(activeUser);
			} else {
				activeUsers.add(activeUser);
			}
		}
		return new FebsResponse().data(activeUsers);
	}

	@DeleteMapping("kickout/{id}")
	@RequiresPermissions("user:kickout")
	public boolean kickout(@NotBlank(message = "{required}") @PathVariable String id) throws Exception {
		String now = DateUtil.formatFullTime(LocalDateTime.now());
		Set<String> userOnlineStringSet = redisService.zrangeByScore(FebsConstant.ACTIVE_USERS_ZSET_PREFIX, now,
				"+inf");
		ActiveUser kickoutUser = null;
		String kickoutUserString = "";
		for (String userOnlineString : userOnlineStringSet) {
			ActiveUser activeUser = mapper.readValue(userOnlineString, ActiveUser.class);
			if (StringUtils.equals(activeUser.getId(), id)) {
				kickoutUser = activeUser;
				kickoutUserString = userOnlineString;
			}
		}
		if (kickoutUser != null && StringUtils.isNotBlank(kickoutUserString)) {
			// 删除 zset中的记录
			redisService.zrem(FebsConstant.ACTIVE_USERS_ZSET_PREFIX, kickoutUserString);
			// 删除对应的 token缓存
			redisService.del(FebsConstant.TOKEN_CACHE_PREFIX + kickoutUser.getToken() + "." + kickoutUser.getIp());
			return true;
		} else {
			return false;
		}
	}

	@GetMapping("logout")
	public String logout(@NotBlank(message = "{required}") String id) throws Exception {

		boolean kickout = this.kickout(id);
		if (kickout) {
			
			return JsonUtils.objectToJson(StateResultUtil.build(200, "退出成功"));
		}

		return JsonUtils.objectToJson(StateResultUtil.build(406, "退出失败"));
	}

	@PostMapping("regist")
	public void regist(@RequestBody Admin admin) throws Exception {
		this.userService.regist(admin.getUsername(), admin.getPassword());
	}

	private String saveTokenToRedis(User user, JWTToken token, HttpServletRequest request) throws Exception {
		String ip = IPUtil.getIpAddr(request);

		// 构建在线用户
		ActiveUser activeUser = new ActiveUser();
		activeUser.setUsername(user.getUsername());
		activeUser.setIp(ip);
		activeUser.setToken(token.getToken());

		// activeUser.setLoginAddress(AddressUtil.getCityInfo(ip));

		// zset 存储登录用户，score 为过期时间戳
		this.redisService.zadd(FebsConstant.ACTIVE_USERS_ZSET_PREFIX, Double.valueOf(token.getExipreAt()),
				mapper.writeValueAsString(activeUser));
		// redis 中存储这个加密 token，key = 前缀 + 加密 token + .ip
		this.redisService.set(FebsConstant.TOKEN_CACHE_PREFIX + token.getToken() + StringPool.DOT + ip,
				token.getToken(), properties.getShiro().getJwtTimeOut() * 1000);

		return activeUser.getId();
	}

	/**
	 * 生成前端需要的用户信息，包括： 1. token 2. Vue Router 3. 用户角色 4. 用户权限 5. 前端系统个性化配置信息
	 *
	 * @param token token
	 * @param user  用户信息
	 * @return UserInfo
	 */
	private Map<String, Object> generateUserInfo(JWTToken token, User user) {
		String username = user.getUsername();
		Map<String, Object> userInfo = new HashMap<>();
		// userInfo.put("token", token.getToken());
		userInfo.put("exipreTime", token.getExipreAt());

		// Set<String> roles = this.userManager.getUserRoles(username);
		Role role = roleService.findById(user.getRoleId());
		userInfo.put("role", role);

		// Set<String> permissions = permissionService.findUserPermissions(username);
		// userInfo.put("permissions",
		// JsonUtils.objectToJson(permissionService.findUserPermissions(username)));

		/*
		 * UserConfig userConfig =
		 * this.userManager.getUserConfig(String.valueOf(user.getUserId()));
		 * userInfo.put("config", userConfig);
		 */
		user.setPassword("it's a secret");
		userInfo.put("user", user);
		return userInfo;
	}
}
