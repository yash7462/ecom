package com.ecom.modal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserModal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", length = 10)
	private Long userId;

	@Column(name = "user_name", length = 150)
	private String userName;

	@Column(name = "mobile")
	private String mobile;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "is_deleted", length = 1, columnDefinition = "int default 0")
	private int isDeleted;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<RoleModel> roles = new HashSet<>();

	@CreationTimestamp
	@Column(name = "created_on", updatable = false)
	private LocalDateTime createdOn = LocalDateTime.now();;

	@UpdateTimestamp
	@Column(name = "modified_on")
	private LocalDateTime modifiedOn = LocalDateTime.now();;

}
