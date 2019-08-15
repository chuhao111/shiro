package cc.mrbird.febs.manager.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wuwenze.poi.ExcelKit;

import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.JsonUtils;
import cc.mrbird.febs.common.utils.StateResultUtil;
import cc.mrbird.febs.manager.entity.StScoretype;
import cc.mrbird.febs.manager.service.StScoretypeService;
import cc.mrbird.febs.system.domain.StModel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chuhao
 */
@Slf4j
@Validated
@RestController
@RequestMapping("scoretype")//领导打分比例占比
public class StScoretypeController extends BaseController {
	 private String message;

	    @Autowired
	    private StScoretypeService stScoretypeService;

	    @GetMapping("checkStScopes")
	    //@RequiresPermissions("stScoretype:view")
	    public String StScoretypeList() {
	       // return getDataTable(this.stScoretypeService.findStScopes());

			return JsonUtils.objectToJson(StateResultUtil.ok(this.stScoretypeService.findStScopes()));
	    }

	    //@Log("新增字典")
	    @PostMapping("addStScope")
	    //@RequiresPermissions("stScoretype:add")
	    public void addStScoretype(@Valid StScoretype stScoretype) throws FebsException {
	        try {
	            this.stScoretypeService.createStScoretype(stScoretype);
	        } catch (Exception e) {
	            message = "新增比分占比成功";
	            log.error(message, e);
	            throw new FebsException(message);
	        }
	    }

	  

	    //@Log("修改字典")
	    @PostMapping("updateStScope")
	 //   @RequiresPermissions("stScoretype:update")
	    public String updateStScoretype( @RequestBody StModel stScoretypes) throws FebsException {
	        try {
	        	for (StScoretype stScoretype2 : stScoretypes.getStScoretypes()) {
					
	        		this.stScoretypeService.updateStScoretype(stScoretype2);
				}
	        	return JsonUtils.objectToJson(StateResultUtil.build(200, "修改成功"));
	            
	        } catch (Exception e) {
	            message = "修改比分占比失败";
	            log.error(message, e);
	            throw new FebsException(message);
	        }
	    }

	    @PostMapping("excel")
	    @RequiresPermissions("stScoretype:export")
	    public void export(QueryRequest request, StScoretype stScoretype, HttpServletResponse response) throws FebsException {
	        try {
	            List<StScoretype> stScoretypes = this.stScoretypeService.findStScopes();
	            ExcelKit.$Export(StScoretype.class, response).downXlsx(stScoretypes, false);
	        } catch (Exception e) {
	            message = "导出Excel失败";
	            log.error(message, e);
	            throw new FebsException(message);
	        }
	    }
}
