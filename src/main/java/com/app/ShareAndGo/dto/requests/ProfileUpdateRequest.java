package com.app.ShareAndGo.dto.requests;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ProfileUpdateRequest {
    private String phoneNumber;
    private String oldPassword;
    private String newPassword;
    private String newConfirmPassword;
    private String firstname;
    private String lastname;
    private MultipartFile newProfilePicture;
    private String birthDate;
}
