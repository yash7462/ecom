package com.ecom.dto.request;

import lombok.Data;

@Data
public class OtpRequestPayload {
	private String email;
	private String otp;
}
