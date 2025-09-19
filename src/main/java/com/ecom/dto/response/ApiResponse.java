// src/main/java/com/ecom/dto/ApiResponse.java
package com.ecom.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
    private T error;

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data,null);
    }

    public static <T> ApiResponse<T> error(int status, T error) {
        return new ApiResponse<>(status, null, null, error);
    }

    public static <T> ApiResponse<T> error(int status, String message, T error) {
        return new ApiResponse<>(status, message, null, error);
    }
}