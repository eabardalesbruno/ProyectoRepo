package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.BedroomEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface BedroomRepository extends R2dbcRepository<BedroomEntity, Integer>{
}
