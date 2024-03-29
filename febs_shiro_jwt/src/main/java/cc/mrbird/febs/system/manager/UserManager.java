package cc.mrbird.febs.system.manager;

import cc.mrbird.febs.common.domain.router.RouterMeta;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.service.CacheService;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.TreeUtil;
import cc.mrbird.febs.system.domain.Menu;
import cc.mrbird.febs.system.domain.Permission;
import cc.mrbird.febs.system.domain.Role;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.domain.UserConfig;
import cc.mrbird.febs.system.service.MenuService;
import cc.mrbird.febs.system.service.PermissionService;
import cc.mrbird.febs.system.service.RoleService;
import cc.mrbird.febs.system.service.UserConfigService;
import cc.mrbird.febs.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 封装一些和 User相关的业务操作
 */
@Service
public class UserManager {

	@Autowired
	private CacheService cacheService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserConfigService userConfigService;
	@Autowired
	private PermissionService permissionService;

	/**
	 * 通过用户名获取用户基本信息
	 *
	 * @param username 用户名
	 * @return 用户基本信息
	 */
	public User getUser(String loginName) {
		return FebsUtil.selectCacheByTemplate(() -> this.cacheService.getUser(loginName),
				() -> this.userService.findDetail(loginName));
	}

	/**
	 * 通过用户名获取用户角色集合
	 *
	 * @param username 用户名
	 * @return 角色集合
	 */
	/*public Set<String> getUserRoles(String username) {
		List<Role> roleList = FebsUtil.selectCacheByTemplate(() -> this.cacheService.getRoles(username),
				() -> this.roleService.findUserRole(username));
		return roleList.stream().map(Role::getRoleName).collect(Collectors.toSet());
	}
*/
	/**
	 * 通过用户名获取用户权限集合
	 *
	 * @param username 用户名
	 * @return 权限集合
	 */
	public Set<String> getUserPermissions(String username) {
		List<Permission> permissionList = FebsUtil.selectCacheByTemplate(() -> this.cacheService.getPermissions(username),
				() -> this.permissionService.findUserPermissions(username));
		return permissionList.stream().map(Permission::getPermission).collect(Collectors.toSet());
	}

	/**
	 * 通过用户名构建 Vue路由
	 *
	 * @param username 用户名
	 * @return 路由集合
	 */
	public ArrayList<VueRouter<Menu>> getUserRouters(String username) {
		List<VueRouter<Menu>> routes = new ArrayList<>();
		List<Menu> menus = this.menuService.findUserMenus(username);
		menus.forEach(menu -> {
			VueRouter<Menu> route = new VueRouter<>();
			route.setId(menu.getMenuId().toString());
			route.setParentId(menu.getParentId().toString());
			route.setIcon(menu.getIcon());
			route.setPath(menu.getPath());
			route.setComponent(menu.getComponent());
			route.setName(menu.getMenuName());
			route.setMeta(new RouterMeta(true, null));
			routes.add(route);
		});
		return TreeUtil.buildVueRouter(routes);
	}

	/**
	 * 通过用户 ID获取前端系统个性化配置
	 *
	 * @param userId 用户 ID
	 * @return 前端系统个性化配置
	 */
	public UserConfig getUserConfig(String userId) {
		return FebsUtil.selectCacheByTemplate(() -> this.cacheService.getUserConfig(userId),
				() -> this.userConfigService.findByUserId(userId));
	}

	/**
	 * 将用户相关信息添加到 Redis缓存中
	 *
	 * @param user user
	 */
	public void loadUserRedisCache(User user) throws Exception {
		// 缓存用户
		cacheService.saveUser(user.getLoginName());
		// 缓存用户角色
		//cacheService.saveRoles(user.getUsername());
		// 缓存用户权限
		cacheService.savePermissions(user.getLoginName());
		// 缓存用户个性化配置
		cacheService.saveUserConfigs(String.valueOf(user.getUserId()));
	}

	/**
	 * 将用户角色和权限添加到 Redis缓存中
	 *
	 * @param userIds userIds
	 */
	public void loadUserPermissionRoleRedisCache(List<String> userIds) throws Exception {
		for (String userId : userIds) {
			User user = userService.getById(userId);
			// 缓存用户角色
			//cacheService.saveRoles(user.getUsername());
			// 缓存用户权限
			cacheService.savePermissions(user.getLoginName());
		}
	}

	/**
	 * 通过用户 id集合批量删除用户 Redis缓存
	 *
	 * @param userIds userIds
	 */
	public void deleteUserRedisCache(String... userIds) throws Exception {
		for (String userId : userIds) {
			User user = userService.getById(userId);
			if (user != null) {
				cacheService.deleteUser(user.getLoginName());
				cacheService.deleteRoles(user.getLoginName());
				cacheService.deletePermissions(user.getLoginName());
			}
			cacheService.deleteUserConfigs(userId);
		}
	}

}
