package com.proriberaapp.ribera.Api.controllers.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends Exception {

    private HttpStatus status;

    public CustomException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    @Override
    public String toString() {
        return "CustomException{" +
                "status=" + status +
                ", message=" + getMessage() +
                '}';
    }
}
