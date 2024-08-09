package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.RoomDetailEntity;
import com.proriberaapp.ribera.services.client.RoomDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.manager}/room-detail")
@RequiredArgsConstructor
public class ManagerRoomDetailController extends BaseManagerController<RoomDetailEntity, RoomDetailEntity>{
    private final RoomDetailService roomDetailService;

}
