package com.app.ShareAndGo.controllers;

import com.app.ShareAndGo.dto.requests.*;
import com.app.ShareAndGo.entities.User;
import com.app.ShareAndGo.services.interfaces.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
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

    @MessageMapping("/user.connectUser")
    @SendTo("/user/public")
    public User connectUser(){
        return userService.connectUser();
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public User disconnectUser(){
        return userService.disconnectUser();
    }

    @GetMapping("/user/auth-user")
    public ResponseEntity<?> getAuthenticatedUser(){
        return userService.getAuthenticatedUserResponse();
    }

    @PutMapping(value = "/user/update-profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateUserProfile(@ModelAttribute ProfileUpdateRequest profileUpdateRequest){
        return userService.updateUserData(profileUpdateRequest);
    }

    @GetMapping("/user/balance")
    public ResponseEntity<?> getBalanceOfAuthenticatedUser(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAuthenticatedUser().getAccountBalance());
    }

    @GetMapping("/admin/auth-admin")
    public ResponseEntity<?> getAuthenticatedAdmin(){
        return userService.getAuthenticatedAdmin();
    }


}
