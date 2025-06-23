package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.UserRewardEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.channels.FileChannel;

@Repository
public interface UserRewardRepository extends ReactiveCrudRepository<UserRewardEntity, Long> {

    Flux<UserRewardEntity> findByUserId(Long userId);

    Flux<UserRewardEntity> findByUserIdOrderByDateDesc(Long userId);

    Flux<UserRewardEntity> findByUserIdAndStatus(Long userId, String status);

    Flux<UserRewardEntity> findByUserIdAndType(Long userId, String type);

    Flux<UserRewardEntity> findByType(String name);

    Flux<UserRewardEntity> findByStatus(int status);

    Flux<UserRewardEntity> findByUserIdAndStatus(Long userId,int status);

    @Query(value = """
            UPDATE user_reward
            SET status = :status
            WHERE booking_id = :bookingId AND user_id = :userId
            """)
    Mono<Void> updateStatusByBookingIdAndUserId(Integer status,Integer bookingId,Integer userId);

    @Query(value = """
            SELECT SUM(ur.points) AS total_points
            FROM user_reward ur
            WHERE ur.booking_id = :bookingId AND user_id = :userId;
            """)
    Mono<Double> sumPointsByBookingIdAndUserId(Integer bookingId,Integer userId);
}
