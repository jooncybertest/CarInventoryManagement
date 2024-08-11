
package com.junsoo.project.carinventorymanagement.service;

import com.junsoo.project.carinventorymanagement.config.FeignClientInterceptor;
import com.junsoo.project.carinventorymanagement.dto.CarDto;
import com.junsoo.project.carinventorymanagement.dto.UserDto;
import com.junsoo.project.carinventorymanagement.entity.*;
import com.junsoo.project.carinventorymanagement.exception.NotFoundException;
import com.junsoo.project.carinventorymanagement.repository.CarRepository;
import com.junsoo.project.carinventorymanagement.request.CreateCarRequest;
import com.junsoo.project.carinventorymanagement.request.DeleteCarRequest;
import com.junsoo.project.carinventorymanagement.request.UpdateCarStatusRequest;
import com.junsoo.project.carinventorymanagement.response.IsUserAdmin;
import com.junsoo.project.carinventorymanagement.response.UpdateCarStatusResponse;
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
public class CarService {
    private final CarRepository carRepository;
    private final FeignService feignService;

    public Page<Car> findAllCars(String header, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        UserDto userDto = feignService.getUserInformation(header);
        if (Objects.equals(userDto.getRole(), UserRole.ROLE_ADMIN.toString())) {
            return carRepository.findAll(pageable);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied: You do not have the necessary permissions.");
        }

    }

    public List<Car> findMyCars(String header) {
        try {
            UserDto userDto = feignService.getUserInformation(header);
            return carRepository.findAllBySellingUserEmail(userDto.getEmail());
        } finally {
            FeignClientInterceptor.clear();
        }
    }

    public List<Car> registerMyCars(String header, List<CreateCarRequest> requests) {
        try {
            UserDto userDto = feignService.getUserInformation(header);
            return createMyCarObjects(requests, userDto);
        } finally {
            FeignClientInterceptor.clear();
        }
    }

    public void deleteMyCars(String header, DeleteCarRequest request) {
        try {
            UserDto userDto = feignService.getUserInformation(header);
            carRepository.deleteAllByIds(request.getIds(), userDto.getEmail());
        } finally {
            FeignClientInterceptor.clear();
        }
    }

    public UpdateCarStatusResponse updateCarsStatus(String header, List<UpdateCarStatusRequest> requests) {
        try {
            UserDto userDto = feignService.getUserInformation(header);
            if (Objects.equals(userDto.getRole(), UserRole.ROLE_ADMIN.toString())) {
                List<Car> updatedCars = updateCarObjects(requests);
                List<CarDto> cars = updatedCars.stream()
                        .map(this::convertToDto)
                        .toList();
                return new UpdateCarStatusResponse(true, IsUserAdmin.ADMIN, cars);
            } else {
                return new UpdateCarStatusResponse(false, IsUserAdmin.NOT_ADMIN, null);
            }
        } finally {
            FeignClientInterceptor.clear();
        }
    }


    // NOTE: 'availability' and two kinds of 'status' will be updated by system owner later.
    private List<Car> createMyCarObjects(List<CreateCarRequest> requests, UserDto userDto) {
        List<Car> cars = new ArrayList<>();
        for (CreateCarRequest request : requests) {
            Car car = new Car();
            car.setMake(request.getMake());
            car.setColor(request.getColor());
            car.setMileage(request.getMileage());
            car.setLicensePlate(request.getLicensePlate());
            car.setYear(request.getYear());
            car.setVin(request.getVin());
            car.setModel(request.getModel());
            car.setRentStatus(RentStatus.NOT_AVAILABLE);
            car.setSellStatus(SellStatus.PENDING);
            car.setAvailability(false);
            User user = new User();
            user.setEmail(userDto.getEmail());
            car.setSellingUser(user);
            cars.add(car);
        }
        return saveAll(cars);
    }

    private List<Car> updateCarObjects(List<UpdateCarStatusRequest> requests) {
        List<Car> updatedCars = new ArrayList<>();
        for (UpdateCarStatusRequest request : requests) {
            Car car = carRepository.findById(request.getId())
                    .orElseThrow(() -> new NotFoundException("car not found with id: ", request.getId()));
            car.setSellStatus(request.getSellStatus());
            car.setAvailability(request.getAvailability());
            updatedCars.add(car);
        }
        carRepository.saveAll(updatedCars);
        return updatedCars;
    }

    private CarDto convertToDto(Car car) {
        return new CarDto(
                car.getId(),
                car.getAvailability(),
                car.getSellStatus()
        );
    }


    public List<Car> saveAll(List<Car> cars) {
        return carRepository.saveAll(cars);
    }
}
