package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.FinalCostumerEntity;
import com.proriberaapp.ribera.services.client.FinalCostumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.manager}/final-costumer")
@RequiredArgsConstructor
public class ManagerFinalCostumerController extends BaseManagerController<FinalCostumerEntity, FinalCostumerEntity>{
    private final FinalCostumerService finalCostumerService;

}
