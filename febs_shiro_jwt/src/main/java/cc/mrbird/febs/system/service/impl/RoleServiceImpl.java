package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.dao.RoleMapper;
import cc.mrbird.febs.system.dao.RoleMenuMapper;
import cc.mrbird.febs.system.domain.Role;
import cc.mrbird.febs.system.domain.RoleMenu;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.manager.UserManager;
import cc.mrbird.febs.system.service.RoleMenuServie;
import cc.mrbird.febs.system.service.RoleService;
import cc.mrbird.febs.system.service.UserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service("roleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

	@Autowired
	private RoleMenuMapper roleMenuMapper;
	@Autowired
	private UserRoleService userRoleService;
	@Autowired
	private RoleMenuServie roleMenuService;
	@Autowired
	private UserManager userManager;
	@Autowired
	private RoleMapper roleMapper;

	@Override
	public IPage<Role> findRoles(Role role, QueryRequest request) {

		try {
			Page<User> page = new Page<>();
			SortUtil.handlePageSort(request, page, "ROLE_ID", FebsConstant.ORDER_ASC, false);
			return this.baseMapper.findRoles(page, role);
		} catch (Exception e) {
			log.error("查询用户异常", e);
			return null;
		}

	}
	/*
	 * @Override public List<Role> findUserRole(String userName) { return
	 * baseMapper.findUserRole(userName); }
	 */

	@Override
	public Role findByName(String roleName) {
		return baseMapper.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleName, roleName));
	}

	@Override
	public void createRole(Role role) {
		role.setCreateTime(new Date());
		this.save(role);

	}

	@Override
	public void deleteRoles(String[] roleIds) throws Exception {
		// 查找这些角色关联了那些用户
		List<String> userIds = this.userRoleService.findUserIdsByRoleId(roleIds);

		List<String> list = Arrays.asList(roleIds);

		baseMapper.deleteBatchIds(list);

		this.roleMenuService.deleteRoleMenusByRoleId(roleIds);
		this.userRoleService.deleteUserRolesByRoleId(roleIds);

		// 重新将这些用户的角色和权限缓存到 Redis中
		this.userManager.loadUserPermissionRoleRedisCache(userIds);

	}

	@Override
	public void updateRole(Role role) throws Exception {
		// 查找这些角色关联了那些用户
		/*
		 * String[] roleId = {String.valueOf(role.getRoleId())}; List<String> userIds =
		 * this.userRoleService.findUserIdsByRoleId(roleId);
		 */

		role.setModifyTime(new Date());
		baseMapper.updateById(role);

		/*
		 * roleMenuMapper.delete(new
		 * LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, role.getRoleId()));
		 * 
		 * String[] menuIds = role.getMenuId().split(StringPool.COMMA);
		 * setRoleMenus(role, menuIds);
		 * 
		 * // 重新将这些用户的角色和权限缓存到 Redis中
		 * this.userManager.loadUserPermissionRoleRedisCache(userIds);
		 */
	}

	/*
	 * private void updateRole(Role role) { String menuId = role.getRoleId()
	 * RoleMenu rm = new RoleMenu(); rm.setMenuId(Long.valueOf(menuId));
	 * rm.setRoleId(role.getRoleId()); this.roleMenuMapper.insert(rm);
	 * 
	 * }
	 */
	@Override
	public Role findById(String roleId) {

		return roleMapper.findRoleById(roleId);
	}

	
	@Override
	public List<Role> findRoles() {
		// TODO Auto-generated method stub
		return roleMapper.findRole();
	}

	

}
