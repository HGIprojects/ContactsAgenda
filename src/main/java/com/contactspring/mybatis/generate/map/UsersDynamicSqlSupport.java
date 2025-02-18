package com.contactspring.mybatis.generate.map;

import java.sql.JDBCType;
import java.time.LocalDateTime;

import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

import jakarta.annotation.Generated;

public final class UsersDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final Users users = new Users();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> id = users.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> username = users.username;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> password = users.password;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> role = users.role;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Boolean> active = users.active;
    
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> email = users.email;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<LocalDateTime> dateAdded = users.dateAdded;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class Users extends AliasableSqlTable<Users> {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<String> username = column("username", JDBCType.VARCHAR);

        public final SqlColumn<String> password = column("password", JDBCType.VARCHAR);

        public final SqlColumn<String> role = column("role", JDBCType.VARCHAR);

        public final SqlColumn<Boolean> active = column("active", JDBCType.BIT);

        public final SqlColumn<String> email = column("email", JDBCType.VARCHAR);
        
        public final SqlColumn<LocalDateTime> dateAdded = column("date_added", JDBCType.TIMESTAMP);

        public Users() {
            super("users", Users::new);
        }
    }
}