package com.contactspring.controller;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactspring.mybatis.form.ValidatedUserBasics;
import com.contactspring.mybatis.form.ValidatedUserProperties;
import com.contactspring.mybatis.form.ValidatedUserSecurity;
import com.contactspring.mybatis.generate.map.UserPropertiesMapper;
import com.contactspring.mybatis.generate.map.UserSecurityMapper;
import com.contactspring.mybatis.generate.model.LoginInfo;
import com.contactspring.mybatis.original.EmailVerificationService;
import com.contactspring.mybatis.original.QueryMapper;
import com.contactspring.mybatis.service.LoginService;


////////////////////////////////////////////////////////////////////////////
// URL: http://localhost:8080/
////////////////////////////////////////////////////////////////////////////

@Controller
@RequestMapping("/")
public class LoginController implements Serializable {
	
	@Autowired
	private ServerFunctions serverFunctions;
	@Autowired
	private LoginService loginServicer;
	@Autowired
	private UserSecurityMapper userSecurityMapper;
	@Autowired
	private UserPropertiesMapper userPropertiesMapper;
	@Autowired
	private QueryMapper queryMapper;
	@Autowired
	private EmailVerificationService emailServicer;
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
    @ModelAttribute("loginInfo")
    public LoginInfo loginInfo() {
    	LoginInfo loginInformation = new LoginInfo();
    	loginInformation.setValidatedUserProperties(new ValidatedUserProperties());
    	loginInformation.setValidatedUserSecurity(new ValidatedUserSecurity());
        return loginInformation;
    }
	
////////////////////////////////////////////////////////////////////////////

	@GetMapping({"","/"})
	public String loginPage(@RequestParam (value = "error", required = false) String error, Model model) {
	    System.out.println("Login site OK!");
		model.addAttribute("passwordChange", false);
    	serverFunctions.tokenExpiration();
	    if (error != null) {
	    	model.addAttribute("error", true);
	    }
		return "Login/index-login";
	}
	
////////////////////////////////////////////////////////////////////////////

	@PostMapping("/newUserF")
	public String newUserForm(@RequestParam (required = true) String username, @RequestParam (required = true) String password, 
					@RequestParam (required = true) String email,  Model model) {

		if ((username.isBlank()) || (password.isBlank()) || (email.isBlank())) {
			model.addAttribute("action", "creationError");
			return "Login/index-action";
		} else {
			int defaultRole = 1; // default value = PRODUCTION
			boolean userUnique = queryMapper.isUniqueUsername(username);
			model.addAttribute("userUnique", userUnique);
			boolean emailUnique = queryMapper.isUniqueEmail(email);
			model.addAttribute("emailUnique", emailUnique);
			if (userUnique && emailUnique) {
				ValidatedUserSecurity userSec = new ValidatedUserSecurity();
				ValidatedUserProperties userPro = new ValidatedUserProperties();
				ValidatedUserBasics userBas = new ValidatedUserBasics();
				userSec.setUsername(username);
				userPro.setUsername(username);
				userBas.setUsername(username);
				userSec.setPassword(encoder.encode(password));
				userPro.setEmail(email);
				userBas.setEmail(email);
				userPro.setActive(false);
				String verificationToken = UUID.randomUUID().toString();
				userPro.setVerificationToken(verificationToken);
				userPro.setRoleId(defaultRole);
				userPro.setDateAdded(LocalDateTime.now());
				userPro.setTokenDate(LocalDateTime.now());
				loginServicer.setNewUserBas(userBas);
				loginServicer.setNewUserSec(userSec);
				loginServicer.setNewUserPro(userPro);
				model.addAttribute("action", "userCreated");
			} else {
				if (!userUnique) {
					System.out.println("Username duplicated");
					model.addAttribute("action", "usernameDuplicated");				
					return "Login/index-action";
				}
				if (!emailUnique) {
					System.out.println("Email duplicated");
					model.addAttribute("action", "emailDuplicated");
					return "Login/index-action";
				}
			}
			return "Login/index-action";
			}
	}
	
////////////////////////////////////////////////////////////////////////////

