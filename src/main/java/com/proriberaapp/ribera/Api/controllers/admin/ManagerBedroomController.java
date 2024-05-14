package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.BedroomEntity;
import com.proriberaapp.ribera.services.BedroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/manager/bedroom")
@RequiredArgsConstructor
public class ManagerBedroomController extends BaseManagerController<BedroomEntity, BedroomEntity> {
    private final BedroomService bedroomService;

}
