package com.dinendrop.orderservice.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class TokenExtractor {
    private final HttpServletRequest request;

    public TokenExtractor(HttpServletRequest request) {
        this.request = request;
    }

    public String getBearerToken() {
        String header = request.getHeader("Authorization");
        System.out.println("header:" + header);
        return header;
    }
}
