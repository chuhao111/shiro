package cc.mrbird.febs.manager.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 
 *
 * @author chuhao
 */
@Data
@TableName("st_jobperformance")
public class Jobperformance implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 业绩表主键id
	 */
	@TableId(value = "JP_ID", type = IdType.AUTO)
	private Long jpId;

	/**
	 * 用户id
	 */
	@TableField("DEPT_ID")
	private Long deptId;

	/**
	 * 不超过五十字业绩点评
	 */
	@TableField("JOB_PERFORMANCE")
	private String jobPerformance;

	/**
	 * 创建时间
	 */
	@TableField("MONTH")
	private String month;

}
