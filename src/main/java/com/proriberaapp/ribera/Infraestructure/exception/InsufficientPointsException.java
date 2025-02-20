package com.proriberaapp.ribera.Infraestructure.exception;

public class InsufficientPointsException extends Exception {
    public InsufficientPointsException() {
        super("El usuario no tiene suficientes puntos");
    }

}
