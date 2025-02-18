package com.contactspring.mybatis.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.contactspring.mybatis.form.ValidatedUserProperties;
import com.contactspring.mybatis.generate.map.ContactFieldsMapper;
import com.contactspring.mybatis.generate.map.UserPropertiesMapper;
import com.contactspring.mybatis.generate.map.UserSecurityMapper;
import com.contactspring.mybatis.generate.model.ContactFields;
import com.contactspring.mybatis.generate.model.UserProperties;
import com.contactspring.mybatis.original.QueryMapper;



@Service
public class UserPropertiesService{
	
	@Autowired
	private ContactFieldsMapper contactMapper;
	@Autowired
	private QueryMapper queryMapper;
	@Autowired
	private LoginService loginServicer;
	@Autowired
	private UserSecurityMapper userSecurityMapper;
	@Autowired
	private UserPropertiesMapper userPropertiesMapper;

		
	public boolean deleteUserService(int id) {
		Optional<UserProperties> existingUser = userPropertiesMapper.selectByPrimaryKey(id);
		if(existingUser.isPresent()) {
			userPropertiesMapper.deleteByPrimaryKey(id);
			System.out.println("Deletion of id: " + id + " succesful");
			return true;
		}
		System.out.println("Deletion of id: " + id + " failed");
		return false;
	}
	
/////////////////////////////////////////////////////////
	
	public UserProperties setModUser(int id, String username, String email,
				int roleId) {
		
		Optional<UserProperties> opt = userPropertiesMapper.selectByPrimaryKey(id);
		UserProperties userInfo = opt.get();
		userInfo.setUsername(username);
		userInfo.setEmail(email);
		userInfo.setRoleId(roleId);
		
		return userInfo;
	}
	
	public List<ValidatedUserProperties> searchUserService(String username, String email, int roleId, boolean active) {
		
		List<ValidatedUserProperties> bufferList = queryMapper.searchUser(username, email, roleId, active);
		for (ValidatedUserProperties searchedUser:bufferList) {
			searchedUser.setRoleName(convertIdToRoleText(searchedUser.getRoleId())); 
		}
		System.out.println("Search parameters:");
		System.out.println("ID: not sent"+ ", Username: " + username + ", email: " + email + ", roleId: " + roleId + ", active: " + active);
		System.out.println("[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[");
		System.out.println("Search results:");
		for (ValidatedUserProperties userInfo : bufferList) {
			System.out.println("ID: " + userInfo.getId() + ", Username: " + userInfo.getUsername() + ", Email: " + userInfo.getEmail() + "," + 
						", Role: " + userInfo.getRoleId() + " // " + userInfo.getRoleName() + ", active: " + userInfo.getActive());
			System.out.println("==================================================================================");
		}
		System.out.println("]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]");
		return bufferList;
	}

	public List<ValidatedUserProperties> initialDisplay(int usersPerPage, int offset) {

		int requestedPage = 0;
		// 1o contar
		// 2o dividir de 5 en 5
		// 3o query para sacar los 5 iniciales (por defecto? o sacar dos displayerFunctions, uno para recien carga y otro para cuando se navega? creoq ue esto es mejor)
		// 4o los siguientes aplicar logica
		List<ValidatedUserProperties> initialUsers = queryMapper.paginatorQueryUsers(usersPerPage, requestedPage);
		
		return initialUsers;
	}
	
	
	public List<ValidatedUserProperties> pageDisplay(int usersPerPage, int requestedPage) {
		
System.out.println("La cantidad de usuarios por pagina es: " + usersPerPage);
System.out.println("La pagina solicitada es: " + requestedPage);
		int offset = ((requestedPage - 1) * usersPerPage);
		List<ValidatedUserProperties> paginatedUsers = queryMapper.paginatorQueryUsers(usersPerPage, offset);
System.out.println("Los usuarios solicitados son: ");
		for (ValidatedUserProperties user : paginatedUsers) {
			user.setRoleName(convertIdToRoleText(user.getRoleId()));
			System.out.println("ID: " + user.getId() + ", Username: " + user.getUsername() + ", Email: " + user.getEmail() + "," + 
					", Role: " + user.getRoleName() + ", active: " + user.getActive());
			System.out.println("==================================================================================");
		}
		return paginatedUsers;
	}
	
