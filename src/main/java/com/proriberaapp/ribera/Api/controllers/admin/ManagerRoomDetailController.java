package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.RoomDetailEntity;
import com.proriberaapp.ribera.services.client.RoomDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/room-detail")
@RequiredArgsConstructor
public class ManagerRoomDetailController extends BaseManagerController<RoomDetailEntity, RoomDetailEntity> {
    private final RoomDetailService roomDetailService;

    @GetMapping("/{id}")
    public Mono<RoomDetailEntity> findById(@PathVariable Integer id) {
        return roomDetailService.findById(id);
    }
}
