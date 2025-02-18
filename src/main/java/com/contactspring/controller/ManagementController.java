package com.contactspring.controller;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactspring.config.SecurityConfig;
import com.contactspring.mybatis.form.ValidatedUserProperties;
import com.contactspring.mybatis.form.ValidatedUserSecurity;
import com.contactspring.mybatis.generate.map.ContactFieldsMapper;
import com.contactspring.mybatis.generate.map.UserPropertiesMapper;
import com.contactspring.mybatis.generate.map.UserSecurityMapper;
import com.contactspring.mybatis.generate.model.UserProperties;
import com.contactspring.mybatis.original.EmailVerificationService;
import com.contactspring.mybatis.original.QueryMapper;
import com.contactspring.mybatis.service.ContactsService;
import com.contactspring.mybatis.service.UserPropertiesService;

import jakarta.validation.Valid;

////////////////////////////////////////////////////////////////////////////
// URL: http://localhost:8080/ContactsController
////////////////////////////////////////////////////////////////////////////

@Controller
@RequestMapping("/Management")
public class ManagementController implements Serializable {
	
	@Autowired
	private ContactFieldsMapper contactMapper;
	@Autowired
	private UserPropertiesMapper userPropertiesMapper;
	@Autowired
	private ContactsService contactsServicer;
	@Autowired
	private UserPropertiesService userPropertiesServicer;
	@Autowired
	private UserSecurityMapper userSecurityMapper;
	@Autowired
    private ServerFunctions serverFunctions;
	@Autowired
	private QueryMapper queryMapper;
	@Autowired
	private SecurityConfig secConf;
	@Autowired
	private EmailVerificationService emailService;
	
	private boolean isDemo = false;
	
	private int usersPerPage = 3;
	

    @ModelAttribute("userInfo")
    public ValidatedUserProperties userInfo() {
    	ValidatedUserProperties userInformation = new ValidatedUserProperties();
        return userInformation;
    }
 
////////////////////////////////////////////////////////////////////////////

	@GetMapping({"","/"})
	public String index(Model model) {
		int page = 1;
		model.addAttribute("newUser", false);
	    model.addAttribute("viewUser", false);
	    model.addAttribute("modifyUser", false);
	    model.addAttribute("searchUser", false); 
	    model.addAttribute("deleteUser", false); 
		
		Double totalUsers = (double) queryMapper.usersCounter();
		Double totalPages = totalUsers / usersPerPage;
		int roundedTotalPages = (int)Math.ceil(totalPages);
		model.addAttribute("totalPages", roundedTotalPages);
		model.addAttribute("currentPage", page);
		if (page == 1) {
			model.addAttribute("previousPage", page);
		} else {
			model.addAttribute("previousPage", page - 1);
		}
		if (page == roundedTotalPages) {
			model.addAttribute("nextPage", page);
		} else {
			model.addAttribute("nextPage", page + 1);
		}
		List<ValidatedUserProperties> pageUsers = userPropertiesServicer.pageDisplay(usersPerPage, page);
		model.addAttribute("users", pageUsers);

		secConf.credentialsChecker(model);
		serverFunctions.tokenExpiration();
		System.out.println("Management site OK!");
		
		return "Management/index-manage";
	}
	
////////////////////////////////////////////////////////////////////////////
	
	@PostMapping("/modifyUserF") 
	public String modifyUserForm(@Valid @RequestParam int id, @RequestParam String action, Model model) { 
	
		model.addAttribute("newUser", false);
		model.addAttribute("viewUser", false);
		model.addAttribute("modifyUser", true);
		model.addAttribute("searchUser", false);
		model.addAttribute("deleteUser", false); 
		secConf.credentialsChecker(model);
		
		Optional<UserProperties> opt = userPropertiesMapper.selectByPrimaryKey(id);
		if (opt.isPresent()) {
			userPropertiesServicer.optionInitializer(action, model);
			UserProperties clickedUser = opt.get();
			model.addAttribute("selectedUser", clickedUser);
			model.addAttribute("id", clickedUser.getId());
			System.out.println("The id is " + clickedUser.getId());
		} else {
			throw new IllegalArgumentException("User with ID " + id + " does not exist when retrieved by modifyForm()");
		}
		return "Management/index-view";
	}

////////////////////////////////////////////////////////////////////////////

	@PostMapping("/updateUserF") 
	public String updateForm(@Valid @ModelAttribute ValidatedUserProperties selectedContact, BindingResult bindingResult, Model model) { 
		
		int role = 1; //by default set to USER
		System.out.println("ID to modify: " + selectedContact.getId());
		System.out.println("Username to modify: " + selectedContact.getUsername());
		System.out.println("Email to modify: " + selectedContact.getEmail());
		System.out.println("Role to modify: " + selectedContact.getRoleId());
		System.out.println("Active status: " + selectedContact.getActive());
		secConf.credentialsChecker(model);
		
		return serverFunctions.userModification(selectedContact, bindingResult, model);
		}

////////////////////////////////////////////////////////////////////////////
	
