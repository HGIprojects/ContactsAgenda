
package com.contactspring.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.contactspring.mybatis.form.ValidatedContactFields;
import com.contactspring.mybatis.form.ValidatedUserProperties;
import com.contactspring.mybatis.generate.map.ContactFieldsMapper;
import com.contactspring.mybatis.generate.map.UserBasicsMapper;
import com.contactspring.mybatis.generate.map.UserHistoryMapper;
import com.contactspring.mybatis.generate.map.UserPropertiesMapper;
import com.contactspring.mybatis.generate.map.UserSecurityMapper;
import com.contactspring.mybatis.generate.model.ContactFields;
import com.contactspring.mybatis.generate.model.UserBasics;
import com.contactspring.mybatis.generate.model.UserHistory;
import com.contactspring.mybatis.generate.model.UserProperties;
import com.contactspring.mybatis.generate.model.UserSecurity;
import com.contactspring.mybatis.original.EmailVerificationService;
import com.contactspring.mybatis.original.QueryMapper;
import com.contactspring.mybatis.service.ContactsService;
import com.contactspring.mybatis.service.UserPropertiesService;

import jakarta.validation.Valid;

@Component
public class ServerFunctions {

	@Autowired
    private ContactsService contactsServicer;
	@Autowired
	private UserPropertiesService userPropertiesServicer;
	@Autowired
	private ContactFieldsMapper contactMapper;   
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
	@Autowired
	private EmailVerificationService emailVerificationService;
	
	
	public String searcherFunction(ContactFields contactFields, BindingResult bindingResult, Model model, boolean isDemo) {

	    if (bindingResult.hasErrors()) {
			if (isDemo) {
	        	return "Demo/index-demo";
	        } else {
	        	return "Production/index"; 
	        }
	    }
	    System.out.println("Search Parameters:");
	    System.out.println("Postal Code: " + contactFields.getPostalCode());
	    System.out.println("Address: " + contactFields.getAddress());
	    System.out.println("Company Name: " + contactFields.getCompanyName());
	    System.out.println("Last Name: " + contactFields.getLastName());
	    System.out.println("First Name: " + contactFields.getFirstName());
	    System.out.println("Phone Number: " + contactFields.getPhoneNumber());
		List<ValidatedContactFields> foundContacts = contactsServicer.searchService(contactFields.getPostalCode(), 
				contactFields.getAddress(), contactFields.getCompanyName(), contactFields.getLastName(), 
				contactFields.getFirstName(), contactFields.getPhoneNumber());
	    if (foundContacts.isEmpty()) {
	        model.addAttribute("message", "No contacts found matching the search criteria.");
	        System.out.println("No contacts found.");
	    } else {
	        model.addAttribute("searchResult", foundContacts);
	        model.addAttribute("message", foundContacts.size() + " contact(s) found.");
	        model.addAttribute("resultType", "search");
	        System.out.println(foundContacts.size() + " contact(s) found:");
	        for (ContactFields contact : foundContacts) {
	            System.out.println("ID: " + contact.getId() + ", Name: " + contact.getFirstName() + " " + contact.getLastName());
	        }
	    }
       if (isDemo) {
        	return "Demo/result";
        } else {
    		return "Production/index";
        }
    }

////////////////////////////////////////////////////////////////////////////

	public String creatorFunction(ValidatedContactFields contactFields, 
							BindingResult bindingResult, Model model, boolean isDemo) { 
		
		if (bindingResult.hasErrors()) { 		
	        if (isDemo) {
				return "Demo/index-demo"; 
	        } else {
				return "Production/index";
	        }
		} 
		model.addAttribute("message", "Form submission successful!"); 

		boolean successfulCreation = false;
		ContactFields contact = contactsServicer.setNewContact(contactFields.getPostalCode(), contactFields.getAddress(), contactFields.getCompanyName(), 
		contactFields.getLastName(), contactFields.getFirstName(), contactFields.getPhoneNumber());
		contactMapper.insert(contact);
		Long id = contact.getId();
		Optional<ContactFields> opt = contactMapper.selectByPrimaryKey(id);
		if(opt.isPresent()) {
			model.addAttribute("contact", opt.get());
			model.addAttribute("message", "Contact creation successful, ID: " + id);
			model.addAttribute("resultType", "creation");
			successfulCreation = true;
			System.out.println("Contact creation successful with ID: " + id);			
		}
		if(!successfulCreation) {
			System.out.println("Creation unsuccessful");
		}
        if (isDemo) {
        	return "Demo/result";
        } else {
            return "redirect:/Production";
        }
	}
	
////////////////////////////////////////////////////////////////////////////

	public String eraserFunction(Long id, Model model, boolean isDemo) { 

		Optional<ContactFields> opt = contactMapper.selectByPrimaryKey(id);

		if(opt.isPresent()) {
			ContactFields contact = opt.get();
			System.out.println("Contact to delete with ID: " + id);
			System.out.println(contact.getAddress()); 
			System.out.println(contact.getCompanyName()); 
			System.out.println(contact.getFirstName()); 
			System.out.println(contact.getLastName()); 
			System.out.println(contact.getPhoneNumber()); 
			System.out.println(contact.getPostalCode()); 
			model.addAttribute("contact", opt.get());
			contactMapper.deleteByPrimaryKey(id);
			model.addAttribute("message", "Contact deletion successful, ID: " + id);
			model.addAttribute("resultType", "deletion");
			System.out.println("Contact deletion successful with ID: " + id);
		} else {
			model.addAttribute("message", "That ID does not exist"); 
			System.out.println("Contact with the specified ID (" + id + ") not found in the eraserFunction");
		}
        if (isDemo) {
        	return "Demo/result";
        } else {
            return "redirect:/Production";
        }
	}
	
////////////////////////////////////////////////////////////////////////////

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

