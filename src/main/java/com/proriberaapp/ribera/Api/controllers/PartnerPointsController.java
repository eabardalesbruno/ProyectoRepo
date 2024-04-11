package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.services.PartnerPointsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/partner-points")
@RequiredArgsConstructor
@Slf4j
public class PartnerPointsController {
    private final PartnerPointsService partnerPointsService;
}
