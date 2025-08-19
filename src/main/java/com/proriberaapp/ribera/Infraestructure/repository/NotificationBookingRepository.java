package com.proriberaapp.ribera.Infraestructure.repository;


import com.proriberaapp.ribera.Domain.entities.NotificationBookingEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface NotificationBookingRepository extends R2dbcRepository<NotificationBookingEntity, Integer>{
    Flux<NotificationBookingEntity> findAllByUserClientId(int userclientid);

    @Query("SELECT * FROM notification " +
            "WHERE userclientid = :userclientid " +
            "ORDER BY createdat DESC " +
            "LIMIT :limit")
    Flux<NotificationBookingEntity> findLastNotificationsByUserClientId(int userclientid, int limit);

    @Modifying
    @Query("UPDATE notification SET read = true WHERE id IN (:ids)")
    Mono<Void> markAsRead(@Param("ids") List<Integer> ids);
}
