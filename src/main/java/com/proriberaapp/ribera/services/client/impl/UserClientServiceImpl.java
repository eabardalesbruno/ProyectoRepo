package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.TotalUsersDTO;
import com.proriberaapp.ribera.Api.controllers.admin.dto.UserClientDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.ContactInfo;
import com.proriberaapp.ribera.Api.controllers.client.dto.EventContactInfo;
import com.proriberaapp.ribera.Api.controllers.client.dto.TokenResult;
import com.proriberaapp.ribera.Api.controllers.client.dto.UserDataDTO;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.dto.CompanyDataDto;
import com.proriberaapp.ribera.Domain.dto.DiscountDto;
import com.proriberaapp.ribera.Domain.dto.UserNameAndDiscountDto;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.services.client.BookingService;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.services.client.PasswordResetCodeService;
import com.proriberaapp.ribera.services.client.UserApiClient;
import com.proriberaapp.ribera.services.client.UserClientService;
import com.proriberaapp.ribera.services.client.VerifiedDiscountService;
import com.proriberaapp.ribera.utils.emails.BaseEmailReserve;
import com.proriberaapp.ribera.utils.emails.EmailTemplateCodeRecoveryPassword;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

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

    @Autowired
    private VerifiedDiscountService verifiedDiscountService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private PasswordResetCodeService passwordResetCodeService;
    private final WalletServiceImpl walletServiceImpl;
    @Value("${url.api.ruc}")
    private String rucApi;
    @Value("${url.api.ruc.token}")
    private String rucApiToken;

    /*
     * @Override
     * public Mono<UserClientEntity> registerUser(UserClientEntity userClient) {
     * return userClientRepository.findByEmail(userClient.getEmail())
     * .flatMap(existingUser -> Mono.error(new
     * RuntimeException("El correo electrónico ya está registrado")))
     * .then(Mono.defer(() -> {
     * validatePassword(userClient.getPassword());
     * return
     * userClientRepository.findByDocumentNumber(userClient.getDocumentNumber())
     * .flatMap(existingUser -> Mono.error(new
     * RuntimeException("El número de documento ya está registrado")))
     * .then(Mono.just(userClient));
     * }))
     * .map(userToSave -> {
     * userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword())); //
     * Cifra la contraseña
     * return userToSave;
     * })
     * .flatMap(userClientRepository::save);
     * }
     * 
     */

    /*
     * 01072024 REGISTRO DE USUARIO SIN EMAIL
     * public Mono<UserClientEntity> registerUser(UserClientEntity userClient) {
     * // Obtener la fecha y hora actual
     * long currentTimeMillis = System.currentTimeMillis();
     * Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
     * // Establecer la fecha y hora de creación
     * userClient.setCreatedat(currentTimestamp);
     * return userClientRepository.findByEmail(userClient.getEmail())
     * .flatMap(existingUser -> {
     * if ("1".equals(userClient.getGoogleAuth())) {
     * if (existingUser.getPassword() != null) {
     * return Mono.error(new
     * RuntimeException("El correo electrónico ya está registrado con una contraseña"
     * ));
     * } else {
     * return Mono.just(existingUser);
     * }
     * } else {
     * if (existingUser.getPassword() != null) {
     * return Mono.error(new
     * RuntimeException("El correo electrónico ya está registrado con una contraseña"
     * ));
     * } else {
     * return Mono.just(existingUser);
     * }
     * }
     * })
     * .switchIfEmpty(Mono.defer(() -> {
     * if (!"1".equals(userClient.getGoogleAuth())) {
     * validatePassword(userClient.getPassword());
     * }
     * return
     * userClientRepository.findByDocumentNumber(userClient.getDocumentNumber())
     * .flatMap(existingUser -> Mono.error(new
     * RuntimeException("El número de documento ya está registrado")))
     * .then(Mono.just(userClient));
     * }))
     * .map(userToSave -> {
     * if (!"1".equals(userToSave.getGoogleAuth())) {
     * log.info("Cifrar contraseña");
     * userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
     * }
     * return userToSave;
     * })
     * .flatMap(userClientRepository::save);
     * }
     * 
     */
    /*
     * 02072024
     * public Mono<UserClientEntity> registerUser(UserClientEntity userClient) {
     * long currentTimeMillis = System.currentTimeMillis();
     * Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
     * userClient.setCreatedat(currentTimestamp);
     * 
     * return userClientRepository.findByEmail(userClient.getEmail())
     * .flatMap(existingUser -> {
     * if ("1".equals(userClient.getGoogleAuth())) {
     * if (existingUser.getPassword() != null) {
     * return Mono.error(new
     * RuntimeException("El correo electrónico ya está registrado con una contraseña"
     * ));
     * } else {
     * return Mono.just(existingUser);
     * }
     * } else {
     * if (existingUser.getPassword() != null) {
     * return Mono.error(new
     * RuntimeException("El correo electrónico ya está registrado con una contraseña"
     * ));
     * } else {
     * return Mono.just(existingUser);
     * }
     * }
     * })
     * .switchIfEmpty(Mono.defer(() -> {
     * if (!"1".equals(userClient.getGoogleAuth())) {
     * validatePassword(userClient.getPassword());
     * }
     * return
     * userClientRepository.findByDocumentNumber(userClient.getDocumentNumber())
     * .flatMap(existingUser -> Mono.error(new
     * RuntimeException("El número de documento ya está registrado")))
     * .then(Mono.just(userClient));
     * }))
     * .map(userToSave -> {
     * if (!"2".equals(userToSave.getGoogleAuth())) {
     * userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
     * }
     * return userToSave;
     * })
     * .flatMap(userClientRepository::save)
     * .flatMap(savedUser -> {
     * // Generar el cuerpo del correo de confirmación
     * String emailBody = generateUserRegistrationEmailBody(savedUser);
     * // Enviar el correo de confirmación
     * return emailService.sendEmail(savedUser.getEmail(),
     * "Confirmación de Registro", emailBody)
     * .thenReturn(savedUser);
     * });
     * }
     * 
     */

    public Mono<UserClientEntity> registerUser(UserClientEntity userClient, String randomPassword) {
        long currentTimeMillis = System.currentTimeMillis();
        Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
        userClient.setCreatedat(currentTimestamp);
        userClient.setPassword(randomPassword);
        return userClientRepository.findByEmail(userClient.getEmail())
                .flatMap(existingUser -> {
                    if ("1".equals(userClient.getGoogleAuth())) {
                        if (existingUser.getPassword() != null) {
                            return Mono.error(new RuntimeException(
                                    "El correo electrónico ya está registrado con una contraseña"));
                        } else {
                            return Mono.just(existingUser);
                        }
                    } else {
                        if (existingUser.getPassword() != null) {
                            return Mono.error(new RuntimeException(
                                    "El correo electrónico ya está registrado con una contraseña"));
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
                            .flatMap(existingUser -> Mono
                                    .error(new RuntimeException("El número de documento ya está registrado")))
                            .then(Mono.just(userClient));
                }))
                .map(userToSave -> {
                    if ("1".equals(userToSave.getGoogleAuth())
                            && (userToSave.getPassword() == null || userToSave.getPassword().isEmpty())) {
                        // Si googleAuth es "1" y no hay contraseña, establecer la contraseña como null
                        // para evitar el cifrado vacío
                        userToSave.setPassword(null);
                    } else if (!"1".equals(userToSave.getGoogleAuth())) {
                        // Cifrar la contraseña si no es una autenticación de Google
                        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
                    }
                    return userToSave;
                })
                .flatMap(userClientRepository::save)
                .flatMap(savedUser -> {
                    // Creacion de la wallet con el número de la wallet único
                    return walletServiceImpl.createWalletUsuario(savedUser.getUserClientId(), 1) // Creamos la wallet
                            .flatMap(wallet -> {
                                // Asociamos el walletId al usuario
                                savedUser.setWalletId(wallet.getWalletId()); // Establecemos el walletId en el usuario
                                return userClientRepository.save(savedUser)
                                        .flatMap(updatedUser -> {
                                            String emailBody = generateUserRegistrationEmailBody(updatedUser,
                                                    randomPassword);
                                            return emailService
                                                    .sendEmail(updatedUser.getEmail(), "Confirmación de Registro",
                                                            emailBody)
                                                    .thenReturn(updatedUser);
                                        });
                            });
                });
    }

    private void validatePassword(String password) {
        if (!password.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$")) {
            throw new RuntimeException(
                    "La contraseña debe contener al menos una letra y un número, y tener una longitud mínima de 8 caracteres");
        }
    }

    private String generateUserRegistrationEmailBody(UserClientEntity userClient, String tempPassword) {
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
                "            <h1>Registro exitoso!</h1>\n" +
                "            <p>Hola " + userClient.getFirstName() + ",</p>\n" +
                "            <p>Gracias por registrarte a la plataforma. Su correo registrado es "
                + userClient.getEmail() + ".</p>\n" +
                "            <p>Y su contraseña es " + tempPassword + "</p>\n" +
                "            <p>Si tienes alguna consulta, envianos tu consulta por correo. Tener en cuenta si creo una cuenta google, omitir la contraseña.</p>\n"
                +
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
    public Flux<UserClientEntity> findByUserPromotorId(Integer userPromotorId) {
        return userClientRepository.findByUserPromotorId(userPromotorId);
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
                            .flatMap(existingUser -> Mono
                                    .error(new RuntimeException("El número de documento ya está registrado")))
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
                        // Se verifica si al momento de loguearse el usuario tiene una wallet creada y
                        // si no tiene se le crea una
                        if (user.getWalletId() == null) {
                            return walletServiceImpl.createWalletUsuario(user.getUserClientId(), 1)
                                    .flatMap(wallet -> {
                                        user.setWalletId(wallet.getWalletId());
                                        return userClientRepository.save(user)
                                                .thenReturn(jwtUtil.generateToken(user));
                                    });
                        } else {
                            return Mono.just(jwtUtil.generateToken(user));
                        }
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

    /*
     * public Mono<String> checkAndGenerateToken(String email) {
     * return userClientRepository.findByEmailOrGoogleIdOrGoogleEmail(email, email,
     * email)
     * .filter(user -> "1".equals(user.getGoogleAuth()))
     * .flatMap(user -> Mono.just(jwtUtil.generateToken(user)))
     * .switchIfEmpty(Mono.error(new RuntimeException("")));
     * }
     */

    public Mono<TokenResult> checkAndGenerateToken(String email) {
        return userClientRepository.findByEmailOrGoogleIdOrGoogleEmail(email, email, email)
                .filter(user -> "1".equals(user.getGoogleAuth()))
                .flatMap(user -> Mono.just(new TokenResult(jwtUtil.generateToken(user), "tokenizado")))
                .switchIfEmpty(Mono.just(new TokenResult("", "sin token")));
    }

    @Override
    public Mono<Void> sendContactInfo(ContactInfo contactInfo) {
        String emailBody = getContactInfo(contactInfo);
        return emailService.sendEmail("informesyreservasribera@inresorts.club", contactInfo.getSubject(), emailBody)
                .then(emailService.sendEmail(contactInfo.getEmail(), "Confirmación de envío", emailBody));
    }

    @Override
    public Mono<Void> sendEventContactInfo(EventContactInfo eventContactInfo) {
        String emailBody = getEventContactInfo(eventContactInfo);
        return emailService.sendEmail("informesyreservasribera@inresorts.club", "Eventos", emailBody)
                .then(emailService.sendEmail(eventContactInfo.getEmail(), "Confirmación de envío", emailBody));
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

    public static String getEventContactInfo(EventContactInfo eventContactInfo) {
        return "<!DOCTYPE html>" +
                "<html lang='es'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <style>" +
                "        body {" +
                "            font-family: Arial, sans-serif;" +
                "            background-color: #f4f4f4;" +
                "            color: #333;" +
                "            margin: 0;" +
                "            padding: 20px;" +
                "        }" +
                "        .container {" +
                "            background-color: #ffffff;" +
                "            padding: 20px;" +
                "            margin: auto;" +
                "            max-width: 600px;" +
                "            border-radius: 8px;" +
                "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);" +
                "        }" +
                "        h2 {" +
                "            color: #333;" +
                "            font-size: 24px;" +
                "            margin-bottom: 10px;" +
                "        }" +
                "        p {" +
                "            font-size: 14px;" +
                "            color: #666;" +
                "            margin-bottom: 20px;" +
                "        }" +
                "        .data-group {" +
                "            margin-bottom: 15px;" +
                "        }" +
                "        .data-group label {" +
                "            font-weight: bold;" +
                "            margin-bottom: 5px;" +
                "            color: #555;" +
                "        }" +
                "        .data-group span {" +
                "            display: block;" +
                "            padding: 10px;" +
                "            font-size: 14px;" +
                "            background-color: #f9f9f9;" +
                "            border: 1px solid #ddd;" +
                "            border-radius: 4px;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <img style='width:100%' src='http://imgfz.com/i/hGIOrlW.png'>" +
                "    <div class='container'>" +
                "        <h2>Notificacion:</h2>" +
                "        <h3>Se ha brindado los datos de contacto.</h3>" +
                "        <div>" +
                "            <div class='data-group'>" +
                "                <label for='usuario'>Nombres</label>" +
                "                <span id='usuario'>" + eventContactInfo.getName() + "</span>" +
                "            </div>" +
                "            <div class='data-group'>" +
                "                <label for='apellido'>Apellidos</label>" +
                "                <span id='apellido'>" + eventContactInfo.getLastName() + "</span>" +
                "            </div>" +
                "        </div>" +
                "        <div>" +
                "            <div class='data-group'>" +
                "                <label for='phone'>Nro. Celular</label>" +
                "                <span>+51 " + eventContactInfo.getCellphone() + "</span>" +
                "            </div>" +
                "            <div class='data-group'>" +
                "                <label for='email'>Correo electronico</label>" +
                "                <span id='email'>" + eventContactInfo.getEmail() + "</span>" +
                "            </div>" +
                "        </div>" +
                "        <div>" +
                "            <div class='data-group'>" +
                "                <label for='entrada'>Fecha de entrada</label>" +
                "                <span id='entrada'>" + eventContactInfo.getStartDate() + "</span>" +
                "            </div>" +
                "            <div class='data-group'>" +
                "                <label for='salida'>Fecha de salida</label>" +
                "                <span id='salida'>" + eventContactInfo.getEndDate() + "</span>" +
                "            </div>" +
                "        </div>" +
                "        <div>" +
                "            <div class='data-group'>" +
                "                <label for='adultos'>Numero de adultos</label>" +
                "                <span id='adultos'>" + eventContactInfo.getNumberAdults() + "</span>" +
                "            </div>" +
                "            <div class='data-group'>" +
                "                <label for='niños'>Numero de infantes</label>" +
                "                <span id='niños'>" + eventContactInfo.getNumberChildren() + "</span>" +
                "            </div>" +
                "        </div>" +
                "        <div>" +
                "            <div class='data-group'>" +
                "                <label for='mensaje'>Mensaje</label>" +
                "                <span>" + eventContactInfo.getMessage() + "</span>" +
                "            </div>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    public static String getContactInfo(ContactInfo contactInfo) {
        return "<!DOCTYPE html>" +
                "<html lang='es'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <style>" +
                "        body {" +
                "            font-family: Arial, sans-serif;" +
                "            background-color: #f4f4f4;" +
                "            color: #333;" +
                "            margin: 0;" +
                "            padding: 20px;" +
                "        }" +
                "        .container {" +
                "            background-color: #ffffff;" +
                "            padding: 20px;" +
                "            margin: auto;" +
                "            max-width: 600px;" +
                "            border-radius: 8px;" +
                "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);" +
                "        }" +
                "        h2 {" +
                "            color: #333;" +
                "            font-size: 24px;" +
                "            margin-bottom: 10px;" +
                "        }" +
                "        p {" +
                "            font-size: 14px;" +
                "            color: #666;" +
                "            margin-bottom: 20px;" +
                "        }" +
                "        .data-group {" +
                "            margin-bottom: 15px;" +
                "        }" +
                "        .data-group label {" +
                "            font-weight: bold;" +
                "            margin-bottom: 5px;" +
                "            color: #555;" +
                "        }" +
                "        .data-group span {" +
                "            display: block;" +
                "            padding: 10px;" +
                "            font-size: 14px;" +
                "            background-color: #f9f9f9;" +
                "            border: 1px solid #ddd;" +
                "            border-radius: 4px;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <img style='width:100%' src='http://imgfz.com/i/hGIOrlW.png'>" +
                "    <div class='container'>" +
                "        <h2>Notificacion:</h2>" +
                "        <h3>Se ha brindado los datos de contacto.</h3>" +
                "        <div>" +
                "            <div class='data-group'>" +
                "                <label for='usuario'>Nombres</label>" +
                "                <span id='usuario'>" + contactInfo.getName() + "</span>" +
                "            </div>" +
                "            <div class='data-group'>" +
                "                <label for='apellido'>Apellidos</label>" +
                "                <span id='apellido'>" + contactInfo.getLastName() + "</span>" +
                "            </div>" +
                "        </div>" +
                "        <div>" +
                "            <div class='data-group'>" +
                "                <label for='phone'>Nro. Celular</label>" +
                "                <span>+51 " + contactInfo.getCellphone() + "</span>" +
                "            </div>" +
                "            <div class='data-group'>" +
                "                <label for='email'>Correo electronico</label>" +
                "                <span id='email'>" + contactInfo.getEmail() + "</span>" +
                "            </div>" +
                "        </div>" +
                "        <div>" +
                "            <div class='data-group'>" +
                "                <label for='entrada'>Numero de documento</label>" +
                "                <span id='entrada'>" + contactInfo.getDocumentNumber() + "</span>" +
                "            </div>" +
                "        </div>" +
                "        <div>" +
                "            <div class='data-group'>" +
                "                <label for='mensaje'>Mensaje</label>" +
                "                <span>" + contactInfo.getMessage() + "</span>" +
                "            </div>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    @Override
    public Mono<UserNameAndDiscountDto> getPercentageDiscount(Integer userId, Integer bookingId) {

        return Mono
                .zip(this.bookingService.findById(bookingId),
                        this.verifiedDiscountService.verifiedPercentajeDiscount(userId),
                        this.bookingService.getTotalFeedingAmount(bookingId).switchIfEmpty(Mono.just(0F)))
                .flatMap(data -> {
                    BookingEntity booking = data.getT1();
                    UserNameAndDiscountDto discount = data.getT2();
                    float totalPercentageDiscountAccommodation = discount.getDiscounts().stream()
                            .filter(d -> d.isApplyToReservation()).map(DiscountDto::getPercentage)
                            .reduce(0F, Float::sum);
                    float totalPercentageDiscountFood = discount.getDiscounts().stream()
                            .filter(d -> d.isApplyToFood()).map(DiscountDto::getPercentage)
                            .reduce(0F, Float::sum);
                    Float totalAmountFeeding = data.getT3();
                    float totalAccommodation = booking.getCostFinal().subtract(new BigDecimal(totalAmountFeeding))
                            .floatValue();
                    float totalAccommodationWithDiscount = totalAccommodation * totalPercentageDiscountAccommodation
                            / 100;
                    float totalFoodWithDiscount = totalAmountFeeding * totalPercentageDiscountFood / 100;
                    discount.setTotalDiscountAccommodation(totalAccommodationWithDiscount);
                    discount.setTotalPercentageDiscountAccommodation(totalPercentageDiscountAccommodation);
                    discount.setTotalDiscountFood(totalFoodWithDiscount);
                    discount.setTotalPercentageDiscountFood(totalPercentageDiscountFood);
                    discount.setTotalAmount(
                            booking.getCostFinal().subtract(
                                    new BigDecimal(totalAccommodationWithDiscount + totalFoodWithDiscount))
                                    .floatValue());

                    return Mono.just(discount);
                });
    }

    @Override
    public Mono<CompanyDataDto> loadDataRuc(String ruc) {
        WebClient client = WebClient.create(this.rucApi);
        return client.get()
                .uri(uriBuilder -> uriBuilder.queryParam("numero",
                        ruc).build())
                .header("authorization", "Bearer " + this.rucApiToken)
                .retrieve()
                .bodyToMono(CompanyDataDto.class);
    }

    @Override
    public Mono<TotalUsersDTO> countUsers(Integer month) {
        TotalUsersDTO resp = new TotalUsersDTO();
        return userClientRepository.countUsers(month).flatMap(totalUsers -> {
            resp.setTotalMonth(totalUsers);
            return userClientRepository.countLastUsers(month).flatMap(totalUsersLast -> {
                resp.setTotalLastMonth(totalUsersLast);
                return Mono.just(resp);
            });
        });
    }

    @Override
    public Mono<Void> updateUser(UserClientDto userClientEntity) {
        Mono<Void> updateUserM = Mono.defer(() -> {
            return userClientRepository.updateBasicData(userClientEntity);
        });

        return userClientRepository.findByEmailOrDocumentNumberAndIgnoreId(userClientEntity.getEmail(),
                userClientEntity.getDocumentNumber(), userClientEntity.getUserClientId())
                .hasElement().flatMap(existUser -> {
                    if (existUser) {
                        return Mono.error(new RuntimeException(
                                "El correo electrónico o el número de documento ya está registrado"));
                    }
                    return updateUserM;
                }).flatMap(d -> updateUserM)
                .then();

    }

    @Override
    public Mono<Void> sendCodeRecoveryPassword(String email) {
        return userClientRepository.findByEmail(email)
                .switchIfEmpty(Mono.empty())
                .flatMap(user -> {
                    return this.passwordResetCodeService.generateResetCode("client", user.getUserClientId())
                            .flatMap(code -> {
                                BaseEmailReserve emailTemplate = new BaseEmailReserve();
                                EmailTemplateCodeRecoveryPassword emailTemplateRecovery = new EmailTemplateCodeRecoveryPassword(
                                        code,
                                        user.getFirstName());
                                emailTemplate.addEmailHandler(emailTemplateRecovery);
                                String bodyEmail = emailTemplate.execute();
                                return this.emailService.sendEmail(email, "Recuperar contraseña", bodyEmail);
                            });
                });
    }

    @Override
    public Mono<Void> validateCode(String code) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateCode'");
    }

}
