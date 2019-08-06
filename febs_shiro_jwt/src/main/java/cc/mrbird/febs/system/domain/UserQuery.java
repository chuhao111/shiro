package cc.mrbird.febs.system.domain;

import java.io.Serializable;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.JsonUtils;
import lombok.Data;
@Data
public class UserQuery implements Serializable{
	/**
	 * 
	 */
	private User user;
	private static final long serialVersionUID = -4869594085374565813L;
	private QueryRequest queryRequest;

	public static void main(String[] args) {
		User user = new User();
		user.setUserId(1L);
		QueryRequest queryRequest = new QueryRequest();
		queryRequest.setPageNum(1);
		queryRequest.setPageSize(10);
		UserQuery userQuery = new UserQuery();
		userQuery.setUser(user);
		userQuery.setQueryRequest(queryRequest);
		System.out.println(JsonUtils.objectToJson(userQuery));
	}

}
