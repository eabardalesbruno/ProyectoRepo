package com.proriberaapp.ribera.Domain.dto;

import lombok.Data;

@Data
public class UpdatePasswordDto {

    private String code;
    private String password;
    private String confirmPassword;
    private String currentPassword;
    
}
