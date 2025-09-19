package com.ecom.modal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;

@Entity
@Table(name = "otp_details")
@Getter
@Setter
public class OtpDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "otp_details_id", length = 10)
	private long otpDetailsId;

	@Column(name = "mobile")
	private String mobile;

	@Column(name = "email")
	private String email;

	@Column(name = "otp")
	private String otp;

	@CreationTimestamp
	@Column(name = "created_on", updatable = false)
	private Date createdOn;

}
