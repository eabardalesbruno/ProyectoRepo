package com.proriberaapp.ribera.Domain.dto;

import lombok.Data;

@Data
public class MembresiaDto {
    private Integer id;
    private Integer idUser;
    private PackDto pack;
    private Integer status;
    // Otros campos según la API externa
}

@Data
class PackDto {
    private Integer idPackage;
    private String name;
    private String codeMembership;
    private String description;
    private Integer idFamilyPackage;
    private Integer status;
    // Otros campos según la API externa
}
