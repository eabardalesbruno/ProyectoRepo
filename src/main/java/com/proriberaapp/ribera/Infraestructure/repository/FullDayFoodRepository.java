package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.FullDayFoodEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface FullDayFoodRepository extends R2dbcRepository<FullDayFoodEntity,Integer> {
}
