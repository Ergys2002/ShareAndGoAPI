package com.app.ShareAndGo.controllers;

import com.app.ShareAndGo.dto.requests.AdminCreationRequest;
import com.app.ShareAndGo.dto.requests.AdminLoginRequest;
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
        System.out.println(adminLoginRequest);
        return userService.adminLogin(adminLoginRequest);
    }

    @PostMapping("/sup-admin/create-admin")
    public ResponseEntity<?> createAdmin(@Valid @RequestBody AdminCreationRequest adminData){
        System.out.println(adminData);
        return userService.createAdminRequest(adminData);
    }
}
