package com.ecom.dto;

import java.time.LocalDateTime;

public interface UserDto {
    public String getUserId();
    public String getEmail();
    public String getMobile();
    public String getRoleName();
    public String getFirstName();
    public String getLastName();
    public LocalDateTime getCreatedOn();
    public LocalDateTime getModifiedOn();
}
