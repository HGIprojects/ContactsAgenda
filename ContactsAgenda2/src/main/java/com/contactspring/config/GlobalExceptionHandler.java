package com.contactspring.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadSqlGrammarException.class)
    public ModelAndView handleDatabaseError(BadSqlGrammarException ex, Model model) {
        ModelAndView mav = new ModelAndView("Error/database-error");  // This matches the HTML filename
        System.out.println("Database error detected");
        mav.addObject("errorMessage", "The database is currently unavailable. Please try again later.");
        return mav;
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex){
    	
    	Map<String, String> errors = new HashMap<>();
    	
    	ex.getBindingResult().getAllErrors().forEach((error) -> {
    		String fieldName = ((FieldError) error).getField();
    		String errorMessage = error.getDefaultMessage();
    		errors.put(fieldName,  errorMessage);
    	});
    	
    	return new ResponseEntity<>(ex.getBindingResult().toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public RedirectView handleRollbackException(RuntimeException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return new RedirectView("/Management", true);
    }
}