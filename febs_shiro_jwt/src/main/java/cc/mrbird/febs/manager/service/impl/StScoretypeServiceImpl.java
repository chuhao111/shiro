package cc.mrbird.febs.manager.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.manager.dao.StScoretypeMapper;
import cc.mrbird.febs.manager.entity.StScoretype;
import cc.mrbird.febs.manager.service.StScoretypeService;
import cc.mrbird.febs.system.domain.Dept;

/**
 * @author chuhao
 */
@Service
public class StScoretypeServiceImpl extends ServiceImpl<StScoretypeMapper, StScoretype> implements StScoretypeService {
	@Autowired
	private StScoretypeMapper stScoretypeMapper;
	  @Override
	    public List<StScoretype> findStScopes() {
		//  StScoretype stScoretype = new StScoretype();
	        try {
	        	//QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();

	          
	           // QueryRequest request = new QueryRequest();
	           // SortUtil.handleWrapperSort(request, queryWrapper, "scoreType", FebsConstant.ORDER_ASC, true);
	            //SortUtil.handleWrapperSort(request, queryWrapper, "scoreType", FebsConstant.ORDER_ASC, true);
	            return stScoretypeMapper.selectList(null);
	        } catch (Exception e) {
	            log.error("获取比分占比信息失败", e);
	            return null;
	        }
	    }

	    @Override
	    @Transactional
	    public void createStScoretype(StScoretype stScoretype) {
	        this.save(stScoretype);
	    }

	    @Override
	    @Transactional
	    public void updateStScoretype(StScoretype stScoretype) {
	        this.baseMapper.update(stScoretype, new LambdaQueryWrapper<StScoretype>().eq(StScoretype::getScoreType, stScoretype.getScoreType()));
	    }

	  
}
