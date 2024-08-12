
package com.junsoo.project.carinventorymanagement.service;

import com.junsoo.project.carinventorymanagement.config.FeignClientInterceptor;
import com.junsoo.project.carinventorymanagement.dto.CarSellDto;
import com.junsoo.project.carinventorymanagement.dto.UserDto;
import com.junsoo.project.carinventorymanagement.entity.*;
import com.junsoo.project.carinventorymanagement.exception.NotFoundException;
import com.junsoo.project.carinventorymanagement.repository.CarRepository;
import com.junsoo.project.carinventorymanagement.request.CreateCarRequest;
import com.junsoo.project.carinventorymanagement.request.DeleteCarRequest;
import com.junsoo.project.carinventorymanagement.request.UpdateCarInfoRequest;
import com.junsoo.project.carinventorymanagement.request.UpdateCarSellStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CarSellService {
    private final CarRepository carRepository;
    private final FeignService feignService;

    public Page<Car> findAllCars(String header, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        UserDto userDto = feignService.getUserInformation(header);
        if (Objects.equals(userDto.getRole(), UserRole.ROLE_ADMIN.toString())) {
            return carRepository.findAll(pageable);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied. Only admin uses this api");
        }

    }
    public List<Car> findMySellingCars(String header) {
        try {
            UserDto userDto = feignService.getUserInformation(header);
            return carRepository.findAllBySellingUserEmail(userDto.getEmail());
        } finally {
            FeignClientInterceptor.clear();
        }
    }
    public List<Car> registerMySellingCars(String header, List<CreateCarRequest> requests) {
        try {
            UserDto userDto = feignService.getUserInformation(header);
            return createMyCars(requests, userDto);
        } finally {
            FeignClientInterceptor.clear();
        }
    }
    public void deleteMySellingCars(String header, DeleteCarRequest request) {
        try {
            UserDto userDto = feignService.getUserInformation(header);
            carRepository.deleteAllByIds(request.getIds(), userDto.getEmail());
        } finally {
            FeignClientInterceptor.clear();
        }
    }
    public Car updateCarInfo(String header, UpdateCarInfoRequest request) {
        try {
            UserDto userDto = feignService.getUserInformation(header);
            return updateMyCar(request);
        } finally {
            FeignClientInterceptor.clear();
        }
    }
    public List<CarSellDto> updateCarsStatus(String header, List<UpdateCarSellStatusRequest> requests) {
        try {
            UserDto userDto = feignService.getUserInformation(header);
            if (Objects.equals(userDto.getRole(), UserRole.ROLE_ADMIN.toString())) {
                List<Car> updatedCars = updateCarsStatus(requests);
                return updatedCars.stream()
                        .map(this::convertToDto)
                        .toList();
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied. Only admin uses this api");
            }
        } finally {
            FeignClientInterceptor.clear();
        }
    }
    // NOTE: 'availability' and two kinds of 'status' will be updated by system owner later.
    private List<Car> createMyCars(List<CreateCarRequest> requests, UserDto userDto) {
        List<Car> cars = new ArrayList<>();
        for (CreateCarRequest request : requests) {
            Car car = new Car();
            setCarObject(car,
                    request.getMake(),
                    request.getColor(),
                    request.getMileage(),
                    request.getLicensePlate(), request.getYear(), request.getVin(), request.getModel());
            car.setRentStatus(RentStatus.NOT_AVAILABLE);
            car.setSellStatus(SellStatus.PENDING);
            User sellingUser = new User();
            sellingUser.setEmail(userDto.getEmail());
            car.setSellingUser(sellingUser);
            car.setRentingUser(null); // Set rentingUser to null
            cars.add(car);
        }
        return saveAll(cars);
    }
    private Car updateMyCar(UpdateCarInfoRequest request) {
        Car car = carRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("car not found with id: ", request.getId()));
        setCarObject(car,
                request.getMake(),
                request.getColor(),
                request.getMileage(),
                request.getLicensePlate(),
                request.getYear(),
                request.getVin(),
                request.getModel());
        return car;
    }
    private void setCarObject(Car car,
                              String make,
                              String color,
                              Integer mileage,
                              String licensePlate, Integer year, String vin, String model) {
        car.setMake(make);
        car.setColor(color);
        car.setMileage(mileage);
        car.setLicensePlate(licensePlate);
        car.setYear(year);
        car.setVin(vin);
        car.setModel(model);
    }
    private List<Car> updateCarsStatus(List<UpdateCarSellStatusRequest> requests) {
        List<Car> updatedCars = new ArrayList<>();
        for (UpdateCarSellStatusRequest request : requests) {
            Car car = carRepository.findById(request.getId())
                    .orElseThrow(() -> new NotFoundException("car not found with id: ", request.getId()));
            car.setSellStatus(request.getSellStatus());
            car.setRentStatus(request.getRentStatus());
            updatedCars.add(car);
        }
        carRepository.saveAll(updatedCars);
        return updatedCars;
    }
    private CarSellDto convertToDto(Car car) {
        return new CarSellDto(
                car.getId(),
                car.getSellStatus(),
                car.getRentStatus()
        );
    }
    public List<Car> saveAll(List<Car> cars) {
        return carRepository.saveAll(cars);
    }
}
