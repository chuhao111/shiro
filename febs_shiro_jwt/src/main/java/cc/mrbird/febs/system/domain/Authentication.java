package cc.mrbird.febs.system.domain;

import lombok.Data;

@Data
public class Authentication {
	private String authentication;

	public Authentication(String authentication) {
		super();
		this.authentication = authentication;
	}

	public Authentication() {
		super();
	}
}
