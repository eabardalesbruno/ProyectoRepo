package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.OfferTypeEntity;
import com.proriberaapp.ribera.services.client.OfferTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.manager}/offer-type")
@RequiredArgsConstructor
public class ManagerOfferTypeController extends BaseManagerController<OfferTypeEntity, OfferTypeEntity>{
    private final OfferTypeService offerTypeService;

}
