package com.contactspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.contactspring.mybatis.service.ErrorService;

@Controller
public class CustomErrorController implements ErrorController{

	@Autowired
	private ErrorService errorServicer;
	
	@RequestMapping ("/error")
	public ModelAndView handleError(WebRequest webRequest) {
			
		return errorServicer.handleErrorService(webRequest);
	}
	

}
