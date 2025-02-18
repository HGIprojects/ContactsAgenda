package com.contactspring.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactspring.config.SecurityConfig;
import com.contactspring.mybatis.form.ValidatedContactFields;
import com.contactspring.mybatis.generate.map.ContactFieldsMapper;
import com.contactspring.mybatis.generate.model.ContactFields;
import com.contactspring.mybatis.original.QueryMapper;
import com.contactspring.mybatis.service.ContactsService;

import jakarta.validation.Valid;

////////////////////////////////////////////////////////////////////////////
// URL: http://localhost:8080/Production
////////////////////////////////////////////////////////////////////////////

@Controller
@RequestMapping("/Production")
public class ProductionController implements Serializable {
	
	@Autowired
	private ContactFieldsMapper contactMapper;
	@Autowired
	private ContactsService contactsServicer;
	@Autowired
    private ServerFunctions serverFunctions;
	@Autowired
	private QueryMapper queryMapper;
	
	@Autowired
	private SecurityConfig secConf;
	
	private boolean isDemo = false;
	
	private int contactsPerPage = 3;
	

    @ModelAttribute("contactFields")
    public ContactFields contactFields() {
        return new ContactFields();
    }
	
////////////////////////////////////////////////////////////////////////////

	@GetMapping({"","/"})
	public String index(Model model) {
		
		int page = 1;
    	serverFunctions.tokenExpiration();
		model.addAttribute("newContact", false);
	    model.addAttribute("viewContact", false);
	    model.addAttribute("modifyContact", false);
	    model.addAttribute("searchContact", false); 
	    model.addAttribute("deleteContact", false); 	
 
		Double totalContacts = (double) queryMapper.contactsCounter();
		Double totalPages = totalContacts / contactsPerPage;
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
		List<ContactFields> pageContacts = contactsServicer.pageDisplay(contactsPerPage, page);
		model.addAttribute("contacts", pageContacts);
		secConf.credentialsChecker(model);

	    System.out.println("Website OK!");
		return "Production/index";
	}
	
////////////////////////////////////////////////////////////////////////////
	
	@GetMapping({"/navigate"}) 
	public String paginationClicked(@RequestParam(required = true) int page, Model model) {

		model.addAttribute("newContact", false);
		model.addAttribute("viewContact", false);
		model.addAttribute("modifyContact", false);
		model.addAttribute("searchContact", false); 
		model.addAttribute("deleteContact", false); 

		Double totalContacts = (double) queryMapper.contactsCounter();
		Double totalPages = totalContacts / contactsPerPage;
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
		List<ContactFields> pageContacts = contactsServicer.pageDisplay(contactsPerPage, page);
		model.addAttribute("contacts", pageContacts);
		secConf.credentialsChecker(model);
		System.out.println("Website OK!");
		
		return "Production/index";
	}
	
////////////////////////////////////////////////////////////////////////////
	
	@GetMapping({"/admin-site"})
	public String userAdministration(Model model) {
		

		return "Management";
	}
	
////////////////////////////////////////////////////////////////////////////
	
	@GetMapping({"/create"})
	public String createContact(Model model) {
		
		model.addAttribute("newContact", false);
		model.addAttribute("viewContact", false);
		model.addAttribute("modifyContact", false);
		model.addAttribute("searchContact", false); 
		model.addAttribute("deleteContact", false); 

		secConf.credentialsChecker(model);
	    List<ContactFields> allContacts = contactMapper.select(c -> c);
		model.addAttribute("contacts", allContacts);
		
		System.out.println("Website OK!");
		return "Production/index-create";
	}
	
////////////////////////////////////////////////////////////////////////////
	
	@GetMapping("/actionSelector")
	public String actionSelector(@RequestParam(required = false) String action, Model model) {
		
		contactsServicer.optionInitializer(action, model);
		return "Production/index";
	}
	
////////////////////////////////////////////////////////////////////////////

	@PostMapping("/searchF")
	public String searchForm(@ModelAttribute ValidatedContactFields contactFields, BindingResult bindingResult, Model model) {
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
		return serverFunctions.searcherFunction(contactFields, bindingResult, model, isDemo);	
	}

////////////////////////////////////////////////////////////////////////////
	
	@PostMapping("/createF") 
	public String createForm(@Valid @ModelAttribute ValidatedContactFields contactFields, 
					BindingResult bindingResult, Model model) { 

		model.addAttribute("newContact", true);
	    model.addAttribute("viewContact", false);
	    model.addAttribute("modifyContact", false);
	    model.addAttribute("searchContact", false);		
	    model.addAttribute("deleteContact", false); 
	    
	    String result = serverFunctions.creatorFunction(contactFields, bindingResult, model, isDemo);

		return result;
	}
	
////////////////////////////////////////////////////////////////////////////
	

	@PostMapping("/deleteF") 
	public String deleteForm(@RequestParam Long id, Model model) { 
		
		model.addAttribute("newContact", false);
	    model.addAttribute("viewContact", false);
	    model.addAttribute("modifyContact", false);
	    model.addAttribute("searchContact", false);
	    model.addAttribute("deleteContact", true); 
		String result = serverFunctions.eraserFunction(id, model, isDemo);
		
	    return result;
	}

////////////////////////////////////////////////////////////////////////////
	
	@PostMapping("/modifyF") 
	public String modifyForm(@Valid @RequestParam Long id, @RequestParam String action, Model model) { 

		model.addAttribute("newContact", false);
	    model.addAttribute("viewContact", false);
	    model.addAttribute("modifyContact", true);
	    model.addAttribute("searchContact", false);
	    model.addAttribute("deleteContact", false); 
		
		Optional<ContactFields> opt = contactMapper.selectByPrimaryKey(id);
		if (opt.isPresent()) {
			contactsServicer.optionInitializer(action, model);
			ContactFields clickedContact = opt.get();
			model.addAttribute("selectedContact", clickedContact);
			model.addAttribute("id", clickedContact.getId());
			secConf.credentialsChecker(model);
		} else {
			throw new IllegalArgumentException("Contact with ID " + id + " does not exist when retrieved by modifyForm()");
		}
		return "Production/index-view";
	}

	
	@PostMapping("/updateF") 
	public String updateForm(@Valid @ModelAttribute ValidatedContactFields contactFields, BindingResult bindingResult, Model model) { 
		
		return serverFunctions.modifierFunction(contactFields, bindingResult, model, isDemo);
		}
	

////////////////////////////////////////////////////////////////////////////
	
	@PostMapping("/viewF") 
	public String viewForm(@RequestParam Long id, @RequestParam String action, Model model) { 
		
		Optional<ContactFields> opt = contactMapper.selectByPrimaryKey(id);
		if (opt.isPresent()) {
			contactsServicer.optionInitializer(action, model);
			ContactFields clickedContact = opt.get();
			model.addAttribute("selectedContact", clickedContact);
			model.addAttribute("id", clickedContact.getId());
			secConf.credentialsChecker(model);
		} else {
			throw new IllegalArgumentException("Contact with ID " + id + " does not exist when retrieved by viewForm()");
		}
		return "Production/index-view";

	}
	
	@PostMapping("/exportF")
	public ResponseEntity<ByteArrayResource> exportForm() throws IOException {
		
		System.out.println("Exporting all of the contact database");
		
		try {
			 
			 ByteArrayResource resource = serverFunctions.exportAll();
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

	
}	
