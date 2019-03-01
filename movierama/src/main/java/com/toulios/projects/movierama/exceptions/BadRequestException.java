package com.toulios.projects.movierama.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    public BadRequestException( String message) {
        super(String.format("Validation error: %s", message));
    }

}
