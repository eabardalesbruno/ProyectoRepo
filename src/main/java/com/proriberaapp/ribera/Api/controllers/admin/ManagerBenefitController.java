package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.BenefitEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${url.manager}/benefit")
@RequiredArgsConstructor
public class ManagerBenefitController extends BaseManagerController<BenefitEntity, BenefitEntity>{
}
