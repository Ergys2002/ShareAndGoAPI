package com.app.ShareAndGo.repositories.logs;

import com.app.ShareAndGo.entities.logs.PasswordLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordLogRepository extends JpaRepository<PasswordLog, Long> {
}
