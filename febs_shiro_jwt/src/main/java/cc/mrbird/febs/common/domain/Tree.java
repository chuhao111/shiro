package cc.mrbird.febs.common.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonInclude;

import cc.mrbird.febs.system.domain.User;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tree<T> {

	private String id;
	private int level;
	private String key;

	private String icon;

	// private String title;
	private IPage<User> user;
	private String value;

	private String text;

	private String permission;

	private String type;

	private int order;

	private String path;

	private String component;

	private List<Tree<T>> children;

	private String parentId;

	private boolean hasParent = false;

	private boolean hasChildren = false;

	private Date createTime;

	private Date modifyTime;

	public void initChildren() {
		this.children = new ArrayList<>();
	}

}
