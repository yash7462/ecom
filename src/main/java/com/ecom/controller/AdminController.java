package com.ecom.controller;

import com.ecom.dto.request.UserRegistrationPayload;
import com.ecom.dto.response.ApiResponse;
import com.ecom.service.user.IUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final IUserService userService;

    public AdminController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @ResponseBody
    public ApiResponse<?> insertUserRegistration(@RequestBody UserRegistrationPayload userRegistrationPayload) {
        return userService.insertUserRegistration(userRegistrationPayload);
    }

    @GetMapping("/users")
    public ApiResponse<?> users() {
        return userService.getAllUsers();
    }

}
