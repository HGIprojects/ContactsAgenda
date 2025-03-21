package com.contactspring.mybatis.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.contactspring.mybatis.form.ValidatedUserBasics;
import com.contactspring.mybatis.form.ValidatedUserProperties;
import com.contactspring.mybatis.form.ValidatedUserSecurity;
import com.contactspring.mybatis.generate.map.UserBasicsMapper;
import com.contactspring.mybatis.generate.map.UserHistoryMapper;
import com.contactspring.mybatis.generate.map.UserPropertiesMapper;
import com.contactspring.mybatis.generate.map.UserSecurityMapper;
import com.contactspring.mybatis.generate.model.LoginInfo;
import com.contactspring.mybatis.generate.model.UserProperties;
import com.contactspring.mybatis.generate.model.UserSecurity;
import com.contactspring.mybatis.original.QueryMapper;

import jakarta.servlet.http.HttpSession;

@Service
public class LoginService{
	
	@Autowired
	private QueryMapper queryMapper;
	
	@Autowired
	private UserSecurityMapper userSecurityMapper;
	
	@Autowired
	private UserPropertiesMapper userPropertiesMapper;
	
	@Autowired
	private UserBasicsMapper userBasicsMapper;
	
	@Autowired
	private UserHistoryMapper userHistoryMapper;
	
	@Autowired
	private EmailVerificationService emailServicer;
	
	@Autowired
	private EmailVerificationService emailVerificationService;
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

	public ValidatedUserSecurity setNewUserSec(ValidatedUserSecurity userSec) {

		userSecurityMapper.insert(userSec);
		System.out.println("New user created, security table");
		return userSec;
	}

////////////////////////////////////////////////////////////////////////////

	public ValidatedUserProperties setNewUserPro(ValidatedUserProperties userPro) {
		
		userPropertiesMapper.insert(userPro);
		emailServicer.sendVerificationEmail(userPro.getEmail(), userPro.getVerificationToken());
		System.out.println("New user created, properties table");
		return userPro;
	}

////////////////////////////////////////////////////////////////////////////

	public ValidatedUserBasics setNewUserBas(ValidatedUserBasics userBas) {
		
		userBasicsMapper.insert(userBas);
		System.out.println("New user created, basics table");
		return userBas;
	}

////////////////////////////////////////////////////////////////////////////

	public String getRole(String username) {

		return queryMapper.checkRole(username);
	}
	
////////////////////////////////////////////////////////////////////////////

	@Scheduled(fixedRate = 5 * 60 * 1000) // every 5 min in milliseconds
	public void tokenExpiration() {
	
		int expiration = 900; //15 min token life
		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime expirationCut =currentTime.minusSeconds(expiration);
		List<ValidatedUserProperties> expiredTokens = queryMapper.getExpiredTokens(expirationCut);
		if (expiredTokens != null && !expiredTokens.isEmpty()){
			for (ValidatedUserProperties expiredUser : expiredTokens) {
				String userToDelete = expiredUser.getUsername();
				if (queryMapper.usernameExistsHis(userToDelete)) {
					System.out.println("The user " + userToDelete + " has an expired token and has accessed the service, it will now be deactivated.");
					UserProperties deactivatedUserPro = new UserProperties();
					deactivatedUserPro.setId(expiredUser.getId());
					deactivatedUserPro.setUsername(expiredUser.getUsername());
					deactivatedUserPro.setActive(false);
					deactivatedUserPro.setVerificationToken("Token revoked - " + LocalDateTime.now());
					userPropertiesMapper.updateByPrimaryKeySelective(deactivatedUserPro);
					System.out.println("User " + deactivatedUserPro.getUsername() + " has been deactivated.");
					UserSecurity deactivatedUserSec = new UserSecurity();
					deactivatedUserSec.setId(queryMapper.getSecurityId(expiredUser.getUsername()));
					deactivatedUserSec.setUsername(userToDelete);
					//set to "" instead of null because of the not-null DB policy
					deactivatedUserSec.setPassword("");
					userSecurityMapper.updateByPrimaryKey(deactivatedUserSec);
					System.out.println("User " + expiredUser.getUsername() + " has had its password revoked.");
					emailVerificationService.sendDeactivationMail(expiredUser.getEmail());
				} else {
					Integer hisId = queryMapper.getHistoryId(userToDelete);
					userHistoryMapper.deleteByPrimaryKey(hisId);
					emailVerificationService.sendExpirationMail(expiredUser.getEmail());
					System.out.println("The user " + userToDelete + " has an expired token and never logged in, it will now be deleted.");
					int proId = expiredUser.getId();
					userPropertiesMapper.deleteByPrimaryKey(proId);
					int basId = queryMapper.getBasicsId(userToDelete);
					userBasicsMapper.deleteByPrimaryKey(basId);
					int secId = queryMapper.getSecurityId(userToDelete);
					userSecurityMapper.deleteByPrimaryKey(secId);
				}
			}
			System.out.println("All expired tokens have been deleted");
			} else {
				System.out.println("No expired tokens were found.");
			}
		}
	

//////////////////////
// Controller methods
//////////////////////
	
