package com.proriberaapp.ribera.Api.controllers.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proriberaapp.ribera.Domain.entities.FamilyGroupEntity;
import com.proriberaapp.ribera.services.admin.impl.FamilyGroupManagerSeviceImpl;

import reactor.core.publisher.Flux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/maintenance/family-group")
public class FamiliyGroupManagerController {
    @Autowired
    private FamilyGroupManagerSeviceImpl familyGroupManagerSeviceImpl;

    @GetMapping()
    public Flux<FamilyGroupEntity> getActives() {
        return this.familyGroupManagerSeviceImpl.getActive();
    }

}
