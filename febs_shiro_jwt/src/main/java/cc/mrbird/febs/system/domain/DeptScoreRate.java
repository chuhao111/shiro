package cc.mrbird.febs.system.domain;

import java.io.Serializable;
import java.util.List;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.JsonUtils;
import cc.mrbird.febs.manager.entity.MtLeaderdepartment;
import lombok.Data;
@Data
public class DeptScoreRate implements Serializable{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -4869594085374565813L;
	private Long deptId;
	private String deptName;
	private String  userName;//科室经理名字
	private List<MtLeaderdepartment> rateList;
	

}
