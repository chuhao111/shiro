package cc.mrbird.febs.manager.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import cc.mrbird.febs.common.utils.JsonUtils;
import io.lettuce.core.ScanCursor;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 
 *
 * @author chuhao
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryScore implements Serializable {

    private static final long serialVersionUID = -4869594085374386813L;

    private int pageSize = 10;
    private int pageNum = 1;

    private String sortField;
    private String sortOrder;
    private ScoreDetail scoreDetail;
    public static void main(String[] args) {
		QueryScore queryScore = new QueryScore();
		ScoreDetail scoreDetail = new ScoreDetail();
		scoreDetail.setDeptId(12L);
		scoreDetail.setMonthFrom("7");
		scoreDetail.setMonthTo("7");
		queryScore.setScoreDetail(scoreDetail);
		String objectToJson = JsonUtils.objectToJson(queryScore);
		System.out.println(objectToJson);
	}
}
