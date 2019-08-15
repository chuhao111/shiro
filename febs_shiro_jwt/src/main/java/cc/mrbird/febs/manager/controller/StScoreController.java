package cc.mrbird.febs.manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.JsonUtils;
import cc.mrbird.febs.common.utils.StateResultUtil;
import cc.mrbird.febs.manager.entity.QueryScore;
import cc.mrbird.febs.manager.entity.Score;
import cc.mrbird.febs.manager.entity.ScoreModel;
import cc.mrbird.febs.manager.service.StScoreService;
import cc.mrbird.febs.system.controller.UserController;
import cc.mrbird.febs.system.domain.User;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chuhao
 */
@Slf4j
@Validated
@RestController
@RequestMapping("score")
public class StScoreController {
	@Autowired
	private StScoreService stScoreService;

	@PostMapping("checkScore")
	// @RequiresPermissions("user:view")
	public String userList(@RequestBody QueryScore queryScore) {
		User user = null;
		QueryRequest queryRequest = new QueryRequest();
		queryRequest.setPageNum(queryScore.getPageNum());
		queryRequest.setPageSize(queryScore.getPageSize());

		return JsonUtils.objectToJson(
				StateResultUtil.ok(stScoreService.findScoreDetail(queryScore.getScoreDetail(), queryRequest)));

	}

	@PostMapping("addScores")
	// @RequiresPermissions("user:view")
	public String create(@RequestBody ScoreModel scores) throws FebsException {
		try {
			
			for (Score score : scores.getScores()) {

				stScoreService.createScore(score);
			}
			return JsonUtils.objectToJson(StateResultUtil.build(200, "增加打分分数成功"));
		} catch (Exception e) {
			String message = "增加打分分数失败";
			log.error(message, e);
			throw new FebsException(message);
		}
		

	}
}
