package com.proriberaapp.ribera.Api.controllers.admin;


import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomReturn;
import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import com.proriberaapp.ribera.services.client.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("${url.manager}/room")
@RequiredArgsConstructor
@Slf4j
public class ManagerRoomController extends BaseManagerController<RoomEntity, RoomEntity>{

    private final RoomService roomService;

    @GetMapping("/find/all/view")
    public Flux<ViewRoomReturn> findAllViewRoomReturn() {
        return roomService.findAllView();
    }
}
