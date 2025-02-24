package com.proriberaapp.ribera.Infraestructure.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("El usuario  no fue encontrado");
    }
    public UserNotFoundException(String message) {
        super(message);
    }
    
}
