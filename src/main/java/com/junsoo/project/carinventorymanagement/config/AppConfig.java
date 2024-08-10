
package com.junsoo.project.carinventorymanagement.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Getter
@Configuration
@EnableTransactionManagement
public class AppConfig {
    @Value("${user.service.url}")
    private String userURL;
}
