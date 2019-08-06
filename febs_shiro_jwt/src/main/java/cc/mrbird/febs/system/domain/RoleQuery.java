package cc.mrbird.febs.system.domain;

import java.io.Serializable;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.JsonUtils;
import lombok.Data;
@Data
public class RoleQuery implements Serializable{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -4869594085374565813L;
	private QueryRequest queryRequest;
	private Role role;
	public static void main(String[] args) {
		Role role = new Role();
		role.setRoleName("超级管理员");
		QueryRequest queryRequest = new QueryRequest();
		queryRequest.setPageNum(1);
		queryRequest.setPageSize(10);
		RoleQuery roleQuery = new RoleQuery();
		roleQuery.setRole(role);
		roleQuery.setQueryRequest(queryRequest);
		System.out.println(JsonUtils.objectToJson(roleQuery));
	}

}
