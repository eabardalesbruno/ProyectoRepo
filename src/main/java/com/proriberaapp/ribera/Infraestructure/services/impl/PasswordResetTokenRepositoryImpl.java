package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.PasswordResetTokenEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class PasswordResetTokenRepositoryImpl implements PasswordResetTokenRepository {

    private final Map<Long, PasswordResetTokenEntity> tokenMap = new HashMap<>();

    @Override
    public PasswordResetTokenEntity findByUserAndToken(Integer user, String token) {
        for (PasswordResetTokenEntity resetToken : tokenMap.values()) {
            if (resetToken.getUserid().equals(user) && resetToken.getToken().equals(token)) {
                return resetToken;
            }
        }
        return null;
    }

    @Override
    public <S extends PasswordResetTokenEntity> S save(S entity) {
        tokenMap.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public <S extends PasswordResetTokenEntity> Iterable<S> saveAll(Iterable<S> entities) {
        for (S entity : entities) {
            tokenMap.put(entity.getId(), entity);
        }
        return entities;
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
        tokenMap.remove(entity.getId());
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
            tokenMap.remove(entity.getId());
        }
    }

    @Override
    public void deleteAll() {
        tokenMap.clear();
    }
}