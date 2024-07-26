package com.app.ShareAndGo.repositories.logs;

import com.app.ShareAndGo.entities.logs.WithdrawalLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WithdrawalLogRepository extends JpaRepository<WithdrawalLog, Long> {
}
