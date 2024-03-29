package cc.mrbird.febs.system.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.authentication.JWTUtil;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.JsonUtils;
import cc.mrbird.febs.common.utils.MD5Util;
import cc.mrbird.febs.common.utils.StateResultUtil;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.domain.UserConfig;
import cc.mrbird.febs.system.domain.UserQuery;
import cc.mrbird.febs.system.service.UserConfigService;
import cc.mrbird.febs.system.service.UserService;
import freemarker.template.utility.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("user")
public class UserController extends BaseController {
	private static final String TOKEN = "Authentication";
	private String message;

	@Autowired
	private UserService userService;
	@Autowired
	private UserConfigService userConfigService;

	@GetMapping("check/{username}")
	public boolean checkUserName(@NotBlank(message = "{required}") String username) {
		return this.userService.findByName(username) == null;

	}

	@GetMapping("select")
	public String detail(String name) {

		if (StringUtils.isEmpty(name)) {
			return JsonUtils.objectToJson(StateResultUtil.build(405, "空值查不到", null));
		} else {
			return JsonUtils.objectToJson(StateResultUtil.ok(this.userService.findByName(name)));
		}
	}

	@GetMapping("select/deptId")
	public String staffDetail(String deptId, String pageSize, String pageNum) {
		QueryRequest queryRequest = new QueryRequest();
		queryRequest.setPageNum(Integer.valueOf(pageNum));
		queryRequest.setPageSize(Integer.valueOf(pageSize));
		if (StringUtils.isEmpty(deptId)) {
			return JsonUtils.objectToJson(StateResultUtil.build(405, "空值查不到", null));
		} else {
			return JsonUtils.objectToJson(StateResultUtil.ok(this.userService.findByDeptId(deptId, queryRequest)));
		}
	}

	@GetMapping("check")
	public String detail1(@NotBlank(message = "{required}") String userId) {

		return JsonUtils.objectToJson(StateResultUtil.ok(this.userService.findById(userId)));
	}

	@GetMapping("leaderUser")
	public String selectLeaderUser() {

		return JsonUtils.objectToJson(StateResultUtil.ok(this.userService.findByLevel()));
	}

	@PostMapping("checkUser")
	// @RequiresPermissions("user:view")
	public String userList(@RequestBody QueryRequest queryRequest) {
		User user = null;

		return JsonUtils.objectToJson(StateResultUtil.ok(getDataTable(userService.findUserDetail(user, queryRequest))));

	}
	@GetMapping("checkOneUser")
	// @RequiresPermissions("user:view")
	public String userList(String loginName) {
		User findDetail = userService.findOneUser(loginName);
		
		
		return JsonUtils.objectToJson(StateResultUtil.ok(findDetail));
		
	}

