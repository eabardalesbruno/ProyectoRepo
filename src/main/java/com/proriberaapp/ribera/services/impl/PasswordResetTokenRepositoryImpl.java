package com.proriberaapp.ribera.services.impl;

import com.proriberaapp.ribera.Domain.entities.PasswordResetTokenEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PasswordResetTokenRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class PasswordResetTokenRepositoryImpl {
/*
    private final Map<Long, PasswordResetTokenEntity> tokenMap = new HashMap<>();

    @Override
    public PasswordResetTokenEntity findByUserIdAndToken(Integer userId, String token) {
        for (PasswordResetTokenEntity resetToken : tokenMap.values()) {
            if (resetToken.getUserClientId().equals(userId) && resetToken.getToken().equals(token)) {
                return resetToken;
            }
        }
        return null;
    }

    @Override
    public PasswordResetTokenEntity findByUserId(Integer userId) {
        for (PasswordResetTokenEntity resetToken : tokenMap.values()) {
            if (resetToken.getUserClientId().equals(userId)) {
                return resetToken;
            }
        }
        return null;
    }

 */
/*
    @Override
    public void insertResetToken(Integer userId, String token, Timestamp expiryDate) {
        PasswordResetTokenEntity resetToken = new PasswordResetTokenEntity();
        resetToken.setUserClientId(userId);
        resetToken.setToken(token);
        resetToken.setExpiryDate(expiryDate);
        resetToken.setPasswordState(0);
        save(resetToken);
    }

 */
/*
    @Override
    public <S extends PasswordResetTokenEntity> S save(S entity) {
        tokenMap.put(Long.valueOf(entity.getUserClientId()), entity);
        return entity;
    }

    @Override
    public <S extends PasswordResetTokenEntity> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<PasswordResetTokenEntity> findById(Long id) {
        PasswordResetTokenEntity entity = tokenMap.get(id);
        return Optional.ofNullable(entity);
    }

    @Override
    public boolean existsById(Long id) {
        return tokenMap.containsKey(id);
    }

    @Override
    public Iterable<PasswordResetTokenEntity> findAll() {
        return tokenMap.values();
    }

    @Override
    public Iterable<PasswordResetTokenEntity> findAllById(Iterable<Long> ids) {
        return null;
    }

    @Override
    public long count() {
        return tokenMap.size();
    }

    @Override
    public void deleteById(Long id) {
        tokenMap.remove(id);
    }

    @Override
    public void delete(PasswordResetTokenEntity entity) {
        tokenMap.remove(entity.getUserClientId());
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        for (Long id : ids) {
            tokenMap.remove(id);
        }
    }

    @Override
    public void deleteAll(Iterable<? extends PasswordResetTokenEntity> entities) {
        for (PasswordResetTokenEntity entity : entities) {
            tokenMap.remove(entity.getUserClientId());
        }
    }

    @Override
    public void deleteAll() {
        tokenMap.clear();
    }
    */
}