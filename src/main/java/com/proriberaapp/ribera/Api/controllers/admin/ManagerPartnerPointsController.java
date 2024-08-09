package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.PartnerPointsEntity;
import com.proriberaapp.ribera.services.client.PartnerPointsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.manager}/partner-points")
@RequiredArgsConstructor
public class ManagerPartnerPointsController extends BaseManagerController<PartnerPointsEntity, PartnerPointsEntity>{
    private final PartnerPointsService partnerPointsService;

}
