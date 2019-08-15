package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.JsonUtils;
import cc.mrbird.febs.common.utils.StateResultUtil;
import cc.mrbird.febs.system.domain.Dept;
import cc.mrbird.febs.system.domain.DeptScoreRate;
import cc.mrbird.febs.system.service.DeptService;
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

@Slf4j
@Validated
@RestController
@RequestMapping("dept")
public class DeptController extends BaseController {

    private String message;

    @Autowired
    private DeptService deptService;

    @GetMapping("deptList")
    public String  deptList(QueryRequest request) {
    	Dept dept = new Dept();
       
        return JsonUtils.objectToJson(StateResultUtil.ok(this.deptService.findDepts(dept, request)));
    }
    @GetMapping("deptScoreRateList")
    public String  deptScoreRateList() {
    	//Dept dept = new Dept();
    	
    	return JsonUtils.objectToJson(StateResultUtil.ok(this.deptService.findDeptScoreRate()));
    }
    @GetMapping("deptTree")
    public String  deptTree(QueryRequest request) {
    	Dept dept = new Dept();
       
        return JsonUtils.objectToJson(StateResultUtil.ok(this.deptService.findDepts(request, dept)));
    }
    @GetMapping("threeDept")
    public String  deptThree() {
    	
    	
    	return JsonUtils.objectToJson(StateResultUtil.ok(this.deptService.findThreeDepts()));
    }
    
    @GetMapping("DeptsWithUser")
    public String  findDeptsWithUser(String deptId,String pageSize,String pageNum) {
    	
    	
    	QueryRequest queryRequest = new QueryRequest();
		queryRequest.setPageNum(Integer.valueOf(pageNum));
		queryRequest.setPageSize(Integer.valueOf(pageSize));
    	
    	return JsonUtils.objectToJson(StateResultUtil.ok(this.deptService.findDeptsWithUser(queryRequest, deptId)));
    }

    @Log("新增部门")
    @PostMapping("addDept")
   // @RequiresPermissions("dept:add")
    public String  addDept(@RequestBody Dept dept) throws FebsException {
        try {
            this.deptService.createDept(dept);
            return JsonUtils.objectToJson(StateResultUtil.build(200, "新增部门成功"));
        } catch (Exception e) {
            message = "新增部门失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除部门")
    @GetMapping("deleteDept")
    //@RequiresPermissions("dept:delete")
    public String deleteDepts(@NotBlank(message = "{required}")String deptId) throws FebsException {
        try {
            String[] ids = deptId.split(StringPool.COMMA);
            this.deptService.deleteDepts(ids);
            return JsonUtils.objectToJson(StateResultUtil.build(200, "删除部门成功"));
        } catch (Exception e) {
            message = "删除部门失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    //@Log("修改部门")
    @PostMapping("updateDept")
    //@RequiresPermissions("dept:update")
    public String updateDept(@RequestBody Dept dept) throws FebsException {
        try {
            this.deptService.updateDept(dept);
            return JsonUtils.objectToJson(StateResultUtil.build(200, "修改部门成功"));
        } catch (Exception e) {
            message = "修改部门失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    
  
    
  //  @Log("修改部门")
    @PostMapping("updateDeptScoreRate")
    //@RequiresPermissions("dept:update")
    public String updateDeptScoreRate(@RequestBody DeptScoreRate deptScoreRate) throws FebsException {
    	try {
    		this.deptService.updateDeptScoreRate(deptScoreRate);
    		return JsonUtils.objectToJson(StateResultUtil.build(200, "修改部门打分占比成功"));
    	} catch (Exception e) {
    		message = "修改部门打分占比失败";
    		log.error(message, e);
    		throw new FebsException(message);
    	}
    }

    @PostMapping("excel")
    @RequiresPermissions("dept:export")
    public void export(Dept dept, QueryRequest request, HttpServletResponse response) throws FebsException {
        try {
            List<Dept> depts = this.deptService.findDepts(dept, request);
            ExcelKit.$Export(Dept.class, response).downXlsx(depts, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
