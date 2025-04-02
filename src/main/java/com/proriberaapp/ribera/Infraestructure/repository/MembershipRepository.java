package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.client.dto.LoginInclub.MembershipDto;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MembershipRepository extends R2dbcRepository<MembershipDto, Integer> {

    Flux<MembershipDto> findAllByUserclientId(Integer userclientId);

    @Query("UPDATE membership SET data = GREATEST(0, data - :totalQuantity), dataupdate = NOW() " +
            "WHERE id = :Id AND data > 0 RETURNING *")
    Mono<MembershipDto> actualizarData(@Param("Id") Integer Id, @Param("membershipQuantity") Integer membershipQuantity);

}
