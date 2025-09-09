package com.proriberaapp.ribera.Domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MembershipResponse {
    private Integer id;
    private Integer idUser;
    private Pack pack;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Pack getPack() {
        return pack;
    }

    public void setPack(Pack pack) {
        this.pack = pack;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pack {
        private Integer idPackage;
        private String name;
        private String codeMembership;
        private String description;

        public Integer getIdPackage() {
            return idPackage;
        }

        public void setIdPackage(Integer idPackage) {
            this.idPackage = idPackage;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCodeMembership() {
            return codeMembership;
        }

        public void setCodeMembership(String codeMembership) {
            this.codeMembership = codeMembership;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
