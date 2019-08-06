package cc.mrbird.febs.system.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.JsonUtils;
import cc.mrbird.febs.common.utils.StateResultUtil;
import cc.mrbird.febs.system.domain.Permission;
import cc.mrbird.febs.system.manager.UserManager;
import cc.mrbird.febs.system.service.PermissionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/permission")
public class PermissionController {
	private String message;
 
   
    @Autowired 
    private PermissionService permissionService;

    @GetMapping("getPermission") 
    public String getUserRouters(@NotBlank(message = "{required}")  String roleId) {
       
        return  JsonUtils.objectToJson(StateResultUtil.ok(this.permissionService.getPermission(roleId)));
    }
    @GetMapping("getPermission1")
    public String getUserRouter(@NotBlank(message = "{required}")  String userName) {
       
        return  JsonUtils.objectToJson(StateResultUtil.ok(this.permissionService.findUserPermissions(userName)));
    }

   /* @PostMapping("check")
    @RequiresPermissions("permission:view")
    public String permissionList(@RequestBody Permission permission) {
       // return ;
        return JsonUtils.objectToJson(StateResultUtil.ok(this.permissionService.findPermissions(permission)));
    }
*/
    @Log("新增权限/按钮")
    @PostMapping("addPermission")
    public String addPermission(@NotBlank(message = "{required}") @PathVariable String permission) throws FebsException {
        try {
        	//return JsonUtils.objectToJson(StateResultUtil.ok(this.permissionService.createPermission(permission)));
        	boolean createPermission = this.permissionService.createPermission(permission);
        	if (createPermission) {
        		return JsonUtils.objectToJson(StateResultUtil.build(200, "新增菜单成功"));
			}else {
				return JsonUtils.objectToJson(StateResultUtil.build(400, "新增菜单失败"));
			}
        	
        } catch (Exception e) {
            message = "新增权限/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

   /* @Log("删除菜单/按钮")
    @GetMapping("/deletePermission/{permissionIds}")
    @RequiresPermissions("permission:delete")
    public String deletePermissions(@NotBlank(message = "{required}") @PathVariable String permissionIds) throws FebsException {
        try {
            String[] ids = permissionIds.split(StringPool.COMMA);
            boolean deleteMeuns = this.permissionService.deleteMeuns(ids);
            if (deleteMeuns) {
        		return JsonUtils.objectToJson(StateResultUtil.build(200, "删除菜单成功"));
			}else {
				return JsonUtils.objectToJson(StateResultUtil.build(400, "删除菜单失败"));
			}
        } catch (Exception e) {
            message = "删除菜单/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }*/

    
	@Log("修改权限/按钮")
    @PostMapping("updatePermission")
    //@RequiresPermissions("permission:update")
    public String updatePermission(@RequestBody Permission permission) throws FebsException {
        try {
            int updatePermission = this.permissionService.updatePermission(permission);
            if (updatePermission==1) {
        		return JsonUtils.objectToJson(StateResultUtil.build(200, "修改权限成功"));
			}else {
				return JsonUtils.objectToJson(StateResultUtil.build(400, "修改权限失败"));
			}
        } catch (Exception e) {
            message = "修改权限/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

  /*  @PostMapping("excel")
    @RequiresPermissions("permission:export")
    public void export(Permission permission, HttpServletResponse response) throws FebsException {
        try {
            List<Permission> permissions = this.permissionService.findPermissionList(permission);
            ExcelKit.$Export(Permission.class, response).downXlsx(permissions, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }*/
}
