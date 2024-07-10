package com.proriberaapp.ribera.Api.controllers.payme;

import com.proriberaapp.ribera.Api.controllers.payme.entity.AuthorizationEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface AuthorizationRepository extends R2dbcRepository<AuthorizationEntity, Integer> {
}
