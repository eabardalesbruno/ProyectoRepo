package com.proriberaapp.ribera.Infraestructure.viewRepository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.*;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewAdminBookingAvailabilityReturn;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewAdminBookingInventoryReturn;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewAdminBookingReturn;
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
public class BookingViewRepository {
    @Autowired
    private DatabaseClient databaseClient;

    public Flux<ViewAdminBookingReturn> viewAdminBookingReturn(SearchFiltersBooking filters) {
        StringBuilder query = new StringBuilder("SELECT * FROM viewadminbookingreturn");

        List<String> conditions = new ArrayList<>();
        List<Consumer<DatabaseClient.GenericExecuteSpec>> binders = new ArrayList<>();

        addCondition(filters.roomId(), "roomId = " + filters.roomId(), "roomId", conditions, binders);
        addCondition(filters.roomOfferId(), "roomOfferId = " + filters.roomOfferId(), "roomOfferId", conditions, binders);
        addCondition(filters.roomTypeId(), "roomTypeId = " + filters.roomTypeId(), "roomTypeId", conditions, binders);
        addCondition(filters.bookingId(), "bookingid = " + filters.bookingId(), "bookingId", conditions, binders);
        addCondition(filters.bookingStateId(), "bookingStateId = " + filters.bookingStateId(), "bookingStateId", conditions, binders);
        addCondition(filters.userClientId(), "userClientId = " + filters.userClientId(), "userClientId", conditions, binders);
        addCondition(filters.dateCreate(), "dateCreate = " + filters.dateCreate(), "dateCreate", conditions, binders);
        addCondition(filters.numberBooking(), "numberBooking = " + filters.numberBooking(), "numberBooking", conditions, binders);
        addCondition(filters.typeRoom(), "typeRoom = " + filters.typeRoom(), "typeRoom", conditions, binders);
        addCondition(filters.client(), "client = " + filters.client(), "client", conditions, binders);
        addCondition(filters.numberDocument(), "numberDocument = " + filters.numberDocument(), "numberDocument", conditions, binders);
        addCondition(filters.costTotal(), "costTotal = " + filters.costTotal(), "costTotal", conditions, binders);

        if (!conditions.isEmpty()) {
            query.append(" WHERE ");
            query.append(String.join(" AND ", conditions));
        }

        DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(query.toString());

        return spec.map(ViewAdminBookingReturn::convertTo)
                .all();
    }

    public Flux<ViewAdminBookingInventoryReturn> viewAdminBookingInventoryReturn(SearchFiltersBookingInventory filters) {
        StringBuilder query = new StringBuilder("SELECT * FROM viewadminbookinginventoryreturn");

        List<String> conditions = new ArrayList<>();
        List<Consumer<DatabaseClient.GenericExecuteSpec>> binders = new ArrayList<>();

        addCondition(filters.roomId(), "roomId = " + filters.roomId(), "roomId", conditions, binders);
        addCondition(filters.roomOfferId(), "roomOfferId = " + filters.roomOfferId(), "roomOfferId", conditions, binders);
        addCondition(filters.roomTypeId(), "roomTypeId = " + filters.roomTypeId(), "roomTypeId", conditions, binders);
        addCondition(filters.bookingId(), "bookingid = " + filters.bookingId(), "bookingId", conditions, binders);
        addCondition(filters.bookingStateId(), "bookingStateId = " + filters.bookingStateId(), "bookingStateId", conditions, binders);
        addCondition(filters.userClientId(), "userClientId = " + filters.userClientId(), "userClientId", conditions, binders);
        addCondition(filters.dateCreate(), "dateCreate = " + filters.dateCreate(), "dateCreate", conditions, binders);
        addCondition(filters.numberBooking(), "numberBooking = " + filters.numberBooking(), "numberBooking", conditions, binders);
        addCondition(filters.typeRoom(), "typeRoom = " + filters.typeRoom(), "typeRoom", conditions, binders);
        addCondition(filters.client(), "client = " + filters.client(), "client", conditions, binders);
        addCondition(filters.numberDocument(), "numberDocument = " + filters.numberDocument(), "numberDocument", conditions, binders);
        addCondition(filters.costTotal(), "costTotal = " + filters.costTotal(), "costTotal", conditions, binders);

        if (!conditions.isEmpty()) {
            query.append(" WHERE ");
            query.append(String.join(" AND ", conditions));
        }

        DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(query.toString());

        return spec.map(ViewAdminBookingInventoryReturn::convertTo)
                .all();
    }

    public Flux<ViewAdminBookingAvailabilityReturn> viewAdminBookingAvailabilityReturn(SearchFiltersBookingAvailability filters) {
        StringBuilder query = new StringBuilder("SELECT * FROM viewadminbookingavailabilityreturn");

        List<String> conditions = new ArrayList<>();
        List<Consumer<DatabaseClient.GenericExecuteSpec>> binders = new ArrayList<>();

        addCondition(filters.roomId(), "roomId = " + filters.roomId(), "roomId", conditions, binders);
        addCondition(filters.roomOfferId(), "roomOfferId = " + filters.roomOfferId(), "roomOfferId", conditions, binders);
        addCondition(filters.roomTypeId(), "roomTypeId = " + filters.roomTypeId(), "roomTypeId", conditions, binders);
        addCondition(filters.bookingId(), "bookingid = " + filters.bookingId(), "bookingId", conditions, binders);
        addCondition(filters.bookingStateId(), "bookingStateId = " + filters.bookingStateId(), "bookingStateId", conditions, binders);
        addCondition(filters.userClientId(), "userClientId = " + filters.userClientId(), "userClientId", conditions, binders);
        addCondition(filters.dateCreate(), "dateCreate = " + filters.dateCreate(), "dateCreate", conditions, binders);
        addCondition(filters.numberBooking(), "numberBooking = " + filters.numberBooking(), "numberBooking", conditions, binders);
        addCondition(filters.typeRoom(), "typeRoom = " + filters.typeRoom(), "typeRoom", conditions, binders);
        addCondition(filters.client(), "client = " + filters.client(), "client", conditions, binders);
        addCondition(filters.numberDocument(), "numberDocument = " + filters.numberDocument(), "numberDocument", conditions, binders);
        addCondition(filters.costTotal(), "costTotal = " + filters.costTotal(), "costTotal", conditions, binders);

        if (!conditions.isEmpty()) {
            query.append(" WHERE ");
            query.append(String.join(" AND ", conditions));
        }

        DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(query.toString());

        return spec.map(ViewAdminBookingAvailabilityReturn::convertTo)
                .all();
    }

    private <T> void addCondition(T value, String condition, String paramName, List<String> conditions, List<Consumer<DatabaseClient.GenericExecuteSpec>> binders) {
        if (Objects.nonNull(value)) {
            conditions.add(condition);
            binders.add(spec -> spec.bind(paramName, value));
        }
    }

}
