package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Api.controllers.dto.UserDataDTO;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserApiClient {

    private static final String SEARCH_USER_URL = "https://backoffice.keola.club/api/User/username";
    private static final String LOGIN_USER_URL = "https://backoffice.keola.club/api/token";

    private final RestTemplate restTemplate;

    public UserApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserDataDTO searchUser(String username) {
        UserDataDTO userDataDTO = restTemplate.getForObject(SEARCH_USER_URL + username, UserDataDTO.class);
        if (userDataDTO != null) {
            return userDataDTO;
        } else {
            throw new RuntimeException("No se pudieron obtener los datos del usuario desde la API");
        }
    }

    public String loginUser(String username, String password) {
        // Preparar el cuerpo de la solicitud
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject request = new JSONObject();
        request.put("username", username);
        request.put("password", password);
        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

        // Realizar la solicitud POST para iniciar sesión
        ResponseEntity<String> response = restTemplate.exchange(LOGIN_USER_URL, HttpMethod.POST, entity, String.class);

        // Verificar si la solicitud fue exitosa y devolver el token
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Fallo al iniciar sesión en la API de backoffice. Código de estado: " + response.getStatusCodeValue());
        }
    }
}