	@GetMapping ("/activate")
	public String activateUser(@RequestParam("token") String token, Model model) {

		ValidatedUserProperties user = queryMapper.searchToken(token);
		if (user != null) {
			user.setActive(true);
			user.setVerificationToken("Token validated - " + LocalDateTime.now());
			userPropertiesMapper.updateByPrimaryKey(user);
			model.addAttribute("action", "userActivated");
			System.out.println("User activation successful.");
			return "Login/index-action";
		} else {
			System.out.println("User activation failed");
			return "redirect:/error?invalidToken";	
		}
	}
	
////////////////////////////////////////////////////////////////////////////
	
	@GetMapping ("/passwordReset")
	public String passwordReset(@RequestParam("token") String token, Model model) {
		
		ValidatedUserProperties user = queryMapper.searchToken(token);
		if (user != null) {
			user.setVerificationToken("Token validated - " + LocalDateTime.now());
			userPropertiesMapper.updateByPrimaryKey(user);
			model.addAttribute("modifyUser", user);
			ValidatedUserSecurity userSec = new ValidatedUserSecurity();
			userSec = queryMapper.findByUsernameSec(user.getUsername());
			model.addAttribute("secUser", userSec);
			model.addAttribute("passwordChange", true);
			return "Login/index-login";
		} else {
			return "redirect:/error?invalidToken";
		}
	}
		
////////////////////////////////////////////////////////////////////////////
		
	@PostMapping ("/setNewPassword")
	public String setNewPassword(@RequestParam("passwordToReset") String  password, @RequestParam("id") int id, Model model) {
	
		ValidatedUserProperties userPro = new ValidatedUserProperties();
		ValidatedUserSecurity userSec = new ValidatedUserSecurity();
		userSec = queryMapper.findByIdSec(id);
		userSec.setPassword(encoder.encode(password));
		userPro = queryMapper.findByUsernamePro(userSec.getUsername());
		userPro.setActive(true);
		userPropertiesMapper.updateByPrimaryKey(userPro);
		userSecurityMapper.updateByPrimaryKey(userSec);
		model.addAttribute("passwordChange", false);
		return "Login/index-login";
		
	}

////////////////////////////////////////////////////////////////////////////
	
	@PostMapping ("/forgottenPasswordF")
	public String resetPassword(@RequestParam("emailForPassword") String email, Model model) {
		
		ValidatedUserProperties userPro = new ValidatedUserProperties();
		ValidatedUserSecurity userSec = new ValidatedUserSecurity();
		
		boolean emailExists = queryMapper.emailExistsPro(email);
		if (emailExists) {
			userPro = queryMapper.findByEmailPro(email);
			
			String verificationToken = UUID.randomUUID().toString();
			userPro.setVerificationToken(verificationToken);
			userPro.setTokenDate(LocalDateTime.now());
			if (userPro.getActive()) {
				userPro.setActive(false);
			}
			emailServicer.passwordResetEmail(email, verificationToken);
			userPropertiesMapper.updateByPrimaryKey(userPro);
			userSec = queryMapper.findByUsernameSec(userPro.getUsername());
			userSec.setPassword("");
			userSecurityMapper.updateByPrimaryKey(userSec);
			model.addAttribute("action", "passwordReset");
			return "Login/index-action";
		} else {
			System.out.println("The email inputted was not found in our system.");
			return "Error/notRegistered";
			
		}
	}
	
////////////////////////////////////////////////////////////////////////////

	@GetMapping ("/forgottenUsernameF")
	public String rememberUsername(@RequestParam("emailForUsername") String email, Model model) {

		boolean emailExists = queryMapper.emailExistsPro(email);
		if (emailExists) {
			ValidatedUserProperties userPro = new ValidatedUserProperties();
			userPro = queryMapper.findByEmailPro(email);
			emailServicer.sendUsernameOverEmail(email, userPro.getUsername());
			model.addAttribute("action", "usernameSent");
			return "Login/index-action";
		} else {
			System.out.println("The email inputted was not found in our system.");
			return "Error/notRegistered";	
		}
	}	
	

}
