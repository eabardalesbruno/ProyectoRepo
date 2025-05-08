package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.dto.WalletPointHistoryDto;
import com.proriberaapp.ribera.Domain.entities.WalletPointHistoryEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface WalletPointHistoryRepository extends ReactiveCrudRepository<WalletPointHistoryEntity, Integer> {
    @Query(value = """
            SELECT wph.points AS points,TO_CHAR(wph.created_at, 'DD/MM/YYYY') AS datebuy
            FROM wallet_point_history wph
            WHERE wph.user_id = :userId
            AND wph.created_at::DATE BETWEEN TO_DATE(:startDate, 'DD/MM/YYYY')
                AND COALESCE(TO_DATE(:endDate, 'DD/MM/YYYY'), CURRENT_DATE)
            LIMIT :limit OFFSET :offset;
            """)
    Flux<WalletPointHistoryDto> findPointsHistoryByUserIdAndRangeDate(
            Integer userId, String startDate, String endDate,Integer limit, Integer offset);

    @Query(value = """
            SELECT count(wph.points)
            FROM wallet_point_history wph
            WHERE wph.user_id = :userId
            AND wph.created_at::DATE BETWEEN TO_DATE(:startDate, 'DD/MM/YYYY')
                AND COALESCE(TO_DATE(:endDate, 'DD/MM/YYYY'), CURRENT_DATE)
            """)
    Mono<Long> countListPointsHistoryByUserId(Integer userId,String startDate, String endDate);
}
