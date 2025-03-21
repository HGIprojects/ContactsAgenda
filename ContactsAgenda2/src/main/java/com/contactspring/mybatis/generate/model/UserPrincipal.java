package com.contactspring.mybatis.generate.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

//principal es el usuario en uso
public class UserPrincipal implements UserDetails{

	private UserSecurity userSecurity;
	
	public UserPrincipal(UserSecurity userSecurity) {
		this.userSecurity = userSecurity;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return Collections.singleton(new SimpleGrantedAuthority("USER"));
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return userSecurity.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userSecurity.getUsername();
	}

}
