package com.proriberaapp.ribera.Infraestructure.repository.activity;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import com.proriberaapp.ribera.Domain.Interfaces.ActivityRoomProjection;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActivityDashboardCustomRepository {
    Flux<ActivityRoomProjection> findAllRoomsPaginated(LocalDateTime dateStart, LocalDateTime dateEnd, int size,
            int offset);

    Mono<Long> countAllRoomsFiltered(LocalDateTime dateStart, LocalDateTime dateEnd);
}
