package com.contactspring.controller;

import java.io.Serializable;
import java.util.List;

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
import com.contactspring.mybatis.generate.map.ContactFieldsMapper;
import com.contactspring.mybatis.generate.model.ContactFields;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/Demo")
public class DemoController implements Serializable{
	
////////////////////////////////////////////////////////////////////////////
// URL: http://localhost:8080/Demo
////////////////////////////////////////////////////////////////////////////
		
	
	@Autowired
	private ContactFieldsMapper contactMapper;
	@Autowired
    private ServerFunctions serverFunctions;

	
	private boolean isDemo = true;
	
////////////////////////////////////////////////////////////////////////////

	@GetMapping({"","/"})
	public String index(Model model) {
    	serverFunctions.tokenExpiration();
		List<ContactFields> allContacts = contactMapper.select(c -> c);
		model.addAttribute("contacts", allContacts);
		model.addAttribute("contactFields", new ContactFields());
		System.out.println("Demo website OK!");
		return "Demo/index-demo";
	}

////////////////////////////////////////////////////////////////////////////

	@PostMapping("/searchF")
	public String searchF(@ModelAttribute ValidatedContactFields contactFields, BindingResult bindingResult, Model model) {

		return serverFunctions.searcherFunction(contactFields, bindingResult, model, isDemo);
	}

////////////////////////////////////////////////////////////////////////////

	@PostMapping("/createF") 
	public String submitForm(@Valid @ModelAttribute ValidatedContactFields contactFields, 
		BindingResult bindingResult, Model model) { 

		return serverFunctions.creatorFunction(contactFields, bindingResult, model, isDemo);
	}

////////////////////////////////////////////////////////////////////////////

	@PostMapping("/deleteF") 
	public String deleteForm(@RequestParam Long id, Model model) { 
		
		return serverFunctions.eraserFunction(id, model, isDemo);
	}

////////////////////////////////////////////////////////////////////////////

	@PostMapping("/modifyF") 
	public String modifyForm(@Valid @ModelAttribute ValidatedContactFields contactFields, BindingResult bindingResult, Model model) {
		
		return serverFunctions.modifierFunction(contactFields, bindingResult, model, isDemo);
	}

	

}
