package com.app.ShareAndGo.services.classes;

import com.app.ShareAndGo.entities.Transaction;
import com.app.ShareAndGo.entities.Trip;
import com.app.ShareAndGo.entities.User;
import com.app.ShareAndGo.enums.TransactionStatus;
import com.app.ShareAndGo.repositories.TransactionRepository;
import com.app.ShareAndGo.repositories.UserRepository;
import com.app.ShareAndGo.services.interfaces.ITransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionService implements ITransactionService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    @Override
    public ResponseEntity<?> createTransaction(User authenticatedUser, Trip activeTripOfAuthenticatedUser, double totalPrice) {
        double totalPriceAfterCommission = totalPrice * 1.05;

        if(authenticatedUser.getAccountBalance() < totalPriceAfterCommission){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Nuk keni kredit te mjaftueshem ne llogari");
        }

        authenticatedUser.setAccountBalance(authenticatedUser.getAccountBalance() - totalPriceAfterCommission);
        userRepository.save(authenticatedUser);

        User driver = activeTripOfAuthenticatedUser.getDriver();

        driver.setAccountBalance(driver.getAccountBalance() + totalPrice);

        Transaction transaction = Transaction.builder()
                .amount(totalPrice)
                .sender(authenticatedUser)
                .recipient(driver)
                .transactionStatus(TransactionStatus.COMPLETED)
                .build();

        transactionRepository.save(transaction);
        return ResponseEntity.status(HttpStatus.OK).body("Pagesa u krye me sukses");
    }
}
