package com.contactspring.mybatis.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactspring.config.SecurityConfig;
import com.contactspring.mybatis.form.ValidatedContactFields;
import com.contactspring.mybatis.generate.map.ContactFieldsMapper;
import com.contactspring.mybatis.generate.model.ContactFields;
import com.contactspring.mybatis.original.QueryMapper;
import com.contactspring.mybatis.original.WhereAmIDTO;



@Service
public class ContactsService{
	
	@Autowired
	private ContactFieldsMapper contactMapper;
	@Autowired
	private QueryMapper queryMapper;
	@Autowired
	private LoginService loginServicer;
	@Autowired
	private SecurityConfig secConf;
	@Autowired
	private ServerFunctions serverFunctioner;

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
	
////////////////////////////////////////////////////////////////////////////
	
	@Transactional
	public ContactFields setNewContact(String postalCode, String address,
			String companyName, String lastName, String firstName, String phoneNumber, String username) {
		
		LocalDateTime  creationDate = LocalDateTime .now();
		ContactFields contact = new ContactFields();
		contact.setPostalCode(postalCode);
		contact.setAddress(address);
		contact.setCompanyName(companyName);
		contact.setLastName(lastName);
		contact.setFirstName(firstName);
		contact.setPhoneNumber(phoneNumber);
		contact.setDateAdded(creationDate);
		contact.setCreator(username);

	return contact;
	}
	
////////////////////////////////////////////////////////////////////////////

	public List<ValidatedContactFields> searchService(String postalCode, String address, String companyName, String lastName, String firstName, String phoneNumber, String username, String role) {

		int adminRoleId = 3;
		List<ValidatedContactFields> bufferList = new ArrayList<>();
		if (role.equals(queryMapper.getRoleFromId(adminRoleId))) {
			bufferList = queryMapper.searchContactsAdmin(postalCode, address, companyName, lastName, firstName, phoneNumber);
		} else {
			bufferList = queryMapper.searchContacts(postalCode, address, companyName, lastName, firstName, phoneNumber, username);
		}

		return bufferList;
	}

////////////////////////////////////////////////////////////////////////////
/*
	public List<ContactFields> initialDisplay(int contactsPerPage, int offset) {

		int requestedPage = 0;
		// 1o contar
		// 2o dividir de 5 en 5
		// 3o query para sacar los 5 iniciales (por defecto? o sacar dos displayerFunctions, uno para recien carga y otro para cuando se navega? creoq ue esto es mejor)
		// 4o los siguientes aplicar logica
		List<ContactFields> initialContacts = queryMapper.paginatorQuery(contactsPerPage, requestedPage, serverFunctioner.getUsername());
		
		return initialContacts;
	}
	*/
	
////////////////////////////////////////////////////////////////////////////

	public List<ContactFields> pageDisplay(int contactsPerPage, int requestedPage) {
		
		int adminRoleId = 3;
		List<ContactFields> paginatedContacts = new ArrayList<>();
		String username = serverFunctioner.getUsername();
		int offset = ((requestedPage - 1) * contactsPerPage);
		String role = loginServicer.getRole(username);
		
		if (role.equals(queryMapper.getRoleFromId(adminRoleId))) {
			paginatedContacts = queryMapper.paginatorQueryAdmin(contactsPerPage, offset);
		} else {
			paginatedContacts = queryMapper.paginatorQuery(contactsPerPage, offset, username);
		}
		System.out.println("The requested contacts are: ");
		for (ContactFields contact : paginatedContacts) {
			System.out.println("ID: " + contact.getId() + ", Address: " + contact.getAddress() + ", Company: " + contact.getCompanyName() + ",");
			System.out.println("==================================================================================");
		}
		
		return paginatedContacts;
	}
	
////////////////////////////////////////////////////////////////////////////
	
	public String optionInitializer(String action, Model model) {

		model.addAttribute("newContact", "newContact".equals(action));
		model.addAttribute("viewContact", "viewContact".equals(action));
		model.addAttribute("modifyContact", "modifyContact".equals(action));
		model.addAttribute("searchContact", "searchContact".equals(action));
		model.addAttribute("deleteContact", "deleteContact".equals(action));
		
		return "Production/index";
	}
	

////////////////////////////////////////////////////////////////////////////

