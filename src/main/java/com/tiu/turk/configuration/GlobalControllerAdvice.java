package com.tiu.turk.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ModelAttribute(value="request")
    public HttpServletRequest getServletRequest(HttpServletRequest request) {
        return request;
    }
}

