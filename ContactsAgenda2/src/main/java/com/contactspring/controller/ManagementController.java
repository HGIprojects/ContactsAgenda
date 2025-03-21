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
import com.contactspring.mybatis.service.ManagementService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

////////////////////////////////////////////////////////////////////////////
// URL: http://localhost:8080/ContactsController
////////////////////////////////////////////////////////////////////////////

@Controller
@RequestMapping("/Management")
public class ManagementController implements Serializable {
	
	@Autowired
	private ManagementService managementServicer;
	private int usersPerPage = 3;
	

    @ModelAttribute("userInfo")
    public ValidatedUserProperties userInfo() {
    	ValidatedUserProperties userInformation = new ValidatedUserProperties();
        return userInformation;
    }
 
////////////////////////////////////////////////////////////////////////////
    
    @ModelAttribute("selectedUser")
    public ValidatedUserProperties selectedUser() {
    	ValidatedUserProperties userInformation = new ValidatedUserProperties();
    	return userInformation;
    }
    
////////////////////////////////////////////////////////////////////////////

	@GetMapping({"","/"})
	public String index(Model model, HttpSession session) {
		
		return managementServicer.indexService(model, usersPerPage, session);
	}
	
////////////////////////////////////////////////////////////////////////////
	
	@PostMapping("/modifyUserF") 
	public String modifyUserForm(@RequestParam int id, @RequestParam String action, Model model, HttpSession session) { 
	
		return managementServicer.modifyUserFormService(id, action, model, session);
	}

////////////////////////////////////////////////////////////////////////////

	@PostMapping("/updateUserF") 
	public String updateForm(@Valid @ModelAttribute("selectedUser") ValidatedUserProperties selectedContact, BindingResult bindingResult, Model model) { 

	    if (bindingResult.hasErrors()) {

	    	return managementServicer.returnToView(selectedContact.getId(), model);
	    }	
	    
		return managementServicer.updateFormService(selectedContact, bindingResult, model);
	}

////////////////////////////////////////////////////////////////////////////
	
	@PostMapping("/searchUserF")
	public String searchUserForm(@ModelAttribute ValidatedUserProperties userInfo, BindingResult bindingResult, Model model) {
		
		return managementServicer.searchUserFormService(userInfo, bindingResult, model);	
	}
	
////////////////////////////////////////////////////////////////////////////

	@PostMapping("/deleteUserF") 
	public String deleteForm(@RequestParam int id, Model model) { 
	
		return managementServicer.deleteFormService(id, model);
	}

////////////////////////////////////////////////////////////////////////////
	
	@PostMapping("/viewUserF") 
	public String viewUserForm(@RequestParam int id, @RequestParam String action, Model model) { 

		return managementServicer.viewUserFormService(id, action, model);
	}
	
////////////////////////////////////////////////////////////////////////////
		
	@GetMapping("/navigate") 
	public String paginationClicked(@RequestParam(required = true) int page, Model model) {
	
		return managementServicer.paginationClickedService(page, model, usersPerPage);
	}

////////////////////////////////////////////////////////////////////////////

	@PostMapping("/resetPassword")
	public String userPasswordReset(@RequestParam int id, Model model) {
		
		return managementServicer.userPasswordResetService(id, model);
	}
	
////////////////////////////////////////////////////////////////////////////

	@PostMapping("/resendActivationMail")
	public String resendActivation(@RequestParam int id, Model model) {

		return managementServicer.resendActivationService(id, model);
	}
}	
