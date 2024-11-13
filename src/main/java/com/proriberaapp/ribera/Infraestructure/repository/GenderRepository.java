package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.GenderEntity;

import org.springframework.data.annotation.Id;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.mapping.Table;

public interface GenderRepository extends R2dbcRepository<GenderEntity, Integer> {

    @Table("gender")
    public class GenderEntity {
        @Id
        private Integer id;
        private String name;

        // Getters y setters
    }
}
