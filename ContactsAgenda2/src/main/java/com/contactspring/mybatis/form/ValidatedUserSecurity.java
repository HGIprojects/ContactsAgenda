package com.contactspring.mybatis.form;

import java.io.Serializable;

import com.contactspring.annotations.ValidPassword;
import com.contactspring.annotations.ValidUsername;
import com.contactspring.mybatis.generate.model.UserSecurity;

import lombok.Data;

@Data
public class ValidatedUserSecurity extends UserSecurity implements Serializable{

	@ValidUsername
	private String username;
    
    @ValidPassword
    private String password;

}
