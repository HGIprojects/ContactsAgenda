package com.contactspring.controller;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactspring.mybatis.form.ValidatedUserProperties;
import com.contactspring.mybatis.form.ValidatedUserSecurity;
import com.contactspring.mybatis.generate.model.LoginInfo;
import com.contactspring.mybatis.service.LoginService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


////////////////////////////////////////////////////////////////////////////
// URL: http://localhost:8080/
////////////////////////////////////////////////////////////////////////////

@Controller
@RequestMapping("/")
public class LoginController implements Serializable {
	
	@Autowired
	private LoginService loginServicer;

    @ModelAttribute("userModel")
    public LoginInfo userModel() {
    	LoginInfo newUserInformation = new LoginInfo();
    	newUserInformation.setValidatedUserProperties(new ValidatedUserProperties());
    	newUserInformation.setValidatedUserSecurity(new ValidatedUserSecurity());
    	
        return newUserInformation;
    }
	
    @ModelAttribute("forgottenPassword")
    public ValidatedUserProperties forgottenPassword() {
    	ValidatedUserProperties userProPass = new ValidatedUserProperties();

    	return userProPass;
    }
    
    @ModelAttribute("forgottenUser")
    public ValidatedUserProperties forgottenUser() {
    	ValidatedUserProperties userProUser = new ValidatedUserProperties();
    	
    	return userProUser;
    }
    
////////////////////////////////////////////////////////////////////////////

	@GetMapping({"","/"})
	public String loginPage(@RequestParam (value = "error", required = false) String error, Model model, HttpSession session) {
		
		return loginServicer.loginPageService(error, model, session);
	}
	
////////////////////////////////////////////////////////////////////////////

	@PostMapping("/newUserF")
	public String newUserForm(@Valid @ModelAttribute("userModel") LoginInfo userModel, BindingResult bindingResult, 
				Model model) {

	    if (bindingResult.hasErrors()) {

	    	return loginServicer.newUserBindingErrors(model);
	    }			
		
		return loginServicer.creationService(userModel, bindingResult, model);
	}
	
////////////////////////////////////////////////////////////////////////////

	@GetMapping ("/activate")
	public String activateUser(@RequestParam("token") String token, Model model) {

			return loginServicer.activationService(token, model);
	}
	
////////////////////////////////////////////////////////////////////////////
	
	@GetMapping ("/passwordReset")
	public String passwordReset(@RequestParam("token") String token, Model model) {
		
			return loginServicer.passwordResetService(token,  model);
	}
	
////////////////////////////////////////////////////////////////////////////
		
	@PostMapping ("/setNewPassword")
	public String setNewPassword(@RequestParam("passwordToReset") String  password, @RequestParam("id") int id, Model model) {
	
		return loginServicer.newPasswordService(password, id, model);
		
	}

////////////////////////////////////////////////////////////////////////////
	
	@PostMapping ("/forgottenPasswordF")
	public String forgottenPassword(@Valid @ModelAttribute("forgottenPassword") ValidatedUserProperties forgottenPassword, BindingResult bindingResult, Model model) {
	   
		if (bindingResult.hasErrors()) {

			return loginServicer.forgottenPasswordBindingErrors(model);
	    }	
		
		return loginServicer.forgottenPasswordService(forgottenPassword, bindingResult, model);
			
		
	}
	
////////////////////////////////////////////////////////////////////////////

	@GetMapping ("/forgottenUsernameF")
	public String rememberUsername(@Valid @ModelAttribute("forgottenUser") ValidatedUserProperties forgottenUser, BindingResult bindingResult, Model model) {

	    if (bindingResult.hasErrors()) {

			return loginServicer.forgottenUserBindingErrors(model);
	    }
	    
		return loginServicer.forgottenUsernameService(forgottenUser, bindingResult, model);	
		
	}	
	

}
