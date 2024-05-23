package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.BedroomEntity;
import com.proriberaapp.ribera.services.BedroomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.manager}/bedroom")
@Slf4j
public class ManagerBedroomController extends BaseManagerController<BedroomEntity, BedroomEntity> {
    @Autowired
    private BedroomService bedroomService;

}
