package com.proriberaapp.ribera.services.impl;

import com.proriberaapp.ribera.Domain.entities.TokenPointsTransaction;
import com.proriberaapp.ribera.Infraestructure.repository.TokenPointsTransactionRepository;
import com.proriberaapp.ribera.services.TokenPointsTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class TokenPointsTransactionServiceImpl implements TokenPointsTransactionService {

    private final TokenPointsTransactionRepository tokenPointsTransactionRepository;

    @Autowired
    public TokenPointsTransactionServiceImpl(TokenPointsTransactionRepository tokenPointsTransactionRepository) {
        this.tokenPointsTransactionRepository = tokenPointsTransactionRepository;
    }

    @Override
    public Mono<TokenPointsTransaction> createToken(Integer partnerPointId, Integer bookingId) {
        TokenPointsTransaction tokenPointsTransaction = TokenPointsTransaction.builder()
                .codigoToken(UUID.randomUUID().toString())
                .dateCreated(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)))
                .expirationDate(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).plusHours(24)))
                .partnerPointId(partnerPointId)
                .bookingId(bookingId)
                .build();

        return tokenPointsTransactionRepository.save(tokenPointsTransaction);
    }
}
