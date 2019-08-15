package cc.mrbird.febs.manager.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.manager.entity.StScoretype;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author chuhao
 */
public interface StScoretypeService extends IService<StScoretype> {
	 public List<StScoretype> findStScopes();
	 public void createStScoretype(StScoretype stScoretype);
	 public void updateStScoretype(StScoretype stScoretype);
}
