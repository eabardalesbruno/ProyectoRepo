package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Crosscutting.security.JwtTokenProvider;
import com.proriberaapp.ribera.Domain.entities.ComfortRoomOfferDetailEntity;
import com.proriberaapp.ribera.services.BaseService;
import com.proriberaapp.ribera.services.ComfortRoomOfferDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manager/comfort-room-offer-detail")
@RequiredArgsConstructor
public class ManagerComfortRoomOfferDetailController extends BaseManagerController<ComfortRoomOfferDetailEntity, ComfortRoomOfferDetailEntity>{
    private final ComfortRoomOfferDetailService comfortRoomOfferDetailService;

}
