package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.SendAndReceiveEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendAndReceiveRepository extends R2dbcRepository<SendAndReceiveEntity, Integer> {}