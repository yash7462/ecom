package com.ecom.dao;

import com.ecom.dto.UserDto;
import com.ecom.modal.UserModal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<UserModal, Long> {

	Optional<UserModal> findByEmailAndIsDeleted(String mobile, int isDeleted);

	@Query(nativeQuery = true, value = """
			select users.user_id as userId, users.email, users.mobile, roles.name as roleName, 
			users.first_name as firstName, users.last_name as lastName, users.created_on as createdOn,  
			users.modified_on as modifiedOn
			from users
			LEFT JOIN user_roles ON user_roles.user_id = users.user_id
			LEFT JOIN roles ON roles.role_id = user_roles.role_id
			where users.is_deleted = 0 and roles.role_id > 1 order by users.created_on desc
			""")
	List<UserDto> findAllUsers();

	@Query(nativeQuery = true, value = """
			select users.user_id as userId, users.email, users.mobile, roles.name as roleName, 
			users.first_name as firstName, users.last_name as lastName, users.created_on as createdOn,  
			users.modified_on as modifiedOn
			from users
			LEFT JOIN user_roles ON user_roles.user_id = users.user_id
			LEFT JOIN roles ON roles.role_id = user_roles.role_id
			where users.email = :emailId and users.is_deleted = 0 order by users.created_on desc
			""")
	UserDto findCustomUserDetailsByEmailId(@Param("emailId") String emailId);
}
