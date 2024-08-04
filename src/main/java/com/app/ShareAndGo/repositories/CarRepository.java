package com.app.ShareAndGo.repositories;

import com.app.ShareAndGo.dto.responses.CarResponse;
import com.app.ShareAndGo.entities.Car;
import com.app.ShareAndGo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Set<CarResponse> findCarsByOwner(User owner);
}
