package com.contactspring.controller;

import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomErrorController implements ErrorController{

	@RequestMapping ("/error")
	public ModelAndView handleError(WebRequest webRequest) {
		
		Map<String, Object> errorAttributes = getErrorAttributes(webRequest, ErrorAttributeOptions.defaults());
		
		ModelAndView modelAndView = new ModelAndView("error/404");
		modelAndView.addObject("errorAttributes", errorAttributes);
		
		return modelAndView;
	}
	
	private Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
		return new DefaultErrorAttributes().getErrorAttributes(webRequest, options);
	}
}
