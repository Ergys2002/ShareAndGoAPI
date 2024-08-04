package com.app.ShareAndGo.configs;

import com.app.ShareAndGo.entities.User;
import com.app.ShareAndGo.entities.UserProfile;
import com.app.ShareAndGo.enums.Role;
import com.app.ShareAndGo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class InitialDataPersistence implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
//       saveAdmin();
    }

    private void saveAdmin(){
        UserProfile profile = UserProfile.builder()
                .firstname("Ergys")
                .lastname("Xhaollari")
                .gender("M")
                .birthDate(LocalDate.of(2002,12,1))
                .build();

        User admin = User.builder()
                .email("ergysxhaollari02@gmail.com")
                .role(Role.SUPERADMIN)
                .password(passwordEncoder.encode("Ergys12345@"))
                .phoneNumber("+355688749829")
                .nid("K21201019M")
                .disabled(false)
                .isDeleted(false)
                .isBanned(false)
                .isActive(true)
                .profile(profile)
                .build();
        userRepository.save(admin);
    }
}
