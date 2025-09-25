package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.DocumentTypeEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentTypeRepository extends R2dbcRepository<DocumentTypeEntity, Integer> {
    default List<DocumentTypeEntity> findAllSync() {
        return findAll().collectList().block();
    }
}
