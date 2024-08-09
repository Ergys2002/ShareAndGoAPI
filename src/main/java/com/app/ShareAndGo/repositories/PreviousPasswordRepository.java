package com.app.ShareAndGo.repositories;

import com.app.ShareAndGo.entities.PreviousPassword;
import com.app.ShareAndGo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PreviousPasswordRepository extends JpaRepository<PreviousPassword, Long> {
    Set<PreviousPassword> findAllByUser(User user);
}
