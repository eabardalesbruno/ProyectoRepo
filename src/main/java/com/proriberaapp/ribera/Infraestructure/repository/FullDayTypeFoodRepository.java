package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.FullDayTypeFoodEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FullDayTypeFoodRepository extends R2dbcRepository<FullDayTypeFoodEntity,Integer> {
}
