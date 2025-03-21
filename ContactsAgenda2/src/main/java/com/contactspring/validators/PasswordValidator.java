package com.contactspring.validators;

import java.util.regex.Pattern;

import com.contactspring.annotations.ValidPassword;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final Pattern UPPERCASE = Pattern.compile("^(?=.*[A-Z]).*$");
    private static final Pattern LOWERCASE = Pattern.compile("^(?=.*[a-z]).*$");
    private static final Pattern DIGIT = Pattern.compile("^(?=.*[0-9]).*$");
    private static final Pattern PUNCTUATION = Pattern.compile("^(?=.*\\p{Punct}).*$");
    

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    	
    }
    
////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
    	
    	boolean isValid = true;
    	boolean isBlank = false;
   	
    	if (password == null || password.isEmpty()) {
    		context.buildConstraintViolationWithTemplate("Must not be empty").addConstraintViolation();
    		isValid = false;
    		isBlank = true;
    	}
    	
    	if (!isBlank) {
			
			if (password.length() < 10) {
	    		context.buildConstraintViolationWithTemplate("Min. 10 characters").addConstraintViolation();
	    		isValid = false;
	    	}
	    	
	    	if (!UPPERCASE.matcher(password).matches()) {
	    		context.buildConstraintViolationWithTemplate("Must have an uppercase letter").addConstraintViolation();
	    		isValid = false;
	    	}
	    	
	    	if (!LOWERCASE.matcher(password).matches()) {
	    		context.buildConstraintViolationWithTemplate("Must have a lowercase letter").addConstraintViolation();
	    		isValid = false;
	    	}
	    	
	    	if (!DIGIT.matcher(password).matches()) {
	    		context.buildConstraintViolationWithTemplate("Must have a number").addConstraintViolation();
	    		isValid = false;
	    	}
	    	
	    	if (!PUNCTUATION.matcher(password).matches()) {
	    		context.buildConstraintViolationWithTemplate("Must have a punctuation sign").addConstraintViolation();
	    		isValid = false;
	    	}
    	}
    	
        return isValid;
    }
}