	public void optionInitializer(String action, Model model) {

		model.addAttribute("viewUser", "viewUser".equals(action));
		model.addAttribute("modifyUser", "modifyUser".equals(action));
		model.addAttribute("searchUser", "searchUser".equals(action));
		model.addAttribute("deleteUser", "deleteUser".equals(action));
	}
	
////////////////////////
// former serverfunctions
///////////////////////

public String searcherUserFunction(ValidatedUserProperties userInfo, BindingResult bindingResult, Model model) {

    if (bindingResult.hasErrors()) {
          	return "Management/index-manage"; 
    }
    
    System.out.println("Search Parameters:");
    System.out.println("Postal Code: " + userInfo.getUsername());
    System.out.println("Address: " + userInfo.getEmail());
    System.out.println("Company Name: " + convertIdToRoleText(userInfo.getRoleId()));
    System.out.println("Active: " + userInfo.getActive());
    
	List<ValidatedUserProperties> foundUsers = searchUserService(userInfo.getUsername(), 
			userInfo.getEmail(), userInfo.getRoleId(), userInfo.getActive());
	
    if (foundUsers.isEmpty()) {
        model.addAttribute("message", "No users found matching the search criteria.");
        System.out.println("No users found.");
    } else {
        model.addAttribute("searchResult", foundUsers);
        model.addAttribute("message", foundUsers.size() + " user(s) found.");
        model.addAttribute("resultType", "search");
        System.out.println(foundUsers.size() + " user(s) found:");
        for (ValidatedUserProperties user : foundUsers) {
            System.out.println("ID: " + user.getId() + ", Name: " + user.getUsername() + " " + user.getEmail());
        }
    }

  	return "Management/index-manage"; 
    
}

public String eraserUserFunction(int roleId, Model model) { 
	boolean successfulDeletion = false;

	Optional<UserProperties> opt = userPropertiesMapper.selectByPrimaryKey(roleId);

	if(opt.isPresent()) {
		UserProperties userInfo = opt.get();
		System.out.println("User to delete with ID: " + roleId);
		System.out.println(userInfo.getUsername()); 
		System.out.println(userInfo.getEmail()); 
		System.out.println(userInfo.getRoleId()); 
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// System.out.println(userInfo.getActive()); meter que hay que desactivar antes de eliminar como metodo de seguridad (a peticion del usuario a lo mejor? //
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		model.addAttribute("userInfo", opt.get());
		userPropertiesMapper.deleteByPrimaryKey(roleId);
		model.addAttribute("message", "User deletion successful, ID: " + roleId);
		model.addAttribute("resultType", "deletion");
		successfulDeletion = true;
		System.out.println("User deletion successful with ID: " + roleId);
	} else {
		model.addAttribute("message", "That ID does not exist"); 
		System.out.println("User with the specified ID (" + roleId + ") not found in the eraserFunction");
		successfulDeletion = false;

	}

  	return "Management/index-manage"; 
	
}
/*
public String modifierFunction(ValidatedContactFields contactFields, BindingResult bindingResult, Model model, boolean isDemo) { 
	
    if (bindingResult.hasErrors()) {
    				
		if (isDemo) {
        	return "Demo/manage";
        } else {
        	return "ContactsController/index"; 
        }
    }
	
	boolean successfulModification = false;

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
		System.out.println("New data to update:"); 
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
		contactMapper.updateByPrimaryKey(contact);
		Optional<ContactFields> newOpt = contactMapper.selectByPrimaryKey(contact.getId());
		model.addAttribute("oldContact", opt.get());
		model.addAttribute("newContact", newOpt.get());
		model.addAttribute("message", "Contact modification successful, ID: " + contactFields.getId());
		model.addAttribute("resultType", "modification");
		successfulModification = true;
		System.out.println("Contact modification successful with ID: " + contactFields.getId());
	
	} else {
		model.addAttribute("message", "That ID does not exist"); 
		System.out.println("Contact with the specified ID (" + contactFields.getId() + ") not found in the modifierFunction");
		successfulModification = false;

	}

    if (isDemo) {
    	return "Demo/result";
    } else {
        return "redirect:/ContactsController";
    }
	
}

public String viewerFunction(@Valid ContactFields contactFields, BindingResult bindingResult, Model model,
		boolean isDemo) {
	
	return "ContactsController";
}

*/


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


/*
public String newUser(@Valid ValidatedUserSecurity userInfo, BindingResult bindingResult, Model model) {


	if (bindingResult.hasErrors()) { 
		
		return "ContactsController/login";	
    }
	 
	model.addAttribute("message", "User form submission successful!"); 
	System.out.println("Now inside newUser in ServerFunctions:"); 
	System.out.println(userInfo.getUsername()); 
	System.out.println(userInfo.getPassword()); 
	//System.out.println(userInfo.getEmail()); 
	//System.out.println(userInfo.getRole()); 
	//System.out.println(userInfo.getActive()); 
	boolean successfulCreation = false;
	//Users user = loginServicer.setNewUser(userInfo.getUsername(), userInfo.getPassword(), userInfo.getRole(), userInfo.getEmail());
	ValidatedUserSecurity user = loginServicer.setNewUser(userInfo);
	//usersMapper.insert(user);
	userSecurityMapper.insert(user);
	int id = user.getId();
	System.out.println("The id of the new user is: " + id);
	//Optional<Users> opt = usersMapper.selectByPrimaryKey(id);
	Optional<UserSecurity> opt = userSecurityMapper.selectByPrimaryKey(id);
	if(opt.isPresent()) {
		model.addAttribute("user", opt.get());
		model.addAttribute("message", "User creation successful, ID: " + id);
		successfulCreation = true;
		System.out.println("User creation successful with ID: " + id);			
	}
	if(!successfulCreation) {
		System.out.println("User creation unsuccessful");
	}
			
	return "ContactsController/login";
    
}
*/
public String logonProcess(String username, String password, Model model, boolean isDemo) {
	// TODO Auto-generated method stub
	System.out.println("Verifying user exists and is active");
	if (isDemo) {
		System.out.println("Demo mode selected");
    	return "redirect:/Demo";
    } else {
        return "redirect:/Production";
    }
	
}
	

public String convertIdToRoleText(int roleId) {
	
	/*
	String roleText = "";
	if (roleId == 1) {
		roleText = "PRODUCTION";
	}
	if (roleId == 2) {
		roleText = "DEMO";
	}
	if (roleId == 3) {
		roleText = "ADMIN";
	} else {
		System.out.println("Incorrect roleId: " + roleId);
	}
	
	return roleText;
	*/
	return queryMapper.getRoleFromId(roleId);

}

public int convertRoleToId(String roleText) {
	
	/*
	int roleId = 0;
	if (roleText == "PRODUCTION") {
		roleId = 1;
	} else if (roleText == "DEMO") {
		roleId = 2;
	} else if (roleText == "ADMIN") {
		roleId = 3;
	} else {
		System.out.println("Incorrect role: " + roleText);
	}
	return roleId;
	*/
	return queryMapper.getIdFromRole(roleText);
}

}

