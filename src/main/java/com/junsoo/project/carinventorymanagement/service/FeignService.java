
package com.junsoo.project.carinventorymanagement.service;

import com.junsoo.project.carinventorymanagement.client.AuthServiceClient;
import com.junsoo.project.carinventorymanagement.config.FeignClientInterceptor;
import com.junsoo.project.carinventorymanagement.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeignService {
    private final AuthServiceClient authServiceClient;
    private final Logger logger = LoggerFactory.getLogger(FeignService.class);
    public UserDto getUserInformation(String header) {
        String token = header.replace("Bearer ", "");
        FeignClientInterceptor.setToken(token);
        UserDto userDto = authServiceClient.authenticatedUser();
        logger.info("Retrieved user info: {}", userDto);
        return userDto;
    }
}
