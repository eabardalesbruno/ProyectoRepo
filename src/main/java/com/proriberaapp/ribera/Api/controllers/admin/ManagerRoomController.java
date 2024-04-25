package com.proriberaapp.ribera.Api.controllers.admin;


import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import com.proriberaapp.ribera.services.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/manager/room")
@RequiredArgsConstructor
@Slf4j
public class ManagerRoomController extends BaseManagerController<RoomEntity, RoomEntity>{
    private final RoomService roomService;

}
