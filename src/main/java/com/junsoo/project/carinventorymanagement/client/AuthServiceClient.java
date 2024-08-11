
package com.junsoo.project.carinventorymanagement.client;

import com.junsoo.project.carinventorymanagement.config.FeignConfig;
import com.junsoo.project.carinventorymanagement.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-service", url = "${user.service.url}", configuration = FeignConfig.class)
public interface AuthServiceClient {
    @GetMapping("/api/v1/auth/validate")
    UserDto authenticatedUser();
}
