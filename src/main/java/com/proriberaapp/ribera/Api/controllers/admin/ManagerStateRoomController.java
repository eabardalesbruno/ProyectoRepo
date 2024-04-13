package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Crosscutting.security.JwtTokenProvider;
import com.proriberaapp.ribera.Domain.entities.StateRoomEntity;
import com.proriberaapp.ribera.services.BaseService;
import com.proriberaapp.ribera.services.StateRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manager/state-room")
@RequiredArgsConstructor
@Slf4j
public class ManagerStateRoomController extends BaseManagerController<StateRoomEntity, StateRoomEntity>{
    private final StateRoomService stateRoomService;

}
