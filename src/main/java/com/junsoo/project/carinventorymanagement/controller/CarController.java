
package com.junsoo.project.carinventorymanagement.controller;

import com.junsoo.project.carinventorymanagement.entity.Car;
import com.junsoo.project.carinventorymanagement.request.CreateCarsRequest;
import com.junsoo.project.carinventorymanagement.request.DeleteCarRequest;
import com.junsoo.project.carinventorymanagement.response.GeneralResponse;
import com.junsoo.project.carinventorymanagement.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;
    @GetMapping("/all")
    public ResponseEntity<Page<Car>> getAllCars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Car> cars = carService.findAllCars(page, size);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }
    @GetMapping("/mine")
    public ResponseEntity<List<Car>> getMyCars(@RequestHeader("Authorization") String header) {
        List<Car> myCars = carService.findMyCars(header);
        return new ResponseEntity<>(myCars, HttpStatus.OK);
    }
    @PostMapping("/mine")
    public ResponseEntity<List<Car>> createMyCars(
            @RequestHeader("Authorization") String header,
            @RequestBody List<CreateCarsRequest> requests) {
        List<Car> createdCars = carService.registerMyCars(header, requests);
        return new ResponseEntity<>(createdCars, HttpStatus.OK);
    }

    @DeleteMapping("/mine")
    public ResponseEntity<GeneralResponse> deleteMyCars(
            @RequestHeader("Authorization") String header,
            @RequestBody DeleteCarRequest request) {
        carService.deleteMyCars(header, request);
        GeneralResponse response = new GeneralResponse();
        response.setMessage("deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
