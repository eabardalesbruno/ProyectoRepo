package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.UserClientDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.UserDataDTO;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserClientRepository extends R2dbcRepository<UserClientEntity, Integer> {
    Mono<UserClientEntity> findByEmail(String email);

    @Query("SELECT * FROM userclient WHERE email = :email OR documentnumber = :document")
    Mono<UserClientEntity> findByEmailOrDocument(@Param("email") String email, @Param("document") String document);

    Mono<UserClientEntity> findByGoogleId(String googleId);

    @Query(value = "SELECT * FROM userclient u WHERE u.documentnumber = :documentNumber")

    Mono<UserClientEntity> findByDocumentNumber(@Param("documentNumber") String documentNumber);

    UserDataDTO save(UserDataDTO userDataDTO);

    Mono<Void> deleteById(Integer id);

    Mono<UserClientEntity> findByUserClientId(Integer userClientId);

    Mono<UserClientEntity> findByUsername(String username);

    @Query("SELECT * FROM userclient WHERE email = :email OR googleid = :googleId OR googleemail = :googleEmail")
    Mono<UserClientEntity> findByEmailOrGoogleIdOrGoogleEmail(String email, String googleId, String googleEmail);

    @Query("SELECT * FROM userclient uc WHERE uc.userclientid IN (SELECT b.userclientid FROM booking b WHERE b.userpromotorid = :userpromotorid)")
    Flux<UserClientEntity> findByUserPromotorId(Integer userpromotorid);

    @Query("SELECT COUNT(*) FROM userclient WHERE (:month = 0 OR TO_CHAR(createdat, 'MM/YYYY') = CAST((:month) AS VARCHAR) ||'/'|| EXTRACT(YEAR FROM CURRENT_DATE))")
    Mono<Long> countUsers(Integer month);

    @Query("SELECT COUNT(*) FROM userclient WHERE (TO_CHAR(createdat, 'MM/YYYY') = CASE :month WHEN 1 THEN TO_CHAR(CURRENT_DATE + interval '-1 month', 'MM/YYYY') ELSE CAST((:month-1) AS VARCHAR) ||'/'|| EXTRACT(YEAR FROM CURRENT_DATE) END)")
    Mono<Long> countLastUsers(Integer month);
    /*
    @Query("""
                SELECT COUNT(*)
                FROM (SELECT uc.*, g.genderdesc, c.countrydesc,
                    (SELECT l.levelname FROM userlevel l WHERE l.userlevelid = uc.userLevelId) levelname,
                    (SELECT DISTINCT bs.bookingstatename FROM booking b
                    INNER JOIN bookingstate bs ON b.bookingstateid = bs.bookingstateid AND b.userclientid = uc.userclientid) statusdesc,
                    (SELECT DISTINCT bs.bookingstateid FROM booking b
                    INNER JOIN bookingstate bs ON b.bookingstateid = bs.bookingstateid AND b.userclientid = uc.userclientid) statusid
                    FROM userclient uc
                    JOIN gender g ON uc.genderid = g.genderid
                    JOIN country c ON uc.countryid = c.countryid
                    ORDER BY uc.createdat) t
                WHERE (:statusId IS NULL OR t.statusid = :statusId)
                AND (:fecha IS NULL OR TO_CHAR(t.createdat, 'YYYY/MM/DD') = :fecha)
                AND (:filter IS NULL OR (UPPER(t.firstName) LIKE UPPER(CONCAT('%', :filter, '%')) OR UPPER(t.lastName) LIKE UPPER(CONCAT('%', :filter, '%')) OR UPPER(t.documentnumber) LIKE UPPER(CONCAT('%', :filter, '%')) OR UPPER(t.email) LIKE UPPER(CONCAT('%', :filter, '%'))))
            """)
    Mono<Long> countUserAll(Integer statusId, String fecha, String filter);

    @Query("""
                SELECT *
                FROM (SELECT uc.*, g.genderdesc, c.countrydesc,
                    (SELECT l.levelname FROM userlevel l WHERE l.userlevelid = uc.userLevelId) levelname,
                    (SELECT DISTINCT bs.bookingstatename FROM booking b
                    INNER JOIN bookingstate bs ON b.bookingstateid = bs.bookingstateid AND b.userclientid = uc.userclientid) statusdesc,
                    (SELECT DISTINCT bs.bookingstateid FROM booking b
                    INNER JOIN bookingstate bs ON b.bookingstateid = bs.bookingstateid AND b.userclientid = uc.userclientid) statusid
                    FROM userclient uc
                    JOIN gender g ON uc.genderid = g.genderid
                    JOIN country c ON uc.countryid = c.countryid
                    ORDER BY uc.createdat) t
                WHERE (:statusId IS NULL OR t.statusid = :statusId)
                AND (:fecha IS NULL OR TO_CHAR(t.createdat, 'YYYY/MM/DD') = :fecha)
                AND (:filter IS NULL OR (UPPER(t.firstName) LIKE UPPER(CONCAT('%', :filter, '%')) OR UPPER(t.lastName) LIKE UPPER(CONCAT('%', :filter, '%')) OR UPPER(t.documentnumber) LIKE UPPER(CONCAT('%', :filter, '%')) OR UPPER(t.email) LIKE UPPER(CONCAT('%', :filter, '%'))))
                OFFSET :indice*10 LIMIT 10
            """)

     */

    @Query("""
            SELECT
               COUNT(*)
            FROM
                userclient uc
                JOIN gender g ON uc.genderid = g.genderid
                JOIN country c ON uc.countryid = c.countryid
                LEFT JOIN userlevel l ON l.userlevelid = uc.userLevelId
                LEFT JOIN (
                    SELECT DISTINCT ON (b.userclientid)
                           b.userclientid,
                           bs.bookingstatename,
                           bs.bookingstateid
                    FROM booking b
                    JOIN bookingstate bs ON b.bookingstateid = bs.bookingstateid
                    ORDER BY b.userclientid, b.createdat DESC
                ) latest_status ON uc.userclientid = latest_status.userclientid
            WHERE
                (:statusId IS NULL OR latest_status.bookingstateid = :statusId)
                AND (:fecha IS NULL OR TO_CHAR(uc.createdat, 'YYYY/MM/DD') = :fecha)
                AND (:filter IS NULL OR (
                    UPPER(uc.firstName) LIKE UPPER(CONCAT('%', :filter, '%')) OR
                    UPPER(uc.lastName) LIKE UPPER(CONCAT('%', :filter, '%')) OR
                    UPPER(uc.documentnumber) LIKE UPPER(CONCAT('%', :filter, '%')) OR
                    UPPER(uc.email) LIKE UPPER(CONCAT('%', :filter, '%'))
                ))
            """)
    Mono<Long> countUserAll(Integer statusId, String fecha, String filter);

    @Query(value = """
            SELECT
                uc.*,
                g.genderdesc,
                c.countrydesc,
                l.levelname,
                latest_status.bookingstatename as statusdesc,
                latest_status.bookingstateid as statusid
            FROM
                userclient uc
                JOIN gender g ON uc.genderid = g.genderid
                JOIN country c ON uc.countryid = c.countryid
                LEFT JOIN userlevel l ON l.userlevelid = uc.userLevelId
                LEFT JOIN (
                    SELECT DISTINCT ON (b.userclientid)
                           b.userclientid,
                           bs.bookingstatename,
                           bs.bookingstateid
                    FROM booking b
                    JOIN bookingstate bs ON b.bookingstateid = bs.bookingstateid
                    ORDER BY b.userclientid, b.createdat DESC
                ) latest_status ON uc.userclientid = latest_status.userclientid
            WHERE
                (:statusId IS NULL OR latest_status.bookingstateid = :statusId)
                AND (:fecha IS NULL OR TO_CHAR(uc.createdat, 'YYYY/MM/DD') = :fecha)
                AND (:filter IS NULL OR (
                    UPPER(uc.firstName) LIKE UPPER(CONCAT('%', :filter, '%')) OR
                    UPPER(uc.lastName) LIKE UPPER(CONCAT('%', :filter, '%')) OR
                    UPPER(uc.documentnumber) LIKE UPPER(CONCAT('%', :filter, '%')) OR
                    UPPER(uc.email) LIKE UPPER(CONCAT('%', :filter, '%'))
                ))
            ORDER BY uc.createdat
            OFFSET :indice*10
            LIMIT 10
            """)
    Flux<UserClientDto> getAllClients(Integer indice, Integer statusId, String fecha, String filter);

    @Query("""
            SELECT
                COUNT(*)
            FROM
                userclient uc
                JOIN gender g ON uc.genderid = g.genderid
                JOIN country c ON uc.countryid = c.countryid
                LEFT JOIN userlevel l ON l.userlevelid = uc.userLevelId
                LEFT JOIN (
                    SELECT DISTINCT ON (b.userclientid)
                           b.userclientid,
                           bs.bookingstatename,
                           bs.bookingstateid
                    FROM booking b
                    JOIN bookingstate bs ON b.bookingstateid = bs.bookingstateid
                    ORDER BY b.userclientid, b.createdat DESC
                ) latest_status ON uc.userclientid = latest_status.userclientid
            WHERE
                (:statusId IS NULL OR latest_status.bookingstateid = :statusId)
                AND (:userLevelId IS NULL OR l.userlevelid = :userLevelId)
                AND (
                    (:fechaInicio IS NULL OR uc.createdat >= TO_TIMESTAMP(:fechaInicio, 'YYYY/MM/DD'))
                    AND (
                        :fechaFin IS NULL OR uc.createdat <= TO_TIMESTAMP(:fechaFin, 'YYYY/MM/DD')
                    )
                    AND (
                        :fechaFin IS NOT NULL OR uc.createdat <= CURRENT_DATE + INTERVAL '1 DAY' - INTERVAL '1 MICROSECOND'
                    )
                )
                AND (:filter IS NULL OR (
                    UPPER(uc.firstName) LIKE UPPER(CONCAT('%', :filter, '%')) OR
                    UPPER(uc.lastName) LIKE UPPER(CONCAT('%', :filter, '%')) OR
                    UPPER(uc.documentnumber) LIKE UPPER(CONCAT('%', :filter, '%')) OR
                    UPPER(uc.email) LIKE UPPER(CONCAT('%', :filter, '%'))
                ))
            """)
    Mono<Long> countUserAllWithParams(Integer statusId, Integer userLevelId, String fechaInicio,String fechaFin,
                                      String filter);

    @Query(value = """
            SELECT
                uc.*,
                g.genderdesc,
                c.countrydesc,
                l.levelname,
                latest_status.bookingstatename as statusdesc,
                latest_status.bookingstateid as statusid
            FROM
                userclient uc
                JOIN gender g ON uc.genderid = g.genderid
                JOIN country c ON uc.countryid = c.countryid
                LEFT JOIN userlevel l ON l.userlevelid = uc.userLevelId
                LEFT JOIN (
                    SELECT DISTINCT ON (b.userclientid)
                           b.userclientid,
                           bs.bookingstatename,
                           bs.bookingstateid
                    FROM booking b
                    JOIN bookingstate bs ON b.bookingstateid = bs.bookingstateid
                    ORDER BY b.userclientid, b.createdat DESC
                ) latest_status ON uc.userclientid = latest_status.userclientid
            WHERE
                (:statusId IS NULL OR latest_status.bookingstateid = :statusId)
                AND (:userLevelId IS NULL OR l.userlevelid = :userLevelId)
                AND (
                    (:fechaInicio IS NULL OR uc.createdat >= TO_TIMESTAMP(:fechaInicio, 'YYYY/MM/DD'))
                    AND (
                        :fechaFin IS NULL OR uc.createdat <= TO_TIMESTAMP(:fechaFin, 'YYYY/MM/DD')
                    )
                    AND (
                        :fechaFin IS NOT NULL OR uc.createdat <= CURRENT_DATE + INTERVAL '1 DAY' - INTERVAL '1 MICROSECOND'
                    )
                )
                AND (:filter IS NULL OR (
                    UPPER(uc.firstName) LIKE UPPER(CONCAT('%', :filter, '%')) OR
                    UPPER(uc.lastName) LIKE UPPER(CONCAT('%', :filter, '%')) OR
                    UPPER(uc.documentnumber) LIKE UPPER(CONCAT('%', :filter, '%')) OR
                    UPPER(uc.email) LIKE UPPER(CONCAT('%', :filter, '%'))
                ))
            ORDER BY uc.createdat
            OFFSET :indice*10
            LIMIT 10
            """)
    Flux<UserClientDto> getAllClientsWithParams(Integer indice, Integer statusId, Integer userLevelId,
                                                String fechaInicio,String fechaFin, String filter);

    @Query("SELECT * FROM userclient WHERE userclientid <>:userClientId and (email = :email OR documentnumber = :documentNumber)  limit 1")
    Mono<UserClientEntity> findByEmailOrDocumentNumberAndIgnoreId(String email, String documentNumber,
            Integer userClientId);
    /*
    @Query("""
                        update userclient set  genderid = :#{#userClientDto.genderId},
                        documentnumber = :#{#userClientDto.documentNumber},
                                address = :#{#userClientDto.address}, cellnumber = :#{#userClientDto.cellNumber}, email = :#{#userClientDto.email},
                                countryId = :#{#userClientDto.countryId}
            where userclientid = :#{#userClientDto.userClientId}
                        """)
    Mono<Void> updateBasicData(UserClientDto userClientDto);
    */

    @Query(value = """
            SELECT
                uc.email
            FROM
                userclient uc
            WHERE
                uc.userclientid <> :userClientId
                AND uc.email = :email
            LIMIT 1;
            """)
    Mono<String> findByEmailAndUserClientIdIsNot(Integer userClientId,String email);


    @Query(value = """
            SELECT
                uc.documentnumber
            FROM
                userclient uc
            WHERE
                uc.userclientid <> :userClientId
                AND uc.documentnumber = :documentNumber
            LIMIT 1;
            """)
    Mono<String> findByDocumentNumberAndUserClientIdIsNot(Integer userClientId,String documentNumber);

    @Query("""
            UPDATE userclient
            SET
                genderid = :#{#userClientDto.genderId},
                documentnumber = :#{#userClientDto.documentNumber},
                address = :#{#userClientDto.address},
                cellnumber = :#{#userClientDto.cellNumber},
                email = :#{#userClientDto.email},
                countryId = :#{#userClientDto.countryId},
                birthdate = :#{#userClientDto.birthDate}
            WHERE
                userclientid = :#{#userClientDto.userClientId};
            """)
    Mono<Void> updateBasicData(UserClientDto userClientDto);

    @Query("""
            update userclient set password = :passwordEnconded
            where userclientid = :userClientId;
                """)
    Mono<Void> updatePassword(Integer userClientId, String passwordEnconded);

    @Query("SELECT * FROM userclient WHERE isuserinclub = false")
    Flux<UserClientEntity> findAllUsersNotInClub();
}
