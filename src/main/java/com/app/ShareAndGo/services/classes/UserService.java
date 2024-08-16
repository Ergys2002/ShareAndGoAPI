package com.app.ShareAndGo.services.classes;

import com.app.ShareAndGo.configs.JwtService;
import com.app.ShareAndGo.dto.requests.*;
import com.app.ShareAndGo.dto.responses.AdminLoginResponse;
import com.app.ShareAndGo.dto.responses.AdminResponse;
import com.app.ShareAndGo.dto.responses.UserLoginResponse;
import com.app.ShareAndGo.dto.responses.UserResponse;
import com.app.ShareAndGo.entities.PreviousPassword;
import com.app.ShareAndGo.entities.User;
import com.app.ShareAndGo.entities.UserProfile;
import com.app.ShareAndGo.enums.ActivityStatus;
import com.app.ShareAndGo.enums.Role;
import com.app.ShareAndGo.repositories.PreviousPasswordRepository;
import com.app.ShareAndGo.repositories.UserProfileRepository;
import com.app.ShareAndGo.repositories.UserRepository;
import com.app.ShareAndGo.services.interfaces.IImageService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final PreviousPasswordRepository previousPasswordRepository;
    private final IImageService imageService;
    private final UserProfileRepository userProfileRepository;

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

        if (!existingUsers.isEmpty()) {
            boolean existingEmailOrPhoneNumber = existingUsers
                    .stream()
                    .anyMatch(
                            (existingUser) -> existingUser.getEmail().equals(userData.getEmail())
                                    || existingUser.getPhoneNumber().equals(userData.getPhoneNumber()));

            if (existingEmailOrPhoneNumber) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email ose numri i telefonit eshte ne perdorim");
            }
        }


        UserProfile userProfile = UserProfile.builder()
                .firstname(userData.getFirstname())
                .lastname(userData.getLastname())
                .birthDate(LocalDate.parse(userData.getBirthDate()))
                .profilePictureUrl("default_profile_picture.jpg")
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
    public ResponseEntity<?> login(UserLoginRequest userLoginRequest) {

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

    @Override
    public ResponseEntity<?> getAuthenticatedUserResponse() {
        User authenticatedUser = getAuthenticatedUser();
        UserResponse userResponse = new UserResponse() {
            @Override
            public String getPhoneNumber() {
                return authenticatedUser.getPhoneNumber();
            }

            @Override
            public double getAccountBalance() {
                return authenticatedUser.getAccountBalance();
            }

            @Override
            public String getEmail() {
                return authenticatedUser.getEmail();
            }

            @Override
            public UserProfile getProfile() {
                return authenticatedUser.getProfile();
            }
        };

        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateUserData(ProfileUpdateRequest profileUpdateRequest) {
        // Get the currently authenticated user
        User user = getAuthenticatedUser();
        UserProfile userProfile = user.getProfile();

        // Update basic profile information
        updateBasicProfileInfo(userProfile, profileUpdateRequest);

        // Update phone number if changed
        if (!user.getPhoneNumber().equals(profileUpdateRequest.getPhoneNumber())) {
            // Check if the new phone number is already in use
            if (isPhoneNumberInUse(profileUpdateRequest.getPhoneNumber())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Numri i telefonit që keni vendosur është tashmë në përdorim");
            }
            user.setPhoneNumber(profileUpdateRequest.getPhoneNumber());
        }

        // Handle password update
        try {
            handlePasswordUpdate(user, profileUpdateRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

        // Handle profile picture update
        if (profileUpdateRequest.getNewProfilePicture() != null) {
            try {
                String url = updateProfilePicture(userProfile, profileUpdateRequest.getNewProfilePicture());
                userProfile.setProfilePictureUrl(url);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Fotoja e profilit nuk u ruajt");
            }
        }

        // Save the updated profile and user information
        userProfileRepository.save(userProfile);
        userRepository.save(user);

        // Re-authenticate the user and generate a new token if the password was updated
        if (profileUpdateRequest.getNewPassword() != null) {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getEmail(), profileUpdateRequest.getNewPassword())
            );
        }

        String jwtToken = jwtService.generateToken(user);
        return ResponseEntity.status(HttpStatus.OK).body(UserLoginResponse.builder()
                .token(jwtToken)
                .message("Ndryshimi i profilit me sukses")
                .role(user.getRole().toString())
                .expiration(jwtService.getExpiration(jwtToken))
                .build());
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public ResponseEntity<?> getAuthenticatedAdmin() {
        User authenticatedUser = getAuthenticatedUser();

        AdminResponse adminResponse = new AdminResponse() {
            @Override
            public String getPhoneNumber() {
                return authenticatedUser.getPhoneNumber();
            }

            @Override
            public double getAccountBalance() {
                return authenticatedUser.getAccountBalance();
            }

            @Override
            public String getEmail() {
                return authenticatedUser.getEmail();
            }

            @Override
            public String getNid() {
                return authenticatedUser.getNid();
            }

            @Override
            public double getSalary() {
                return authenticatedUser.getSalary();
            }

            @Override
            public Role getRole() {
                return authenticatedUser.getRole();
            }

            @Override
            public LocalDateTime getCreatedAt() {
                return authenticatedUser.getCreatedAt();
            }


            @Override
            public UserProfile getProfile() {
                return authenticatedUser.getProfile();
            }
        };
        return ResponseEntity.ok(adminResponse);
    }

    @Override
    public User connectUser() {
        User authenticatedUser = getAuthenticatedUser();
        if (authenticatedUser != null){
            authenticatedUser.setStatus(ActivityStatus.ONLINE);
            userRepository.save(authenticatedUser);
        }
        return authenticatedUser;
    }

    @Override
    public User disconnectUser() {
        User authenticatedUser = getAuthenticatedUser();
        if (authenticatedUser != null){
            authenticatedUser.setStatus(ActivityStatus.OFFLINE);
            userRepository.save(authenticatedUser);
        }
        return authenticatedUser;
    }

    private void updateBasicProfileInfo(UserProfile userProfile, ProfileUpdateRequest profileUpdateRequest) {
        userProfile.setFirstname(profileUpdateRequest.getFirstname());
        userProfile.setLastname(profileUpdateRequest.getLastname());
        userProfile.setBirthDate(LocalDate.parse(profileUpdateRequest.getBirthDate()));
    }

    private boolean isPhoneNumberInUse(String phoneNumber) {
        User phoneNumberOwner = userRepository.findByPhoneNumber(phoneNumber);
        return phoneNumberOwner != null && !phoneNumberOwner.getId().equals(getAuthenticatedUser().getId());
    }

    private void handlePasswordUpdate(User user, ProfileUpdateRequest profileUpdateRequest) {
        String oldPassword = profileUpdateRequest.getOldPassword();
        String newPassword = profileUpdateRequest.getNewPassword();
        String newConfirmPassword = profileUpdateRequest.getNewConfirmPassword();

        if (oldPassword == null || newPassword == null || newConfirmPassword == null) {
            // Return early if no password fields are provided (means no update is needed)
            return;
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Passwordi ekzistues është i gabuar");
        }

        if (!newPassword.equals(newConfirmPassword)) {
            throw new IllegalArgumentException("Passwordi i ri dhe passwordi i konfirmimit janë të ndryshëm");
        }

        // Check if the new password has been used before
        Set<PreviousPassword> previousPasswords = previousPasswordRepository.findAllByUser(user);
        boolean usedBefore = previousPasswords.stream()
                .anyMatch(previousPassword -> passwordEncoder.matches(newPassword, previousPassword.getPassword()));

        if (usedBefore) {
            throw new IllegalArgumentException("Passwordi i vendosur është përdorur më parë");
        }

        // Save the current password as a previous password
        previousPasswordRepository.save(PreviousPassword.builder()
                .password(user.getPassword())
                .user(user)
                .build());

        // Update the user's password
        user.setPassword(passwordEncoder.encode(newPassword));
    }

    private String updateProfilePicture(UserProfile userProfile, MultipartFile newProfilePicture) throws IOException {
        String currentProfilePictureUrl = userProfile.getProfilePictureUrl();
        String imageName;

        // Generate a new image name if the user has the default profile picture
        if ("default_profile_picture.jpg".equals(currentProfilePictureUrl)) {
            imageName = generateImageName(userProfile);
        } else {
            // Use the existing image name, and delete the old image
            imageName = currentProfilePictureUrl.split("\\.")[0];
            imageService.deleteImage("src/main/resources/static/img/users/" + currentProfilePictureUrl);
        }

        // Save the new profile picture and return the URL
        return imageService.saveImage(new File("src/main/resources/static/img/users"), newProfilePicture, imageName);
    }

    private String generateImageName(UserProfile userProfile) {
        return "user_" + userProfile.getFirstname() + "_" + userProfile.getLastname() + "_" +
                userProfile.getBirthDate() + "_" + userProfile.getUser().getPhoneNumber();
    }



}
