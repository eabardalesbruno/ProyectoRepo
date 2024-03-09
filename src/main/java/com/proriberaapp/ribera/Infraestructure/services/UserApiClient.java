package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Api.controllers.dto.UserDataDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserApiClient {

    private static final String SEARCH_USER_URL = "https://backoffice.keola.club/api/User/search/";

    private static final String LOGIN_USER_URL = "https://backoffice.keola.club/api/token";

    private final RestTemplate restTemplate;

    public UserApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserDataDTO searchUser(String username) {
        return restTemplate.getForObject(SEARCH_USER_URL + username, UserDataDTO.class);
    }

    public String loginUser(String username, String password) {
        // Lógica para hacer login y obtener el token
        return null;
    }
}