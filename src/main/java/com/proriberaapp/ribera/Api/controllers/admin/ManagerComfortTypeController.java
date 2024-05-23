package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.ComfortTypeEntity;
import com.proriberaapp.ribera.services.ComfortTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.manager}/comfort-type")
@RequiredArgsConstructor
public class ManagerComfortTypeController extends BaseManagerController<ComfortTypeEntity, ComfortTypeEntity>{
    private final ComfortTypeService comfortTypeService;

}
