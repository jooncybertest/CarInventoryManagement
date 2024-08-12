package com.junsoo.project.carinventorymanagement.controller;

import com.junsoo.project.carinventorymanagement.entity.Car;
import com.junsoo.project.carinventorymanagement.service.CarRentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/cars/rent")
public class CarRentController {
    private final CarRentService carRentService;

    @GetMapping
    public ResponseEntity<Page<Car>> getAvailableCars(
            @RequestHeader("Authorization") String header,
            @RequestParam(defaultValue = "#{T(java.lang.Integer).toString(${data.page.default})}") int page,
            @RequestParam(defaultValue = "#{T(java.lang.Integer).toString(${data.size.default})}") int size) {
        try {
            Page<Car> availableCars = carRentService.findAvailableCars(header, page, size);
            return ResponseEntity.ok(availableCars);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
    }
}