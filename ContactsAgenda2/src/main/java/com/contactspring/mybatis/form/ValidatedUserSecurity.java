package com.contactspring.mybatis.form;

import java.io.Serializable;

import com.contactspring.annotations.ValidPassword;
import com.contactspring.annotations.ValidUsername;
import com.contactspring.mybatis.generate.model.UserSecurity;

//@Data
public class ValidatedUserSecurity extends UserSecurity implements Serializable{

	@ValidUsername
	private String username;
    
    @ValidPassword
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
