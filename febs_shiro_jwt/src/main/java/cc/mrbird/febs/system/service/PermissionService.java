package cc.mrbird.febs.system.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.extension.service.IService;

import cc.mrbird.febs.system.domain.Permission;

public interface PermissionService extends IService<Permission> {

	Permission getPermission(String roleId);

	boolean createPermission(String permission);

	int updatePermission(Permission permission);

	List<Permission> findUserPermissions(String username);
}
