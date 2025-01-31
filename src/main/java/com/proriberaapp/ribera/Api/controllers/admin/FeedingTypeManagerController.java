package com.proriberaapp.ribera.Api.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proriberaapp.ribera.Domain.entities.FeedingTypeEntity;
import com.proriberaapp.ribera.services.admin.impl.FeedingTypeManagerServiceImpl;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("${url.api}/maintenance/feeding-type")
public class FeedingTypeManagerController {
    @Autowired  
    private FeedingTypeManagerServiceImpl feedingTypeManagerServiceImpl;

    @GetMapping()
    public Flux<FeedingTypeEntity> getActives() {
        return this.feedingTypeManagerServiceImpl.getActive();
    }
    
}
