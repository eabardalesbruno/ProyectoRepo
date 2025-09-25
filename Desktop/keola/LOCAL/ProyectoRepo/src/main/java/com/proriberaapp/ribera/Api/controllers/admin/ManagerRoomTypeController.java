package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.RoomTypeEntity;
import com.proriberaapp.ribera.services.client.RoomTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.manager}/room-type")
@RequiredArgsConstructor
@Slf4j
public class ManagerRoomTypeController extends BaseManagerController<RoomTypeEntity, RoomTypeEntity>{
    private final RoomTypeService roomTypeService;

}
