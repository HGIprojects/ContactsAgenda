package com.contactspring.config;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.contactspring.mybatis.generate.map.UserHistoryMapper;
import com.contactspring.mybatis.generate.model.UserHistory;
import com.contactspring.mybatis.generate.model.UserProperties;
import com.contactspring.mybatis.original.QueryMapper;
import com.contactspring.mybatis.service.LoginService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

	private final LoginService loginService;
	private final UserHistoryMapper userHistoryMapper;
	private final QueryMapper queryMapper;
	
	@Autowired
	public CustomSuccessHandler(LoginService loginService, UserHistoryMapper userHistoryMapper, QueryMapper queryMapper) {
		this.loginService = loginService;
		this.userHistoryMapper = userHistoryMapper;
		this.queryMapper = queryMapper;
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
						Authentication authentication) throws IOException, ServletException {

		UserHistory userHistory = new UserHistory();
		UserProperties userProperties = new UserProperties();

		String redirectUrl = null;
		
		Boolean userHisExists = queryMapper.usernameExistsHis(authentication.getName());
		if (userHisExists) {
			userHistory = queryMapper.findByUsernameHis(authentication.getName());
			userHistory.setLastLogin(LocalDateTime.now());
			userHistoryMapper.updateByPrimaryKey(userHistory);
		} else {
			UserProperties userPro = new UserProperties();
			userPro = queryMapper.findByUsernamePro(authentication.getName());
			userHistory.setUsername(authentication.getName());
			userHistory.setDateAdded(userPro.getDateAdded());
			userHistory.setLastLogin(LocalDateTime.now());
			userHistoryMapper.insert(userHistory);
		}
		
		for (GrantedAuthority authority : authentication.getAuthorities()) {
			if (authority.getAuthority().equals("ROLE_PRODUCTION")) {
				redirectUrl = "/Production";
				break;
			} else if (authority.getAuthority().equals("ROLE_DEMO")) {
				redirectUrl = "/Demo";
				break;
			} else if (authority.getAuthority().equals("ROLE_ADMIN")) {
				redirectUrl = "/Management";
				break;
			} 
		}
		
		if (redirectUrl != null) {
			response.sendRedirect(redirectUrl);
		} else {
			throw new IllegalStateException();
		}
		
	}
}
