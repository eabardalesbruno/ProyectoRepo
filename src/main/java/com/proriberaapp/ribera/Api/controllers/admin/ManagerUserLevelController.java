package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.UserLevelEntity;
import com.proriberaapp.ribera.services.UserLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/manager/user-level")
@RequiredArgsConstructor
public class ManagerUserLevelController extends BaseManagerController<UserLevelEntity, UserLevelEntity>{
    private final UserLevelService userLevelService;

}
