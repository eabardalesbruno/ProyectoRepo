package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.UserEntity;
import com.proriberaapp.ribera.Infraestructure.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Controller
public class OAuth2LoginController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;


    private static final String CLIENT_ID = "your-client-id";
    private static final String CLIENT_SECRET = "your-client-secret";
    private static final String REDIRECT_URI = "http://localhost:8777/login/google/callback";
    private static final String AUTHORIZATION_ENDPOINT = "https://accounts.google.com/o/oauth2/auth";
    private static final String TOKEN_ENDPOINT = "https://oauth2.googleapis.com/token";
    private static final String USERINFO_ENDPOINT = "https://www.googleapis.com/oauth2/v3/userinfo";

    @GetMapping("/login/google")
    public String googleLogin() {
        String authorizationUri = getAuthorizationUri();
        return "redirect:" + authorizationUri;
    }

    @GetMapping("/login/google/callback")
    public String googleLoginCallback(@RequestParam("code") String code) {
        String accessToken = getAccessToken(code);
        Map<String, Object> userInfo = getUserInfo(accessToken);

        String googleId = (String) userInfo.get("sub");
        String googleEmail = (String) userInfo.get("email");
        String googleName = (String) userInfo.get("name");

        UserEntity userEntity = new UserEntity();
        userEntity.setGoogleId(googleId);
        userEntity.setGoogleEmail(googleEmail);
        userEntity.setGoogleName(googleName);

        userRepository.save(userEntity);

        return "redirect:/";
    }

    private String getAuthorizationUri() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(AUTHORIZATION_ENDPOINT)
                .queryParam("client_id", CLIENT_ID)
                .queryParam("redirect_uri", REDIRECT_URI)
                .queryParam("response_type", "code")
                .queryParam("scope", "openid profile email");
        return builder.toUriString();
    }

    private String getAccessToken(String code) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("code", code);
        requestBody.add("client_id", CLIENT_ID);
        requestBody.add("client_secret", CLIENT_SECRET);
        requestBody.add("redirect_uri", REDIRECT_URI);
        requestBody.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(requestBody, headers, HttpMethod.POST, URI.create(TOKEN_ENDPOINT));
        ResponseEntity<Map> response = restTemplate.exchange(request, Map.class);
        return (String) response.getBody().get("access_token");
    }

    private Map<String, Object> getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(USERINFO_ENDPOINT));
        ResponseEntity<Map> response = restTemplate.exchange(request, Map.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        return new HashMap<>();
    }
}
