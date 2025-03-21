package com.contactspring.mybatis.generate.model;

import com.contactspring.mybatis.form.ValidatedUserProperties;
import com.contactspring.mybatis.form.ValidatedUserSecurity;

import jakarta.validation.Valid;

public class LoginInfo {
	
	@Valid
	private ValidatedUserProperties validatedUserProperties;
	
	@Valid
	private ValidatedUserSecurity validatedUserSecurity;

	@Valid
	private String roleText;
	
	public ValidatedUserProperties getValidatedUserProperties() {
		return validatedUserProperties;
	}

	public void setValidatedUserProperties(ValidatedUserProperties validatedUserProperties) {
		this.validatedUserProperties = validatedUserProperties;
	}

	public ValidatedUserSecurity getValidatedUserSecurity() {
		return validatedUserSecurity;
	}

	public void setValidatedUserSecurity(ValidatedUserSecurity validatedUserSecurity) {
		this.validatedUserSecurity = validatedUserSecurity;
	}

	public String getRoleText() {
		return roleText;
	}

	public void setRoleText(String roleText) {
		this.roleText = roleText;
	}

	
}
