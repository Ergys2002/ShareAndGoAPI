package com.app.ShareAndGo.services.classes;

import com.app.ShareAndGo.entities.PreviousPassword;
import com.app.ShareAndGo.entities.User;
import com.app.ShareAndGo.repositories.PreviousPasswordRepository;
import com.app.ShareAndGo.services.interfaces.IPreviousPassword;
import com.app.ShareAndGo.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PreviousPasswordService implements IPreviousPassword {
    private final IUserService userService;
    private final PreviousPasswordRepository previousPasswordRepository;
    @Override
    public Set<PreviousPassword> getPreviousPasswords(){
        User authenticatedUser = userService.getAuthenticatedUser();
        return previousPasswordRepository.findAllByUser(authenticatedUser);
    }
}
