package com.app.ShareAndGo.services.interfaces;

import com.app.ShareAndGo.entities.Trip;
import com.app.ShareAndGo.entities.User;
import org.springframework.http.ResponseEntity;

public interface ITransactionService {
    ResponseEntity<?> createTransaction(User authenticatedUser, Trip activeTripOfAuthenticatedUser, double totalPrice);
}
