package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.OfferTypeEntity;
import com.proriberaapp.ribera.services.OfferTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/manager/offer-type")
@RequiredArgsConstructor
public class ManagerOfferTypeController extends BaseManagerController<OfferTypeEntity, OfferTypeEntity>{
    private final OfferTypeService offerTypeService;

}
