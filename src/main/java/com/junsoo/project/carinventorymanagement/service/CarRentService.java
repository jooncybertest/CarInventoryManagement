package com.junsoo.project.carinventorymanagement.service;

import com.junsoo.project.carinventorymanagement.dto.UserDto;
import com.junsoo.project.carinventorymanagement.entity.Car;
import com.junsoo.project.carinventorymanagement.entity.RentStatus;
import com.junsoo.project.carinventorymanagement.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarRentService {
    private final CarRepository carRepository;
    private final FeignService feignService;

    public Page<Car> findAvailableCars(String header, int page, int size) {
        UserDto userDto = feignService.getUserInformation(header);
        Pageable pageable = PageRequest.of(page, size);
        return carRepository.findAllByRentStatus(RentStatus.AVAILABLE, pageable);
    }
}
