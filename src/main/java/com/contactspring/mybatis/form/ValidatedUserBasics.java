package com.contactspring.mybatis.form;

import java.io.Serializable;

import com.contactspring.mybatis.generate.model.UserBasics;

import jakarta.validation.constraints.NotBlank;

public class ValidatedUserBasics extends UserBasics implements Serializable {

    @NotBlank(message = "Must not be empty")
    private String username;
    
    @NotBlank(message = "Must not be empty")
    private String email;

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
    
    
}
