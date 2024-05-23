package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewServiceReturn;
import com.proriberaapp.ribera.services.ServicesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("${url.manager}/service")
@RequiredArgsConstructor
@Slf4j
public class ManagerServicesController {
    private final ServicesService servicesService;

    @GetMapping("/find/all/view")
    public Flux<ViewServiceReturn> findAllViewServiceReturn() {
        return servicesService.findAllViewServiceReturn();
    }
}
