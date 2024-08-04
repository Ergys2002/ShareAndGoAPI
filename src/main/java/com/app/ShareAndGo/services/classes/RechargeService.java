package com.app.ShareAndGo.services.classes;

import com.app.ShareAndGo.dto.requests.RechargeRequest;
import com.app.ShareAndGo.entities.Recharge;
import com.app.ShareAndGo.entities.User;
import com.app.ShareAndGo.repositories.RechargeRepository;
import com.app.ShareAndGo.repositories.UserRepository;
import com.app.ShareAndGo.services.interfaces.IRechargeService;
import com.app.ShareAndGo.services.interfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@AllArgsConstructor
public class RechargeService implements IRechargeService {

    private final IUserService userService;
    private final UserRepository userRepository;
    private final RechargeRepository rechargeRepository;
    @Override
    @Transactional
    public ResponseEntity<?> recharge(RechargeRequest rechargeRequest) {
        User authenticatedUser = userService.getAuthenticatedUser();
        if (rechargeRequest.getCardNumber().equals("0000000000000000") && rechargeRequest.getCvv().equals("000")){
            String expDateString = rechargeRequest.getExpirationDate();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yy");
            simpleDateFormat.setLenient(false);
            Date expiry;
            try {
                expiry = simpleDateFormat.parse(expDateString);
            } catch (ParseException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Format i pasakte date");
            }

            boolean expired = expiry.before(new Date());

            if (expired){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Karta eshte e skaduar");
            }

            Recharge recharge = Recharge.builder()
                    .amount(rechargeRequest.getAmount())
                    .cardHolderName(rechargeRequest.getNameOnCard())
                    .user(authenticatedUser)
                    .build();

            rechargeRepository.save(recharge);

            authenticatedUser.setAccountBalance(authenticatedUser.getAccountBalance() + rechargeRequest.getAmount());
            userRepository.save(authenticatedUser);
            return ResponseEntity.status(HttpStatus.OK).body("Rimbushja u krye me sukses");
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Karta e vendosur nuk eshte e sakte");
        }

    }
}
