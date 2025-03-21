package com.contactspring.controller;

import java.io.IOException;
import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactspring.mybatis.form.ValidatedContactFields;
import com.contactspring.mybatis.generate.model.ContactFields;
import com.contactspring.mybatis.service.ContactsService;

import jakarta.validation.Valid;

////////////////////////////////////////////////////////////////////////////
// URL: http://localhost:8080/Production
////////////////////////////////////////////////////////////////////////////

@Controller
@RequestMapping("/Production")
public class ProductionController implements Serializable {
	
	@Autowired
	private ContactsService contactsServicer;
	
	private boolean isDemo = false;
	
	private int contactsPerPage = 3;
	

    @ModelAttribute("contactFields")
    public ContactFields contactFields() {
        return new ContactFields();
    }
	
////////////////////////////////////////////////////////////////////////////

	@GetMapping({"","/"})
	public String index(Model model) {
		
		return contactsServicer.initializeProductionService(model, contactsPerPage);
	}
	
////////////////////////////////////////////////////////////////////////////
	
	@GetMapping({"/navigate"}) 
	public String paginationClicked(@RequestParam(required = true) int page, Model model) {

		return contactsServicer.paginationService(page, model, contactsPerPage);
	}
	
////////////////////////////////////////////////////////////////////////////
	
	@GetMapping({"/admin-site"})
	public String userAdministration(Model model) {
		
		return "Management";
	}
	
////////////////////////////////////////////////////////////////////////////
	
	@GetMapping({"/create"})
	public String createContact(Model model) {
		
		return contactsServicer.createContactService(model);
	}
	
////////////////////////////////////////////////////////////////////////////
	
	@GetMapping("/actionSelector")
	public String actionSelector(@RequestParam(required = false) String action, Model model) {
		
		return contactsServicer.optionInitializer(action, model);
	}
	
////////////////////////////////////////////////////////////////////////////

	@PostMapping("/searchF")
	public String searchForm(@ModelAttribute ValidatedContactFields contactFields, BindingResult bindingResult, Model model) {

		return contactsServicer.searchFormService(contactFields, bindingResult, model, isDemo);	
	}

////////////////////////////////////////////////////////////////////////////
	
	@PostMapping("/createF") 
	public String createForm(@Valid @ModelAttribute("contactField") ValidatedContactFields contactFields, 
					BindingResult bindingResult, Model model) { 
		
	    if (bindingResult.hasErrors()) {

			return contactsServicer.createBindingErrors(model);
	    }			    
	    
	    return contactsServicer.createFormService(contactFields, bindingResult, model, isDemo);
	}
	
////////////////////////////////////////////////////////////////////////////
	
	@PostMapping("/deleteF") 
	public String deleteForm(@RequestParam Long id, Model model) { 
		
	    return contactsServicer.deleteFormService(id, model, isDemo);
	}

////////////////////////////////////////////////////////////////////////////
	
	@PostMapping("/modifyF") 
	public String modifyForm(@Valid @RequestParam Long id, @RequestParam String action, Model model) { 

		return contactsServicer.modifyFormService(id, action, model);
	}

	
	@PostMapping("/updateF") 
	public String updateForm(@Valid @ModelAttribute("selectedContact") ValidatedContactFields contactFields, BindingResult bindingResult, Model model) { 
		
	    if (bindingResult.hasErrors()) {

			return contactsServicer.modifyBindingErrors(model);
	    }	
		
		return contactsServicer.updateFormService(contactFields, bindingResult, model, isDemo);
	}
	

////////////////////////////////////////////////////////////////////////////
	
	@PostMapping("/viewF") 
	public String viewForm(@RequestParam Long id, @RequestParam String action, Model model) { 
		
		return contactsServicer.viewFormService(id, action, model);
	}
	
////////////////////////////////////////////////////////////////////////////

	@PostMapping("/exportF")
	public ResponseEntity<ByteArrayResource> exportForm()  throws IOException {
		
        return contactsServicer.exportFormService(); 
	}

	
}	
