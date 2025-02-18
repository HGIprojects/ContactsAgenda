package com.contactspring.mybatis.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.contactspring.mybatis.form.ValidatedUserSecurity;
import com.contactspring.mybatis.generate.map.UserBasicsMapper;
import com.contactspring.mybatis.generate.map.UserSecurityMapper;
import com.contactspring.mybatis.generate.model.UserBasics;
import com.contactspring.mybatis.generate.model.UserSecurity;
import com.contactspring.mybatis.original.EmailVerificationService;
import com.contactspring.mybatis.original.QueryMapper;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private QueryMapper queryMapper;
	@Autowired
	private EmailVerificationService emailService;
	@Autowired
	private UserBasicsMapper userBasicsMapper;
	@Autowired
	private UserSecurityMapper userSecurityMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DisabledException {
		
		Boolean userExists = queryMapper.usernameExistsSec(username);
		
		if (userExists) {
			Optional<UserSecurity> opt = userSecurityMapper.selectByPrimaryKey(queryMapper.getSecurityId(username));

			if (opt.isPresent()) {
				ValidatedUserSecurity userSec = queryMapper.findByUsernameSec(username) ;
				boolean userActive = queryMapper.searchActive(username);
				if (userSec == null) {
					System.out.println("Exception UserNameNotFound, the user " + username + " was not found");
					throw new UsernameNotFoundException("The user " + username + " was not found");
				}
				if (!userActive) {
					System.out.println("Exception DisabledException, the user " + username + " is not activated");
					int userId = queryMapper.getBasicsId(username);
					Optional<UserBasics> optBas = userBasicsMapper.selectByPrimaryKey(userId);
					emailService.notifyDeactivation(optBas.get().getEmail());
					throw new DisabledException("The user " + username + " is not activated");
				}
				
				String assignedRole = queryMapper.checkRole(username);
				Map<String, String> userDetails = queryMapper.userParams(username);
	
				return User.builder()
						.username(userDetails.get("username"))
						.password(userDetails.get("password"))
						.roles(userDetails.get("role"))
						.build();
				} else {
					System.out.println("Username not found in the database");
					throw new UsernameNotFoundException("The user " + username + " was not found");
				}		
			} else {
				System.out.println("Username not found in the database");
				throw new UsernameNotFoundException("The user " + username + " was not found");
		}
	}
}
