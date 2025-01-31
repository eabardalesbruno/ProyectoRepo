package com.proriberaapp.ribera.Infraestructure.exception;

public class PasswordNotMatchesException extends RuntimeException {
    public PasswordNotMatchesException(String namePassword,String comparePassword) {
        super("Las constraseñas no coinciden: "+namePassword+" y "+comparePassword);
    }
    
}
