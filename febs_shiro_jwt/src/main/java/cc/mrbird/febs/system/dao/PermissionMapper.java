package cc.mrbird.febs.system.dao;


import cc.mrbird.febs.system.domain.Permission;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;



public interface PermissionMapper extends BaseMapper<Permission> {

	Permission findPermissionById(String roleId);

	List<Permission> findUserPermissions(String username);

    
}