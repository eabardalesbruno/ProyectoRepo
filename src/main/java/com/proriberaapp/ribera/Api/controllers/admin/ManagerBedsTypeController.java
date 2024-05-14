package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.BedsTypeEntity;
import com.proriberaapp.ribera.services.BedsTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/manager/beds-type")
@RequiredArgsConstructor
public class ManagerBedsTypeController extends BaseManagerController<BedsTypeEntity, BedsTypeEntity> {
    private final BedsTypeService bedsTypeService;

}
