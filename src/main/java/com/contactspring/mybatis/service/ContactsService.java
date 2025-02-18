package com.contactspring.mybatis.service;

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
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.contactspring.mybatis.form.ValidatedContactFields;
import com.contactspring.mybatis.generate.map.ContactFieldsMapper;
import com.contactspring.mybatis.generate.map.UserSecurityMapper;
import com.contactspring.mybatis.generate.model.ContactFields;
import com.contactspring.mybatis.original.QueryMapper;

import jakarta.validation.Valid;



@Service
public class ContactsService{
	
	@Autowired
	private ContactFieldsMapper contactMapper;
	@Autowired
	private QueryMapper queryMapper;
	@Autowired
	private LoginService loginServicer;
	@Autowired
	private UserSecurityMapper userSecurityMapper;
		
	public boolean deleteService(Long id) {
		Optional<ContactFields> existingContact = contactMapper.selectByPrimaryKey(id);
		if(existingContact.isPresent()) {
			contactMapper.deleteByPrimaryKey(id);
			System.out.println("Deletion of id: " + id + " succesful");
			return true;
		}
		System.out.println("Deletion of id: " + id + " failed");
		return false;
	}
	
	public ContactFields setModContact(Long id, String postalCode, String address,
				String companyName, String lastName, String firstName, String phoneNumber) {
		
		Optional<ContactFields> opt = contactMapper.selectByPrimaryKey(id);
		ContactFields contact = opt.get();
		contact.setPostalCode(postalCode);
		contact.setAddress(address);
		contact.setCompanyName(companyName);
		contact.setLastName(lastName);
		contact.setFirstName(firstName);
		contact.setPhoneNumber(phoneNumber);
		
		return contact;
	}
	
	public ContactFields setNewContact(String postalCode, String address,
			String companyName, String lastName, String firstName, String phoneNumber) {
		
		LocalDateTime  creationDate = LocalDateTime .now();
		ContactFields contact = new ContactFields();
		contact.setPostalCode(postalCode);
		contact.setAddress(address);
		contact.setCompanyName(companyName);
		contact.setLastName(lastName);
		contact.setFirstName(firstName);
		contact.setPhoneNumber(phoneNumber);
		contact.setDateAdded(creationDate);

	return contact;
	}
	
	public List<ValidatedContactFields> searchService(String postalCode, String address, String companyName, String lastName, String firstName, String phoneNumber) {
		List<ValidatedContactFields> bufferList = queryMapper.searchContacts(postalCode, address, companyName, lastName, firstName, phoneNumber);
		System.out.println("Search parameters:");
		System.out.println("ID: not sent"+ ", Address: " + address + ", Company: " + companyName + ", Name: " + firstName + ", Surname: " + lastName + ", Postal Code: " + phoneNumber);
		System.out.println("[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[=[");
		System.out.println("Search results:");
		for (ContactFields contact : bufferList) {
			System.out.println("ID: " + contact.getId() + ", Address: " + contact.getAddress() + ", Company: " + contact.getCompanyName() + ",");
			System.out.println("Date added: " + contact.getDateAdded() + ", Name: " + contact.getFirstName() + ", Surname: " + contact.getLastName() + ", Postal Code: " + contact.getPostalCode());
			System.out.println("==================================================================================");
		}
		System.out.println("]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]=]");
		return bufferList;
	}

