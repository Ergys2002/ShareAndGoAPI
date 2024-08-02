package com.app.ShareAndGo.controllers;

import com.app.ShareAndGo.dto.requests.AddCarRequest;
import com.app.ShareAndGo.services.interfaces.ICarService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/car")
@RequiredArgsConstructor
public class CarController {
    private final ICarService carService;

    @PostMapping(value = "/add-car", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> addCar(@ModelAttribute AddCarRequest carData) {
        return carService.saveCar(carData);
    }

    @DeleteMapping("/delete-car")
    public ResponseEntity<?> deleteCar(@RequestParam("id")Long carId){
        return carService.deleteCar(carId);
    }
}
