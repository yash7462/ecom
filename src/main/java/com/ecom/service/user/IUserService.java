package com.ecom.service.user;

import com.ecom.dto.request.LoginRequestPayload;
import com.ecom.dto.request.OtpRequestPayload;
import com.ecom.dto.request.UserRegistrationPayload;
import com.ecom.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;

public interface IUserService {
    ApiResponse<?> insertUserRegistration(@RequestBody UserRegistrationPayload userRegistrationPayload);

    ApiResponse<?> loginUser(@RequestBody LoginRequestPayload loginRequestPayload);

    ApiResponse<?> getLoginUserDetails(HttpServletRequest request);

    ApiResponse<?> otpGenerate(@RequestBody OtpRequestPayload otpPayload);

    ApiResponse<?> otpVerify(@RequestBody OtpRequestPayload otpPayload);

    ApiResponse<?> getAllUsers();
}
