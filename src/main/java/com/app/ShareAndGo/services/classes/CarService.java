package com.app.ShareAndGo.services.classes;

import com.app.ShareAndGo.dto.requests.AddCarRequest;
import com.app.ShareAndGo.dto.responses.CarResponse;
import com.app.ShareAndGo.entities.Car;
import com.app.ShareAndGo.entities.User;
import com.app.ShareAndGo.repositories.CarRepository;
import com.app.ShareAndGo.services.interfaces.IImageService;
import com.app.ShareAndGo.services.interfaces.ICarService;
import com.app.ShareAndGo.services.interfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Set;

@Service
@AllArgsConstructor
public class CarService implements ICarService {
    private final IImageService imageService;
    private final IUserService userService;
    private final CarRepository carRepository;

    @Transactional
    @Override
    public ResponseEntity<?> saveCar(AddCarRequest carData) {

        User carOwner = userService.getAuthenticatedUser();

        if (carOwner.getCars().stream().anyMatch(car -> car.getLicencePlateNumber().equals(carData.getLicencePlateNumber()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Makina qe doni te shtoni tashme ekziston");
        }

        Car newCar = Car.builder()
                .make(carData.getMake())
                .model(carData.getModel())
                .owner(carOwner)
                .licencePlateNumber(carData.getLicencePlateNumber())
                .build();

        if (carData.getCarImage() != null) {
            File path = new File("src/main/resources/static/img/cars");

            String imageName =
                    "car_" +
                            carData.getMake() +
                            "_" + carData.getModel() +
                            "_" + carOwner.getProfile().getFirstname() + "_"
                            + carOwner.getProfile().getLastname() + "_"
                    + carData.getLicencePlateNumber();

            try {
                String newProfilePhotoPath = imageService.saveImage(path, carData.getCarImage(), imageName);
                newCar.setCarImageURL(newProfilePhotoPath);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ruajtja e makines deshtoi");
            }
        } else {
            newCar.setCarImageURL("default-car-image.jpg");
        }


        carRepository.save(newCar);
        return ResponseEntity.status(HttpStatus.OK).body("Makina u shtua me sukses");

    }

    @Override
    public ResponseEntity<?> deleteCar(Long carId) {
        Car car = carRepository.findById(carId).orElse(null);

        if (car == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Makina qe doni te fshini nuk ekziston");
        }

        carRepository.deleteById(carId);
        return ResponseEntity.status(HttpStatus.OK).body("Makina u fshi me sukses");
    }

    @Override
    public ResponseEntity<?> getAllCars() {
        User authenticatedUser = userService.getAuthenticatedUser();
        Set<CarResponse> carsOfAuthenticatedUser = carRepository.findCarsByOwner(authenticatedUser);

        if(carsOfAuthenticatedUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nuk ka asnje makine");
        }

        return ResponseEntity.status(HttpStatus.OK).body(carsOfAuthenticatedUser);
    }
}
