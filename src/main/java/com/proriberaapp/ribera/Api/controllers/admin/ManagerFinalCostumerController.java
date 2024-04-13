package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Crosscutting.security.JwtTokenProvider;
import com.proriberaapp.ribera.Domain.entities.FinalCostumerEntity;
import com.proriberaapp.ribera.services.BaseService;
import com.proriberaapp.ribera.services.FinalCostumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manager/final-costumer")
@RequiredArgsConstructor
public class ManagerFinalCostumerController extends BaseManagerController<FinalCostumerEntity, FinalCostumerEntity>{
    private final FinalCostumerService finalCostumerService;

}
