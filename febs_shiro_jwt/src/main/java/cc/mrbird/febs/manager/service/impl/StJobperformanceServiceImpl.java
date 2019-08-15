package cc.mrbird.febs.manager.service.impl;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.manager.dao.StJobperformanceMapper;
import cc.mrbird.febs.manager.entity.Jobperformance;
import cc.mrbird.febs.manager.service.StJobperformanceService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chuhao
 */
@Service
public class StJobperformanceServiceImpl extends ServiceImpl<StJobperformanceMapper, Jobperformance>
		implements StJobperformanceService {
	@Autowired
	private StJobperformanceMapper stJobperformanceMapper;
	@Override
	public void createPerformance(Jobperformance jobPerformance) {
		QueryWrapper<Jobperformance> queryWrapper=new QueryWrapper<>();
		queryWrapper.lambda().in(Jobperformance::getMonth, jobPerformance.getMonth());
		queryWrapper.lambda().in(Jobperformance::getDeptId, jobPerformance.getDeptId());
		
		if (stJobperformanceMapper.selectCount(queryWrapper)!=1) {
			
			stJobperformanceMapper.insert(jobPerformance);
		}
	}
	@Override
	public Jobperformance checkOne(Jobperformance jobPerformance) {
		// TODO Auto-generated method stub
		QueryWrapper<Jobperformance> queryWrapper=new QueryWrapper<>();
		queryWrapper.lambda().in(Jobperformance::getMonth, jobPerformance.getMonth());
		queryWrapper.lambda().in(Jobperformance::getDeptId, jobPerformance.getDeptId());
		
		return stJobperformanceMapper.selectOne(queryWrapper);
	}

}
