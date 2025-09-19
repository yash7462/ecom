package com.ecom.service.user;

import com.ecom.config.JwtProvider;
import com.ecom.dao.OtpDetailsDao;
import com.ecom.dao.RoleDao;
import com.ecom.dao.UserDao;
import com.ecom.dto.UserDto;
import com.ecom.dto.request.LoginRequestPayload;
import com.ecom.dto.request.OtpRequestPayload;
import com.ecom.dto.request.UserRegistrationPayload;
import com.ecom.dto.response.ApiResponse;
import com.ecom.enums.ERole;
import com.ecom.exception.BusinessException;
import com.ecom.exception.CustomAuthenticationException;
import com.ecom.modal.OtpDetails;
import com.ecom.modal.RoleModel;
import com.ecom.modal.UserModal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final UserDao userDao;
    private final OtpDetailsDao otpDao;
    private final RoleDao roleDao;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserDao userDao, OtpDetailsDao otpDao, RoleDao roleDao, JwtProvider jwtProvider, AuthenticationManager authenticationManager) {
        this.userDao = userDao;
        this.otpDao = otpDao;
        this.roleDao = roleDao;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public ApiResponse<?> insertUserRegistration(@RequestBody UserRegistrationPayload userRegistrationPayload) {

        try {
            if (userDao.findByEmailAndIsDeleted(userRegistrationPayload.getEmail(), 0).isPresent()) {
                throw new BusinessException("Email is already in use");
            }

            UserModal userModal = new UserModal();
            userModal.setEmail(userRegistrationPayload.getEmail());
            userModal.setMobile(userRegistrationPayload.getMobile());
            userModal.setUserName(userRegistrationPayload.getUserName());

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            userModal.setPassword(passwordEncoder.encode(userRegistrationPayload.getPassword()));

            RoleModel customerRole = roleDao.findByName(ERole.ROLE_CUSTOMER)
                    .orElseThrow(() -> new BusinessException("Role not found"));
            userModal.setRoles(Collections.singleton(customerRole));

            userDao.save(userModal);

//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(userRegistrationPayload.getMobile(), userRegistrationPayload.getPassword()));
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            String jwt = jwtProvider.generateJwtToken(authentication);

            return ApiResponse.success("User registered successfully", null);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

    }

    @Override
    public ApiResponse<?> loginUser(LoginRequestPayload loginRequestPayload) {
        try {
            UserModal userModal = userDao.findByEmailAndIsDeleted(loginRequestPayload.getEmail(), 0)
                    .orElseThrow(() -> new CustomAuthenticationException("Invalid credentials"));

            BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
            if (!bc.matches(loginRequestPayload.getPassword(), userModal.getPassword())) {
                throw new CustomAuthenticationException("Invalid credentials");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestPayload.getEmail(),
                            loginRequestPayload.getPassword()
                    ));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtProvider.generateJwtToken(authentication);

            Map<String, String> response = new HashMap<>();
            response.put("token", jwt);
            return ApiResponse.success("Login successful", response);
        } catch (Exception e) {
            throw new CustomAuthenticationException("Authentication failed: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<?> getLoginUserDetails(HttpServletRequest request) {
        try {
            // 1. Extract JWT from the Authorization header
            String headerAuth = request.getHeader("Authorization");
            if (headerAuth == null || !headerAuth.startsWith("Bearer ")) {
                return ApiResponse.error(401, "Missing or invalid Authorization header");
            }

            String token = headerAuth.substring(7); // remove "Bearer "

            // 2. Validate token
            if (!jwtProvider.validateJwtToken(token, request)) {
                return ApiResponse.error(401, "Invalid or expired token");
            }

            // 3. Extract username (or userId) from token
            String email = jwtProvider.getUserNameFromJwtToken(token);
            UserDto userDto = userDao.findCustomUserDetailsByEmailId(email);

            // 5. Return user data
            return ApiResponse.success("Token Verified", userDto);

        } catch (Exception e) {
            return ApiResponse.error(500, "Could not fetch login user details: " + e.getMessage());
        }

    }

    @Override
    public ApiResponse<?> otpGenerate(@RequestBody OtpRequestPayload otpPayload) {
        Optional<UserModal> optionalUserModal = userDao.findByEmailAndIsDeleted(otpPayload.getEmail(), 0);
        if(optionalUserModal.isEmpty()) {
            Random random = new Random();
            String otp = String.format("%06d", random.nextInt(1000000));

            OtpDetails otpDetails = new OtpDetails();
            otpDetails.setEmail(otpPayload.getEmail());
            otpDetails.setOtp(otp);
            otpDao.save(otpDetails);

            Map<String, String> response = new HashMap<>();
            response.put("data", otp);
            return ApiResponse.success("Otp Generated", response);

        }else {
            return ApiResponse.error(500,"Email Already Exist");
        }

    }

    @Override
    public ApiResponse<?> otpVerify(@RequestBody OtpRequestPayload otpPayload) {
        List<OtpDetails> otpDetails = otpDao.findByEmailAndOtp(otpPayload.getEmail(),otpPayload.getOtp());
        if(!otpDetails.isEmpty()) {
            return ApiResponse.success("Otp Matched", null);
        }else {
            return ApiResponse.error(500,"Invalid Otp");
        }
    }

    @Override
    public ApiResponse<?> getAllUsers() {
        List<UserDto> userDtos = userDao.findAllUsers();
        return ApiResponse.success("All Users", userDtos);
    }
}
