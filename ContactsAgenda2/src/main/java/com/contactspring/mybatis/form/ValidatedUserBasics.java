package com.contactspring.mybatis.form;

import java.io.Serializable;

import com.contactspring.mybatis.generate.model.UserBasics;

import lombok.Data;

@Data
public class ValidatedUserBasics extends UserBasics implements Serializable {

	// no validation because it doesn't receive data from the outside, only from within the server
    private String username;
    
	// no validation because it doesn't receive data from the outside, only from within the server
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
    
}
