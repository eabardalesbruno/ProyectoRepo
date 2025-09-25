package com.proriberaapp.ribera.Infraestructure.viewRepository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersBooking;
import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersRoomOffer;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewAdminBookingReturn;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomOfferReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;


@Repository
@Slf4j
public class RoomOfferViewRepository {
    @Autowired
    private DatabaseClient databaseClient;

    public Flux<ViewRoomOfferReturn> viewRoomOfferReturn(SearchFiltersRoomOffer filters) {
        StringBuilder query = new StringBuilder("SELECT * FROM ViewRoomOfferReturn");

        List<String> conditions = new ArrayList<>();
        List<Consumer<DatabaseClient.GenericExecuteSpec>> binders = new ArrayList<>(); 

        addCondition(filters.roomId(), "roomId = " + filters.roomId(), "roomId", conditions, binders);
        addCondition(filters.roomOfferId(), "roomOfferId = " + filters.roomOfferId(), "roomOfferId", conditions, binders);
        addCondition(filters.roomTypeId(), "roomTypeId = " + filters.roomTypeId(), "roomTypeId", conditions, binders);

        addCondition(filters.typeRoom(), "typeRoom = " + filters.typeRoom(), "typeRoom", conditions, binders);

        if (!conditions.isEmpty()) {
            query.append(" WHERE ");
            query.append(String.join(" AND ", conditions));
        }

        DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(query.toString());
        return spec.map(ViewRoomOfferReturn::convertTo)
                .all();
    }
    private <T> void addCondition(T value, String condition, String paramName, List<String> conditions,
                                  List<Consumer<DatabaseClient.GenericExecuteSpec>> binders) {
        if (Objects.nonNull(value)) {
            conditions.add(condition);
            binders.add(spec -> spec.bind(paramName, value));
        }
    }
}
