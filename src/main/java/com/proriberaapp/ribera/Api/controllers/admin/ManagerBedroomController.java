package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.BedroomEntity;
import com.proriberaapp.ribera.services.client.BedroomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/bedroom")
@Slf4j
public class ManagerBedroomController extends BaseManagerController<BedroomEntity, BedroomEntity> {
    @Autowired
    private BedroomService bedroomService;

    @GetMapping
    public Flux<BedroomEntity> findAll() {
        return bedroomService.findAll();
    }

}
