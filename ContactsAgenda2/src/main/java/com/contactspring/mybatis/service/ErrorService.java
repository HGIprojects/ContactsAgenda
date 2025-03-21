package com.contactspring.mybatis.service;

import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;



@Service
public class ErrorService{
		
	public ModelAndView handleErrorService(WebRequest webRequest){
		
		Map<String, Object> errorAttributes = getErrorAttributes(webRequest, ErrorAttributeOptions.defaults());
		
		ModelAndView modelAndView = new ModelAndView("error/404");
		modelAndView.addObject("errorAttributes", errorAttributes);
		
		return modelAndView;
	}
	
	private Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
		return new DefaultErrorAttributes().getErrorAttributes(webRequest, options);
	}
}

