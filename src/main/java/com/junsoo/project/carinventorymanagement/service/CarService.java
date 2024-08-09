
package com.junsoo.project.carinventorymanagement.service;

import com.junsoo.project.carinventorymanagement.client.AuthServiceClient;
import com.junsoo.project.carinventorymanagement.config.FeignClientInterceptor;
import com.junsoo.project.carinventorymanagement.dto.UserDto;
import com.junsoo.project.carinventorymanagement.entity.Car;
import com.junsoo.project.carinventorymanagement.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final AuthServiceClient authServiceClient;
    private final Logger logger = LoggerFactory.getLogger(CarService.class);
    public ResponseEntity<List<Car>> findMyCars(String token) {
        try {
            FeignClientInterceptor.setToken(token);
            UserDto user = authServiceClient.authenticatedUser();
            logger.info("Retrieved user info: {}", user);
            List<Car> myCars = carRepository.findAllByUserEmail(user.getEmail());
            return new ResponseEntity<>(myCars, HttpStatus.OK);
        } finally {
            FeignClientInterceptor.clear();
        }
    }

    public Page<Car> findAllCars(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return carRepository.findAll(pageable);
    }
}
