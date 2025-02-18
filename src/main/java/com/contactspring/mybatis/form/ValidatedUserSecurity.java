package com.contactspring.mybatis.form;

import java.io.Serializable;

import com.contactspring.mybatis.generate.model.UserSecurity;

import jakarta.validation.constraints.NotBlank;

public class ValidatedUserSecurity extends UserSecurity implements Serializable{

    @NotBlank(message = "Must not be empty")
    private String username;

    @NotBlank(message = "Must not be empty")
    private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
    
}
