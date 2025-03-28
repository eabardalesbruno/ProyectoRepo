package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.MembershipDto;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MembershipRepository extends R2dbcRepository<MembershipDto, Integer> {

    Flux<MembershipDto> findAllByUserclientId(Integer userclientId);
}
