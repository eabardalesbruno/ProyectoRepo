package com.proriberaapp.ribera.Infraestructure.repository.activity;

import java.time.LocalDateTime;

import com.proriberaapp.ribera.Domain.Interfaces.ActivityRoomProjection;
import com.proriberaapp.ribera.Domain.dto.activity.RoomDetailDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActivityDashboardCustomRepository {
    Flux<RoomDetailDTO> findAllRoomsPaginated(LocalDateTime dateStart, LocalDateTime dateEnd, int size,
            int offset);

    Mono<Long> countAllRoomsFiltered(LocalDateTime dateStart, LocalDateTime dateEnd);
}
