package com.app.ShareAndGo.controllers;

import com.app.ShareAndGo.dto.requests.AdminCreationRequest;
import com.app.ShareAndGo.dto.requests.AdminLoginRequest;
import com.app.ShareAndGo.dto.requests.UserLoginRequest;
import com.app.ShareAndGo.dto.requests.UserSignUpRequest;
import com.app.ShareAndGo.services.interfaces.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@Valid @RequestBody AdminLoginRequest adminLoginRequest){
        return userService.adminLogin(adminLoginRequest);
    }

    @PostMapping("/sup-admin/create-admin")
    public ResponseEntity<?> createAdmin(@Valid @RequestBody AdminCreationRequest adminData){
        return userService.createAdminRequest(adminData);
    }

    @PostMapping("/user/sign-up")
    public ResponseEntity<?> userSignUp(@Valid @RequestBody UserSignUpRequest userData){
        return userService.signUp(userData);
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> userLogin(@Valid @RequestBody UserLoginRequest userLoginRequest){
        return userService.login(userLoginRequest);
    }
}