	public List<ContactFields> initialDisplay(int contactsPerPage, int offset) {

		int requestedPage = 0;
		// 1o contar
		// 2o dividir de 5 en 5
		// 3o query para sacar los 5 iniciales (por defecto? o sacar dos displayerFunctions, uno para recien carga y otro para cuando se navega? creoq ue esto es mejor)
		// 4o los siguientes aplicar logica
		List<ContactFields> initialContacts = queryMapper.paginatorQuery(contactsPerPage, requestedPage);
		
		return initialContacts;
	}
	
	
	public List<ContactFields> pageDisplay(int contactsPerPage, int requestedPage) {
		
		// 1o contar
		// 2o dividir de 5 en 5
		// 3o query para sacar los 5 iniciales (por defecto? o sacar dos displayerFunctions, uno para recien carga y otro para cuando se navega? creoq ue esto es mejor)
		// 4o los siguientes aplicar logica
System.out.println("La cantidad de contactos por pagina es: " + contactsPerPage);
System.out.println("La pagina solicitada es: " + requestedPage);
		int offset = ((requestedPage - 1) * contactsPerPage);
		List<ContactFields> paginatedContacts = queryMapper.paginatorQuery(contactsPerPage, offset);
System.out.println("Los contacts solicitados son: ");
		for (ContactFields contact : paginatedContacts) {
			System.out.println("ID: " + contact.getId() + ", Address: " + contact.getAddress() + ", Company: " + contact.getCompanyName() + ",");
			System.out.println("==================================================================================");
		}
		

		return paginatedContacts;
	}
	
	public void optionInitializer(String action, Model model) {

		model.addAttribute("newContact", "newContact".equals(action));
		model.addAttribute("viewContact", "viewContact".equals(action));
		model.addAttribute("modifyContact", "modifyContact".equals(action));
		model.addAttribute("searchContact", "searchContact".equals(action));
		model.addAttribute("deleteContact", "deleteContact".equals(action));
	}
	
////////////////////////
// former serverfunctions
///////////////////////

public String searcherFunction(ValidatedContactFields contactFields, BindingResult bindingResult, Model model, boolean isDemo) {

    if (bindingResult.hasErrors()) {
    	
//		model.addAttribute("contactFields", contactFields);	
		
		if (isDemo) {
        	return "Demo/manage";
        } else {
        	return "ContactsController/index"; 
        }
    }
    
    System.out.println("Search Parameters:");
    System.out.println("Postal Code: " + contactFields.getPostalCode());
    System.out.println("Address: " + contactFields.getAddress());
    System.out.println("Company Name: " + contactFields.getCompanyName());
    System.out.println("Last Name: " + contactFields.getLastName());
    System.out.println("First Name: " + contactFields.getFirstName());
    System.out.println("Phone Number: " + contactFields.getPhoneNumber());
    
	List<ValidatedContactFields> foundContacts = searchService(contactFields.getPostalCode(), 
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
        for (ValidatedContactFields contact : foundContacts) {
            System.out.println("ID: " + contact.getId() + ", Name: " + contact.getFirstName() + " " + contact.getLastName());
        }
    }

   if (isDemo) {
    	return "Demo/result";
    } else {
		return "ContactsController/index";
    }
    
}

public String creatorFunction(ValidatedContactFields contactFields, 
						BindingResult bindingResult, Model model, boolean isDemo) { 
	
	if (bindingResult.hasErrors()) { 		
        if (isDemo) {
			return "Demo/manage"; 
        } else {
			return "ContactsController/index";
        }
	} 
	model.addAttribute("message", "Form submission successful!"); 
	System.out.println(contactFields.getAddress()); 
	System.out.println(contactFields.getCompanyName()); 
	System.out.println(contactFields.getFirstName()); 
	System.out.println(contactFields.getLastName()); 
	System.out.println(contactFields.getPhoneNumber()); 
	System.out.println(contactFields.getPostalCode()); 
	boolean successfulCreation = false;
	ContactFields contact = setNewContact(contactFields.getPostalCode(), contactFields.getAddress(), contactFields.getCompanyName(), 
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
        return "redirect:/ContactsController";
    }
}

public String eraserFunction(Long id, Model model, boolean isDemo) { 
	boolean successfulDeletion = false;

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
		successfulDeletion = true;
		System.out.println("Contact deletion successful with ID: " + id);
	
	} else {
		model.addAttribute("message", "That ID does not exist"); 
		System.out.println("Contact with the specified ID (" + id + ") not found in the eraserFunction");
		successfulDeletion = false;

	}

    if (isDemo) {
    	return "Demo/result";
    } else {
        return "redirect:/ContactsController";
    }
	
}

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
        return "redirect:/ContactsController";
    }
	
}
	
	
}