	public String loginPageService(String error, Model model, HttpSession session) {
		
		model.addAttribute("newUser", false);
		model.addAttribute("forgotPassword", false);
		model.addAttribute("forgotUser", false);
	    System.out.println("Login site OK!");
		model.addAttribute("passwordChange", false);
		session.setAttribute("whereAmIDTO", "loginPage");
		
		tokenExpiration();
	    if (error != null) {
	    	model.addAttribute("error", true);
	    }
		return "Login/index-login";
	}
	
////////////////////////////////////////////////////////////////////////////
	
	public String loginNewUserService(Model model) {
		
		model.addAttribute("newUser", true);
		model.addAttribute("forgotPassword", false);
		model.addAttribute("forgotUser", false);
		System.out.println("Login site OK!");
		model.addAttribute("passwordChange", false);
		tokenExpiration();

		return "Login/index-login";
	}
	
////////////////////////////////////////////////////////////////////////////
	
	public String loginForgotPasswordService(Model model) {
		
		model.addAttribute("newUser", false);
		model.addAttribute("forgotPassword", true);
		model.addAttribute("forgotUser", false);
		System.out.println("Login site OK!");
		model.addAttribute("passwordChange", false);
		tokenExpiration();

		return "Login/index-login";
	}
	
////////////////////////////////////////////////////////////////////////////
	
	public String loginForgotUserService(Model model) {
		
		model.addAttribute("newUser", false);
		model.addAttribute("forgotPassword", false);
		model.addAttribute("forgotUser", true);
		System.out.println("Login site OK!");
		model.addAttribute("passwordChange", false);
		tokenExpiration();

		return "Login/index-login";
	}
	
////////////////////////////////////////////////////////////////////////////
	
