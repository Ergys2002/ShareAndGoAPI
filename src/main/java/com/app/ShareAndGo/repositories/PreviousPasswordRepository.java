package com.app.ShareAndGo.repositories;

import com.app.ShareAndGo.entities.PreviousPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreviousPasswordRepository extends JpaRepository<PreviousPassword, Long> {
}