	@Log("新增用户")
	@PostMapping("addUser")
	// @RequiresPermissions("user:add")
	public String addUser(@Valid @RequestBody User user) throws FebsException {
		try {
			this.userService.createUser(user);
			return JsonUtils.objectToJson(StateResultUtil.build(200, "新增用户成功"));
		} catch (Exception e) {
			message = "新增用户失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	@Log("修改用户")
	@PostMapping("update")
	// @RequiresPermissions("user:update")
	public String updateUser(@RequestBody User user) throws FebsException {
		try {
			this.userService.updateUser(user);

			return JsonUtils.objectToJson(StateResultUtil.build(200, "修改用户成功"));
		} catch (Exception e) {
			message = "修改用户失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	// @Log("删除用户")
	@GetMapping("delete")
	// @RequiresPermissions("user:delete")
	public String deleteUsers(@NotBlank(message = "{required}") String userId) throws FebsException {
		try {
			String[] ids = userId.split(StringPool.COMMA);
			this.userService.deleteUsers(ids);
			return JsonUtils.objectToJson(StateResultUtil.build(200, "删除用户成功"));
		} catch (Exception e) {
			message = "删除用户失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	// @Log("删除用户")
	@GetMapping("addUserDeptId")
	// @RequiresPermissions("user:delete")
	public String addUserDeptId(@NotBlank String deptId, @NotBlank(message = "{required}") String userId)
			throws FebsException {
		try {
			String[] ids = userId.split(StringPool.COMMA);
			// User user = null;
			User user = null;
			Long deptId1 = Long.valueOf(deptId);
			for (String id : ids) {
				// Long id1 = Long.valueOf(id);
				user = userService.findById(id);
				// user.setUserId(id1);
				user.setDeptId(deptId1);
				this.userService.updateById(user);

			}

			return JsonUtils.objectToJson(StateResultUtil.build(200, "增加部门id成功"));
		} catch (Exception e) {
			message = "新增用户部门id失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	@PutMapping("profile")
	public void updateProfile(@Valid User user) throws FebsException {
		try {
			this.userService.updateProfile(user);
		} catch (Exception e) {
			message = "修改个人信息失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	@PutMapping("avatar")
	public void updateAvatar(@NotBlank(message = "{required}") String loginName,
			@NotBlank(message = "{required}") String avatar) throws FebsException {
		try {
			this.userService.updateAvatar(loginName, avatar);
		} catch (Exception e) {
			message = "修改头像失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	@PutMapping("userconfig")
	public void updateUserConfig(@Valid UserConfig userConfig) throws FebsException {
		try {
			this.userConfigService.update(userConfig);
		} catch (Exception e) {
			message = "修改个性化配置失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	@GetMapping("password/update")
	public String checkPassword(HttpServletRequest request, @NotBlank(message = "{required}") String password)
			throws FebsException {
		String token = request.getHeader(TOKEN);

		String username = JWTUtil.getUsername(FebsUtil.decryptToken(token));

		// User user = this.userService.findByName(username);

		String encryptPassword = MD5Util.encrypt(username, password);
		User user = userService.findDetail(username);
		if (user != null)
			if (StringUtils.equals(user.getPassword(), encryptPassword) != true) {
				try {
					userService.updatePassword(username, password);
				} catch (Exception e) {
					message = "修改密码失败";
					log.error(message, e);
					throw new FebsException(message);

				}
				return JsonUtils.objectToJson(StateResultUtil.build(200, "修改用户密码成功"));
			}
		return JsonUtils.objectToJson(StateResultUtil.build(400, "初试密码跟修改密码一样,修改用户密码失败"));
	}

	@GetMapping("password/reset")
	public String resetPassword(@NotBlank(message = "{required}") String userId,
			@NotBlank(message = "{required}") String password) throws FebsException {

		// User user = this.userService.findByName(username);
		User user = userService.findById(userId);

		String encryptPassword = MD5Util.encrypt(user.getLoginName(), password);
		if (user != null)
			if (StringUtils.equals(user.getPassword(), encryptPassword) != true) {
				try {
					userService.updatePassword(user.getLoginName(), password);
				} catch (Exception e) {
					message = "修改密码失败";
					log.error(message, e);
					throw new FebsException(message);

				}
				return JsonUtils.objectToJson(StateResultUtil.build(200, "修改用户密码成功"));
			}
		return JsonUtils.objectToJson(StateResultUtil.build(400, "初试密码跟修改密码一样,修改用户密码失败"));
	}

	@PutMapping("password")
	public void updatePassword(@NotBlank(message = "{required}") String loginName,
			@NotBlank(message = "{required}") String password) throws FebsException {
		try {
			userService.updatePassword(loginName, password);
		} catch (Exception e) {
			message = "修改密码失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	/*
	 * @PutMapping("password/reset")
	 * 
	 * @RequiresPermissions("user:reset") public void
	 * resetPassword(@NotBlank(message = "{required}") String usernames) throws
	 * FebsException { try { String[] usernameArr =
	 * usernames.split(StringPool.COMMA);
	 * this.userService.resetPassword(usernameArr); } catch (Exception e) { message
	 * = "重置用户密码失败"; log.error(message, e); throw new FebsException(message); } }
	 */

	@PostMapping("excel")
	@RequiresPermissions("user:export")
	public void export(QueryRequest queryRequest, User user, HttpServletResponse response) throws FebsException {
		try {
			List<User> users = this.userService.findUserDetail(user, queryRequest).getRecords();
			ExcelKit.$Export(User.class, response).downXlsx(users, false);
		} catch (Exception e) {
			message = "导出Excel失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}
}
