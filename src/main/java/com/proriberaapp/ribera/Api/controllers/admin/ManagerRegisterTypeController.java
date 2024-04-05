package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.RegisterTypeRequest;
import com.proriberaapp.ribera.Domain.entities.RegisterTypeEntity;
import com.proriberaapp.ribera.services.RegisterTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/admin/manager/register-type")
@RequiredArgsConstructor
public class ManagerRegisterTypeController extends BaseManagerController<RegisterTypeEntity, RegisterTypeRequest>{
    private final RegisterTypeService registerTypeService;

}
