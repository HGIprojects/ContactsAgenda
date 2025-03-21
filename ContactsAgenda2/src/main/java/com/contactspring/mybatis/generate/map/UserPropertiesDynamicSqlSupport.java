package com.contactspring.mybatis.generate.map;

import jakarta.annotation.Generated;
import java.sql.JDBCType;
import java.time.LocalDateTime;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class UserPropertiesDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final UserProperties userProperties = new UserProperties();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> id = userProperties.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> username = userProperties.username;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Boolean> active = userProperties.active;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<LocalDateTime> dateAdded = userProperties.dateAdded;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> email = userProperties.email;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> roleId = userProperties.roleId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> verificationToken = userProperties.verificationToken;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<LocalDateTime> tokenDate = userProperties.tokenDate;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class UserProperties extends AliasableSqlTable<UserProperties> {
        public final SqlColumn<Integer> id = column("id", JDBCType.INTEGER);

        public final SqlColumn<String> username = column("username", JDBCType.VARCHAR);

        public final SqlColumn<Boolean> active = column("active", JDBCType.BIT);

        public final SqlColumn<LocalDateTime> dateAdded = column("date_added", JDBCType.TIMESTAMP);

        public final SqlColumn<String> email = column("email", JDBCType.VARCHAR);

        public final SqlColumn<Integer> roleId = column("role_id", JDBCType.INTEGER);

        public final SqlColumn<String> verificationToken = column("verification_token", JDBCType.VARCHAR);

        public final SqlColumn<LocalDateTime> tokenDate = column("token_date", JDBCType.TIMESTAMP);

        public UserProperties() {
            super("user_properties", UserProperties::new);
        }
    }
}