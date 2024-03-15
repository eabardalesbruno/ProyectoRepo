package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Infraestructure.services.ComfortTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/manager/comfort-type")
@RequiredArgsConstructor
public class ManagerComfortTypeController {
    private final ComfortTypeService comfortTypeService;
}
