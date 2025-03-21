package com.contactspring.validators;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import com.contactspring.annotations.ValidEmail;
import com.contactspring.mybatis.original.QueryMapper;
import com.contactspring.mybatis.service.ServerFunctions;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    @Autowired
    private QueryMapper queryMapper;
    @Autowired
    private ServerFunctions serverFunctioner;
    
    @Override
    public void initialize(ValidEmail constraintAnnotation) {
    }
 
////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isValid(String newEmail, ConstraintValidatorContext context) {
    	
    	boolean isValid = true;
    	boolean isBlank = false;    	

    	if (newEmail == null || newEmail.isEmpty()) {
		
			context.disableDefaultConstraintViolation();

			context.buildConstraintViolationWithTemplate("Must not be empty").addConstraintViolation();
			isValid = false;
			isBlank = true;
		}
		
		if (!isBlank) {
			
    		if (serverFunctioner.lastVisitedPage().equals("loginPage")) {
    			isValid = queryMapper.isUniqueEmailPro(newEmail);
	    		if (!isValid) {
	    			context.disableDefaultConstraintViolation();
		    		context.buildConstraintViolationWithTemplate("Email already in use").addConstraintViolation();
	    		}
    		}
    		
    		if (serverFunctioner.lastVisitedPage().equals("managementPage") && queryMapper.getProIdFromEmail(newEmail) != null) {   
	    		isValid = queryMapper.isUniqueEmailExceptMePro(newEmail, serverFunctioner.getUserIdToModify());
	    		if (!isValid) {
	    			context.disableDefaultConstraintViolation();
		    		context.buildConstraintViolationWithTemplate("Email already in use").addConstraintViolation();
	    		}
    		}
	    	
	    	if (newEmail.length() > 50) {
				context.disableDefaultConstraintViolation();
	    		context.buildConstraintViolationWithTemplate("Max. 50 characters").addConstraintViolation();
	    		isValid = false;
	    	}
	    	
	    	if (!EMAIL_PATTERN.matcher(newEmail).matches()) {
				context.disableDefaultConstraintViolation();
	    		context.buildConstraintViolationWithTemplate("Must be a valid email").addConstraintViolation();
	    		isValid = false;
	    	}
		}

    	return isValid;
    }
    
}
