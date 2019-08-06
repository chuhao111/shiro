package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.router.VueRouter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.JsonUtils;
import cc.mrbird.febs.common.utils.StateResultUtil;
import cc.mrbird.febs.system.domain.Menu;
import cc.mrbird.febs.system.manager.UserManager;
import cc.mrbird.febs.system.service.MenuService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController {

    private String message;

    @Autowired
    private UserManager userManager;
    @Autowired
    private MenuService menuService;

    @GetMapping("/{username}")
    public ArrayList<VueRouter<Menu>> getUserRouters(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.userManager.getUserRouters(username);
    }

    @PostMapping("check")
    @RequiresPermissions("menu:view")
    public String menuList(@RequestBody Menu menu) {
       // return ;
        return JsonUtils.objectToJson(StateResultUtil.ok(this.menuService.findMenus(menu)));
    }

    @Log("新增菜单/按钮")
    @PostMapping("addMenu")
    @RequiresPermissions("menu:add")
    public String addMenu(@RequestBody @Valid Menu menu) throws FebsException {
        try {
        	//return JsonUtils.objectToJson(StateResultUtil.ok(this.menuService.createMenu(menu)));
        	boolean createMenu = this.menuService.createMenu(menu);
        	if (createMenu) {
        		return JsonUtils.objectToJson(StateResultUtil.build(200, "新增菜单成功"));
			}else {
				return JsonUtils.objectToJson(StateResultUtil.build(400, "新增菜单失败"));
			}
        	
        } catch (Exception e) {
            message = "新增菜单/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除菜单/按钮")
    @GetMapping("/deleteMenu/{menuIds}")
    @RequiresPermissions("menu:delete")
    public String deleteMenus(@NotBlank(message = "{required}") @PathVariable String menuIds) throws FebsException {
        try {
            String[] ids = menuIds.split(StringPool.COMMA);
            boolean deleteMeuns = this.menuService.deleteMeuns(ids);
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
    }

    
	@Log("修改菜单/按钮")
    @PostMapping("updateMenu")
    @RequiresPermissions("menu:update")
    public String updateMenu(@RequestBody @Valid Menu menu) throws FebsException {
        try {
            int updateMenu = this.menuService.updateMenu(menu);
            if (updateMenu==1) {
        		return JsonUtils.objectToJson(StateResultUtil.build(200, "修改菜单成功"));
			}else {
				return JsonUtils.objectToJson(StateResultUtil.build(400, "修改菜单失败"));
			}
        } catch (Exception e) {
            message = "修改菜单/按钮失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("menu:export")
    public void export(Menu menu, HttpServletResponse response) throws FebsException {
        try {
            List<Menu> menus = this.menuService.findMenuList(menu);
            ExcelKit.$Export(Menu.class, response).downXlsx(menus, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
