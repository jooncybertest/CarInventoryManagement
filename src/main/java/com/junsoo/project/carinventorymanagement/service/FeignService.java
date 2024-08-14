
package com.junsoo.project.carinventorymanagement.service;

import com.junsoo.project.carinventorymanagement.client.AuthServiceClient;
import com.junsoo.project.carinventorymanagement.client.ReservationServiceClient;
import com.junsoo.project.carinventorymanagement.config.FeignClientInterceptor;
import com.junsoo.project.carinventorymanagement.dto.UserDto;
import com.junsoo.project.carinventorymanagement.request.CreateReservationRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeignService {
    private final AuthServiceClient authServiceClient;
    private final ReservationServiceClient reservationServiceClient;
    private final Logger logger = LoggerFactory.getLogger(FeignService.class);
    public UserDto getUserInformation(String header) {
        String token = header.replace("Bearer ", "");
        FeignClientInterceptor.setToken(token);
        UserDto userDto = authServiceClient.authenticatedUser();
        logger.info("Retrieved user info: {}", userDto);
        return userDto;
    }

    public void createReservations(List<CreateReservationRequest> requests) {
        reservationServiceClient.createReservation(requests);
    }
}
