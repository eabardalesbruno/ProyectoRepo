package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserAdminEntity {
    private Integer userAdminId;
    private String email;
    private String password;
    private String username;
    private String firstName;
    private String lastName;
    private String role;
    private String status;
    private List<String> permission;
}

