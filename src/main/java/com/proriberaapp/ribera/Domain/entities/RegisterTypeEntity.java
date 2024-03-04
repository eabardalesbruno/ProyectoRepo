package com.proriberaapp.ribera.Domain.entities;

public class RegisterTypeEntity {
    private Integer registerTypeId;
    private String registerTypeName;

    public RegisterTypeEntity() {
    }

    public Integer getRegisterTypeId() {
        return registerTypeId;
    }

    public void setRegisterTypeId(Integer registerTypeId) {
        this.registerTypeId = registerTypeId;
    }

    public String getRegisterTypeName() {
        return registerTypeName;
    }

    public void setRegisterTypeName(String registerTypeName) {
        this.registerTypeName = registerTypeName;
    }
}
