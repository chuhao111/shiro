package cc.mrbird.febs.manager.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 
 *
 * @author chuhao
 */
@Data
@TableName("st_score")
public class Score implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value ="SCORE_ID", type = IdType.AUTO)
    private Long scoreId;

	/**
	 * 员工id
	 */
	@TableField("USER_ID")
	private Long userId;

	private transient String name;
	
	
	private transient String deptName;
	
	private transient String jobName;
	private transient String loginName;
	
	
	/**
	 * 分数
	 */
	@TableField("SCORE")
	private Double score;

	/**
	 * 分数创建时间
	 */
	@TableField("CREATE_TIME")
	private Date createTime;

	/**
	 * 状态值
	 */
	@TableField("STATE")
	private Integer state;

	/**
	 * 创建月份
	 */
	@TableField("ADJUST")
	private String adjust;
	
	
	
	@TableField("MONTH")
	private String month;
	

	/**
	 * 评语:不同id不一样评语,分管领导id对应的是对科室员工评语,科室领导id对应的是对AB档分数评语
	 */
	@TableField("COMMENT")
	private String comment;

	
}
