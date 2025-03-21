package com.contactspring.validators;

import org.springframework.beans.factory.annotation.Autowired;

import com.contactspring.annotations.ValidUsername;
import com.contactspring.mybatis.original.QueryMapper;
import com.contactspring.mybatis.service.ServerFunctions;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

    @Autowired
    private QueryMapper queryMapper;
    @Autowired
    private ServerFunctions serverFunctioner;
    
    @Override
    public void initialize(ValidUsername constraintAnnotation) {
    }
 
////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isValid(String newUsername, ConstraintValidatorContext context ) {
    	
    	boolean isValid = true;
		boolean isBlank = false;

    	if (newUsername == null || newUsername.isEmpty()) {
			context.disableDefaultConstraintViolation();
    		context.buildConstraintViolationWithTemplate("Must not be empty").addConstraintViolation();
    		isValid = false;
    		isBlank = true;
    	}
    	
    	if (!isBlank) {
    		if (serverFunctioner.lastVisitedPage().equals("loginPage")) {
				isValid = queryMapper.isUniqueUsernamePro(newUsername);
	    		if (!isValid) {
					context.disableDefaultConstraintViolation();
		    		context.buildConstraintViolationWithTemplate("Username already in use").addConstraintViolation();
	    		}
    		}
    		
			if (serverFunctioner.lastVisitedPage().equals("managementPage") && queryMapper.getProIdFromUsername(newUsername) != null) {   
    			isValid = queryMapper.isUniqueUsernameExceptMePro(newUsername, serverFunctioner.getUserIdToModify());
	    		if (!isValid) {
					context.disableDefaultConstraintViolation();
		    		context.buildConstraintViolationWithTemplate("Username already in use").addConstraintViolation();
	    		}
    		}
    	
	    	if (newUsername.length() > 20) {
				context.disableDefaultConstraintViolation();
	    		context.buildConstraintViolationWithTemplate("Max. 20 characters").addConstraintViolation();
	    		isValid = false;
	    	}
    	}

    	return isValid;
    }

}
