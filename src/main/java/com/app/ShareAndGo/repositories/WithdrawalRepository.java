package com.app.ShareAndGo.repositories;


import com.app.ShareAndGo.dto.responses.WithdrawalResponse;
import com.app.ShareAndGo.entities.User;
import com.app.ShareAndGo.entities.Withdrawal;
import com.app.ShareAndGo.enums.WithdrawalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
    Set<WithdrawalResponse> getWithdrawalByWithdrawalStatus(WithdrawalStatus withdrawalStatus);
    Set<WithdrawalResponse> findAllByUser(User user);
}
