package com.app.ShareAndGo.services.classes;

import com.app.ShareAndGo.dto.requests.WithdrawalRequest;
import com.app.ShareAndGo.dto.responses.WithdrawalResponse;
import com.app.ShareAndGo.entities.User;
import com.app.ShareAndGo.entities.Withdrawal;
import com.app.ShareAndGo.enums.WithdrawalStatus;
import com.app.ShareAndGo.repositories.UserRepository;
import com.app.ShareAndGo.repositories.WithdrawalRepository;
import com.app.ShareAndGo.services.interfaces.IUserService;
import com.app.ShareAndGo.services.interfaces.IWithdrawalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class WithdrawalService implements IWithdrawalService {

    private final IUserService userService;
    private final WithdrawalRepository withdrawalRepository;
    private final UserRepository userRepository;
    @Override
    public ResponseEntity<?> requestWithdrawal(WithdrawalRequest withdrawalRequest) {
        User authenticatedUser = userService.getAuthenticatedUser();

        if (withdrawalRequest.getAmount() < 1000){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Minimumi i terheqjes eshte 1000ALL");
        }

        if(authenticatedUser.getAccountBalance() >= withdrawalRequest.getAmount()){
            Withdrawal withdrawal = Withdrawal.builder()
                    .amount(withdrawalRequest.getAmount())
                    .accountNumber(withdrawalRequest.getAccountNumber())
                    .withdrawalStatus(WithdrawalStatus.PENDING)
                    .user(authenticatedUser)
                    .build();

            authenticatedUser.setAccountBalance(authenticatedUser.getAccountBalance() - withdrawalRequest.getAmount());

            withdrawalRepository.save(withdrawal);
            userRepository.save(authenticatedUser);

            return ResponseEntity.status(HttpStatus.OK).body("Kerkesa per terheqje u dergua me sukses");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Balanca juaj nuk mjafton");
        }
    }

    @Override
    public ResponseEntity<?> getPendingWithdrawals() {
        Set<WithdrawalResponse> pendingWithdrawals = withdrawalRepository.getWithdrawalByWithdrawalStatus(WithdrawalStatus.PENDING);

        if(pendingWithdrawals.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nuk ka terheqje ne pritje");
        }

        return ResponseEntity.status(HttpStatus.OK).body(pendingWithdrawals);
    }

    @Override
    public ResponseEntity<?> confirmWithdrawal(Long id) {

        Withdrawal pendingWithdrawal = withdrawalRepository.findById(id).orElse(null);

        if(pendingWithdrawal == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Kerkesa per terheqje nuk ekziston");
        }

        pendingWithdrawal.setWithdrawalStatus(WithdrawalStatus.COMPLETED);
        withdrawalRepository.save(pendingWithdrawal);

        return ResponseEntity.status(HttpStatus.OK).body("Kerkesa per terheqje u konfirmua");
    }

    @Override
    public ResponseEntity<?> rejectWithdrawal(Long id) {
        Withdrawal pendingWithdrawal = withdrawalRepository.findById(id).orElse(null);


        if(pendingWithdrawal == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Kerkesa per terheqje nuk ekziston");
        }

        User withdrew = pendingWithdrawal.getUser();

        pendingWithdrawal.setWithdrawalStatus(WithdrawalStatus.CANCELED);
        withdrew.setAccountBalance(withdrew.getAccountBalance() + pendingWithdrawal.getAmount());
        userRepository.save(withdrew);
        withdrawalRepository.save(pendingWithdrawal);

        return ResponseEntity.status(HttpStatus.OK).body("Kerkesa per terheqje u anulua");
    }

    @Override
    public ResponseEntity<?> getWithdrawalsOfAuthenticatedUser() {
        User user = userService.getAuthenticatedUser();

        Set<WithdrawalResponse> withdrawals = withdrawalRepository.findAllByUser(user);

        if (withdrawals.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nuk keni asnje terheqje");
        }

        return ResponseEntity.status(HttpStatus.OK).body(withdrawals);
    }
}
