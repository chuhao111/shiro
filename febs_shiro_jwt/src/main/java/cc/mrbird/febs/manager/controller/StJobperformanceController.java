package cc.mrbird.febs.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.JsonUtils;
import cc.mrbird.febs.common.utils.StateResultUtil;
import cc.mrbird.febs.manager.entity.Jobperformance;
import cc.mrbird.febs.manager.service.StJobperformanceService;
import cc.mrbird.febs.system.domain.User;

/**
 * @author chuhao
 */
@RestController
@RequestMapping("jobperformance")
public class StJobperformanceController {
	@Autowired
	private StJobperformanceService stJobperformanceService;

	@PostMapping("createPerformance")
	// @RequiresPermissions("user:view")
	public String addPerformance(@RequestBody Jobperformance jobperformance) {
		stJobperformanceService.createPerformance(jobperformance);
		return JsonUtils.objectToJson(StateResultUtil.build(200, "添加部门业绩总结成功"));

	}
	@PostMapping("checkPerformance")
	// @RequiresPermissions("user:view")
	public String checkPerformance(@RequestBody Jobperformance jobperformance) {
		return JsonUtils.objectToJson(StateResultUtil.ok(stJobperformanceService.checkOne(jobperformance)));
		
	}
}