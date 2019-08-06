package cc.mrbird.febs.system.dao;

import cc.mrbird.febs.system.domain.Role;
import cc.mrbird.febs.system.domain.User;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface RoleMapper extends BaseMapper<Role> {
	Role findRoleById(String roleId);
	IPage<Role> findRoles(Page page, @Param(value = "role") Role role);
	List<Role> findRole();
}