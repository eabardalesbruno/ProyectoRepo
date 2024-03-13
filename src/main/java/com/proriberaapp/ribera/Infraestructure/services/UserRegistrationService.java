package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Api.controllers.dto.UserDataDTO;
import com.proriberaapp.ribera.Domain.entities.UserApiEntity;
import com.proriberaapp.ribera.Infraestructure.repository.UserApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserRegistrationService {

    private final UserApiRepository userApiRepository;
    private final UserApiClient userApiClient;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRegistrationService(UserApiRepository userApiRepository, UserApiClient userApiClient, PasswordEncoder passwordEncoder) {
        this.userApiRepository = userApiRepository;
        this.userApiClient = userApiClient;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<Void> loginAndRegisterUser(String username, String password) {
        String token = userApiClient.loginUser(username, password);

        if (token != null) {
            UserDataDTO userDataDTO = userApiClient.searchUser(username);
            if (userDataDTO != null) {
                UserApiEntity userApiEntity = new UserApiEntity();
                userApiEntity.setUsername(username);
                userApiEntity.setDocumentNumber(userDataDTO.getDocumentNumber());
                userApiEntity.setCivilStatus(userDataDTO.getCivilStatus());
                userApiEntity.setEmail(userDataDTO.getEmail());
                userApiEntity.setCity(userDataDTO.getCity());
                userApiEntity.setCellNumber(userDataDTO.getCellNumber());
                userApiEntity.setBirthDate(userDataDTO.getBirthDate());
                userApiEntity.setFirstName(userDataDTO.getFirstName());
                userApiEntity.setLastName(userDataDTO.getLastName());
                userApiEntity.setPassword(passwordEncoder.encode(password));
                return userApiRepository.save(userApiEntity).then();
            } else {
                return Mono.error(new RuntimeException("No se pudieron obtener los datos del usuario desde la API"));
            }
        } else {
            return Mono.error(new RuntimeException("El inicio de sesión en la API de backoffice falló"));
        }
    }
}