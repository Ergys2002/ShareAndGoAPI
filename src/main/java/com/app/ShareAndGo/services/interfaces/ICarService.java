package com.app.ShareAndGo.services.interfaces;

import com.app.ShareAndGo.dto.requests.AddCarRequest;
import org.springframework.http.ResponseEntity;

public interface ICarService {

    ResponseEntity<?> saveCar(AddCarRequest carData);

    ResponseEntity<?> deleteCar(Long carId);

    ResponseEntity<?> getAllCars();
}
