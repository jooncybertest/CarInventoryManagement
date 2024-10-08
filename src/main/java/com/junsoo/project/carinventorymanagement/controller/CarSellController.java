
package com.junsoo.project.carinventorymanagement.controller;

import com.junsoo.project.carinventorymanagement.dto.CarStatusDto;
import com.junsoo.project.carinventorymanagement.entity.Car;
import com.junsoo.project.carinventorymanagement.request.CreateRequest;
import com.junsoo.project.carinventorymanagement.request.DeleteRequest;
import com.junsoo.project.carinventorymanagement.request.UpdateInfoRequest;
import com.junsoo.project.carinventorymanagement.request.UpdateSellStatusRequest;
import com.junsoo.project.carinventorymanagement.response.GeneralResponse;
import com.junsoo.project.carinventorymanagement.response.IsUserAdmin;
import com.junsoo.project.carinventorymanagement.response.UpdateCarSellStatusResponse;
import com.junsoo.project.carinventorymanagement.service.CarSellService;
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
    private final CarSellService carSellService;

    /**
     * user can see cars they registered when status is PENDING
     */
    @GetMapping("/all")
    public ResponseEntity<Page<Car>> getAllCars(
            @RequestHeader("Authorization") String header,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Car> cars = carSellService.findAllCars(header, page, size);
            return ResponseEntity.ok(cars);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
    }

    /**
     * user can see cars they sold to a company.
     */
    @GetMapping("/mine")
    public ResponseEntity<List<Car>> getMyCars(@RequestHeader("Authorization") String header) {
        List<Car> myCars = carSellService.findMySellingCars(header);
        return new ResponseEntity<>(myCars, HttpStatus.OK);
    }

    /**
     * users can upload their selling cars to rental company owners
     */
    @PostMapping("/mine")
    public ResponseEntity<List<Car>> createMySellingCars(
            @RequestHeader("Authorization") String header,
            @RequestBody List<CreateRequest> requests) {
        List<Car> createdCars = carSellService.registerMySellingCars(header, requests);
        return new ResponseEntity<>(createdCars, HttpStatus.OK);
    }

    /**
     * ADMIN user update status of cars (rent status, sell status)
     */
    @PutMapping("/status")
    public ResponseEntity<UpdateCarSellStatusResponse> updateCarsStatus(
            @RequestHeader("Authorization") String header,
            @RequestBody List<UpdateSellStatusRequest> requests) {
        try{
            List<CarStatusDto> cars = carSellService.updateCarsStatus(header, requests);
            UpdateCarSellStatusResponse response =
                    new UpdateCarSellStatusResponse(true, IsUserAdmin.ADMIN, cars);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
    }

    /**
     * user can update their car info when car's status is PENDING
     */
    @PutMapping("/mine")
    public ResponseEntity<Car> updateCarInfo(
            @RequestHeader("Authorization") String header,
            @RequestBody UpdateInfoRequest request) {
        Car car = carSellService.updateCarInfo(header, request);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    /**
     * users can delete their selling cars only when car's status is PENDING.
     */
    @DeleteMapping("/mine")
    public ResponseEntity<GeneralResponse> deleteMyCars(
            @RequestHeader("Authorization") String header,
            @RequestBody DeleteRequest request) {
        carSellService.deleteMySellingCars(header, request); // safe delete: they only can delete their own car
        return ResponseEntity.ok(null);
    }
}
