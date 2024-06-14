package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.StateRoomEntity;
import com.proriberaapp.ribera.services.client.StateRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.manager}/state-room")
@RequiredArgsConstructor
@Slf4j
public class ManagerStateRoomController extends BaseManagerController<StateRoomEntity, StateRoomEntity>{
    private final StateRoomService stateRoomService;

}
