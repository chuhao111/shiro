package cc.mrbird.febs.system.domain;

import cc.mrbird.febs.common.converter.TimeConverter;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.JsonUtils;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("rt_role")
@Excel("角色信息表")
public class Role implements Serializable {

	private static final long serialVersionUID = -1714476694755654924L;

	@TableId(value = "ROLE_ID", type = IdType.AUTO)
	private Long roleId;

	@NotBlank(message = "{required}")
	@Size(max = 10, message = "{noMoreThan}")
	@ExcelField(value = "角色名称")
	private String roleName;
	
	private String permission;
	@Size(max = 50, message = "{noMoreThan}")
	@ExcelField(value = "角色描述")
	private String remark;

	@ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
	private Date createTime;

	@ExcelField(value = "修改时间", writeConverter = TimeConverter.class)
	private Date modifyTime;

	
	

	public static void main(String[] args) {
		Role role = new Role(1L, "超级管理员", "可任何操作", new Date(), new Date(), null, null, "1,3,11,12,13");

		System.out.println(JsonUtils.objectToJson(role));
	}

	public Role(Long roleId,
			@NotBlank(message = "{required}") @Size(max = 10, message = "{noMoreThan}") String roleName,
			@Size(max = 50, message = "{noMoreThan}") String remark, Date createTime, Date modifyTime,
			String createTimeFrom, String createTimeTo, String menuId) {
		super();
		this.roleId = roleId;
		this.roleName = roleName;
		this.remark = remark;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		
	}

	public Role() {
		super();
	}

}