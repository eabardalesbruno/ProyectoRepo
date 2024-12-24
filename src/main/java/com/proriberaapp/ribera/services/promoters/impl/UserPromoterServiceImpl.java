package com.proriberaapp.ribera.services.promoters.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.*;
import com.proriberaapp.ribera.Api.controllers.client.dto.PromotorDataDTO;
import com.proriberaapp.ribera.Api.controllers.client.dto.TokenResult;
import com.proriberaapp.ribera.Api.controllers.client.dto.UserDataDTO;
import com.proriberaapp.ribera.Api.controllers.exception.CustomException;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Domain.entities.UserPromoterEntity;
import com.proriberaapp.ribera.Domain.enums.StatesUser;
import com.proriberaapp.ribera.Infraestructure.repository.UserPromoterRepository;
import com.proriberaapp.ribera.Infraestructure.repository.WalletRepository;
import com.proriberaapp.ribera.services.client.impl.WalletServiceImpl;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.services.client.impl.WalletServiceImpl;
import com.proriberaapp.ribera.services.promoters.UserPromoterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPromoterServiceImpl implements UserPromoterService {

    private final UserPromoterRepository userPromoterRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    @Autowired
    private EmailService emailService;
    private final WalletServiceImpl walletService;

    @Override
    public Mono<TokenDto> login(LoginRequest loginRequest) {
        return userPromoterRepository.findByUsernameOrEmail(loginRequest.username(), loginRequest.email())
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NO_CONTENT, "user not found")))
                .filter(user -> user.getStatus() == StatesUser.ACTIVE)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "user is not active")))
                .filter(user -> passwordEncoder.matches(loginRequest.password(), user.getPassword()))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "bad credentials")))
                .flatMap(user -> {
                    if (user.getWalletId() == null) {
                        // Crea una nueva wallet si el usuario no tiene una asociada
                        return walletService.createWalletUsuario(user.getUserPromoterId(), 1)
                                .flatMap(wallet -> {
                                    user.setWalletId(wallet.getWalletId());
                                    return userPromoterRepository.save(user).thenReturn(user);
                                });
                    } else {
                        return Mono.just(user);
                    }
                })
                .map(user -> new TokenDto(jwtProvider.generateTokenPromoter(user)))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "error generating token")));
    }

    public Mono<UserPromoterEntity> register(UserPromoterEntity userPromoter, String randomPassword) {
        long currentTimeMillis = System.currentTimeMillis();
        Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
        userPromoter.setCreatedAt(currentTimestamp);
        userPromoter.setPassword(randomPassword);
        //userCreate.setCreatedId(idUserPromoter);

        String username = userPromoter.getFirstName().toUpperCase() + " " + userPromoter.getLastName().toUpperCase();
        return userPromoterRepository.findByEmail(userPromoter.getEmail())
                .flatMap(existingUser -> {
                            if (existingUser.getPassword() != null) {
                                return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "El correo electrónico ya está registrado con una contraseña!!"));
                            } else {
                                return Mono.just(existingUser);
                            }
                        }
                    )
                .switchIfEmpty(Mono.defer(() -> {
                    if (!"1".equals(userPromoter.getGoogleAuth())) {
                        validatePassword(userPromoter.getPassword());
                    }
                    return userPromoterRepository.findByDocumentNumber(userPromoter.getDocumentNumber())
                            .flatMap(existingUser -> Mono.error(new RuntimeException("El número de documento ya está registrado")))
                            .then(Mono.just(userPromoter));
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
                .flatMap(userPromoterRepository::save)
                .flatMap(savedUser -> {
                    // Creamos la wallet
                    return walletService.createWalletPromoter(savedUser.getUserPromoterId(), 1)
                            .flatMap(wallet -> {
                                // Asignamos el walletId generado al userPromoter
                                savedUser.setWalletId(wallet.getWalletId());
                                return userPromoterRepository.save(savedUser);
                            });
                })
                .flatMap(savedUser -> {
                    String emailBody = generatePromoterRegistrationEmailBody(savedUser, randomPassword);
                    return emailService.sendEmail(savedUser.getEmail(), "Confirmación de Registro como Promotor", emailBody)
                            .thenReturn(savedUser);
                });
    }

    private void validatePassword(String password) {
        if (!password.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$")) {
            throw new RuntimeException("La contraseña debe contener al menos una letra y un número, y tener una longitud mínima de 8 caracteres");
        }
    }
    @Override
    public Mono<UserPromoterEntity> findByEmail(String email) {
        return userPromoterRepository.findByEmail(email);
    }

    private String generatePromoterRegistrationEmailBody(UserPromoterEntity userClient, String tempPassword) {
        String body = "<html>\n" +
                "<head>\n" +
                "    <title>Bienvenido</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            color: black;\n" +
                "            background-color: white; /* Color de fondo */\n" +
                "        }\n" +
                "        .header {\n" +
                "            width: 100%;\n" +
                "            position: relative;\n" +
                "            background-color: white; /* Color de fondo del encabezado */\n" +
                "            padding: 20px 0; /* Espaciado superior e inferior para el encabezado */\n" +
                "        }\n" +
                "        .logos-right {\n" +
                "            position: absolute;\n" +
                "            top: 10px;\n" +
                "            right: 10px;\n" +
                "            display: flex;\n" +
                "            gap: 5px;\n" +
                "        }\n" +
                "        .logos-right img {\n" +
                "            width: 30px;\n" +
                "            height: 30px;\n" +
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
                "            background-color: #f4f4f4; /* Fondo blanco del contenido */\n" +
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
                "            background-color: #f4f4f4; /* Fondo blanco del contenido */\n" +
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
                "        <img class=\"logo-left\" src=\"https://bit.ly/4d7FuGX\" alt=\"Logo Izquierda\">\n" +
                "    </div>\n" +
                "\n" +
                "    <!-- Imagen de banner -->\n" +
                "    <img class=\"banner\" src=\"https://bit.ly/46vO7sq\" alt=\"Bienvenido\">\n" +
                "\n" +
                "    <!-- Contenedor blanco con el contenido del mensaje -->\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"content\">\n" +
                "            <h1>Registro exitoso como Promotor!</h1>\n" +
                "            <p>Hola " + userClient.getFirstName() + ",</p>\n" +
                "            <p>Gracias por registrarte a la plataforma. Su correo registrado es " + userClient.getEmail() + ".</p>\n" +
                "            <p>Y su contraseña es " + tempPassword + "</p>\n" +
                "            <p>Si tienes alguna consulta, envianos tu consulta por correo. Tener en cuenta si creo una cuenta google, omitir la contraseña.</p>\n" +
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

    public Mono<TokenResult> checkAndGenerateToken(String email) {
        return userPromoterRepository.findByEmailOrGoogleIdOrGoogleEmail(email, email, email)
                .filter(user -> "1".equals(user.getGoogleAuth()))
                .flatMap(user -> Mono.just(new TokenResult(jwtProvider.generateTokenPromoter(user), "tokenizado")))
                .switchIfEmpty(Mono.just(new TokenResult("", "sin token")));
    }

    @Override
    public Mono<PromotorDataDTO> findPromotorDTOById(Integer id) {
        return userPromoterRepository.findById(id)
                .flatMap(promotor -> new PromotorDataDTO().convertTo(promotor));
    }

    @Override
    public Mono<String> loginWithGoogle(String googleId) {
        return userPromoterRepository.findByGoogleId(googleId)
                .map(jwtProvider::generateTokenPromoter)
                .switchIfEmpty(Mono.error(new RuntimeException("Promotor no encontrado")));
    }

    @Override
    public Mono<UserPromoterPageDto> getAllPromoters(Integer indice, String status, String fecha, String filter) {
        UserPromoterPageDto resp = new UserPromoterPageDto();
        return userPromoterRepository.countPromoterAll(status, fecha, filter).flatMap(totalPromoters -> {
            resp.setTotalPromoters(totalPromoters);
            return userPromoterRepository.getAllPromoter(indice, status, fecha, filter).collectList().map(userPromoterDtos -> {
                resp.setUserPromoter(userPromoterDtos);
                return resp;
            });
        });
    }

    @Override
    public Mono<Void> savePromoter(UserPromoterDto entity) {
        return userPromoterRepository.findById(entity.getUserPromoterId()).flatMap(userPromoterEntity -> {
            userPromoterEntity.setFirstName(entity.getFirstName());
            userPromoterEntity.setLastName(entity.getLastName());
            userPromoterEntity.setEmail(entity.getEmail());
            userPromoterEntity.setDocumentNumber(entity.getDocumentNumber());
            userPromoterEntity.setGenderId(entity.getGenderId());
            userPromoterEntity.setAddress(entity.getAddress());
            return userPromoterRepository.save(userPromoterEntity).flatMap(resp -> {
                return Mono.empty();
            });
        });
    }

    @Override
    public Mono<List<String>> getStatus() {
        return userPromoterRepository.getStatus().collectList();
    }

}
