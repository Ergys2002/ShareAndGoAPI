package com.app.ShareAndGo.repositories.logs;

import com.app.ShareAndGo.entities.logs.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
}