	public String searcherFunction(ContactFields contactFields, BindingResult bindingResult, Model model, boolean isDemo) {
		  
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
		String role = loginServicer.getRole(username);
	    
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
		List<ValidatedContactFields> foundContacts = searchService(contactFields.getPostalCode(), 
				contactFields.getAddress(), contactFields.getCompanyName(), contactFields.getLastName(), 
				contactFields.getFirstName(), contactFields.getPhoneNumber(), username, role);
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

	@Transactional
	public String creatorFunction(ValidatedContactFields contactFields, Model model, boolean isDemo) { 

		model.addAttribute("message", "Form submission successful!"); 
		boolean successfulCreation = false;
		model.addAttribute("inputErrorExists", false);
		String postalCode = contactFields.getPostalCode();
		String address = contactFields.getAddress();
		String companyName = contactFields.getCompanyName();
		String lastName =  contactFields.getLastName();
		String firstName = contactFields.getFirstName();
		String phoneNumber = contactFields.getPhoneNumber();
		
		ContactFields contact = setNewContact(postalCode, address, companyName, lastName, firstName, phoneNumber, serverFunctioner.getUsername());
		
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
	
	@Transactional
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

////////////////////////
//former serverfunctions
///////////////////////
	
// used in demo	
@Transactional
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
		ContactFields contact = setModContact(contactFields.getId(), contactFields.getPostalCode(), contactFields.getAddress(), 
				contactFields.getCompanyName(), contactFields.getLastName(), contactFields.getFirstName(), contactFields.getPhoneNumber());
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
/*
public String viewerFunction(@Valid ContactFields contactFields, BindingResult bindingResult, Model model,
		boolean isDemo) {
	
	return "ContactsController";
}
*/

////////////////////////////////////////////////////////////////////////////

public ByteArrayResource exportAll(String username) throws IOException {
	    
	List<ContactFields> allContacts = new ArrayList<>();
		if (username == "ADMIN") {
			allContacts = contactMapper.select(c -> c);
			allContacts.sort(Comparator.comparing(ContactFields::getLastName));
		} else {
			allContacts = queryMapper.selectByCreator(username);
		}
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

////////////////////////////////////////////////////////////////////////////

	public String logonProcess(String username, String password, Model model, boolean isDemo) {

		System.out.println("Verifying user exists and is active");
		if (isDemo) {
			System.out.println("Demo mode selected");
	    	return "redirect:/Demo";
	    } else {
	        return "redirect:/ContactsController";
	    }
	}
	
////////////////////////////////////////////////////////////////////////////
	
	@Transactional
	public String updateFormService(ValidatedContactFields contactFields, BindingResult bindingResult, Model model, boolean isDemo) { 
	
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

//////////////////////
// Controller methods
//////////////////////

	public String initializeProductionService(Model model, int contactsPerPage) {
		
		WhereAmIDTO dto = new WhereAmIDTO();
		dto.setLastPageVisited("productionPage");
		model.addAttribute("whereAmIDTO", dto);

		int page = 1;
		int adminRoleId = 3;
		loginServicer.tokenExpiration();
		model.addAttribute("newContact", false);
	    model.addAttribute("viewContact", false);
	    model.addAttribute("modifyContact", false);
	    model.addAttribute("searchContact", false); 
	    model.addAttribute("deleteContact", false); 	
	    model.addAttribute("inputErrorExists", false);
		String username = serverFunctioner.getUsername();
		String role = loginServicer.getRole(username);
		Double totalContacts = 0d;
		
		if (role.equals(queryMapper.getRoleFromId(adminRoleId))) {
			// if user is an ADMIN, that user can see all contacts created by everyone
			totalContacts = (double) queryMapper.contactsCounterAdmin();
		} else {
			totalContacts = (double) queryMapper.contactsCounter(username);
		}
		Double totalPages = totalContacts / contactsPerPage;
		int roundedTotalPages = (int)Math.ceil(totalPages);
		if (roundedTotalPages == 0) {
			model.addAttribute("totalPages", 1);
		} else {
			model.addAttribute("totalPages", roundedTotalPages);
		}
		model.addAttribute("currentPage", page);
		if (page == 1) {
			model.addAttribute("previousPage", page);
		} else {
			model.addAttribute("previousPage", page - 1);
		}
		if (page == roundedTotalPages || roundedTotalPages == 0) {
			model.addAttribute("nextPage", page);
		} else {
			model.addAttribute("nextPage", page + 1);
		}
		List<ContactFields> pageContacts = pageDisplay(contactsPerPage, page);
		model.addAttribute("contacts", pageContacts);
		secConf.credentialsChecker(model);

	    System.out.println("Website OK!");
		return "Production/index";
		
	}

////////////////////////////////////////////////////////////////////////////

	public String paginationService(int page, Model model, int contactsPerPage) {
		
		int adminRoleId = 3;
		model.addAttribute("newContact", false);
		model.addAttribute("viewContact", false);
		model.addAttribute("modifyContact", false);
		model.addAttribute("searchContact", false); 
		model.addAttribute("deleteContact", false); 
		String username = serverFunctioner.getUsername();
		String role = loginServicer.getRole(username);
		Double totalContacts = 0d;
		
		if (role.equals(queryMapper.getRoleFromId(adminRoleId))) {
			// if user is an ADMIN, that user can see all contacts created by everyone
			totalContacts = (double) queryMapper.contactsCounterAdmin();
		} else {
			totalContacts = (double) queryMapper.contactsCounter(username);
		}
		Double totalPages = totalContacts / contactsPerPage;
		int roundedTotalPages = (int)Math.ceil(totalPages);
		if (roundedTotalPages == 0) {
			model.addAttribute("totalPages", 1);
		} else {
			model.addAttribute("totalPages", roundedTotalPages);
		}		model.addAttribute("currentPage", page);
		if (page == 1) {
			model.addAttribute("previousPage", page);
		} else {
			model.addAttribute("previousPage", page - 1);
		}
		if (page == roundedTotalPages || roundedTotalPages == 0) {
			model.addAttribute("nextPage", page);
		} else {
			model.addAttribute("nextPage", page + 1);
		}
		List<ContactFields> pageContacts = pageDisplay(contactsPerPage, page);
		model.addAttribute("contacts", pageContacts);
		secConf.credentialsChecker(model);
		System.out.println("Website OK!");
		
		return "Production/index";
	}

////////////////////////////////////////////////////////////////////////////

	public String createContactService(Model model) {
		
		model.addAttribute("newContact", false);
		model.addAttribute("viewContact", false);
		model.addAttribute("modifyContact", false);
		model.addAttribute("searchContact", false); 
		model.addAttribute("deleteContact", false); 

		secConf.credentialsChecker(model);
	    //List<ContactFields> allContacts = contactMapper.select(c -> c);
		//model.addAttribute("contacts", allContacts);
		model.addAttribute("contactField", new ValidatedContactFields());
		
		System.out.println("Website OK!");
		return "Production/index-create";
	}
	
////////////////////////////////////////////////////////////////////////////

	public String searchFormService(ValidatedContactFields contactFields, BindingResult bindingResult, Model model, Boolean isDemo) {
		
		model.addAttribute("newContact", false);
	    model.addAttribute("viewContact", false);
	    model.addAttribute("modifyContact", false);
	    model.addAttribute("searchContact", true);
	    model.addAttribute("deleteContact", false); 

		secConf.credentialsChecker(model);
		if (bindingResult.hasErrors()) {
	        model.addAttribute("errors", bindingResult.getAllErrors());
	        return "Production/index";  
	    }
		return searcherFunction(contactFields, bindingResult, model, isDemo);	
	}
	
////////////////////////////////////////////////////////////////////////////

	public String createFormService(ValidatedContactFields contactFields, BindingResult bindingResult, Model model, Boolean isDemo) {
		
		model.addAttribute("newContact", true);
	    model.addAttribute("viewContact", false);
	    model.addAttribute("modifyContact", false);
	    model.addAttribute("searchContact", false);		
	    model.addAttribute("deleteContact", false); 
	    ///
		String postalCode = contactFields.getPostalCode();
		String address = contactFields.getAddress();
		String companyName = contactFields.getCompanyName();
		String lastName =  contactFields.getLastName();
		String firstName = contactFields.getFirstName();
		String phoneNumber = contactFields.getPhoneNumber();
		ContactFields contact = setNewContact(postalCode, address, companyName, lastName, firstName, phoneNumber, serverFunctioner.getUsername());
	    ///
		model.addAttribute("contactFields", contact);
	    
	    if (bindingResult.hasErrors()) {
	    	secConf.credentialsChecker(model);
	    	model.addAttribute("inputErrorExists", true);
	    	model.addAttribute("errors", bindingResult.getAllErrors());
System.out.println("Full error stored in model: " + model.getAttribute("errors"));
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			for (FieldError error : fieldErrors) {
System.out.println("Error en el campo " + error.getField() + " y el error es " + error.getDefaultMessage());
				String errorDetail = error.getField() + "_error";
				model.addAttribute(errorDetail, error.getDefaultMessage());

			}

			return "Production/index-create";
	    }
	    
    	model.addAttribute("inputErrorExists", false);

		return creatorFunction(contactFields, model, isDemo);

	}
	
////////////////////////////////////////////////////////////////////////////
	
public String createBindingErrors(Model model) {

	secConf.credentialsChecker(model);
	
	return "Production/index-create";
}	

////////////////////////////////////////////////////////////////////////////

	public String deleteFormService(Long id, Model model, Boolean isDemo) {
		
		model.addAttribute("newContact", false);
	    model.addAttribute("viewContact", false);
	    model.addAttribute("modifyContact", false);
	    model.addAttribute("searchContact", false);
	    model.addAttribute("deleteContact", true); 
		String result = eraserFunction(id, model, isDemo);
		
	    return result;
	}
	
////////////////////////////////////////////////////////////////////////////

	public String modifyFormService(Long id, @RequestParam String action, Model model) {
		
		model.addAttribute("newContact", false);
	    model.addAttribute("viewContact", false);
	    model.addAttribute("modifyContact", true);
	    model.addAttribute("searchContact", false);
	    model.addAttribute("deleteContact", false); 
		
		Optional<ContactFields> opt = contactMapper.selectByPrimaryKey(id);
		if (opt.isPresent()) {
			optionInitializer(action, model);
			ContactFields clickedContact = opt.get();
			model.addAttribute("selectedContact", clickedContact);
			model.addAttribute("id", clickedContact.getId());
			secConf.credentialsChecker(model);
		} else {
			throw new IllegalArgumentException("Contact with ID " + id + " does not exist when retrieved by modifyForm()");
		}
		
		return "Production/index-view";
	}
		
////////////////////////////////////////////////////////////////////////////
	
	public String modifyBindingErrors(Model model) {
		
		secConf.credentialsChecker(model);
		model.addAttribute("newContact", false);
		model.addAttribute("viewContact", false);
		model.addAttribute("modifyContact", true);
		model.addAttribute("searchContact", false);
		model.addAttribute("deleteContact", false); 

		return "Production/index-view";
	}
	
////////////////////////////////////////////////////////////////////////////

	public String viewFormService(Long id, String action, Model model) {
		
		Optional<ContactFields> opt = contactMapper.selectByPrimaryKey(id);
		if (opt.isPresent()) {
			optionInitializer(action, model);
			ContactFields clickedContact = opt.get();
			model.addAttribute("selectedContact", clickedContact);
			model.addAttribute("id", clickedContact.getId());
			secConf.credentialsChecker(model);
		} else {
			throw new IllegalArgumentException("Contact with ID " + id + " does not exist when retrieved by viewForm()");
		}
		
		return "Production/index-view";
	}
	
////////////////////////////////////////////////////////////////////////////

	public ResponseEntity<ByteArrayResource> exportFormService() {
		
		System.out.println("Exporting all of the contact database");
		
		try {
			ByteArrayResource resource = exportAll(serverFunctioner.getUsername());
            String fileName = "contactsBackupCSV_" + 
	                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv";
            
			return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(resource);
		} catch (Exception e) {
            e.printStackTrace(); 
            
            return ResponseEntity.status(500).build(); 
		}
	}
	
////////////////////////////////////////////////////////////////////////////
	
	public String indexDemoService(Model model) {
		
		WhereAmIDTO dto = new WhereAmIDTO();
		dto.setLastPageVisited("demoPage");
		model.addAttribute("whereAmIDTO", dto);

		int adminRoleId = 3;
		int offset = 0;
		int contactsPerPage = 1000;
		List<ContactFields> allContacts = new ArrayList<>();
		String username = serverFunctioner.getUsername();
		String role = loginServicer.getRole(username);
		
		secConf.credentialsChecker(model);
		loginServicer.tokenExpiration();

		if (role.equals(queryMapper.getRoleFromId(adminRoleId))) {
			allContacts = queryMapper.paginatorQueryAdmin(contactsPerPage, offset);
		} else {
			allContacts = queryMapper.paginatorQuery(contactsPerPage, offset, username);
		}
		model.addAttribute("contacts", allContacts);
		model.addAttribute("contactFields", new ContactFields());
		System.out.println("Demo website OK!");
		
		return "Demo/index-demo";
	}
	
	
}

