package com.junsoo.project.carinventorymanagement.client;

import com.junsoo.project.carinventorymanagement.config.FeignConfig;
import com.junsoo.project.carinventorymanagement.request.CreateReservationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "reservation-service", url = "${booking.service.url}", configuration = FeignConfig.class)
public interface ReservationServiceClient {
    @PostMapping(value = "/api/v1/cars", consumes = "application/json")
    void createReservation(@RequestBody List<CreateReservationRequest> requests);
}
