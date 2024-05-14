package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.RoomOfferEntity;
import com.proriberaapp.ribera.services.RoomOfferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/manager/room-offer")
@RequiredArgsConstructor
@Slf4j
public class ManagerRoomOfferController extends BaseManagerController<RoomOfferEntity, RoomOfferEntity>{
    private final RoomOfferService roomOfferService;

}
