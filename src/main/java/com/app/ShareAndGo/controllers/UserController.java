package com.app.ShareAndGo.controllers;

import com.app.ShareAndGo.dto.requests.*;
import com.app.ShareAndGo.services.interfaces.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

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

    @GetMapping("/user/auth-user")
    public ResponseEntity<?> getAuthenticatedUser(){
        return userService.getAuthenticatedUserResponse();
    }

    @PutMapping(value = "/user/update-profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateUserProfile(@ModelAttribute ProfileUpdateRequest profileUpdateRequest){
        return userService.updateUserData(profileUpdateRequest);
    }


}
