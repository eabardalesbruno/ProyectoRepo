package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long> {
    PasswordResetTokenEntity findByUserIdAndToken(Integer userId, String token);
    PasswordResetTokenEntity findByUserId(Integer userId);

    @Query(value = "INSERT INTO passwordresettoken (userclientid, token, passwordstate, expirydate) VALUES (:userId, :token, 0, :expiryDate)", nativeQuery = true)
    void insertResetToken(@Param("userClientId") Integer userClientId, @Param("token") String token, @Param("expiryDate") Timestamp expiryDate);
}