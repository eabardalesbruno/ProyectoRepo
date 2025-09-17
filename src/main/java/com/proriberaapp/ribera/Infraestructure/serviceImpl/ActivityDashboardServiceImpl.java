package com.proriberaapp.ribera.Infraestructure.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.proriberaapp.ribera.Domain.Interfaces.ActivityRoomProjection;
import com.proriberaapp.ribera.Domain.dto.activity.ActivitySummaryDTO;
import com.proriberaapp.ribera.Domain.dto.activity.GuestInfoDTO;
import com.proriberaapp.ribera.Domain.dto.activity.PaginationDTO;
import com.proriberaapp.ribera.Domain.dto.activity.PaymentInfoDTO;
import com.proriberaapp.ribera.Domain.dto.activity.ReservationDetailDTO;
import com.proriberaapp.ribera.Domain.dto.activity.RoomCapacityDTO;
import com.proriberaapp.ribera.Domain.dto.activity.RoomDetailDTO;
import com.proriberaapp.ribera.Domain.dto.activity.response.ActivityDashboardResponseDTO;
import com.proriberaapp.ribera.Infraestructure.repository.activity.ActivityDashboardRepository;
import com.proriberaapp.ribera.application.service.ActivityDashboardService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.tools.shaded.net.bytebuddy.asm.Advice.Local;

@Service
@RequiredArgsConstructor
public class ActivityDashboardServiceImpl implements ActivityDashboardService {
        private final ActivityDashboardRepository activityDashboardRepository;

        @Override
        public Mono<ActivityDashboardResponseDTO> getActivityDashboard(LocalDateTime date, int page, int size) {
                return Mono.zip(getActivitySummary(date), getRoomDetails(date, page, size))
                                .map(tuple -> ActivityDashboardResponseDTO.builder().success(
                                                true)
                                                .data(tuple.getT1())
                                                .timestamp(LocalDateTime.now())
                                                .build());
        }

        private Mono<ActivitySummaryDTO> getActivitySummary(LocalDateTime date) {
                return Mono.zip(
                                activityDashboardRepository.countTotalPaid(date),
                                activityDashboardRepository.countTotalCheckIn(date),
                                activityDashboardRepository.countTotalCheckOut(date),
                                activityDashboardRepository.countTotalReservations(date),
                                activityDashboardRepository.countTotalNoShow(date),
                                activityDashboardRepository.countTotalAvailables(date)).map(
                                                tuple -> ActivitySummaryDTO.builder()
                                                                .totalPaid(tuple.getT1())
                                                                .totalCheckIn(tuple.getT2())
                                                                .totalCheckOut(tuple.getT3())
                                                                .totalReservations(tuple.getT4())
                                                                .totalNoShow(tuple.getT5())
                                                                .totalAvailables(tuple.getT6())
                                                                .build());
        }

        private Mono<RoomDetailResult> getRoomDetails(LocalDateTime date, int page, int size) {
                int offset = page * size;

                Mono<List<RoomDetailDTO>> roomsMono = activityDashboardRepository.findAllRooms(date, size, offset)
                                .map(this::mapToRoomDetailDTO).collectList();
                Mono<Long> countMono = activityDashboardRepository.countAllRooms(date);

                return Mono.zip(roomsMono, countMono)
                                .map(tuple -> {
                                        List<RoomDetailDTO> rooms = tuple.getT1();
                                        long totalElements = tuple.getT2();
                                        int totalPages = (int) Math.ceil((double) totalElements / size);
                                        return new RoomDetailResult(rooms, PaginationDTO.builder()
                                                        .currentPage(page)
                                                        .size(size)
                                                        .totalElements(totalElements)
                                                        .totalPages(totalPages)
                                                        .build());
                                });
        }

        private RoomDetailDTO mapToRoomDetailDTO(ActivityRoomProjection projection) {
                return RoomDetailDTO.builder()
                                .roomId(projection.getRoomId())
                                .roomNumber(projection.getRoomNumber())
                                .roomName(projection.getRoomName())
                                .roomType(projection.getCategoryName())
                                .status(projection.getStatus())
                                .reservation(projection.getBookingId() != null ? mapToReservationDetail(projection)
                                                : null)
                                .payment(projection.getPaymentMethod() != null ? mapToPaymentInfo(projection) : null)
                                .build();
        }

        private ReservationDetailDTO mapToReservationDetail(ActivityRoomProjection projection) {
                return ReservationDetailDTO.builder()
                                .bookingId(projection.getBookingId())
                                .checkIn(projection.getDayBookingInit())
                                .checkOut(projection.getDayBookingEnd())
                                .guest(GuestInfoDTO.builder()
                                                .name(projection.getFirstName() + " " + projection.getLastName())
                                                .type(projection.getIsUserInClub() ? "Socio" : "Externo")
                                                .build())
                                .capacity(RoomCapacityDTO.builder()
                                                .adults(projection.getNumberAdults())
                                                .children(projection.getNumberChildren())
                                                .babies(projection.getNumberBabies())
                                                .adultsExtra(projection.getNumberAdultsExtra())
                                                .adultsMayor(projection.getNumberAdultsMayor())
                                                .total(projection.getNumberAdults() + projection.getNumberChildren()
                                                                + projection.getNumberBabies()
                                                                + projection.getNumberAdultsExtra() +
                                                                projection.getNumberAdultsMayor())
                                                .build())
                                .build();
        }

        private PaymentInfoDTO mapToPaymentInfo(ActivityRoomProjection projection) {
                return PaymentInfoDTO.builder()
                                .method(projection.getPaymentMethod())
                                .hasFeeding(projection.getHasFeeding())
                                .build();
        }

        private record RoomDetailResult(
                        List<RoomDetailDTO> rooms, PaginationDTO pagination) {
        }
}
