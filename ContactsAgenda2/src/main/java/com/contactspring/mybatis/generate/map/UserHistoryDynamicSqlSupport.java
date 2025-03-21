package com.contactspring.mybatis.generate.map;

import jakarta.annotation.Generated;
import java.sql.JDBCType;
import java.time.LocalDateTime;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class UserHistoryDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final UserHistory userHistory = new UserHistory();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> id = userHistory.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> username = userHistory.username;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<LocalDateTime> dateAdded = userHistory.dateAdded;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<LocalDateTime> lastLogin = userHistory.lastLogin;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class UserHistory extends AliasableSqlTable<UserHistory> {
        public final SqlColumn<Integer> id = column("id", JDBCType.INTEGER);

        public final SqlColumn<String> username = column("username", JDBCType.VARCHAR);

        public final SqlColumn<LocalDateTime> dateAdded = column("date_added", JDBCType.TIMESTAMP);

        public final SqlColumn<LocalDateTime> lastLogin = column("last_login", JDBCType.TIMESTAMP);

        public UserHistory() {
            super("user_history", UserHistory::new);
        }
    }
}