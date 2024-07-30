package com.app.ShareAndGo.services.classes;

import com.app.ShareAndGo.configs.JwtService;
import com.app.ShareAndGo.dto.requests.AdminCreationRequest;
import com.app.ShareAndGo.dto.requests.AdminLoginRequest;
import com.app.ShareAndGo.dto.requests.UserLoginRequest;
import com.app.ShareAndGo.dto.requests.UserSignUpRequest;
import com.app.ShareAndGo.dto.responses.AdminLoginResponse;
import com.app.ShareAndGo.entities.User;
import com.app.ShareAndGo.entities.UserProfile;
import com.app.ShareAndGo.enums.Role;
import com.app.ShareAndGo.repositories.UserRepository;
import com.app.ShareAndGo.services.interfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public ResponseEntity<?> adminLogin(AdminLoginRequest loginRequest) {
        User admin = userRepository.findByEmail(loginRequest.getEmail());
        if (admin == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Admini me email: " + loginRequest.getEmail() + " nuk ekziston");
        } else {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getEmail(),
                                loginRequest.getPassword())
                );
                String jwtToken = jwtService.generateToken(admin);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(AdminLoginResponse
                                .builder()
                                .token(jwtToken)
                                .message("Hyrja si admin me sukses")
                                .role(admin.getRole().toString())
                                .expiration(jwtService.getExpiration(jwtToken))
                                .build()
                        );
            } catch (BadCredentialsException e) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Kredencialet e vendosura jane gabim");
            } catch (Exception e) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Problem: " + e.getMessage());
            }
        }
    }


    @Override
    @Transactional
    public ResponseEntity<?> createAdminRequest(AdminCreationRequest adminData) {

        User fromDB = userRepository.findByEmail(adminData.getEmail());

        if (fromDB != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Admini qe doni te shtoni tashme ekziston");
        } else {
            UserProfile profile = UserProfile.builder()
                    .birthDate(LocalDate.parse(adminData.getBirthDate()))
                    .gender(adminData.getGender())
                    .firstname(adminData.getFirstname())
                    .lastname(adminData.getLastname())
                    .build();

            User admin = User.builder()
                    .email(adminData.getEmail())
                    .isActive(true)
                    .isBanned(false)
                    .isDeleted(false)
                    .nid(adminData.getNid())
                    .password(passwordEncoder.encode(adminData.getPassword()))
                    .phoneNumber(adminData.getPhoneNumber())
                    .salary(adminData.getSalary())
                    .role(Role.valueOf(adminData.getRole()))
                    .disabled(false)
                    .profile(profile)
                    .build();

            userRepository.save(admin);

            return ResponseEntity.status(HttpStatus.OK).body("Admini u shtua me sukses");
        }

    }

    @Override
    @Transactional
    public ResponseEntity<?> signUp(UserSignUpRequest userData) {
        List<User> existingUsers = userRepository.findAll();

        if(!existingUsers.isEmpty()){
            boolean existingEmailOrPhoneNumber = existingUsers
                    .stream()
                    .anyMatch(
                            (existingUser) -> existingUser.getEmail().equals(userData.getEmail())
                                    || existingUser.getPhoneNumber().equals(userData.getPhoneNumber()));

            if(existingEmailOrPhoneNumber){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email ose numri i telefonit eshte ne perdorim");
            }
        }


        UserProfile userProfile = UserProfile.builder()
                .firstname(userData.getFirstname())
                .lastname(userData.getLastname())
                .birthDate(LocalDate.parse(userData.getBirthDate()))
                .gender(userData.getGender())
                .build();

        User user = User.builder()
                .role(Role.USER)
                .isDeleted(false)
                .disabled(false)
                .isActive(true)
                .isBanned(false)
                .email(userData.getEmail())
                .phoneNumber(userData.getPhoneNumber())
                .password(passwordEncoder.encode(userData.getPassword()))
                .profile(userProfile)
                .build();

        userRepository.save(user);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userData.getEmail(),
                            userData.getPassword())
            );
            String jwtToken = jwtService.generateToken(user);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(AdminLoginResponse
                            .builder()
                            .token(jwtToken)
                            .message("Regjistrimi me sukses")
                            .role(user.getRole().toString())
                            .expiration(jwtService.getExpiration(jwtToken))
                            .build()
                    );
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Kredencialet e vendosura jane gabim");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Problem: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> login(UserLoginRequest userLoginRequest){

        User userFromDB = userRepository.findByEmail(userLoginRequest.getEmail());

        if (userFromDB == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Email: " + userLoginRequest.getEmail() + " nuk ekziston");
        } else {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                userLoginRequest.getEmail(),
                                userLoginRequest.getPassword())
                );
                String jwtToken = jwtService.generateToken(userFromDB);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(AdminLoginResponse
                                .builder()
                                .token(jwtToken)
                                .message("Hyrja me sukses")
                                .role(userFromDB.getRole().toString())
                                .expiration(jwtService.getExpiration(jwtToken))
                                .build()
                        );
            } catch (BadCredentialsException e) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Kredencialet e vendosura jane gabim");
            } catch (Exception e) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Problem: " + e.getMessage());
            }
        }
    }

    @Override
    public User getAuthenticatedUser() {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(authenticatedUserEmail);
    }


}
