package com.ecom.controller;

import com.ecom.dto.request.LoginRequestPayload;
import com.ecom.dto.request.OtpRequestPayload;
import com.ecom.dto.response.ApiResponse;
import com.ecom.service.user.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Log
@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final IUserService userService;

    public AuthController(IUserService userService) {
        this.userService = userService;
    }

	@PostMapping("/login")
	@ResponseBody
	public ApiResponse<?> loginUser(@RequestBody LoginRequestPayload loginRequestPayload) {
		return userService.loginUser(loginRequestPayload);
	}

	@PostMapping("/login-user")
	@ResponseBody
	public ApiResponse<?> getLoginUserDetails(HttpServletRequest request) {
		return userService.getLoginUserDetails(request);
	}
	
	@PostMapping("/otp/generate")
	@ResponseBody
	public ApiResponse<?> otpGenerate(@RequestBody OtpRequestPayload otpPayload) {
		return userService.otpGenerate(otpPayload);
		
	}
	
	@PostMapping("/otp/verify")
	@ResponseBody
	public ApiResponse<?> otpVerify(@RequestBody OtpRequestPayload otpPayload) {
		return userService.otpVerify(otpPayload);
	}

}
 