package com.proriberaapp.ribera.services.client;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.proriberaapp.ribera.Domain.entities.PasswordResetCodeEntity;
import com.proriberaapp.ribera.Infraestructure.exception.PasswordResetCodeExpiretTimeException;
import com.proriberaapp.ribera.Infraestructure.exception.PasswordResetCodeNotFoundException;
import com.proriberaapp.ribera.Infraestructure.exception.PasswordResetCodeUsedException;
import com.proriberaapp.ribera.Infraestructure.repository.PasswordResetCodeRepository;
import com.proriberaapp.ribera.utils.CodeGenerator;

import reactor.core.publisher.Mono;

@Service
public class PasswordResetCodeService {

    @Autowired
    private PasswordResetCodeRepository passwordResetCodeRepository;

    @Value("${app.password-reset-code-expiration-time}")
    private Long expirationTime;

    public Mono<String> generateResetCode(String userType, Integer userId) {
        String code = CodeGenerator.generateCode();
        LocalDateTime expirationTimeCalc = LocalDateTime.now().plus(expirationTime, ChronoUnit.MILLIS);
        PasswordResetCodeEntity entity = PasswordResetCodeEntity.builder()
                .reset_code(code)
                .user_type(userType)
                .user_id(userId)
                .used(false)
                .expiresAt(
                        expirationTimeCalc)
                .build();
        return passwordResetCodeRepository.save(entity).map(PasswordResetCodeEntity::getReset_code);
    }

    public Mono<PasswordResetCodeEntity> verfiedCode(String code) {
        Mono<PasswordResetCodeEntity> codeEntity = passwordResetCodeRepository.findByCode(code);
        return codeEntity.flatMap(entity -> {
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime expiration = entity.getExpiresAt();
            if (entity.getUsed()) {
                return Mono.error(new PasswordResetCodeUsedException(code));
            }
            if (currentTime.isAfter(expiration)) {
                return Mono.error(new PasswordResetCodeExpiretTimeException(code));
            }
            return Mono.just(entity);
        }).switchIfEmpty(Mono.error(new PasswordResetCodeNotFoundException(code)));
    }
    public Mono<Void>   useCode(String code){
        return passwordResetCodeRepository.usedCode(code);
    }
}
