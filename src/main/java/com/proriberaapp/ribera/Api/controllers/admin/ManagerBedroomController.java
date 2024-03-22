package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Crosscutting.security.JwtTokenProvider;
import com.proriberaapp.ribera.Domain.entities.BedroomEntity;
import com.proriberaapp.ribera.services.BaseService;
import com.proriberaapp.ribera.services.BedroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manager/bedroom")
@RequiredArgsConstructor
public class ManagerBedroomController extends BaseManagerController<BedroomEntity, BedroomEntity> {
    private final BedroomService bedroomService;

}
