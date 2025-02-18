package com.contactspring.mybatis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contactspring.mybatis.form.ValidatedUserBasics;
import com.contactspring.mybatis.form.ValidatedUserProperties;
import com.contactspring.mybatis.form.ValidatedUserSecurity;
import com.contactspring.mybatis.generate.map.UserBasicsMapper;
import com.contactspring.mybatis.generate.map.UserPropertiesMapper;
import com.contactspring.mybatis.generate.map.UserSecurityMapper;
import com.contactspring.mybatis.original.EmailVerificationService;
import com.contactspring.mybatis.original.QueryMapper;

@Service
public class LoginService{
/*	
	@Autowired
	private UserRepo repo;
*/	
	@Autowired
	private QueryMapper queryMapper;
	
	@Autowired
	private UserSecurityMapper userSecurityMapper;
	
	@Autowired
	private UserPropertiesMapper userPropertiesMapper;
	
	@Autowired
	private UserBasicsMapper userBasicsMapper;
	
	@Autowired
	private EmailVerificationService emailServicer;
	
	public ValidatedUserSecurity setNewUserSec(ValidatedUserSecurity userSec) {

		userSecurityMapper.insert(userSec);
		System.out.println("New user created, security table");
		return userSec;
	}
	
	public ValidatedUserProperties setNewUserPro(ValidatedUserProperties userPro) {
		
		userPropertiesMapper.insert(userPro);
		emailServicer.sendVerificationEmail(userPro.getEmail(), userPro.getVerificationToken());
		System.out.println("New user created, properties table");
		return userPro;
	}
	
	public ValidatedUserBasics setNewUserBas(ValidatedUserBasics userBas) {
		
		userBasicsMapper.insert(userBas);
		System.out.println("New user created, basics table");
		return userBas;
	}
	
	public String whatRoleWithUser(String username) {
		System.out.println("User " + username + " trying to access the service");
		String userRole = queryMapper.checkRole(username);
		System.out.println("The user's role that is trying to access is: " + userRole);
		return userRole;
	}
	
}
