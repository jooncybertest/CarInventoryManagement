
package com.junsoo.project.carinventorymanagement.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class EnvConfig {
    @Value("${user.service.url}")
    private String userURL;
}
