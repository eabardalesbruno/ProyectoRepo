package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.UserApiEntity;
import com.proriberaapp.ribera.Domain.entities.UserEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserApiRepository extends R2dbcRepository<UserApiEntity, Integer> {
}