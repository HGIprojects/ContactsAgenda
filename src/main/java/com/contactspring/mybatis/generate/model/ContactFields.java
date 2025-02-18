package com.contactspring.mybatis.generate.model;

import jakarta.annotation.Generated;
import java.io.Serializable;
import java.time.LocalDateTime;

public class ContactFields implements Serializable {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String address;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String companyName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private LocalDateTime dateAdded;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String firstName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String lastName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String phoneNumber;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String postalCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private static final long serialVersionUID = 1L;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getAddress() {
        return address;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getCompanyName() {
        return companyName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getFirstName() {
        return firstName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setFirstName(String firstName) {
        this.firstName = firstName == null ? null : firstName.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getLastName() {
        return lastName;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setLastName(String lastName) {
        this.lastName = lastName == null ? null : lastName.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getPostalCode() {
        return postalCode;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode == null ? null : postalCode.trim();
    }
}