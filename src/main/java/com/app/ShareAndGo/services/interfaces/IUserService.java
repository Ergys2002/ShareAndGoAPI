package com.app.ShareAndGo.services.interfaces;

import com.app.ShareAndGo.dto.requests.*;
import com.app.ShareAndGo.dto.responses.UserResponse;
import com.app.ShareAndGo.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

public interface IUserService {
     ResponseEntity<?> adminLogin(AdminLoginRequest loginRequest);
     ResponseEntity<?> createAdminRequest(AdminCreationRequest adminData);

     ResponseEntity<?> signUp(UserSignUpRequest userData);

     ResponseEntity<?> login(UserLoginRequest userLoginRequest);

     User getAuthenticatedUser();

     ResponseEntity<?> getAuthenticatedUserResponse();

     ResponseEntity<?> updateUserData(ProfileUpdateRequest profileUpdateRequest);

     User getUserById(Long id);

    ResponseEntity<?> getAuthenticatedAdmin();

    User connectUser();

    User disconnectUser();

    ResponseEntity<?> getUserStatistics();

    ResponseEntity<?> banUser(Long userId);

    ResponseEntity<?> getUserInfo(Long id);
}
