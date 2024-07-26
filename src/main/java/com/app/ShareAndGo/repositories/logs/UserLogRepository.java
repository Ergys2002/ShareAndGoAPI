package com.app.ShareAndGo.repositories.logs;

import com.app.ShareAndGo.entities.logs.Log;
import com.app.ShareAndGo.entities.logs.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLogRepository extends JpaRepository<UserLog, Long> {
}
