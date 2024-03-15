package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Infraestructure.services.StateRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/manager/state-room")
@RequiredArgsConstructor
@Slf4j
public class StateRoomController {
    private final StateRoomService stateRoomService;
}
