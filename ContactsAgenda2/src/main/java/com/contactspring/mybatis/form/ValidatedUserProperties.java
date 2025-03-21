package com.contactspring.mybatis.form;

import java.io.Serializable;

import com.contactspring.annotations.ValidEmail;
import com.contactspring.annotations.ValidUsername;
import com.contactspring.mybatis.generate.model.UserProperties;

import lombok.Data;

@Data
public class ValidatedUserProperties extends UserProperties implements Serializable {

	@ValidUsername
    private String username;
    
    @ValidEmail
    private String email;   
    
    private String roleName;
    
	public String verificationToken;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}


}
