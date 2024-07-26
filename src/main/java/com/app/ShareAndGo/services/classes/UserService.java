package com.app.ShareAndGo.services.classes;

import com.app.ShareAndGo.configs.JwtService;
import com.app.ShareAndGo.dto.requests.AdminCreationRequest;
import com.app.ShareAndGo.dto.requests.AdminLoginRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


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
        System.out.println(admin);
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
                                .message("Hyrja me sukses")
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
}
