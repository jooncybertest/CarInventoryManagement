
package com.junsoo.project.carinventorymanagement.service;

import com.junsoo.project.carinventorymanagement.config.FeignClientInterceptor;
import com.junsoo.project.carinventorymanagement.dto.CarDto;
import com.junsoo.project.carinventorymanagement.dto.UserDto;
import com.junsoo.project.carinventorymanagement.entity.Car;
import com.junsoo.project.carinventorymanagement.entity.Status;
import com.junsoo.project.carinventorymanagement.entity.User;
import com.junsoo.project.carinventorymanagement.entity.UserRole;
import com.junsoo.project.carinventorymanagement.exception.NotFoundException;
import com.junsoo.project.carinventorymanagement.repository.CarRepository;
import com.junsoo.project.carinventorymanagement.request.CreateCarRequest;
import com.junsoo.project.carinventorymanagement.request.DeleteCarRequest;
import com.junsoo.project.carinventorymanagement.request.UpdateCarRequest;
import com.junsoo.project.carinventorymanagement.response.IsUserAdmin;
import com.junsoo.project.carinventorymanagement.response.UpdateCarResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public UpdateCarResponse updateCars(String header, List<UpdateCarRequest> requests) {
        try {
            UserDto userDto = feignService.getUserInformation(header);
            if (Objects.equals(userDto.getRole(), UserRole.ROLE_ADMIN.toString())) {
                List<Car> updatedCars = updateCarObjects(requests);
                List<CarDto> cars = updatedCars.stream()
                        .map(this::convertToDto)
                        .toList();
                return new UpdateCarResponse(true, IsUserAdmin.ADMIN, cars);
            } else {
                return new UpdateCarResponse(false, IsUserAdmin.NOT_ADMIN, null);
            }
        } finally {
            FeignClientInterceptor.clear();
        }
    }


    // NOTE: 'availability' and 'status' are determined by system owner. will be later determined
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
            car.setStatus(Status.PENDING);
            User user = new User();
            user.setEmail(userDto.getEmail());
            car.setUser(user);
            cars.add(car);
        }
        return saveAll(cars);
    }

    private List<Car> updateCarObjects(List<UpdateCarRequest> requests) {
        List<Car> updatedCars = new ArrayList<>();
        for (UpdateCarRequest request : requests) {
            Car car = carRepository.findById(request.getId())
                    .orElseThrow(() -> new NotFoundException("car not found with id: ", request.getId()));
            car.setStatus(request.getStatus());
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
                car.getStatus()
        );
    }


    public List<Car> saveAll(List<Car> cars) {
        return carRepository.saveAll(cars);
    }
}
