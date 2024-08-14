
package com.junsoo.project.carinventorymanagement.service;

import com.junsoo.project.carinventorymanagement.config.FeignClientInterceptor;
import com.junsoo.project.carinventorymanagement.dto.CarStatusDto;
import com.junsoo.project.carinventorymanagement.dto.UserDto;
import com.junsoo.project.carinventorymanagement.entity.*;
import com.junsoo.project.carinventorymanagement.exception.NotFoundException;
import com.junsoo.project.carinventorymanagement.repository.CarRepository;
import com.junsoo.project.carinventorymanagement.request.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
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
    private final Logger logger = LoggerFactory.getLogger(CarSellService.class);

    public Page<Car> findAllCars(String header, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        UserDto userDto = feignService.getUserInformation(header);
        if (Objects.equals(userDto.getRole(), "ROLE_ADMIN")) {
            return carRepository.findAll(pageable);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied. Only admin uses this api");
        }
    }
    public List<Car> findMySellingCars(String header) {
        try {
            UserDto userDto = feignService.getUserInformation(header);
            return carRepository.findAllByUserEmail(userDto.getEmail());
        } finally {
            FeignClientInterceptor.clear();
        }
    }
    public List<Car> registerMySellingCars(String header, List<CreateRequest> requests) {
        try {
            UserDto userDto = feignService.getUserInformation(header);
            return createMyCars(requests, userDto);
        } finally {
            FeignClientInterceptor.clear();
        }
    }
    public void deleteMySellingCars(String header, DeleteRequest request) {
        try {
            UserDto userDto = feignService.getUserInformation(header);
            carRepository.deleteAllByIds(request.getIds(), userDto.getEmail());
        } finally {
            FeignClientInterceptor.clear();
        }
    }
    public Car updateCarInfo(String header, UpdateInfoRequest request) {
        try {
            UserDto userDto = feignService.getUserInformation(header);
            return updateMyCar(request);
        } finally {
            FeignClientInterceptor.clear();
        }
    }
    public List<CarStatusDto> updateCarsStatus(String header, List<UpdateSellStatusRequest> requests) {
        try {
            UserDto userDto = feignService.getUserInformation(header);
            if (Objects.equals(userDto.getRole(), "ROLE_ADMIN")) {
                List<Car> updatedCars = updateSellStatus(requests);
                createReservationCall(updatedCars); // async call to send data to booking management microservice
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
    private List<Car> updateSellStatus(List<UpdateSellStatusRequest> requests) {
        List<Car> updatedCars = new ArrayList<>();
        for (UpdateSellStatusRequest request : requests) {
            Car car = carRepository.findById(request.getId())
                    .orElseThrow(() -> new NotFoundException("car not found with id: ", request.getId()));
            car.setSellStatus(request.getSellStatus());
            updatedCars.add(car);
        }
        return carRepository.saveAll(updatedCars);
    }

    /**
     * send purchase cars to booking management microservice asynchronously
     */
    @Async
    protected void createReservationCall(List<Car> updatedCars){
        List<CreateReservationRequest> requests = new ArrayList<>();
        List<Car> filteredCars = updatedCars.stream()
                .filter(car -> car.getSellStatus() == SellStatus.PURCHASED)
                .toList();
        logger.info("filtered list of cars: {}", filteredCars);
        for (Car filteredCar : filteredCars) {
            CreateReservationRequest reservationRequest = new CreateReservationRequest();
            reservationRequest.setCarId(filteredCar.getId());
            reservationRequest.setUserEmail(filteredCar.getUserEmail());
            reservationRequest.setMileage(filteredCar.getMileage());
            requests.add(reservationRequest);
        }
        feignService.createReservations(requests);
        logger.info("reservations successfully created");
    }

    // NOTE: 'availability' and 'status' will be updated by system owner later.
    private List<Car> createMyCars(List<CreateRequest> requests, UserDto userDto) {
        List<Car> cars = new ArrayList<>();
        for (CreateRequest request : requests) {
            Car car = new Car();
            setCarObject(car,
                    request.getMake(),
                    request.getColor(),
                    request.getMileage(),
                    request.getLicensePlate(),
                    request.getYear(),
                    request.getVin(),
                    request.getModel());
            car.setSellStatus(SellStatus.PENDING);
            car.setUserEmail(userDto.getEmail());
            cars.add(car);
        }
        return saveAll(cars);
    }

    private Car updateMyCar(UpdateInfoRequest request) {
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

    private CarStatusDto convertToDto(Car car) {
        return new CarStatusDto(
                car.getId(),
                car.getSellStatus()
        );
    }
    public List<Car> saveAll(List<Car> cars) {
        return carRepository.saveAll(cars);
    }
}
