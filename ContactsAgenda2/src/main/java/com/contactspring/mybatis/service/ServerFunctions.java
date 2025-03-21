
package com.contactspring.mybatis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.contactspring.mybatis.form.ValidatedUserProperties;
import com.contactspring.mybatis.generate.map.UserBasicsMapper;
import com.contactspring.mybatis.generate.map.UserHistoryMapper;
import com.contactspring.mybatis.generate.map.UserPropertiesMapper;
import com.contactspring.mybatis.generate.map.UserSecurityMapper;
import com.contactspring.mybatis.generate.model.UserBasics;
import com.contactspring.mybatis.generate.model.UserHistory;
import com.contactspring.mybatis.generate.model.UserProperties;
import com.contactspring.mybatis.generate.model.UserSecurity;
import com.contactspring.mybatis.original.QueryMapper;

import jakarta.servlet.http.HttpSession;

@Component
public class ServerFunctions {

	@Autowired
	private UserPropertiesService userPropertiesServicer;
	@Autowired
	private UserSecurityMapper userSecurityMapper;   
	@Autowired
	private UserBasicsMapper userBasicsMapper;   
	@Autowired
	private UserHistoryMapper userHistoryMapper;   
	@Autowired
	private UserPropertiesMapper userPropertiesMapper;
	@Autowired
	private QueryMapper queryMapper;

	

	public String userEraserFunction(int id, Model model) { 

		Optional<UserProperties> opt = userPropertiesMapper.selectByPrimaryKey(id);
		
		if(opt.isPresent()) {
			UserProperties userPro = opt.get();
			UserBasics userBas = new UserBasics();
			UserSecurity userSec = new UserSecurity();
			UserHistory userHis = new UserHistory();
			userBas.setUsername(userPro.getUsername());
			userSec.setUsername(userPro.getUsername());
			userHis.setUsername(userPro.getUsername());
			System.out.println("User to delete with ID: " + id);
			System.out.println(userPro.getUsername()); 
			System.out.println(userPro.getEmail()); 
			System.out.println(userPropertiesServicer.convertIdToRoleText(userPro.getRoleId())); 
			model.addAttribute("user", opt.get());
			userPropertiesMapper.deleteByPrimaryKey(id);
			userBas.setId(queryMapper.getBasicsId(userBas.getUsername()));
			userBasicsMapper.deleteByPrimaryKey(userBas.getId());
			userSec.setId(queryMapper.getSecurityId(userSec.getUsername()));
			userSecurityMapper.deleteByPrimaryKey(userSec.getId());
			if (queryMapper.usernameExistsHis(userHis.getUsername())) {
				userHis.setId(queryMapper.getHistoryId(userHis.getUsername()));
				userHistoryMapper.deleteByPrimaryKey(userHis.getId());
			} else {
				System.out.println("This user never logged in.");
			}
			model.addAttribute("message", "User deletion successful, ID: " + id);
			model.addAttribute("resultType", "deletion");
			System.out.println("User deletion successful with ID: " + id);
		} else {
			model.addAttribute("message", "That ID does not exist"); 
			System.out.println("User with the specified ID (" + id + ") not found in the userEraserFunction");
		}
        return "redirect:/Management";
	}
	
////////////////////////////////////////////////////////////////////////////
 
