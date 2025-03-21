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

import com.contactspring.mybatis.form.ValidatedContactFields;
import com.contactspring.mybatis.service.ContactsService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/Demo")
public class DemoController implements Serializable{
	
////////////////////////////////////////////////////////////////////////////
// URL: http://localhost:8080/Demo
////////////////////////////////////////////////////////////////////////////
		
	
	@Autowired
	private ContactsService contactsServicer;

	
	private boolean isDemo = true;
	
////////////////////////////////////////////////////////////////////////////

	@GetMapping({"","/"})
	public String index(Model model) {

		return contactsServicer.indexDemoService(model);
	}

////////////////////////////////////////////////////////////////////////////

	@PostMapping("/searchF")
	public String searchF(@ModelAttribute ValidatedContactFields contactFields, BindingResult bindingResult, Model model) {

		return contactsServicer.searcherFunction(contactFields, bindingResult, model, isDemo);
	}

////////////////////////////////////////////////////////////////////////////

	@PostMapping("/createF") 
	public String submitForm(@Valid @ModelAttribute ValidatedContactFields contactFields, 
		BindingResult bindingResult, Model model) { 
		// removed because of redundancy with production service
		// return contactsServicer.creatorFunction(contactFields, bindingResult, model, isDemo);
		return contactsServicer.creatorFunction(contactFields, model, isDemo);
	}

////////////////////////////////////////////////////////////////////////////

	@PostMapping("/deleteF") 
	public String deleteForm(@RequestParam Long id, Model model) { 
		
		return contactsServicer.eraserFunction(id, model, isDemo);
	}

////////////////////////////////////////////////////////////////////////////

	@PostMapping("/modifyF") 
	public String modifyForm(@Valid @ModelAttribute ValidatedContactFields contactFields, BindingResult bindingResult, Model model) {
		
		return contactsServicer.modifierFunction(contactFields, bindingResult, model, isDemo);
	}

	

}
