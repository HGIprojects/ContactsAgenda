package com.contactspring.mybatis.generate.model;

import jakarta.annotation.Generated;
import java.io.Serializable;

public class RoleProperties implements Serializable {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer roleId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String role;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private static final long serialVersionUID = 1L;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getRoleId() {
        return roleId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getRole() {
        return role;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setRole(String role) {
        this.role = role == null ? null : role.trim();
    }
}