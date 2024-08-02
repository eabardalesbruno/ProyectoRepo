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
        String body = "<html><head><title>Bienvenido</title></head><body style='color:black; margin: 0; padding: 0;'>";
        body += "<div style='width: 100%; position: relative;'>";
        body += "<img style='width: 100%; margin-top: 20px; border-top-left-radius: 20px; border-top-right-radius: 20px;' src='https://scontent.flim1-2.fna.fbcdn.net/v/t39.30808-6/453514514_2238864093126059_4377276491425541120_n.jpg?_nc_cat=110&ccb=1-7&_nc_sid=127cfc&_nc_ohc=r0fzgelec-UQ7kNvgFL0EDI&_nc_ht=scontent.flim1-2.fna&oh=00_AYAJLos7io5zNmz08RwyK1pc5ZGwN5Cn8jt8Eg17N73CQQ&oe=66B1E807' alt='Bienvenido'>";

        body += "<div style='position: absolute; top: 10px; right: 10px; display: flex; align-items: center;'>";
        body += "<img style='width: 10px; height: 10px; margin-left: 5px;' src='https://scontent.flim1-2.fna.fbcdn.net/v/t39.30808-6/453503393_2238863839792751_3678586622785113323_n.jpg?stp=cp0_dst-jpg&_nc_cat=108&ccb=1-7&_nc_sid=127cfc&_nc_ohc=OMKWsE877hcQ7kNvgHnzNGq&_nc_ht=scontent.flim1-2.fna&oh=00_AYBSmgM6SVV33fWdVeqn9sUMleFSdtOGZPcc0m-USS93bg&oe=66B20925' alt='Logo 1'>";
        body += "<img style='width: 10px; height: 10px; margin-left: 5px;' src='https://scontent.flim1-2.fna.fbcdn.net/v/t39.30808-6/453501437_2238863739792761_5553627034492335729_n.jpg?stp=cp0_dst-jpg&_nc_cat=111&ccb=1-7&_nc_sid=127cfc&_nc_ohc=fcEltLDDNeMQ7kNvgFNAsL6&_nc_ht=scontent.flim1-2.fna&oh=00_AYBD75zTjdsLuKmtk3vPYR7fBfCg5U2aVQ_tYm8679ZFCQ&oe=66B1FF76' alt='Logo 2'>";
        body += "<img style='width: 10px; height: 10px; margin-left: 5px;' src='https://scontent.flim1-1.fna.fbcdn.net/v/t39.30808-6/453497633_2238863526459449_291281439279005519_n.jpg?stp=cp0_dst-jpg&_nc_cat=104&ccb=1-7&_nc_sid=127cfc&_nc_ohc=vMzblHxFzGUQ7kNvgHhI3YO&_nc_ht=scontent.flim1-1.fna&oh=00_AYAEn_ThdeZSWqvo7RurNrnoAulbgxM7V5YzJc_CGsYACg&oe=66B1E905' alt='Logo 3'>";
        body += "</div>";

        body += "<h1 style='margin-top: 2px; text-align: center; font-weight: bold; font-style: italic;'>Bienvenido, " + userClient.getFirstName() + "!</h1>";
        body += "<h3 style='text-align: center;'>Gracias por registrarte en nuestra plataforma</h3>";
        body += "<p style='text-align: center;'>Email: " + userClient.getEmail() + "</p>";
        body += "<center><div style='width: 100%'>";
        body += "<p style='margin-left: 10%; margin-right: 10%;'></p>";
        body += "<center>Disfruta de nuestros servicios y promociones exclusivas.</center>";
        body += "</div></center>";
        body += "<center><div style='width: 100%'>";
        body += "<p style='margin-left: 10%; margin-right: 10%;'>-------------- o --------------</p>";
        body += "</div></center>";
        body += "</div>";
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

    public Mono<String> checkAndGenerateToken(String email) {
        return userClientRepository.findByEmailOrGoogleIdOrGoogleEmail(email, email, email)
                .filter(user -> "1".equals(user.getGoogleAuth()))
                .flatMap(user -> Mono.just(jwtUtil.generateToken(user)))
                .switchIfEmpty(Mono.error(new RuntimeException("")));
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
