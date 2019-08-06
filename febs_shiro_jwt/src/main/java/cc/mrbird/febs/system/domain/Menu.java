package cc.mrbird.febs.system.domain;

import cc.mrbird.febs.common.converter.TimeConverter;
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
@TableName("rt_menu")
@Excel("菜单信息表")
public class Menu implements Serializable {

    private static final long serialVersionUID = 7187628714679791771L;

    public static final String TYPE_MENU = "0";

    public static final String TYPE_BUTTON = "1";

    @TableId(value = "MENU_ID", type = IdType.AUTO)
    private Long menuId;

    private Long parentId;

    @NotBlank(message = "{required}")
    @Size(max = 10, message = "{noMoreThan}")
    @ExcelField(value = "名称")
    private String menuName;

    @Size(max = 50, message = "{noMoreThan}")
    @ExcelField(value = "地址")
    private String path;

    @Size(max = 100, message = "{noMoreThan}")
    @ExcelField(value = "对应Vue组件")
    private String component;

    @Size(max = 50, message = "{noMoreThan}")
    @ExcelField(value = "权限")
    private String perms;

    @ExcelField(value = "图标")
    private String icon;

    @NotBlank(message = "{required}")
    @ExcelField(value = "类型", writeConverterExp = "0=按钮,1=菜单")
    private String type;

    private int orderNum;

    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date createTime;

    @ExcelField(value = "修改时间", writeConverter = TimeConverter.class)
    private Date modifyTime;

    private transient String createTimeFrom;
    private transient String createTimeTo;
    
    public static void main(String[] args) {
  		//Role role = new Role(1L, "超级管理员", "可任何操作", new Date(), new Date(), null, null, "1,3,11,12,13");
  		
  		Menu menu = new Menu(5L, 1L, "菜单管理", "1", new Date());
  		System.out.println(JsonUtils.objectToJson(menu));
  	}

	public Menu(Long menuId, Long parentId,
			@NotBlank(message = "{required}") @Size(max = 10, message = "{noMoreThan}") String menuName,
			@NotBlank(message = "{required}") String type, Date createTime) {
		super();
		this.menuId = menuId;
		this.parentId = parentId;
		this.menuName = menuName;
		this.type = type;
		this.createTime = createTime;
	}

	public Menu() {
		super();
	}

	public Menu(@NotBlank(message = "{required}") @Size(max = 10, message = "{noMoreThan}") String menuName) {
		super();
		this.menuName = menuName;
	}

}