	@PostMapping("/searchUserF")
	public String searchUserForm(@ModelAttribute ValidatedUserProperties userInfo, BindingResult bindingResult, Model model) {
		
		model.addAttribute("newUser", false);
		model.addAttribute("viewUser", false);
		model.addAttribute("modifyUser", false);
		model.addAttribute("searchUser", true);
		model.addAttribute("deleteUser", false); 
		secConf.credentialsChecker(model);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errors", bindingResult.getAllErrors());
			return "Management/index-manage";  
		}
		return serverFunctions.userSearcherFunction(userInfo, bindingResult, model);	
	}
	
	
////////////////////////////////////////////////////////////////////////////


	@PostMapping("/deleteUserF") 
	public String deleteForm(@RequestParam int id, Model model) { 
	
		model.addAttribute("newUser", false);
		model.addAttribute("viewUser", false);
		model.addAttribute("modifyUser", false);
		model.addAttribute("searchUser", false);
		model.addAttribute("deleteUser", true); 
		secConf.credentialsChecker(model);
		String result = serverFunctions.userEraserFunction(id, model);
		
		return result;
	}

	////////////////////////////////////////////////////////////////////////////
	
	@PostMapping("/viewUserF") 
	public String viewUserForm(@RequestParam int id, @RequestParam String action, Model model) { 

		secConf.credentialsChecker(model);

		Optional<UserProperties> opt = userPropertiesMapper.selectByPrimaryKey(id);
		if (opt.isPresent()) {
			userPropertiesServicer.optionInitializer(action, model);
			UserProperties clickedUser = opt.get();
			model.addAttribute("selectedUser", clickedUser);
			model.addAttribute("id", clickedUser.getId());
		} else {
			throw new IllegalArgumentException("User with ID " + id + " does not exist when retrieved by viewForm()");
		}
		return "Management/index-view";
	
	}
	
////////////////////////////////////////////////////////////////////////////
		
	@GetMapping("/navigate") 
	public String paginationClicked(@RequestParam(required = true) int page, Model model) {
	
		model.addAttribute("newUser", false);
		model.addAttribute("viewUser", false);
		model.addAttribute("modifyUser", false);
		model.addAttribute("searchUser", false); 
		model.addAttribute("deleteUser", false); 
		
		Double totalUsers = (double) queryMapper.usersCounter();
		Double totalPages = totalUsers / usersPerPage;
		int roundedTotalPages = (int)Math.ceil(totalPages);
		model.addAttribute("totalPages", roundedTotalPages);
		model.addAttribute("currentPage", page);
		if (page == 1) {
			model.addAttribute("previousPage", page);
		} else {
			model.addAttribute("previousPage", page - 1);
		}
		if (page == roundedTotalPages) {
			model.addAttribute("nextPage", page);
		} else {
			model.addAttribute("nextPage", page + 1);
		}

		List<ValidatedUserProperties> pageUsers = userPropertiesServicer.pageDisplay(usersPerPage, page);
		model.addAttribute("users", pageUsers);
		
		secConf.credentialsChecker(model);
		System.out.println("Website OK!");
		return "Management/index-manage";
	}

////////////////////////////////////////////////////////////////////////////

	@PostMapping("/resetPassword")
	public String userPasswordReset(@RequestParam int id, Model model) {
		
		secConf.credentialsChecker(model);
		model.addAttribute("id", id);
		Optional<UserProperties> opt = userPropertiesMapper.selectByPrimaryKey(id);
		if (opt.isPresent()) {
			UserProperties userPro = opt.get();
			String verificationToken = UUID.randomUUID().toString();
			emailService.passwordResetEmail(userPro.getEmail(), verificationToken);
			userPro.setActive(false);
			userPro.setVerificationToken(verificationToken);
			userPro.setTokenDate(LocalDateTime.now());
			userPropertiesMapper.updateByPrimaryKey(userPro);
			ValidatedUserSecurity userSec = new ValidatedUserSecurity();
			userSec = queryMapper.findByUsernameSec(userPro.getUsername());
			userSec.setPassword("");
			userSecurityMapper.updateByPrimaryKey(userSec);
			model.addAttribute("selectedUser", userPro);
			model.addAttribute("id", userPro.getId());
		} else {
			throw new IllegalArgumentException("User with ID " + id + " does not exist when retrieved by viewForm()");
		}
		model.addAttribute("newUser", false);
		model.addAttribute("viewUser", false);
		model.addAttribute("modifyUser", true);
		model.addAttribute("searchUser", false);
		model.addAttribute("deleteUser", false); 
		return "Management/index-view";
	}
	
////////////////////////////////////////////////////////////////////////////

	@PostMapping("/resendActivationMail")
	public String resendActivation(@RequestParam int id, Model model) {
		
		secConf.credentialsChecker(model);
		model.addAttribute("id", id);
		Optional<UserProperties> opt = userPropertiesMapper.selectByPrimaryKey(id);
		model.addAttribute("selectedUser", opt.get());
		
		UserProperties userPro = new UserProperties();
		userPro.setId(id);
		userPro.setActive(false);
		String verificationToken = UUID.randomUUID().toString();
		userPro.setVerificationToken(verificationToken);
		userPropertiesMapper.updateByPrimaryKeySelective(userPro);
		emailService.sendVerificationEmail(opt.get().getEmail(), verificationToken);
		
		model.addAttribute("newUser", false);
		model.addAttribute("viewUser", false);
		model.addAttribute("modifyUser", true);
		model.addAttribute("searchUser", false);
		model.addAttribute("deleteUser", false); 
		return "Management/index-view";

	}
}	
