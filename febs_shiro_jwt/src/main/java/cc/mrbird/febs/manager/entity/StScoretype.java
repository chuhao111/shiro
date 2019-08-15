package cc.mrbird.febs.manager.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

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
@TableName("st_scoretype")
public class StScoretype implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 不同档位分数人口的占比
     */
        @TableField("ST_RATE")
    private Double stRate;

    /**
     * 分数类型
     */
        @TableField("SCORE_TYPE")
    private String scoreType;


}
