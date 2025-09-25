package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.UserClientDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.UserPromoterDto;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Domain.entities.UserPromoterEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserPromoterRepository extends R2dbcRepository<UserPromoterEntity, Integer> {
    Mono<UserPromoterEntity> findByUsernameOrEmail(String username, String email);
    Mono<UserPromoterEntity> findByUsernameOrEmailOrDocumentNumber(String username, String email, String document);
    Mono<UserPromoterEntity> findById(Integer id);

    Mono<UserPromoterEntity> findByGoogleId(String googleId);

    @Query("SELECT * FROM userpromoter WHERE email = :email OR googleid = :googleId OR googleemail = :googleEmail")
    Mono<UserPromoterEntity> findByEmailOrGoogleIdOrGoogleEmail(String email, String googleId, String googleEmail);

    Mono<UserPromoterEntity> findByEmail(String email);
    Mono<UserPromoterEntity> findByDocumentNumber(String documentNumber);

    @Query("""
        SELECT COUNT(*)
        FROM (SELECT up.*,
        (SELECT g.genderdesc FROM gender g WHERE g.genderId = up.genderid) genderdesc
        FROM userpromoter up
        ORDER BY up.createdat) t
        WHERE (:status IS NULL OR t.status = :status)
                AND (:fecha IS NULL OR TO_CHAR(t.createdat, 'YYYY/MM/DD') = :fecha)
        		AND (:filter IS NULL OR (UPPER(t.firstName) LIKE UPPER(CONCAT('%', :filter, '%')) OR UPPER(t.lastName) LIKE UPPER(CONCAT('%', :filter, '%')) OR UPPER(t.documentnumber) LIKE UPPER(CONCAT('%', :filter, '%')) OR UPPER(t.email) LIKE UPPER(CONCAT('%', :filter, '%'))))
    """)
    Mono<Long> countPromoterAll(String status, String fecha, String filter);

    @Query("""
        SELECT *
        FROM (SELECT up.*,
        (SELECT g.genderdesc FROM gender g WHERE g.genderId = up.genderid) genderdesc
        FROM userpromoter up
        ORDER BY up.createdat) t
        WHERE (:status IS NULL OR t.status = :status)
                AND (:fecha IS NULL OR TO_CHAR(t.createdat, 'YYYY/MM/DD') = :fecha)
        		AND (:filter IS NULL OR (UPPER(t.firstName) LIKE UPPER(CONCAT('%', :filter, '%')) OR UPPER(t.lastName) LIKE UPPER(CONCAT('%', :filter, '%')) OR UPPER(t.documentnumber) LIKE UPPER(CONCAT('%', :filter, '%')) OR UPPER(t.email) LIKE UPPER(CONCAT('%', :filter, '%'))))
        OFFSET :indice*10 LIMIT 10
    """)
    Flux<UserPromoterDto> getAllPromoter(Integer indice, String status, String fecha, String filter);

    @Query("""
            SELECT COUNT(*)
            FROM (SELECT up.*,
            (SELECT g.genderdesc FROM gender g WHERE g.genderId = up.genderid) genderdesc
            FROM userpromoter up
            ORDER BY up.createdat) t
            WHERE (:status IS NULL OR t.status = :status)
                    AND (:filter IS NULL OR (UPPER(t.firstName) LIKE UPPER(CONCAT('%', :filter, '%')) OR 
                        UPPER(t.lastName) LIKE UPPER(CONCAT('%', :filter, '%')) OR 
                        UPPER(t.documentnumber) LIKE UPPER(CONCAT('%', :filter, '%')) OR 
                        UPPER(t.email) LIKE UPPER(CONCAT('%', :filter, '%'))))
                    AND (:fechaInicio IS NULL OR t.createdat >= TO_DATE(:fechaInicio, 'YYYY/MM/DD'))
                    AND (:fechaFin IS NULL OR t.createdat <= TO_DATE(COALESCE(:fechaFin, 
                        TO_CHAR(CURRENT_DATE, 'YYYY/MM/DD')), 'YYYY/MM/DD'))
            """)
    Mono<Long> countPromoterAllWithParams(String status, String fechaInicio,String fechaFin, String filter);

    @Query("""
            SELECT *
            FROM (SELECT up.*,
            (SELECT g.genderdesc FROM gender g WHERE g.genderId = up.genderid) genderdesc
            FROM userpromoter up
            ORDER BY up.createdat) t
            WHERE (:status IS NULL OR t.status = :status)
                    AND (:filter IS NULL OR (UPPER(t.firstName) LIKE UPPER(CONCAT('%', :filter, '%')) OR 
                        UPPER(t.lastName) LIKE UPPER(CONCAT('%', :filter, '%')) OR 
                        UPPER(t.documentnumber) LIKE UPPER(CONCAT('%', :filter, '%')) OR 
                        UPPER(t.email) LIKE UPPER(CONCAT('%', :filter, '%'))))
                    AND (:fechaInicio IS NULL OR t.createdat >= TO_DATE(:fechaInicio, 'YYYY/MM/DD'))
                    AND (:fechaFin IS NULL OR t.createdat <= TO_DATE(COALESCE(:fechaFin, 
                        TO_CHAR(CURRENT_DATE, 'YYYY/MM/DD')), 'YYYY/MM/DD'))
            OFFSET :indice*10 LIMIT 10
            """)
    Flux<UserPromoterDto> getAllPromoterWithParams(Integer indice, String status, String fechaInicio,String fechaFin,
                                                   String filter);

    @Query("SELECT DISTINCT status FROM userpromoter")
    Flux<String> getStatus();

    @Query("""
            update userpromoter set password = :passwordEnconded
                        where userpromoterid = :userpromoterid;
                """)
    Mono<Void> updatePassword(Integer userpromoterid, String passwordEnconded);
}


