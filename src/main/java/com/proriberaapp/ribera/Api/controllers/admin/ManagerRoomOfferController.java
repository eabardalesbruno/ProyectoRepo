package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.RoomOfferEntity;
import com.proriberaapp.ribera.services.client.RoomOfferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.manager}/room-offer")
@RequiredArgsConstructor
@Slf4j
public class ManagerRoomOfferController extends BaseManagerController<RoomOfferEntity, RoomOfferEntity>{
    private final RoomOfferService roomOfferService;

}
