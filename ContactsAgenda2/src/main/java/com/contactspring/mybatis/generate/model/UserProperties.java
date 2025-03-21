package com.contactspring.mybatis.generate.model;

import jakarta.annotation.Generated;
import java.io.Serializable;
import java.time.LocalDateTime;

public class UserProperties implements Serializable {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String username;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Boolean active;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private LocalDateTime dateAdded;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String email;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer roleId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String verificationToken;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private LocalDateTime tokenDate;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private static final long serialVersionUID = 1L;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Integer id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getUsername() {
        return username;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Boolean getActive() {
        return active;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setActive(Boolean active) {
        this.active = active;
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
    public String getEmail() {
        return email;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getRoleId() {
        return roleId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getVerificationToken() {
        return verificationToken;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken == null ? null : verificationToken.trim();
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public LocalDateTime getTokenDate() {
        return tokenDate;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setTokenDate(LocalDateTime tokenDate) {
        this.tokenDate = tokenDate;
    }
}