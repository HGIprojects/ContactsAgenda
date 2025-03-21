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
			@Param("phoneNumberQueried") String phoneNumber,
			@Param("usernameQueried") String username
			);
	
	List<ValidatedContactFields> searchContactsAdmin(
			@Param("postalCodeQueried") String postalCode,
			@Param("addressQueried") String address,
			@Param("companyNameQueried") String companyName,
			@Param("lastNameQueried") String lastName,
			@Param("firstNameQueried") String firstName,
			@Param("phoneNumberQueried") String phoneNumber
			);
	
	List<ContactFields> selectByCreator(
			@Param("usernameQueried") String username
			);
	
	List<ContactFields> paginatorQuery(
			@Param("contactsPerPageQueried") int contactsPerPage,
			@Param("offsetQueried") int requestedPage,
			@Param("usernameQueried") String username
			);
	
	List<ContactFields> paginatorQueryAdmin(
			@Param("contactsPerPageQueried") int contactsPerPage,
			@Param("offsetQueried") int requestedPage
			);

/////////////////////////////////////////////////////////////////////////////////////////////////////////

	int contactsCounterAdmin();

	int contactsCounter(
			@Param("usernameQueried") String username
			);

/////////////////////////////////////////////////////////////////////////////////////////////////////////

	boolean isUniqueUsernamePro(
			@Param("usernameQueried") String username
			);
	
	boolean isUniqueUsernameExceptMePro(
			@Param("usernameQueried") String username,
			@Param("idQueried") int id
			);
	
	boolean isUniqueEmailPro(
			@Param("emailQueried") String email
			);
	
	boolean isUniqueEmailExceptMePro(
			@Param("emailQueried") String email,
			@Param("idQueried") int id
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
	
	int usersCounter(
			@Param("usernameQueried") Object username
			);

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
	
	Integer getProIdFromUsername(
			@Param("usernameQueried") String username
			);
	
	Integer getProIdFromEmail(
			@Param("emailQueried") String email
			);
	
	int getRoleFromUsername(
			@Param("usernameQueried") String username
			);
	
	String getProUsernameFromId(
			@Param("idQueried") int id
			);

	String getProEmailFromId(
			@Param("idQueried") int id
			);
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////

	ValidatedUserSecurity findByUsernameSec(
			@Param("usernameQueried") String username
			);

	ValidatedUserSecurity findByIdSec(
			@Param("idQueried") int id
			);

	Boolean usernameExistsSec(
			@Param("usernameQueried") String username
			);
	
	Integer getSecurityId(
			@Param("usernameQueried") String username
			);
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	ValidatedUserBasics findByUsernameBas(
			@Param("usernameQueried") String username
			);
	
	int getBasicsId(
			@Param("usernameQueried") String username
			);
	
	String getTableBasics(
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
