package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.client.dto.UserDataDTO;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.services.client.UserApiClient;
import com.proriberaapp.ribera.services.client.UserClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserClientServiceImpl implements UserClientService {

    private final UserClientRepository userClientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtUtil;
    @Autowired
    private UserApiClient userApiClient;
    @Autowired
    private EmailService emailService;
    /*
    @Override
    public Mono<UserClientEntity> registerUser(UserClientEntity userClient) {
        return userClientRepository.findByEmail(userClient.getEmail())
                .flatMap(existingUser -> Mono.error(new RuntimeException("El correo electrónico ya está registrado")))
                .then(Mono.defer(() -> {
                    validatePassword(userClient.getPassword());
                    return userClientRepository.findByDocumentNumber(userClient.getDocumentNumber())
                            .flatMap(existingUser -> Mono.error(new RuntimeException("El número de documento ya está registrado")))
                            .then(Mono.just(userClient));
                }))
                .map(userToSave -> {
                    userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword())); // Cifra la contraseña
                    return userToSave;
                })
                .flatMap(userClientRepository::save);
    }

     */

    /* 01072024 REGISTRO DE USUARIO SIN EMAIL
    public Mono<UserClientEntity> registerUser(UserClientEntity userClient) {
        // Obtener la fecha y hora actual
        long currentTimeMillis = System.currentTimeMillis();
        Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
        // Establecer la fecha y hora de creación
        userClient.setCreatedat(currentTimestamp);
        return userClientRepository.findByEmail(userClient.getEmail())
                .flatMap(existingUser -> {
                    if ("1".equals(userClient.getGoogleAuth())) {
                        if (existingUser.getPassword() != null) {
                            return Mono.error(new RuntimeException("El correo electrónico ya está registrado con una contraseña"));
                        } else {
                            return Mono.just(existingUser);
                        }
                    } else {
                        if (existingUser.getPassword() != null) {
                            return Mono.error(new RuntimeException("El correo electrónico ya está registrado con una contraseña"));
                        } else {
                            return Mono.just(existingUser);
                        }
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    if (!"1".equals(userClient.getGoogleAuth())) {
                        validatePassword(userClient.getPassword());
                    }
                    return userClientRepository.findByDocumentNumber(userClient.getDocumentNumber())
                            .flatMap(existingUser -> Mono.error(new RuntimeException("El número de documento ya está registrado")))
                            .then(Mono.just(userClient));
                }))
                .map(userToSave -> {
                    if (!"1".equals(userToSave.getGoogleAuth())) {
                        log.info("Cifrar contraseña");
                        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
                    }
                    return userToSave;
                })
                .flatMap(userClientRepository::save);
    }

     */
/* 02072024
    public Mono<UserClientEntity> registerUser(UserClientEntity userClient) {
        long currentTimeMillis = System.currentTimeMillis();
        Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
        userClient.setCreatedat(currentTimestamp);

        return userClientRepository.findByEmail(userClient.getEmail())
                .flatMap(existingUser -> {
                    if ("1".equals(userClient.getGoogleAuth())) {
                        if (existingUser.getPassword() != null) {
                            return Mono.error(new RuntimeException("El correo electrónico ya está registrado con una contraseña"));
                        } else {
                            return Mono.just(existingUser);
                        }
                    } else {
                        if (existingUser.getPassword() != null) {
                            return Mono.error(new RuntimeException("El correo electrónico ya está registrado con una contraseña"));
                        } else {
                            return Mono.just(existingUser);
                        }
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    if (!"1".equals(userClient.getGoogleAuth())) {
                        validatePassword(userClient.getPassword());
                    }
                    return userClientRepository.findByDocumentNumber(userClient.getDocumentNumber())
                            .flatMap(existingUser -> Mono.error(new RuntimeException("El número de documento ya está registrado")))
                            .then(Mono.just(userClient));
                }))
                .map(userToSave -> {
                    if (!"2".equals(userToSave.getGoogleAuth())) {
                        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
                    }
                    return userToSave;
                })
                .flatMap(userClientRepository::save)
                .flatMap(savedUser -> {
                    // Generar el cuerpo del correo de confirmación
                    String emailBody = generateUserRegistrationEmailBody(savedUser);
                    // Enviar el correo de confirmación
                    return emailService.sendEmail(savedUser.getEmail(), "Confirmación de Registro", emailBody)
                            .thenReturn(savedUser);
                });
    }

 */

    public Mono<UserClientEntity> registerUser(UserClientEntity userClient) {
        long currentTimeMillis = System.currentTimeMillis();
        Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
        userClient.setCreatedat(currentTimestamp);

        return userClientRepository.findByEmail(userClient.getEmail())
                .flatMap(existingUser -> {
                    if ("1".equals(userClient.getGoogleAuth())) {
                        if (existingUser.getPassword() != null) {
                            return Mono.error(new RuntimeException("El correo electrónico ya está registrado con una contraseña"));
                        } else {
                            return Mono.just(existingUser);
                        }
                    } else {
                        if (existingUser.getPassword() != null) {
                            return Mono.error(new RuntimeException("El correo electrónico ya está registrado con una contraseña"));
                        } else {
                            return Mono.just(existingUser);
                        }
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    if (!"1".equals(userClient.getGoogleAuth())) {
                        validatePassword(userClient.getPassword());
                    }
                    return userClientRepository.findByDocumentNumber(userClient.getDocumentNumber())
                            .flatMap(existingUser -> Mono.error(new RuntimeException("El número de documento ya está registrado")))
                            .then(Mono.just(userClient));
                }))
                .map(userToSave -> {
                    if ("1".equals(userToSave.getGoogleAuth()) && (userToSave.getPassword() == null || userToSave.getPassword().isEmpty())) {
                        // Si googleAuth es "1" y no hay contraseña, establecer la contraseña como null para evitar el cifrado vacío
                        userToSave.setPassword(null);
                    } else if (!"1".equals(userToSave.getGoogleAuth())) {
                        // Cifrar la contraseña si no es una autenticación de Google
                        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
                    }
                    return userToSave;
                })
                .flatMap(userClientRepository::save)
                .flatMap(savedUser -> {
                    String emailBody = generateUserRegistrationEmailBody(savedUser);
                    return emailService.sendEmail(savedUser.getEmail(), "Confirmación de Registro", emailBody)
                            .thenReturn(savedUser);
                });
    }

    private void validatePassword(String password) {
        if (!password.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$")) {
            throw new RuntimeException("La contraseña debe contener al menos una letra y un número, y tener una longitud mínima de 8 caracteres");
        }
    }

    private String generateUserRegistrationEmailBody(UserClientEntity userClient) {
        String body = "<html><head><title></title></head><body style='color:black'>";
        body += "<div style='width: 100%'>";
        body += "<div style='display:flex;'>";
        body += "</div>";
        body += "<img style='width: 100%' src='http://www.inresorts.club/Views/img/fondo.png'>";
        body += "<h1 style='margin-top: 2px; text-align: center; font-weight: bold; font-style: italic;'>"
                + "Bienvenido, " + userClient.getFirstName() + "!</h1>";
        body += "<h3 style='text-align: center;'>Gracias por registrarte en nuestra plataforma</h3>";
        body += "<p style='text-align: center;'>Email: " + userClient.getEmail() + "</p>";
        body += "<center><div style='width: 100%'>";
        body += "<p style='margin-left: 10%; margin-right: 10%;'></p>";
        body += "<center>Disfruta de nuestros servicios y promociones exclusivas.</center>";
        body += "</div></center>";
        body += "<center><div style='width: 100%'>";
        body += "<p style='margin-left: 10%; margin-right: 10%;'>-------------- o --------------</p>";
        body += "</div></center>";
        body += "</div></center>";
        body += "</body></html>";

        return body;
    }

    @Override
    public Flux<UserClientEntity> findAll() {
        return userClientRepository.findAll();
    }
    @Override
    public Mono<UserClientEntity> findById(Integer id) {
        return userClientRepository.findById(id);
    }
    @Override
    public Mono<UserDataDTO> findUserDTOById(Integer id) {
        return userClientRepository.findById(id)
                .flatMap(user -> new UserDataDTO().convertTo(user));
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return userClientRepository.deleteById(id);
    }

    @Override
    public Mono<UserClientEntity> saveUser(UserClientEntity userClient) {
        // Obtener la fecha y hora actual
        long currentTimeMillis = System.currentTimeMillis();
        Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
        // Establecer la fecha y hora de creación
        userClient.setCreatedat(currentTimestamp);
        return userClientRepository.findByEmail(userClient.getEmail())
                .flatMap(existingUser -> Mono.error(new RuntimeException("El correo electrónico ya está registrado")))
                .then(Mono.defer(() -> {
                    validatePassword(userClient.getPassword());
                    return userClientRepository.findByDocumentNumber(userClient.getDocumentNumber())
                            .flatMap(existingUser -> Mono.error(new RuntimeException("El número de documento ya está registrado")))
                            .then(Mono.just(userClient));
                }))
                .map(userToSave -> {
                    userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword())); // Cifra la contraseña
                    return userToSave;
                })
                .flatMap(userClientRepository::save);
    }

    @Override
    public Mono<String> login(String email, String password) {
        return userClientRepository.findByEmail(email)
                .flatMap(user -> {
                    if (passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.just(jwtUtil.generateToken(user));
                    } else {
                        return Mono.error(new RuntimeException("Credenciales inválidas"));
                    }
                });
    }

    @Override
    public Mono<UserClientEntity> registerWithGoogle(String googleId, String email, String name) {
        UserClientEntity user = UserClientEntity.builder()
                .googleId(googleId)
                .email(email)
                .firstName(name)
                .build();
        return userClientRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userClientRepository.findByEmail(email)
                .map(user -> true)
                .defaultIfEmpty(false)
                .block();
    }

    @Override
    public Mono<String> loginWithGoogle(String googleId) {
        return userClientRepository.findByGoogleId(googleId)
                .map(user -> jwtUtil.generateToken(user))
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")));
    }

    @Override
    public UserDataDTO searchUser(String username) {
        // Llamar al cliente API para buscar el usuario por username
        return userApiClient.searchUser(username);
    }

    @Override
    public UserDataDTO registerUser(UserDataDTO userDataDTO) {
        // Registrar el usuario en la base de datos
        return userClientRepository.save(userDataDTO);
    }

    @Override
    public String loginUser(String username, String password) {
        return userApiClient.loginUser(username, password);
    }

    @Override
    public Mono<UserClientEntity> findByEmail(String email) {
        return userClientRepository.findByEmail(email);
    }

    @Override
    public Mono<UserClientEntity> updatePassword(UserClientEntity userClient, String newPassword) {
        userClient.setPassword(passwordEncoder.encode(newPassword));
        userClientRepository.save(userClient).block();
        return null;
    }
}
