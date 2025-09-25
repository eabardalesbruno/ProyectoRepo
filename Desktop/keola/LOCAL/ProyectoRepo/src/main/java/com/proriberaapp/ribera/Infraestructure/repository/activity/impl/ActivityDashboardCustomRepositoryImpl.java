package com.proriberaapp.ribera.Infraestructure.repository.activity.impl;

import java.time.LocalDateTime;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import com.proriberaapp.ribera.Domain.dto.activity.RoomDetailDTO;
import com.proriberaapp.ribera.Infraestructure.repository.activity.ActivityDashboardCustomRepository;
import com.proriberaapp.ribera.Infraestructure.repository.activity.mapper.ActivityDashboardMapper;
import com.proriberaapp.ribera.Infraestructure.repository.activity.sql.ActivityDashboardQueries;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository("activityDashboardCustomRepository")
public class ActivityDashboardCustomRepositoryImpl implements ActivityDashboardCustomRepository {
    private final DatabaseClient databaseClient;

    public ActivityDashboardCustomRepositoryImpl(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Flux<RoomDetailDTO> findAllRoomsPaginated(
            LocalDateTime dateStart, 
            LocalDateTime dateEnd,
            String search,
            String clientType,
            String paymentType,
            String roomType,
            String status) {

        StringBuilder sql = new StringBuilder(ActivityDashboardQueries.BASE_QUERY);

        if (search != null && !search.isEmpty()) {
            sql.append(ActivityDashboardQueries.SEARCH_FILTER);
        }
        if (clientType != null && !clientType.isEmpty()) {
            sql.append(ActivityDashboardQueries.CLIENT_TYPE_FILTER);
        }
        if (paymentType != null && !paymentType.isEmpty()) {
            sql.append(ActivityDashboardQueries.PAYMENT_TYPE_FILTER);
        }
        if (roomType != null && !roomType.isEmpty()) {
            sql.append(ActivityDashboardQueries.ROOM_TYPE_FILTER);
        }
        if (status != null && !status.isEmpty()) {
            sql.append(ActivityDashboardQueries.STATUS_FILTER);
        }

        sql.append(ActivityDashboardQueries.ORDER_BY);

        DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(sql.toString())
            .bind("dateStart", dateStart)
            .bind("dateEnd", dateEnd);

        if (search != null && !search.isEmpty()) {
            spec = spec.bind("search", "%" + search + "%");
        }
        if (clientType != null && !clientType.isEmpty()) {
            spec = spec.bind("clientType", clientType);
        }
        if (paymentType != null && !paymentType.isEmpty()) {
            spec = spec.bind("paymentType", paymentType);
        }
        if (roomType != null && !roomType.isEmpty()) {
            spec = spec.bind("roomType", roomType);
        }
        if (status != null && !status.isEmpty()) {
            spec = spec.bind("status", status);
        }

        return spec
            .map((row, metadata) -> ActivityDashboardMapper.mapToRoomDetail(row))
            .all();
    }

    @Override
    public Mono<Long> countAllRoomsFiltered(
            LocalDateTime dateStart,
            LocalDateTime dateEnd,
            String search,
            String clientType,
            String paymentType,
            String roomType,
            String status) {
        StringBuilder sql = new StringBuilder(ActivityDashboardQueries.COUNT_QUERY);

        if (search != null && !search.isEmpty()) {
            sql.append(ActivityDashboardQueries.SEARCH_FILTER);
        }
        if (clientType != null && !clientType.isEmpty()) {
            sql.append(ActivityDashboardQueries.CLIENT_TYPE_FILTER);
        }
        if (paymentType != null && !paymentType.isEmpty()) {
            sql.append(ActivityDashboardQueries.PAYMENT_TYPE_FILTER);
        }
        if (roomType != null && !roomType.isEmpty()) {
            sql.append(ActivityDashboardQueries.ROOM_TYPE_FILTER);
        }
        if (status != null && !status.isEmpty()) {
            sql.append(ActivityDashboardQueries.STATUS_FILTER);
        }

        DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(sql.toString())
            .bind("dateStart", dateStart)
            .bind("dateEnd", dateEnd);

        if (search != null && !search.isEmpty()) {
            spec = spec.bind("search", "%" + search + "%");
        }
        if (clientType != null && !clientType.isEmpty()) {
            spec = spec.bind("clientType", clientType);
        }
        if (paymentType != null && !paymentType.isEmpty()) {
            spec = spec.bind("paymentType", paymentType);
        }
        if (roomType != null && !roomType.isEmpty()) {
            spec = spec.bind("roomType", roomType);
        }
        if (status != null && !status.isEmpty()) {
            spec = spec.bind("status", status);
        }

        return spec
            .map((row, metadata) -> row.get("total", Long.class))
            .one();
    }
}
