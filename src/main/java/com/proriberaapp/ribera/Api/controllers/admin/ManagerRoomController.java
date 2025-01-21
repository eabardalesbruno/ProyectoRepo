package com.proriberaapp.ribera.Api.controllers.admin;


import com.proriberaapp.ribera.Api.controllers.admin.dto.RoomDashboardDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.RoomDetailDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomReturn;
import com.proriberaapp.ribera.Domain.dto.PaymentDetailDTO;
import com.proriberaapp.ribera.Domain.dto.ReservationReportDto;
import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import com.proriberaapp.ribera.services.client.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("${url.manager}/room")
@RequiredArgsConstructor
@Slf4j
public class ManagerRoomController extends BaseManagerController<RoomEntity, RoomEntity>{

    private final RoomService roomService;

    @GetMapping("/find/all/view")
    public Flux<ViewRoomReturn> findAllViewRoomReturn() {
        return roomService.findAllView();
    }

    @GetMapping("/find/all/detail")
    public Flux<RoomDashboardDto> findAllViewRoomsDetail(@RequestParam(required = false) String daybookinginit, @RequestParam(required = false) String daybookingend, @RequestParam(required = false) Integer roomtypeid, @RequestParam(required = false) Integer numberadults, @RequestParam(required = false) Integer numberchildren, @RequestParam(required = false) Integer numberbabies, @RequestParam(required = false) Integer bookingid) {
        return roomService.findAllViewRoomsDetail(daybookinginit, daybookingend, roomtypeid, numberadults, numberchildren, numberbabies, bookingid);
    }

    @GetMapping("/find/detailById")
    public Mono<ReservationReportDto> findDetailById(@RequestParam Integer bookingid) {
        return roomService.findDetailById(bookingid);
    }

    @GetMapping("/find/paymentDetailByBookingId")
    public Mono<PaymentDetailDTO> findPaymentDetailByBookingId(@RequestParam Integer bookingid) {
        return roomService.findPaymentDetailByBookingId(bookingid);
    }

}