	@Transactional
	public String userModification(ValidatedUserProperties userInfo, BindingResult bindingResult, Model model) { 
		if (bindingResult.hasErrors()) { 
System.out.println("error de binding result del userModification en el ServerFunciton");
System.out.println(bindingResult.getAllErrors());
			return "Management/index-manage";
		} 				
		Optional<UserProperties> optPro = userPropertiesMapper.selectByPrimaryKey(userInfo.getId());
		
		model.addAttribute("newUser", false);
		model.addAttribute("viewUser", false);
		model.addAttribute("modifyUser", true);
		model.addAttribute("searchUser", false);
		model.addAttribute("deleteUser", false); 
		
		if(optPro.isPresent()) {
			ValidatedUserProperties userPro = new ValidatedUserProperties();
			System.out.println("User to modify with ID: " + userInfo.getId());
			System.out.println(optPro.get().getUsername()); 
			System.out.println(optPro.get().getEmail()); 
			System.out.println(optPro.get().getRoleId());
			System.out.println(optPro.get().getActive());

			System.out.println("New data:"); 
			System.out.println(userInfo.getUsername()); 
			System.out.println(userInfo.getEmail()); 
			System.out.println(userInfo.getRoleId());
			System.out.println(userInfo.getActive());			
			userPro.setId(optPro.get().getId());
			userPro.setUsername(userInfo.getUsername());
			userPro.setEmail(userInfo.getEmail());
			userPro.setRoleId(userInfo.getRoleId());
			userPro.setActive(userInfo.getActive());
			userPropertiesMapper.updateByPrimaryKeySelective(userPro);

			System.out.println("Contact properties modification successful with ID: " + userPro.getId());
			UserHistory userHis = new UserHistory();
			UserSecurity userSec = new UserSecurity();
			UserBasics userBas = new UserBasics();

			userSec.setUsername(userInfo.getUsername());
			userSec.setId(queryMapper.getSecurityId(optPro.get().getUsername()));
			userSecurityMapper.updateByPrimaryKeySelective(userSec);

			userBas.setId(queryMapper.getBasicsId(optPro.get().getUsername()));
			userBas.setUsername(userInfo.getUsername());
			userBas.setEmail(userInfo.getEmail());
			userBasicsMapper.updateByPrimaryKey(userBas);
			userHis.setUsername(optPro.get().getUsername());

			if (queryMapper.usernameExistsHis(userHis.getUsername())) {
				userHis.setId(queryMapper.getHistoryId(userHis.getUsername()));
				userHis.setUsername(userInfo.getUsername());
				userHistoryMapper.updateByPrimaryKeySelective(userHis);
			} else {
				System.out.println("User not found on history table");
			}

		} else {
			model.addAttribute("message", "That ID does not exist"); 
			System.out.println("User with the specified sec ID (" + userInfo.getId() + ") not found in userModification");
		}
		System.out.println("Update complete.");
		
		if (userInfo.getUsername().equals("rollback")) {
			System.out.println(" the username is -" + userInfo.getUsername() + "- and now there will be a rollback");
	        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	        throw new RuntimeException("Transaction rolled back for user: " + userInfo.getUsername());
		}
        return "redirect:/Management";
	}

////////////////////////////////////////////////////////////////////////////

    public String userSearcherFunction(ValidatedUserProperties userInfo, BindingResult bindingResult, Model model) {
    	

	    if (bindingResult.hasErrors()) {
    		return "Production/index"; 
	    }
	    
	    System.out.print("Search Parameters:");
	    System.out.print("Username: " + userInfo.getUsername());
	    System.out.print(", email: " + userInfo.getEmail());
	    System.out.print(", role: " + userPropertiesServicer.convertIdToRoleText(userInfo.getRoleId()));
	    System.out.println(", active: " + userInfo.getActive());

		List<ValidatedUserProperties> foundUsers = userPropertiesServicer.searchUserService(userInfo.getUsername(), 
				userInfo.getEmail(), userInfo.getRoleId(), userInfo.getActive());

	    if (foundUsers.isEmpty()) {
	        model.addAttribute("message", "No contacts found matching the search criteria.");
	        System.out.println("No contacts found.");
	    } else {
	        model.addAttribute("searchResult", foundUsers);
	        model.addAttribute("message", foundUsers.size() + " contact(s) found.");
	        model.addAttribute("resultType", "search");
	        System.out.println(foundUsers.size() + " contact(s) found:");
	        for (ValidatedUserProperties user : foundUsers) {
	            System.out.println("ID: " + user.getId() + ", Name: " + user.getUsername() + ", email: " + user.getEmail() + ", assigned role: " 
	            			+ userPropertiesServicer.convertIdToRoleText(user.getRoleId()) + " , activo: " + user.getActive());
	        }
	    }
    	return "Management/index-manage";
    }

////////////////////////////////////////////////////////////////////////////

    public String getUsername() {
	   
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	return authentication.getName();
    }
    
////////////////////////////////////////////////////////////////////////////
    
    public int getRole() {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	
    	return queryMapper.getRoleFromUsername(authentication.getName());
    }
    
////////////////////////////////////////////////////////////////////////////
    
    public String lastVisitedPage() {
    	
    	ServletRequestAttributes attribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    	HttpSession session = attribute.getRequest().getSession(false);
    	
    	if (session != null) {
    		String whereAmIDTO = (String) session.getAttribute("whereAmIDTO");
    		if (whereAmIDTO != null) {
    			return whereAmIDTO;
    		}
    	}
    	
    	return null;
    }

    public int getUserIdToModify() {
    	
    	ServletRequestAttributes attribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    	HttpSession session = attribute.getRequest().getSession(false);
    	UserProperties userToModify = new ValidatedUserProperties();
    	userToModify = (UserProperties) session.getAttribute("selectedUser");
    	
    	return userToModify.getId();
    }
    
}
