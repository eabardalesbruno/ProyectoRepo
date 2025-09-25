package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.ClientCountResponseDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.ClientResponseDto;
import com.proriberaapp.ribera.services.client.UserClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manager")
@RequiredArgsConstructor
@Slf4j
public class ManagerClientController {

    private final UserClientService userClientService;

    @GetMapping("/clients/list")
    @ResponseStatus(HttpStatus.OK)
    public List<ClientResponseDto> getAllClients() {
        return userClientService.getAllClients();
    }

    @GetMapping("/clients/stats/count-by-type")
    @ResponseStatus(HttpStatus.OK)
    public List<ClientCountResponseDto> getClientCountByType() {
        return userClientService.getCountClientsByType();
    }
}