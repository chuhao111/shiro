package cc.mrbird.febs.system.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;

import cc.mrbird.febs.common.converter.TimeConverter;
import lombok.Data;
 
@Data
@TableName("rt_permission")
@Excel("权限信息表")
public class Permission implements Serializable{
	@TableId(value = "PERMISSION_ID", type = IdType.AUTO)
	private int permissionId;
	private int roleId;
	private String permission;
	@ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
	private Date createTime;
	@ExcelField(value = "修改时间", writeConverter = TimeConverter.class)
	private Date modifyTime;
}
