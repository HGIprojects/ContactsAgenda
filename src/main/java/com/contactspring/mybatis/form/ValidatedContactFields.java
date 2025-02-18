package com.contactspring.mybatis.form;

import java.io.Serializable;

import com.contactspring.mybatis.generate.model.ContactFields;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ValidatedContactFields extends ContactFields implements Serializable{
	
    @Size(max = 50, message = "Must be 50 characters or less")
    private String address;

    @Size(max = 50, message = "Must be 50 characters or less")
    private String companyName;

    @Size(max = 50, message = "Must be 50 characters or less")
    private String firstName;

    @Size(max = 50, message = "Must be 50 characters or less")
    private String lastName;

    @Size(min = 10, max = 11, message = "Must be between 10 and 11 characters long")
    @Pattern(regexp = "\\d{10,11}", message = "Phone number must contain only digits")
    private String phoneNumber;

    @Size(min = 7, max = 7, message = "Must be 7 characters long")
    @Pattern(regexp = "\\d{7}", message = "Postal code must contain only digits")
    private String postalCode;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

    
}
