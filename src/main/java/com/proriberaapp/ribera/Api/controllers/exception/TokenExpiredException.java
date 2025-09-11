package com.proriberaapp.ribera.Api.controllers.exception;

public class TokenExpiredException extends Exception {
    public TokenExpiredException() {
        super("Token expired");
    }
    
    public TokenExpiredException(String message) {
        super(message);
    }
}
