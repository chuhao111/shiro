package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.JsonUtils;
import cc.mrbird.febs.common.utils.StateResultUtil;
import cc.mrbird.febs.system.domain.Role;
import cc.mrbird.febs.system.domain.RoleMenu;
import cc.mrbird.febs.system.domain.RoleQuery;
import cc.mrbird.febs.system.service.RoleMenuServie;
import cc.mrbird.febs.system.service.RoleService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequestMapping("role")
public class RoleController extends BaseController {

	@Autowired
	private RoleService roleService;
	@Autowired
	private RoleMenuServie roleMenuServie;

	private String message; 
  
	@PostMapping("checkRole") 
	//@RequiresPermissions("role:view")
	public String roleList(@RequestBody QueryRequest queryRequest) {
		Role role = null;
		//return JsonUtils.objectToJson(StateResultUtil.ok(getDataTable(roleService.findRoles(role, queryRequest))));
		if (roleService.findRoles(role, queryRequest).getTotal() != 0) {
			return JsonUtils.objectToJson(StateResultUtil.ok(getDataTable(roleService.findRoles(role, queryRequest))));
		}
		return JsonUtils.objectToJson(StateResultUtil.build(400, "抱歉,查无此对象"));
	}
	@GetMapping("checkRoleList") 
	//@RequiresPermissions("role:view")
	public String roleAllList() {
		Role role = null;
		//return JsonUtils.objectToJson(StateResultUtil.ok(getDataTable(roleService.findRoles(role, queryRequest))));
		if (roleService.findRoles().size()!=0) {
			return JsonUtils.objectToJson(StateResultUtil.ok(roleService.findRoles()));
		}
		return JsonUtils.objectToJson(StateResultUtil.build(400, "抱歉,查无此对象"));
	}

	@GetMapping("check")
	public String checkRoleName(@NotBlank(message = "{required}")  String roleId) {
		Role result = this.roleService.findById(roleId);
		//return result == null;
		if (result == null) {
			return JsonUtils.objectToJson(StateResultUtil.build(404, "查询角色失败"));
		}
		return JsonUtils.objectToJson(StateResultUtil.ok(result));
	}

	@GetMapping("permission")
	public String getRoleMenus(@NotBlank(message = "{required}")  String roleId) {
		Role role = this.roleService.findById(roleId);
		
		if (role == null) {
			return JsonUtils.objectToJson(StateResultUtil.build(404, "查询角色失败"));
		}
		return JsonUtils.objectToJson(StateResultUtil.ok(role));
	}

	@Log("新增角色")
	@PostMapping("roleAdd")
	//@RequiresPermissions("role:add")
	public String addRole(@RequestBody Role role) throws FebsException {
		try {
			this.roleService.createRole(role);
			return JsonUtils.objectToJson(StateResultUtil.build(200, "新增角色成功"));
		} catch (Exception e) {
			message = "新增角色失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	@Log("删除角色")
	@GetMapping("delete")
	//@RequiresPermissions("role:delete")
	public String deleteRoles(@NotBlank(message = "{required}")String roleId) throws FebsException {
		try {
			String[] ids = roleId.split(StringPool.COMMA);
			this.roleService.deleteRoles(ids);
			return JsonUtils.objectToJson(StateResultUtil.build(200, "删除角色成功"));
		} catch (Exception e) {
			message = "删除角色失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	@Log("修改角色")
	@PostMapping("update")
	//@RequiresPermissions("role:update")
	public String  updateRole(@RequestBody Role role) throws FebsException {
		try {
			this.roleService.updateRole(role);
			return JsonUtils.objectToJson(StateResultUtil.build(200, "修改角色成功"));
		} catch (Exception e) {
			message = "修改角色失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}

	@PostMapping("excel")
	@RequiresPermissions("role:export")
	public void export(QueryRequest queryRequest, Role role, HttpServletResponse response) throws FebsException {
		try {
			List<Role> roles = this.roleService.findRoles(role, queryRequest).getRecords();
			ExcelKit.$Export(Role.class, response).downXlsx(roles, false);
		} catch (Exception e) {
			message = "导出Excel失败";
			log.error(message, e);
			throw new FebsException(message);
		}
	}
}
