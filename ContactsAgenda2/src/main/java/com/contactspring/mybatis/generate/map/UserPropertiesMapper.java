package com.contactspring.mybatis.generate.map;

import static com.contactspring.mybatis.generate.map.UserPropertiesDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import com.contactspring.mybatis.generate.model.UserProperties;
import jakarta.annotation.Generated;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

@Mapper
public interface UserPropertiesMapper extends CommonCountMapper, CommonDeleteMapper, CommonUpdateMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, username, active, dateAdded, email, roleId, verificationToken, tokenDate);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @Options(useGeneratedKeys=true,keyProperty="row.id")
    int insert(InsertStatementProvider<UserProperties> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insertMultipleWithGeneratedKeys")
    @Options(useGeneratedKeys=true,keyProperty="records.id")
    int insertMultiple(@Param("insertStatement") String insertStatement, @Param("records") List<UserProperties> records);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="UserPropertiesResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="username", property="username", jdbcType=JdbcType.VARCHAR),
        @Result(column="active", property="active", jdbcType=JdbcType.BIT),
        @Result(column="date_added", property="dateAdded", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="email", property="email", jdbcType=JdbcType.VARCHAR),
        @Result(column="role_id", property="roleId", jdbcType=JdbcType.INTEGER),
        @Result(column="verification_token", property="verificationToken", jdbcType=JdbcType.VARCHAR),
        @Result(column="token_date", property="tokenDate", jdbcType=JdbcType.TIMESTAMP)
    })
    List<UserProperties> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("UserPropertiesResult")
    Optional<UserProperties> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, userProperties, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, userProperties, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Integer id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(UserProperties row) {
        return MyBatis3Utils.insert(this::insert, row, userProperties, c ->
            c.map(username).toProperty("username")
            .map(active).toProperty("active")
            .map(dateAdded).toProperty("dateAdded")
            .map(email).toProperty("email")
            .map(roleId).toProperty("roleId")
            .map(verificationToken).toProperty("verificationToken")
            .map(tokenDate).toProperty("tokenDate")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<UserProperties> records) {
        return MyBatis3Utils.insertMultipleWithGeneratedKeys(this::insertMultiple, records, userProperties, c ->
            c.map(username).toProperty("username")
            .map(active).toProperty("active")
            .map(dateAdded).toProperty("dateAdded")
            .map(email).toProperty("email")
            .map(roleId).toProperty("roleId")
            .map(verificationToken).toProperty("verificationToken")
            .map(tokenDate).toProperty("tokenDate")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(UserProperties row) {
        return MyBatis3Utils.insert(this::insert, row, userProperties, c ->
            c.map(username).toPropertyWhenPresent("username", row::getUsername)
            .map(active).toPropertyWhenPresent("active", row::getActive)
            .map(dateAdded).toPropertyWhenPresent("dateAdded", row::getDateAdded)
            .map(email).toPropertyWhenPresent("email", row::getEmail)
            .map(roleId).toPropertyWhenPresent("roleId", row::getRoleId)
            .map(verificationToken).toPropertyWhenPresent("verificationToken", row::getVerificationToken)
            .map(tokenDate).toPropertyWhenPresent("tokenDate", row::getTokenDate)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<UserProperties> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, userProperties, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<UserProperties> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, userProperties, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<UserProperties> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, userProperties, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<UserProperties> selectByPrimaryKey(Integer id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, userProperties, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(UserProperties row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(username).equalTo(row::getUsername)
                .set(active).equalTo(row::getActive)
                .set(dateAdded).equalTo(row::getDateAdded)
                .set(email).equalTo(row::getEmail)
                .set(roleId).equalTo(row::getRoleId)
                .set(verificationToken).equalTo(row::getVerificationToken)
                .set(tokenDate).equalTo(row::getTokenDate);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(UserProperties row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(username).equalToWhenPresent(row::getUsername)
                .set(active).equalToWhenPresent(row::getActive)
                .set(dateAdded).equalToWhenPresent(row::getDateAdded)
                .set(email).equalToWhenPresent(row::getEmail)
                .set(roleId).equalToWhenPresent(row::getRoleId)
                .set(verificationToken).equalToWhenPresent(row::getVerificationToken)
                .set(tokenDate).equalToWhenPresent(row::getTokenDate);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(UserProperties row) {
        return update(c ->
            c.set(username).equalTo(row::getUsername)
            .set(active).equalTo(row::getActive)
            .set(dateAdded).equalTo(row::getDateAdded)
            .set(email).equalTo(row::getEmail)
            .set(roleId).equalTo(row::getRoleId)
            .set(verificationToken).equalTo(row::getVerificationToken)
            .set(tokenDate).equalTo(row::getTokenDate)
            .where(id, isEqualTo(row::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(UserProperties row) {
        return update(c ->
            c.set(username).equalToWhenPresent(row::getUsername)
            .set(active).equalToWhenPresent(row::getActive)
            .set(dateAdded).equalToWhenPresent(row::getDateAdded)
            .set(email).equalToWhenPresent(row::getEmail)
            .set(roleId).equalToWhenPresent(row::getRoleId)
            .set(verificationToken).equalToWhenPresent(row::getVerificationToken)
            .set(tokenDate).equalToWhenPresent(row::getTokenDate)
            .where(id, isEqualTo(row::getId))
        );
    }
}