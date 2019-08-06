package cc.mrbird.febs.system.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cc.mrbird.febs.system.dao.PermissionMapper;
import cc.mrbird.febs.system.domain.Permission;
import cc.mrbird.febs.system.service.PermissionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("permissionService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

	@Override
	public Permission getPermission(String roleId) {
		// TODO Auto-generated method stub
		return this.baseMapper.findPermissionById(roleId);
	}
	
	@Override
	public boolean createPermission(String permission) {
		Permission permission2 = new Permission();
		permission2.setPermission(permission); 
		permission2.setCreateTime(new Date());

		boolean save = this.save(permission2);
		return save;
	}

	@Override
	public int updatePermission(Permission permission) {
		
		
		permission.setCreateTime(new Date());

		permission.setModifyTime(new Date());
		
		int updateById = baseMapper.updateById(permission);
		return updateById;
	} 

	@Override
	public List<Permission> findUserPermissions(String username) {
		 return this.baseMapper.findUserPermissions(username);
	}
	
}
