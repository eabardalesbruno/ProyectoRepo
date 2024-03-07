package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.PaymentStateEntity;
import com.proriberaapp.ribera.Domain.entities.UserAdminEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface UserAdminRepository extends R2dbcRepository<UserAdminEntity, Integer> {
    Mono<UserAdminEntity> findByUsernameOrEmail(String username, String email);
    Mono<UserAdminEntity> findByUsernameOrEmailOrDocument(String username, String email, String document);

    Mono<UserAdminEntity> findByEmail(String email);
}
