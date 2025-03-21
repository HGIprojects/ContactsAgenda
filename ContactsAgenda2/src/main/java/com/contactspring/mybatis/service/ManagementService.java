package com.contactspring.mybatis.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.contactspring.config.SecurityConfig;
import com.contactspring.mybatis.form.ValidatedUserProperties;
import com.contactspring.mybatis.form.ValidatedUserSecurity;
import com.contactspring.mybatis.generate.map.ContactFieldsMapper;
import com.contactspring.mybatis.generate.map.UserPropertiesMapper;
import com.contactspring.mybatis.generate.map.UserSecurityMapper;
import com.contactspring.mybatis.generate.model.UserProperties;
import com.contactspring.mybatis.original.QueryMapper;

import jakarta.servlet.http.HttpSession;

@Service
public class ManagementService {

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
	@Autowired
	private LoginService loginServicer;
	
	public String indexService(Model model, int usersPerPage, HttpSession session) {
		
		session.setAttribute("whereAmIDTO", "managementPage");
		int page = 1;
		model.addAttribute("newUser", false);
	    model.addAttribute("viewUser", false);
	    model.addAttribute("modifyUser", false);
	    model.addAttribute("searchUser", false); 
	    model.addAttribute("deleteUser", false); 
	    Object username = model.getAttribute("username");
		
		Double totalUsers = (double) queryMapper.usersCounter(username);
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
		List<ValidatedUserProperties> pageUsers = userPropertiesServicer.pageDisplay(usersPerPage, page, username);
		model.addAttribute("users", pageUsers);

		secConf.credentialsChecker(model);
		loginServicer.tokenExpiration();
		System.out.println("Management site OK!");
		
		return "Management/index-manage";
	}
	
////////////////////////////////////////////////////////////////////////////

	public String modifyUserFormService(int id, String action, Model model, HttpSession session) { 
		
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
			session.setAttribute("selectedUser", clickedUser);	
			model.addAttribute("id", clickedUser.getId());
			model.addAttribute("titleUsername", queryMapper.getProUsernameFromId(id));
			model.addAttribute("titleEmail", queryMapper.getProEmailFromId(id));
		} else {
			throw new IllegalArgumentException("User with ID " + id + " does not exist when retrieved by modifyForm()");
		}
		return "Management/index-view";
	}
	
////////////////////////////////////////////////////////////////////////////
	
	@Transactional
	public String updateFormService(ValidatedUserProperties selectedContact, BindingResult bindingResult, Model model) { 
		
		int role = 1; //by default set to USER
		System.out.println("ID to modify: " + selectedContact.getId());
		System.out.println("Username to modify: " + selectedContact.getUsername());
		System.out.println("Email to modify: " + selectedContact.getEmail());
		System.out.println("Role to modify: " + selectedContact.getRoleId());
		System.out.println("Active status: " + selectedContact.getActive());
		secConf.credentialsChecker(model);
		model.addAttribute("titleUsername", queryMapper.getProUsernameFromId(selectedContact.getId()));
		model.addAttribute("titleEmail", queryMapper.getProEmailFromId(selectedContact.getId()));
		
		return serverFunctions.userModification(selectedContact, bindingResult, model);
	}
	
////////////////////////////////////////////////////////////////////////////

	public String searchUserFormService(ValidatedUserProperties userInfo, BindingResult bindingResult, Model model) {
		
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
	
	@Transactional
	public String deleteFormService(int id, Model model) { 
	
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
	
	public String viewUserFormService(int id, String action, Model model) { 

		secConf.credentialsChecker(model);

		Optional<UserProperties> opt = userPropertiesMapper.selectByPrimaryKey(id);
		if (opt.isPresent()) {
			userPropertiesServicer.optionInitializer(action, model);
			UserProperties clickedUser = opt.get();
			model.addAttribute("selectedUser", clickedUser);
			model.addAttribute("titleUser", clickedUser);
			model.addAttribute("id", clickedUser.getId());
			model.addAttribute("titleUsername", queryMapper.getProUsernameFromId(id));
			model.addAttribute("titleEmail", queryMapper.getProEmailFromId(id));

		} else {
			throw new IllegalArgumentException("User with ID " + id + " does not exist when retrieved by viewForm()");
		}
		
		return "Management/index-view";
	}
	
////////////////////////////////////////////////////////////////////////////
	
	public String paginationClickedService(int page, Model model, int usersPerPage) {
		
		model.addAttribute("newUser", false);
		model.addAttribute("viewUser", false);
		model.addAttribute("modifyUser", false);
		model.addAttribute("searchUser", false); 
		model.addAttribute("deleteUser", false); 
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    System.out.println("El usuario de turno es: " + username);

		Double totalUsers = (double) queryMapper.usersCounter(username);
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

		List<ValidatedUserProperties> pageUsers = userPropertiesServicer.pageDisplay(usersPerPage, page, username);
		model.addAttribute("users", pageUsers);
		
		secConf.credentialsChecker(model);
		System.out.println("Website OK!");
		
		return "Management/index-manage";
	}
	
////////////////////////////////////////////////////////////////////////////

	@Transactional
	public String userPasswordResetService(int id, Model model) {
		
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
	
	@Transactional
	public String resendActivationService(int id, Model model) {
		
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

	public String returnToView(int id, Model model) {
		
		model.addAttribute("newUser", false);
		model.addAttribute("viewUser", false);
		model.addAttribute("modifyUser", true);
		model.addAttribute("searchUser", false);
		model.addAttribute("deleteUser", false); 
		secConf.credentialsChecker(model);
		model.addAttribute("titleUsername", queryMapper.getProUsernameFromId(id));
		model.addAttribute("titleEmail", queryMapper.getProEmailFromId(id));

		return "Management/index-view";
	}
	
////////////////////////////////////////////////////////////////////////////

	
	
	
	
	
	
}
