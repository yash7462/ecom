package com.ecom.dto.request;

import lombok.Data;

@Data
public class UserRegistrationPayload {
	//private long userId;
	private String userName;
	private String mobile;
	private String email;
	private String password;
	
	
}
