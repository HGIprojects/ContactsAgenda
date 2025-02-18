package com.contactspring.mybatis.form;

import java.io.Serializable;

import com.contactspring.mybatis.generate.model.UserProperties;

import jakarta.validation.constraints.NotBlank;

public class ValidatedUserProperties extends UserProperties implements Serializable {

    @NotBlank(message = "Must not be empty")
    private String username;
    
    @NotBlank(message = "Must not be empty")
    private String email;   
    
    private String roleName;
    
	public String getUsername() {
		return username;
	}

	public String verificationToken;
	
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

	public String getVerificationToken() {
		return verificationToken;
	}

	public void setVerificationToken(String verificationToken) {
		this.verificationToken = verificationToken;
	}
    
    
}
