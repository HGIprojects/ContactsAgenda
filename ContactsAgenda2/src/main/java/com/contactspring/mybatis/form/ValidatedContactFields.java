package com.contactspring.mybatis.form;

import java.io.Serializable;

import com.contactspring.mybatis.generate.model.ContactFields;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ValidatedContactFields extends ContactFields implements Serializable{
	
    @Size(max = 50, message = "Max. 50 characters")
    private String address;

    @Size(max = 50, message = "Max. 50 characters")
    private String companyName;

    @Size(max = 50, message = "Max. 50 characters")
    private String firstName;

    @Size(max = 50, message = "Max. 50 characters")
    private String lastName;

    @Size(min = 10, max = 11, message = "Must be between 10 and 11 characters")
    @Pattern(regexp = "\\d{10,11}", message = "Phone number must contain only digits")
    @NotBlank(message = "Must not be empty")
    private String phoneNumber;

    @Size(min = 7, max = 7, message = "Must be 7 characters")
    @Pattern(regexp = "\\d{7}", message = "Postal code must contain only digits")
    @NotBlank(message = "Must not be empty")
    private String postalCode;

}
