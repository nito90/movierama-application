package com.toulios.projects.movierama.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ResourceValidationException extends RuntimeException {
    private final String resourceName;

    public ResourceValidationException( String resourceName, String message) {
        super(String.format("%s validation error: %s", resourceName, message));
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }
}
