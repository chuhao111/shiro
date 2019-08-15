package cc.mrbird.febs.manager.dao;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cc.mrbird.febs.manager.entity.Score;
import cc.mrbird.febs.manager.entity.ScoreDetail;


/**
 * @author chuhao
 */
public interface StScoreMapper extends BaseMapper<Score> {
	 // IPage<StScore> findScoreDetail(StScore score, QueryRequest queryRequest);
	  IPage<Score> findScoreDetail(Page page, @Param("scoreDetail") ScoreDetail scoreDetail);
}
