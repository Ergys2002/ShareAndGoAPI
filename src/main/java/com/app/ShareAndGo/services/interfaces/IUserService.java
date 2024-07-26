package com.app.ShareAndGo.services.interfaces;

import com.app.ShareAndGo.dto.requests.AdminCreationRequest;
import com.app.ShareAndGo.dto.requests.AdminLoginRequest;
import org.springframework.http.ResponseEntity;

public interface IUserService {
     ResponseEntity<?> adminLogin(AdminLoginRequest loginRequest);
     ResponseEntity<?> createAdminRequest(AdminCreationRequest adminData);
}
