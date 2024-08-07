package com.app.ShareAndGo.repositories;

import com.app.ShareAndGo.entities.Recharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RechargeRepository extends JpaRepository<Recharge, Long> {
}
