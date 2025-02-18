package com.contactspring.mybatis.original;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.contactspring.mybatis.form.ValidatedContactFields;
import com.contactspring.mybatis.form.ValidatedUserBasics;
import com.contactspring.mybatis.form.ValidatedUserProperties;
import com.contactspring.mybatis.form.ValidatedUserSecurity;
import com.contactspring.mybatis.generate.model.ContactFields;
import com.contactspring.mybatis.generate.model.UserHistory;

@Mapper
public interface QueryMapper{

	List<ValidatedContactFields> searchContacts(
			@Param("postalCodeQueried") String postalCode,
			@Param("addressQueried") String address,
			@Param("companyNameQueried") String companyName,
			@Param("lastNameQueried") String lastName,
			@Param("firstNameQueried") String firstName,
			@Param("phoneNumberQueried") String phoneNumber
			);
	
	List<ContactFields> paginatorQuery(
			@Param("contactsPerPageQueried") int contactsPerPage,
			@Param("offsetQueried") int requestedPage
			);

/////////////////////////////////////////////////////////////////////////////////////////////////////////

	int contactsCounter();

	
/////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////

	boolean isUniqueUsername(
			@Param("usernameQueried") String username
			);
	
	boolean isUniqueEmail(
			@Param("emailQueried") String email
			);
	
	String checkRole(
			@Param("queriedUsername") String username
			);
	
	Map<String, String> userParams(
			@Param("queriedUsername") String username
			);
	
	
	int getIdFromRole(
			@Param("roleTextQueried") String roleText
			);
	
	int usersCounter();

	boolean searchActive(
			@Param("usernameQueried") String username
			);

	boolean userMailNotActivatedMatch(
			@Param("usernameQueried") String username,
			@Param("emailQueried") String email
			);
		
	String getRoleFromId(
		@Param("roleIdQueried") int roleId
		);


/////////////////////////////////////////////////////////////////////////////////////////////////////////

	List<ValidatedUserProperties> searchUser(
			@Param("usernameQueried") String username,
			@Param("emailQueried") String email,
			@Param("roleIdQueried") int roleId,
			@Param("activeQueried") boolean active
			);
	
	List<ValidatedUserProperties> paginatorQueryUsers(
			@Param("usersPerPageQueried") int usersPerPage,
			@Param("offsetQueried") int offset
			);
	
	ValidatedUserProperties searchToken(
			@Param("tokenQueried") String token
			);
	
	ValidatedUserProperties findByUsernamePro(
			@Param("usernameQueried") String username
			);
	
	ValidatedUserProperties findByIdPro(
			@Param("idQueried") int id
			);
	
	ValidatedUserProperties findByEmailPro(
			@Param("emailQueried") String email
			);
	
	Boolean usernameExistsPro(
			@Param("usernameQueried") String username
			);
	
	Boolean emailExistsPro(
			@Param("emailQueried") String email
			);
	
	List<ValidatedUserProperties> getExpiredTokens(
			@Param("expirationCutQueried") LocalDateTime expirationCut
			);
	
	int getPropertiesId(
			@Param("usernameQueried") String username
			);
/////////////////////////////////////////////////////////////////////////////////////////////////////////

	ValidatedUserSecurity findByUsernameSec(
			@Param("usernameQueried") String username
			);

	ValidatedUserSecurity findByIdSec(
			@Param("idQueried") int id
			);

	int getSecurityId(
			@Param("usernameQueried") String username
			);
	
	Boolean usernameExistsSec(
			@Param("usernameQueried") String username
			);
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	ValidatedUserBasics findByUsernameBas(
			@Param("usernameQueried") String username
			);
	
	int getBasicsId(
			@Param("usernameQueried") String username
			);

/////////////////////////////////////////////////////////////////////////////////////////////////////////

	UserHistory findByUsernameHis(
			@Param("usernameQueried") String username
			);
	
	Boolean usernameExistsHis(
			@Param("usernameQueried") String username
			);
	
	Integer getHistoryId(
			@Param("usernameQueried") String username
			);

/////////////////////////////////////////////////////////////////////////////////////////////////////////


}
