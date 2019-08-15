package cc.mrbird.febs.manager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.manager.entity.Score;
import cc.mrbird.febs.manager.entity.ScoreDetail;

/**
 * @author chuhao
 */
public interface StScoreService extends IService<Score> {
	 IPage<Score> findScoreDetail(ScoreDetail scoreDetail, QueryRequest queryRequest);
	 void createScore(Score score);
}