	public String creationService(LoginInfo userModel, BindingResult bindingResult,  Model model) {
		
		model.addAttribute("passwordChange", false);

		if ((userModel.getValidatedUserSecurity().getUsername().isBlank()) || (userModel.getValidatedUserSecurity().getPassword().isBlank()) || 
				(userModel.getValidatedUserProperties().getEmail().isBlank())) {
			model.addAttribute("action", "creationError");
			return "Login/index-action";
		} else {
			int defaultRole = 1; // default value = PRODUCTION
			boolean userUnique = queryMapper.isUniqueUsernamePro(userModel.getValidatedUserSecurity().getUsername());
			model.addAttribute("userUnique", userUnique);
			boolean emailUnique = queryMapper.isUniqueEmailPro(userModel.getValidatedUserProperties().getEmail());
			model.addAttribute("emailUnique", emailUnique);
			if (userUnique && emailUnique) {
				ValidatedUserSecurity userSec = new ValidatedUserSecurity();
				ValidatedUserProperties userPro = new ValidatedUserProperties();
				ValidatedUserBasics userBas = new ValidatedUserBasics();
				userSec.setUsername(userModel.getValidatedUserSecurity().getUsername());
				userPro.setUsername(userModel.getValidatedUserSecurity().getUsername());
				userBas.setUsername(userModel.getValidatedUserSecurity().getUsername());
				userSec.setPassword(encoder.encode(userModel.getValidatedUserSecurity().getPassword()));
				userPro.setEmail(userModel.getValidatedUserProperties().getEmail());
				userBas.setEmail(userModel.getValidatedUserProperties().getEmail());
				userPro.setActive(false);
				String verificationToken = UUID.randomUUID().toString();
				userPro.setVerificationToken(verificationToken);
				userPro.setRoleId(defaultRole);
				userPro.setDateAdded(LocalDateTime.now());
				userPro.setTokenDate(LocalDateTime.now());
				setNewUserBas(userBas);
				setNewUserSec(userSec);
				setNewUserPro(userPro);
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
			
		    if (bindingResult.hasErrors()) {
		    	model.addAttribute("newUser", true);

				return "Login/index-login";
		    }
			
			return "Login/index-action";
			}
		}
	
////////////////////////////////////////////////////////////////////////////
	
	public String newUserBindingErrors(Model model) {
		
		model.addAttribute("newUser", true);
		model.addAttribute("forgotPassword", false);
		model.addAttribute("forgotUser", false);		
		model.addAttribute("passwordChange", false);

		return "Login/index-login";
	}
	
////////////////////////////////////////////////////////////////////////////

	public String forgottenPasswordBindingErrors(Model model) {
		
		model.addAttribute("newUser", false);
		model.addAttribute("forgotPassword", true);
		model.addAttribute("forgotUser", false);
		model.addAttribute("passwordChange", false);
		
		return "Login/index-login";
	}
	
////////////////////////////////////////////////////////////////////////////

	public String forgottenUserBindingErrors(Model model) {
		
		model.addAttribute("newUser", false);
		model.addAttribute("forgotPassword", false);
		model.addAttribute("forgotUser", true);
		model.addAttribute("passwordChange", false);
		
		return "Login/index-login";
	}
	
////////////////////////////////////////////////////////////////////////////
	
	public String activationService(String token, Model model) {
		
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

	public String passwordResetService(String token, Model model) {
		
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

	public String newPasswordService(String  password, int id, Model model) {
		
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

	public String forgottenPasswordService(ValidatedUserProperties forgottenPassword, BindingResult bindingResultl, Model model) {
		
		ValidatedUserProperties userPro = new ValidatedUserProperties();
		ValidatedUserSecurity userSec = new ValidatedUserSecurity();
		
		boolean emailExists = queryMapper.emailExistsPro(forgottenPassword.getEmail());
		if (emailExists) {
			userPro = queryMapper.findByEmailPro(forgottenPassword.getEmail());
			
			String verificationToken = UUID.randomUUID().toString();
			userPro.setVerificationToken(verificationToken);
			userPro.setTokenDate(LocalDateTime.now());
			emailServicer.passwordResetEmail(forgottenPassword.getEmail(), verificationToken);
			if (userPro.getActive()) {
				userPro.setActive(false);
			}
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

	public String forgottenUsernameService(ValidatedUserProperties forgottenUser, BindingResult bindingResultl, Model model) {
		
		boolean emailExists = queryMapper.emailExistsPro(forgottenUser.getEmail());
		if (emailExists) {
			ValidatedUserProperties userPro = new ValidatedUserProperties();
			userPro = queryMapper.findByEmailPro(forgottenUser.getEmail());
			emailServicer.sendUsernameOverEmail(forgottenUser.getEmail(), userPro.getUsername());
			model.addAttribute("action", "usernameSent");
			
			return "Login/index-action";
		} else {
			System.out.println("The email inputted was not found in our system.");
			
			return "Error/notRegistered";	
		}
	}
	
}
