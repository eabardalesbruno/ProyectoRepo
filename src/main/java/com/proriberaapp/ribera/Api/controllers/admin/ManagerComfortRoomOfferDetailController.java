package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.ComfortRoomOfferDetailEntity;
import com.proriberaapp.ribera.services.ComfortRoomOfferDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/manager/comfort-room-offer-detail")
@RequiredArgsConstructor
public class ManagerComfortRoomOfferDetailController extends BaseManagerController<ComfortRoomOfferDetailEntity, ComfortRoomOfferDetailEntity>{
    private final ComfortRoomOfferDetailService comfortRoomOfferDetailService;

}
