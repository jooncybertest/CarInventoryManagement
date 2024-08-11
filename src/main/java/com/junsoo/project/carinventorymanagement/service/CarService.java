
package com.junsoo.project.carinventorymanagement.service;

import com.junsoo.project.carinventorymanagement.config.FeignClientInterceptor;
import com.junsoo.project.carinventorymanagement.dto.UserDto;
import com.junsoo.project.carinventorymanagement.entity.Car;
import com.junsoo.project.carinventorymanagement.entity.Status;
import com.junsoo.project.carinventorymanagement.entity.User;
import com.junsoo.project.carinventorymanagement.repository.CarRepository;
import com.junsoo.project.carinventorymanagement.request.CreateCarsRequest;
import com.junsoo.project.carinventorymanagement.request.DeleteCarRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final FeignService feignService;

    public Page<Car> findAllCars(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return carRepository.findAll(pageable);
    }

    public List<Car> findMyCars(String header) {
        try {
            UserDto userDto = feignService.getUserInformation(header);
            return carRepository.findAllByUserEmail(userDto.getEmail());
        } finally {
            FeignClientInterceptor.clear();
        }
    }

    public List<Car> registerMyCars(String header, List<CreateCarsRequest> requests) {
        try {
            UserDto userDto = feignService.getUserInformation(header);
            return createMyCarObject(requests, userDto);
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

    // NOTE: 'availability' and 'status' are determined by system owner. will be later determined
    private List<Car> createMyCarObject(List<CreateCarsRequest> requests, UserDto userDto) {
        List<Car> cars = new ArrayList<>();
        for (CreateCarsRequest request : requests) {
            Car car = new Car();
            car.setMake(request.getMake());
            car.setColor(request.getColor());
            car.setMileage(request.getMileage());
            car.setLicensePlate(request.getLicensePlate());
            car.setYear(request.getYear());
            car.setVin(request.getVin());
            car.setModel(request.getModel());
            car.setStatus(Status.PENDING);
            User user = new User();
            user.setEmail(userDto.getEmail());
            car.setUser(user);
            cars.add(car);
        }
        return saveAll(cars);
    }

    public List<Car> saveAll(List<Car> cars) {
        return carRepository.saveAll(cars);
    }
}
