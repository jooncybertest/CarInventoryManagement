package com.junsoo.project.carinventorymanagement.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class FeignClientInterceptor implements RequestInterceptor {
    private final Logger logger = LoggerFactory.getLogger(FeignClientInterceptor.class);
    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();

    public static void setToken(String token) {
        tokenHolder.set(token);
    }

    public static void clear() {
        tokenHolder.remove();
    }

    @Override
    public void apply(RequestTemplate template) {
        String token = tokenHolder.get();
        if (token != null) {
            template.header("Authorization", "Bearer " + token);
        }
        logger.info("[{}] Path {}", template.method(), template.path());
    }
}
