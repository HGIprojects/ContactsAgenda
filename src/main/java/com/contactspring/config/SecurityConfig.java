package com.contactspring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.ui.Model;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Autowired
    private UserDetailsService userDetailsService;
   
 	@Bean 
 	public AuthenticationProvider authenticationProvider() {
 		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
 		provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
 		provider.setUserDetailsService(userDetailsService);
 		return provider;
 	}
 	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity http, CustomSuccessHandler successHandler) throws Exception{
		
		http
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/Production/**").hasAnyAuthority("ROLE_PRODUCTION", "ROLE_ADMIN")
					.requestMatchers("/Demo/**").hasAnyAuthority("ROLE_DEMO", "ROLE_ADMIN")
					.requestMatchers("/Management/**").hasAuthority("ROLE_ADMIN")
				    .requestMatchers("/**").permitAll()
				    .anyRequest().authenticated()
				    
			)
			.formLogin(form -> form
				.loginPage("/")
				.permitAll()
				.failureUrl("/error=true")
				.successHandler(successHandler)
			)
			.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/")
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.deleteCookies("JSESSIONID")
			)
			.exceptionHandling(e -> e
				.accessDeniedPage("/error/illegal-access"));
		
		return http.build();
	}

	public void credentialsChecker(Model model) {

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null) {
	        model.addAttribute("authRole", authentication.getAuthorities());
	        model.addAttribute("username", authentication.getName());
	        System.out.println("Credentials checked");
	    } else {
	        System.out.println("Authentication is null");
	    }	
	}
}

