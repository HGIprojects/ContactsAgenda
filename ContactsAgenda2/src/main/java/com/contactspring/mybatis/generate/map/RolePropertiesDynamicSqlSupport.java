package com.contactspring.mybatis.generate.map;

import jakarta.annotation.Generated;
import java.sql.JDBCType;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class RolePropertiesDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final RoleProperties roleProperties = new RoleProperties();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> roleId = roleProperties.roleId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> role = roleProperties.role;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class RoleProperties extends AliasableSqlTable<RoleProperties> {
        public final SqlColumn<Integer> roleId = column("role_id", JDBCType.INTEGER);

        public final SqlColumn<String> role = column("role", JDBCType.VARCHAR);

        public RoleProperties() {
            super("role_properties", RoleProperties::new);
        }
    }
}