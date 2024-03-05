package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.UserEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, Integer>, R2dbcRepository<UserEntity, Integer> {
}