	public String modifierFunction(ValidatedContactFields contactFields, BindingResult bindingResult, Model model, boolean isDemo) { 
		
	    if (bindingResult.hasErrors()) {
	    				
			if (isDemo) {
	        	return "Demo/index-demo";
	        } else {
	        	return "Production/index"; 
	        }
	    }
		
		Optional<ContactFields> opt = contactMapper.selectByPrimaryKey(contactFields.getId());

		if(opt.isPresent()) {
			ContactFields contact = new ContactFields();
			System.out.println("Contact to modify with ID: " + contactFields.getId());
			System.out.println(opt.get().getAddress()); 
			System.out.println(opt.get().getCompanyName()); 
			System.out.println(opt.get().getFirstName()); 
			System.out.println(opt.get().getLastName()); 
			System.out.println(opt.get().getPhoneNumber()); 
			System.out.println(opt.get().getPostalCode()); 
			System.out.println("New data:"); 
			System.out.println(contactFields.getAddress()); 
			System.out.println(contactFields.getCompanyName()); 
			System.out.println(contactFields.getFirstName()); 
			System.out.println(contactFields.getLastName()); 
			System.out.println(contactFields.getPhoneNumber()); 
			System.out.println(contactFields.getPostalCode()); 
			contact.setId(opt.get().getId());
			contact.setAddress(contactFields.getAddress());
			contact.setCompanyName(contactFields.getCompanyName());
			contact.setFirstName(contactFields.getFirstName());
			contact.setLastName(contactFields.getLastName());
			contact.setPhoneNumber(contactFields.getPhoneNumber());
			contact.setPostalCode(contactFields.getPostalCode());
			contactMapper.updateByPrimaryKeySelective(contact);
			Optional<ContactFields> newOpt = contactMapper.selectByPrimaryKey(contact.getId());
			model.addAttribute("oldContact", opt.get());
			model.addAttribute("newContact", newOpt.get());
			model.addAttribute("message", "Contact modification successful, ID: " + contactFields.getId());
			model.addAttribute("resultType", "modification");
			System.out.println("Contact modification successful with ID: " + contactFields.getId());
		} else {
			model.addAttribute("message", "That ID does not exist"); 
			System.out.println("Contact with the specified ID (" + contactFields.getId() + ") not found in the modifierFunction");
		}
        if (isDemo) {
        	return "Demo/result";
        } else {
            return "redirect:/Production";
        }
	}
	
////////////////////////////////////////////////////////////////////////////

	public String viewerFunction(@Valid ContactFields contactFields, BindingResult bindingResult, Model model,
			boolean isDemo) {
		
		return "Production";
	}
	
////////////////////////////////////////////////////////////////////////////
	
    public ByteArrayResource exportAll() throws IOException {
            
		List<ContactFields> allContacts = contactMapper.select(c -> c);
		allContacts.sort(Comparator.comparing(ContactFields::getLastName));
    	
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            if (!allContacts.isEmpty()) {
                String header = "ID,First Name,Last Name,Phone Number,Address,Postal Code,Company Name,Date Added";
                writer.write(header + "\n");
                for (ContactFields contact : allContacts) {
                    String row = String.join(",",
                            contact.getId() != null ? String.valueOf(contact.getId()) : "",
                            contact.getFirstName() != null ? contact.getFirstName() : "",
                            contact.getLastName() != null ? contact.getLastName() : "",
                            contact.getPhoneNumber() != null ? contact.getPhoneNumber() : "",
                            contact.getAddress() != null ? contact.getAddress() : "",
                            contact.getPostalCode() != null ? contact.getPostalCode() : "",
                            contact.getCompanyName() != null ? contact.getCompanyName() : "",
                            contact.getDateAdded() != null ? contact.getDateAdded().toString() : ""
                        );
                    writer.write(row + "\n");
                }
            }
            writer.flush();
        }
        return new ByteArrayResource(outputStream.toByteArray());
    }
    
////////////////////////////////////////////////////////////////////////////
 
	public String userModification(ValidatedUserProperties userInfo,/* int role, */BindingResult bindingResult, Model model) { 
		if (bindingResult.hasErrors()) { 		
			return "Management/index-manage";
		} 				
		Optional<UserProperties> optPro = userPropertiesMapper.selectByPrimaryKey(userInfo.getId());
		
		model.addAttribute("newUser", false);
		model.addAttribute("viewUser", false);
		model.addAttribute("modifyUser", false);
		model.addAttribute("searchUser", false);
		model.addAttribute("deleteUser", true); 
		
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
        return "redirect:/Management";
	}

////////////////////////////////////////////////////////////////////////////

    public String userSearcherFunction(ValidatedUserProperties userInfo, BindingResult bindingResult, Model model) {
    	

	    if (bindingResult.hasErrors()) {
    		return "Production/index"; 
	    }
	    
	    System.out.println("Search Parameters:");
	    System.out.println("Username: " + userInfo.getUsername());
	    System.out.println("Email: " + userInfo.getEmail());
	    System.out.println("Role: " + userPropertiesServicer.convertIdToRoleText(userInfo.getRoleId()));
	    System.out.println("Active: " + userInfo.getActive());

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
}
