package com.app.ShareAndGo.repositories.logs;

import com.app.ShareAndGo.entities.logs.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageLogRepository extends JpaRepository<MessageLog, Long> {
}
