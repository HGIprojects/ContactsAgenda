package com.contactspring.mybatis.form;

import java.io.Serializable;

import com.contactspring.annotations.ValidEmail;
import com.contactspring.annotations.ValidUsername;
import com.contactspring.mybatis.generate.model.UserProperties;

//@Data
public class ValidatedUserProperties extends UserProperties implements Serializable {

	@ValidUsername
    private String username;
    
    @ValidEmail
    private String email;   
    
    private String roleName;
    
	public String verificationToken;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	
}
