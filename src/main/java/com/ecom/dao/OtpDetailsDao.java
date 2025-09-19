package com.ecom.dao;

import com.ecom.modal.OtpDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtpDetailsDao extends JpaRepository<OtpDetails, Long>{

	List<OtpDetails> findByMobileAndOtp(String mobile, String otp);

	List<OtpDetails> findByEmailAndOtp(String email, String otp);
}
