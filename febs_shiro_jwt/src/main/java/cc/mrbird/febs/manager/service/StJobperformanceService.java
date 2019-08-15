package cc.mrbird.febs.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;

import cc.mrbird.febs.manager.entity.Jobperformance;

/**
 * @author chuhao
 */
public interface StJobperformanceService extends IService<Jobperformance> {
	void createPerformance(Jobperformance jobPerformance);
	Jobperformance checkOne(Jobperformance jobPerformance);
}
