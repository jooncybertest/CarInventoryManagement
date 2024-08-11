
package com.junsoo.project.carinventorymanagement.controller;

import com.junsoo.project.carinventorymanagement.entity.Car;
import com.junsoo.project.carinventorymanagement.request.CreateCarRequest;
import com.junsoo.project.carinventorymanagement.request.DeleteCarRequest;
import com.junsoo.project.carinventorymanagement.request.UpdateCarInfoRequest;
import com.junsoo.project.carinventorymanagement.request.UpdateCarStatusRequest;
import com.junsoo.project.carinventorymanagement.response.GeneralResponse;
import com.junsoo.project.carinventorymanagement.response.UpdateCarStatusResponse;
import com.junsoo.project.carinventorymanagement.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/cars/sell")
@RequiredArgsConstructor
public class CarSellController {
    private final CarService carService;

    /**
     * user can see cars they registered when status is PENDING
     */
    @GetMapping("/all")
    public ResponseEntity<Page<Car>> getAllCars(
            @RequestHeader("Authorization") String header,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Car> cars = carService.findAllCars(header, page, size);
            return ResponseEntity.ok(cars);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
    }

    /**
     * users can upload their selling cars to rental company owners
     */
    @PostMapping("/mine")
    public ResponseEntity<List<Car>> createMySellingCars(
            @RequestHeader("Authorization") String header,
            @RequestBody List<CreateCarRequest> requests) {
        List<Car> createdCars = carService.registerMyCars(header, requests);
        return new ResponseEntity<>(createdCars, HttpStatus.OK);
    }

    /**
     * ADMIN user update status of cars ( AVAILABLE, RENTED, MAINTENANCE, PENDING)
     */
    @PutMapping("/status")
    public ResponseEntity<UpdateCarStatusResponse> updateCarsPurchaseStatus(
            @RequestHeader("Authorization") String header,
            @RequestBody List<UpdateCarStatusRequest> requests) {
        UpdateCarStatusResponse response = carService.updateCarsStatus(header, requests);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * user can update their car info when car's status is PENDING
     */
    @PutMapping("/mine")
    public ResponseEntity<UpdateCarInfoRequest> updateCarInfo(
            @RequestHeader("Authorization") String header,
            @RequestBody UpdateCarInfoRequest request) {
        return null;
    }

    /**
     * users can delete their selling cars only when car's status is PENDING.
     */
    @DeleteMapping("/mine")
    public ResponseEntity<GeneralResponse> deleteMyCars(
            @RequestHeader("Authorization") String header,
            @RequestBody DeleteCarRequest request) {
        carService.deleteMyCars(header, request); // safe delete: they only can delete their own car
        GeneralResponse response = new GeneralResponse();
        response.setMessage("deleted successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
