package cc.mrbird.febs.manager.entity;

import org.omg.CORBA.PRIVATE_MEMBER;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreDetail {
	private String monthFrom;
	private String monthTo;
	private String name;
	private Long deptId;
	
}
