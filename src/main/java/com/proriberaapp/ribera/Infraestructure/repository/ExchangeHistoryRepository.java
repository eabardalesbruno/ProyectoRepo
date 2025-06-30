package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.ExchangeHistoryEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeHistoryRepository extends R2dbcRepository<ExchangeHistoryEntity, Integer> {
    @Query(value = """
            SELECT *
            FROM exchange_history eh
            WHERE eh.user_id = :userId
              AND (
                    (:startDate IS NULL AND :endDate IS NULL)
                    OR
                    (
                        (:startDate IS NULL OR TO_DATE(eh.check_in_date, 'DD/MM/YYYY') >= TO_DATE(:startDate, 'DD/MM/YYYY'))
                        AND
                        (:endDate IS NULL OR TO_DATE(eh.check_out_date, 'DD/MM/YYYY') <= TO_DATE(:endDate, 'DD/MM/YYYY'))
                    )
                  )
              AND (:exchangeType IS NULL OR eh.exchange_type = :exchangeType)
              AND (:serviceType IS NULL OR eh.service = :serviceType)
            LIMIT :size OFFSET :offset;
            """)
    Flux<ExchangeHistoryEntity> findByUserIdAndFilters(Integer userId, String startDate, String endDate,
                                                       String exchangeType, String serviceType,Integer size, Integer offset);

    @Query(value = """
            SELECT COUNT(eh.id)
            FROM exchange_history eh
            WHERE eh.user_id = :userId
              AND (
                    (:startDate IS NULL AND :endDate IS NULL)
                    OR
                    (
                        (:startDate IS NULL OR TO_DATE(eh.check_in_date, 'DD/MM/YYYY') >= TO_DATE(:startDate, 'DD/MM/YYYY'))
                        AND
                        (:endDate IS NULL OR TO_DATE(eh.check_out_date, 'DD/MM/YYYY') <= TO_DATE(:endDate, 'DD/MM/YYYY'))
                    )
                  )
              AND (:exchangeType IS NULL OR eh.exchange_type = :exchangeType)
              AND (:serviceType IS NULL OR eh.service = :serviceType);
            """)
    Mono<Integer> countByUserIdAndFilters(Integer userId, String startDate, String endDate, String exchangeType, String serviceType);

    @Query(value = """
            SELECT *
            FROM public.exchange_history eh
            WHERE eh.username = :username
              AND (
                    (:startDate IS NULL AND :endDate IS NULL)
                    OR
                    (
                        (:startDate IS NULL OR TO_DATE(eh.check_in_date, 'DD/MM/YYYY') >= TO_DATE(:startDate, 'DD/MM/YYYY'))
                        AND
                        (:endDate IS NULL OR TO_DATE(eh.check_out_date, 'DD/MM/YYYY') <= TO_DATE(:endDate, 'DD/MM/YYYY'))
                    )
                  )
              AND (:exchangeType IS NULL OR eh.exchange_type = :exchangeType)
              AND (:serviceType IS NULL OR eh.service = :serviceType)
            LIMIT :size OFFSET :offset;
            """)
    Flux<ExchangeHistoryEntity> findByUsernameAndFilters(String username, String startDate, String endDate,
                                                       String exchangeType, String serviceType,Integer size, Integer offset);

    @Query(value = """
            SELECT COUNT(eh.id)
            FROM exchange_history eh
            WHERE eh.username = :username
              AND (
                    (:startDate IS NULL AND :endDate IS NULL)
                    OR
                    (
                        (:startDate IS NULL OR TO_DATE(eh.check_in_date, 'DD/MM/YYYY') >= TO_DATE(:startDate, 'DD/MM/YYYY'))
                        AND
                        (:endDate IS NULL OR TO_DATE(eh.check_out_date, 'DD/MM/YYYY') <= TO_DATE(:endDate, 'DD/MM/YYYY'))
                    )
                  )
              AND (:exchangeType IS NULL OR eh.exchange_type = :exchangeType)
              AND (:serviceType IS NULL OR eh.service = :serviceType);
            """)
    Mono<Integer> countByUsernameAndFilters(String username, String startDate, String endDate, String exchangeType, String serviceType);
}
