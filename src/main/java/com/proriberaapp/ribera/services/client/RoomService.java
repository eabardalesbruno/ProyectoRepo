package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.admin.dto.BookingStateStatsDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.RoomDashboardDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.RoomDetailDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomReturn;
import com.proriberaapp.ribera.Domain.dto.PaymentDetailDTO;
import com.proriberaapp.ribera.Domain.dto.ReservationReportDto;
import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import com.proriberaapp.ribera.services.BaseService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RoomService extends BaseService<RoomEntity, RoomEntity> {
    Flux<ViewRoomReturn> findAllView();
    Mono<RoomEntity> createRoom(RoomEntity room);

    Mono<RoomEntity> updateRoom(Integer roomId, RoomEntity room);
    Mono<RoomEntity> uploadImage(Integer roomId, String filePath);

    Mono<Void> deleteRoom(Integer roomId);

    Flux<RoomEntity> getAllRooms();

    Mono<RoomEntity> getRoomById(Integer roomId);

    Flux<RoomDashboardDto> findAllViewRoomsDetail(String daybookinginit, String daybookingend, Integer roomtypeid, Integer numberadults, Integer numberchildren, Integer  numberbabies, Integer bookingid);

    Flux<RoomDashboardDto> findViewRoomsDetailWithParams(String daybookinginit, String daybookingend,
                                                         Integer roomtypeid, Integer numberadults,
                                                         Integer numberchildren, Integer  numberbabies,
                                                         Integer bookingid);

    Mono<ReservationReportDto> findDetailById(Integer bookingid);

    Mono<PaymentDetailDTO> findPaymentDetailByBookingId(Integer bookingid);

    Flux<RoomDetailDto> findAllViewRoomsDetailActivities(String daybookinginit, String daybookingend, String roomnumber, Integer bookingstateid, Integer size, Integer page);

    Flux<BookingStateStatsDto> findBookingStateStats(String daybookinginit, String daybookingend);

    Flux<RoomEntity> getAllNumRooms();
}
