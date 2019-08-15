package cc.mrbird.febs.manager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.manager.dao.StScoreMapper;
import cc.mrbird.febs.manager.entity.Score;
import cc.mrbird.febs.manager.entity.ScoreDetail;
import cc.mrbird.febs.manager.entity.StScoretype;
import cc.mrbird.febs.manager.service.StScoreService;
import cc.mrbird.febs.system.domain.User;

/**
 * @author chuhao
 */
@Service
public class StScoreServiceImpl extends ServiceImpl<StScoreMapper, Score> implements StScoreService {
	@Autowired
	private StScoreMapper stScoreMapper;

	@Override
	public IPage<Score> findScoreDetail(ScoreDetail scoreDetail, QueryRequest queryRequest) {
		
		 try {
	            Page<User> page = new Page<>();
	            SortUtil.handlePageSort(queryRequest, page, "userId", FebsConstant.ORDER_ASC, false);
	            return stScoreMapper.findScoreDetail(page, scoreDetail);
	        } catch (Exception e) {
	            log.error("查询用户异常", e);
	            return null;
	        }
	}

	@Override
	public void createScore(Score score) {
		stScoreMapper.insert(score);
		
	}
	
	
}
