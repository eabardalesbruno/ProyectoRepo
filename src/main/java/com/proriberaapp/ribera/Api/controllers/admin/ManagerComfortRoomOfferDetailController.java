package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Infraestructure.services.ComfortRoomOfferDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/manager/comfort-room-offer-detail")
@RequiredArgsConstructor
public class ManagerComfortRoomOfferDetailController {
    private final ComfortRoomOfferDetailService comfortRoomOfferDetailService;
}
