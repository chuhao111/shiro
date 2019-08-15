package cc.mrbird.febs.manager.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("mt_leaderdepartment")
public class MtLeaderdepartment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门id
     */
            @TableId("DEPT_ID")
    private Long deptId;

    /**
     * 分数占比
     */
        @TableField("SCORE_RATE")
    private Double scoreRate;

    /**
     * 员工id
     */
        @TableField("USER_ID")
    private Long userId;


}
