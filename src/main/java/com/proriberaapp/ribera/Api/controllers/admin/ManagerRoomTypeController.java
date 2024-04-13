package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Crosscutting.security.JwtTokenProvider;
import com.proriberaapp.ribera.Domain.entities.RoomTypeEntity;
import com.proriberaapp.ribera.services.BaseService;
import com.proriberaapp.ribera.services.RoomTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manager/room-type")
@RequiredArgsConstructor
@Slf4j
public class ManagerRoomTypeController extends BaseManagerController<RoomTypeEntity, RoomTypeEntity>{
    private final RoomTypeService roomTypeService;

}
