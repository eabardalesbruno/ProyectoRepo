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
        String body = "<html>\n" +
                "<head>\n" +
                "    <title>Bienvenido</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            color: black;\n" +
                "            background-color: #f4f4f4; /* Color de fondo */\n" +
                "        }\n" +
                "        .header {\n" +
                "            width: 100%;\n" +
                "            position: relative;\n" +
                "            background-color: #f4f4f4; /* Color de fondo del encabezado */\n" +
                "            padding: 20px 0; /* Espaciado superior e inferior para el encabezado */\n" +
                "        }\n" +
                "        .logos-right {\n" +
                "            position: absolute;\n" +
                "            top: 10px;\n" +
                "            right: 10px;\n" +
                "            display: flex;\n" +
                "        }\n" +
                "        .logos-right img {\n" +
                "            width: 10px;\n" +
                "            height: 10px;\n" +
                "            margin-left: 5px;\n" +
                "        }\n" +
                "        .logo-left {\n" +
                "            width: 50px;\n" +
                "            position: absolute;\n" +
                "            top: 10px;\n" +
                "            left: 10px;\n" +
                "        }\n" +
                "        .banner {\n" +
                "            width: 540px;\n" +
                "            border-top-left-radius: 20px;\n" +
                "            border-top-right-radius: 20px;\n" +
                "            display: block;\n" +
                "            margin: 0 auto;\n" +
                "        }\n" +
                "        .container {\n" +
                "            width: 500px;\n" +
                "            background-color: white; /* Fondo blanco del contenido */\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "            border-bottom-left-radius: 10px;\n" +
                "            border-bottom-right-radius: 10px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        .content {\n" +
                "            text-align: center;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .content h1 {\n" +
                "            margin-top: 20px;\n" +
                "            font-weight: bold;\n" +
                "            font-style: italic;\n" +
                "        }\n" +
                "        .content h3, .content p {\n" +
                "            margin: 10px 0;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            width: 100%;\n" +
                "            text-align: center;\n" +
                "            margin: 20px 0;\n" +
                "        }\n" +
                "        .help-section {\n" +
                "            width: 500px;\n" +
                "            background-color: white; /* Fondo blanco del contenido */\n" +
                "            margin: 20px auto;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 10px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"header\">\n" +
                "        <!-- Encabezado con logos -->\n" +
                "        <img class=\"logo-left\" src=\"https://scontent.flim1-2.fna.fbcdn.net/v/t39.30808-6/453907774_2238863976459404_4409148998166454890_n.jpg?_nc_cat=111&ccb=1-7&_nc_sid=127cfc&_nc_ohc=1p66qBQN6IYQ7kNvgEnxiv2&_nc_ht=scontent.flim1-2.fna&oh=00_AYACRHyTnMSMkClEmGFw8OmSBT2T_U4LGusY0F3KX0OBVQ&oe=66B1E966\" alt=\"Logo Izquierda\">\n" +
                "        <div class=\"logos-right\">\n" +
                "            <img src=\"https://scontent.flim1-2.fna.fbcdn.net/v/t39.30808-6/453503393_2238863839792751_3678586622785113323_n.jpg?stp=cp0_dst-jpg&_nc_cat=108&ccb=1-7&_nc_sid=127cfc&_nc_ohc=OMKWsE877hcQ7kNvgHnzNGq&_nc_ht=scontent.flim1-2.fna&oh=00_AYBSmgM6SVV33fWdVeqn9sUMleFSdtOGZPcc0m-USS93bg&oe=66B20925\" alt=\"Logo 1\">\n" +
                "            <img src=\"https://scontent.flim1-2.fna.fbcdn.net/v/t39.30808-6/453501437_2238863739792761_5553627034492335729_n.jpg?stp=cp0_dst-jpg&_nc_cat=111&ccb=1-7&_nc_sid=127cfc&_nc_ohc=fcEltLDDNeMQ7kNvgFNAsL6&_nc_ht=scontent.flim1-2.fna&oh=00_AYBD75zTjdsLuKmtk3vPYR7fBfCg5U2aVQ_tYm8679ZFCQ&oe=66B1FF76\" alt=\"Logo 2\">\n" +
                "            <img src=\"https://scontent.flim1-1.fna.fbcdn.net/v/t39.30808-6/453497633_2238863526459449_291281439279005519_n.jpg?stp=cp0_dst-jpg&_nc_cat=104&ccb=1-7&_nc_sid=127cfc&_nc_ohc=vMzblHxFzGUQ7kNvgHhI3YO&_nc_ht=scontent.flim1-1.fna&oh=00_AYAEn_ThdeZSWqvo7RurNrnoAulbgxM7V5YzJc_CGsYACg&oe=66B1E905\" alt=\"Logo 3\">\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <!-- Imagen de banner -->\n" +
                "    <img class=\"banner\" src=\"https://scontent.flim1-2.fna.fbcdn.net/v/t39.30808-6/453514514_2238864093126059_4377276491425541120_n.jpg?_nc_cat=110&ccb=1-7&_nc_sid=127cfc&_nc_ohc=r0fzgelec-UQ7kNvgFL0EDI&_nc_ht=scontent.flim1-2.fna&oh=00_AYAJLos7io5zNmz08RwyK1pc5ZGwN5Cn8jt8Eg17N73CQQ&oe=66B1E807\" alt=\"Bienvenido\">\n" +
                "\n" +
                "    <!-- Contenedor blanco con el contenido del mensaje -->\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"content\">\n" +
                "            <h1>Registro exitoso!</h1>\n" +
                "            <p>Hola "+ userClient.getFirstName()+",</p>\n" +
                "            <p>Gracias por registrarte a la plataforma. Su correo registrado es "+ userClient.getEmail()+".</p>\n" +
                "            <p>Si tienes alguna consulta, envianos tu consulta por correo.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <!-- Sección de ayuda -->\n" +
                "    <div class=\"help-section\">\n" +
                "        <h3>Necesitas ayuda?</h3>\n" +
                "        <p>Comunicate con nosotros a traves de los siguientes medios:</p>\n" +
                "        <p>Correo: informesyreservas@cieneguilladelrio.com</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

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
