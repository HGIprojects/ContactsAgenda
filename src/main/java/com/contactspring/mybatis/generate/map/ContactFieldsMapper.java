package com.contactspring.mybatis.generate.map;

import static com.contactspring.mybatis.generate.map.ContactFieldsDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import com.contactspring.mybatis.generate.model.ContactFields;
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
public interface ContactFieldsMapper extends CommonCountMapper, CommonDeleteMapper, CommonUpdateMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, address, companyName, dateAdded, firstName, lastName, phoneNumber, postalCode);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @Options(useGeneratedKeys=true,keyProperty="row.id")
    int insert(InsertStatementProvider<ContactFields> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insertMultipleWithGeneratedKeys")
    @Options(useGeneratedKeys=true,keyProperty="records.id")
    int insertMultiple(@Param("insertStatement") String insertStatement, @Param("records") List<ContactFields> records);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="ContactFieldsResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="address", property="address", jdbcType=JdbcType.VARCHAR),
        @Result(column="company_name", property="companyName", jdbcType=JdbcType.VARCHAR),
        @Result(column="date_added", property="dateAdded", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="first_name", property="firstName", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_name", property="lastName", jdbcType=JdbcType.VARCHAR),
        @Result(column="phone_number", property="phoneNumber", jdbcType=JdbcType.VARCHAR),
        @Result(column="postal_code", property="postalCode", jdbcType=JdbcType.VARCHAR)
    })
    List<ContactFields> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("ContactFieldsResult")
    Optional<ContactFields> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, contactFields, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, contactFields, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(ContactFields row) {
        return MyBatis3Utils.insert(this::insert, row, contactFields, c ->
            c.map(address).toProperty("address")
            .map(companyName).toProperty("companyName")
            .map(dateAdded).toProperty("dateAdded")
            .map(firstName).toProperty("firstName")
            .map(lastName).toProperty("lastName")
            .map(phoneNumber).toProperty("phoneNumber")
            .map(postalCode).toProperty("postalCode")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<ContactFields> records) {
        return MyBatis3Utils.insertMultipleWithGeneratedKeys(this::insertMultiple, records, contactFields, c ->
            c.map(address).toProperty("address")
            .map(companyName).toProperty("companyName")
            .map(dateAdded).toProperty("dateAdded")
            .map(firstName).toProperty("firstName")
            .map(lastName).toProperty("lastName")
            .map(phoneNumber).toProperty("phoneNumber")
            .map(postalCode).toProperty("postalCode")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(ContactFields row) {
        return MyBatis3Utils.insert(this::insert, row, contactFields, c ->
            c.map(address).toPropertyWhenPresent("address", row::getAddress)
            .map(companyName).toPropertyWhenPresent("companyName", row::getCompanyName)
            .map(dateAdded).toPropertyWhenPresent("dateAdded", row::getDateAdded)
            .map(firstName).toPropertyWhenPresent("firstName", row::getFirstName)
            .map(lastName).toPropertyWhenPresent("lastName", row::getLastName)
            .map(phoneNumber).toPropertyWhenPresent("phoneNumber", row::getPhoneNumber)
            .map(postalCode).toPropertyWhenPresent("postalCode", row::getPostalCode)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<ContactFields> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, contactFields, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<ContactFields> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, contactFields, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<ContactFields> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, contactFields, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<ContactFields> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, contactFields, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(ContactFields row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(address).equalTo(row::getAddress)
                .set(companyName).equalTo(row::getCompanyName)
                .set(dateAdded).equalTo(row::getDateAdded)
                .set(firstName).equalTo(row::getFirstName)
                .set(lastName).equalTo(row::getLastName)
                .set(phoneNumber).equalTo(row::getPhoneNumber)
                .set(postalCode).equalTo(row::getPostalCode);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(ContactFields row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(address).equalToWhenPresent(row::getAddress)
                .set(companyName).equalToWhenPresent(row::getCompanyName)
                .set(dateAdded).equalToWhenPresent(row::getDateAdded)
                .set(firstName).equalToWhenPresent(row::getFirstName)
                .set(lastName).equalToWhenPresent(row::getLastName)
                .set(phoneNumber).equalToWhenPresent(row::getPhoneNumber)
                .set(postalCode).equalToWhenPresent(row::getPostalCode);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(ContactFields row) {
        return update(c ->
            c.set(address).equalTo(row::getAddress)
            .set(companyName).equalTo(row::getCompanyName)
            .set(dateAdded).equalTo(row::getDateAdded)
            .set(firstName).equalTo(row::getFirstName)
            .set(lastName).equalTo(row::getLastName)
            .set(phoneNumber).equalTo(row::getPhoneNumber)
            .set(postalCode).equalTo(row::getPostalCode)
            .where(id, isEqualTo(row::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(ContactFields row) {
        return update(c ->
            c.set(address).equalToWhenPresent(row::getAddress)
            .set(companyName).equalToWhenPresent(row::getCompanyName)
            .set(dateAdded).equalToWhenPresent(row::getDateAdded)
            .set(firstName).equalToWhenPresent(row::getFirstName)
            .set(lastName).equalToWhenPresent(row::getLastName)
            .set(phoneNumber).equalToWhenPresent(row::getPhoneNumber)
            .set(postalCode).equalToWhenPresent(row::getPostalCode)
            .where(id, isEqualTo(row::getId))
        );
    }
}