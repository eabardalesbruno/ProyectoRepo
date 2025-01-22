package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.RoomDashboardDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.RoomDetailDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomReturn;
import com.proriberaapp.ribera.Api.controllers.client.dto.CompanionsDto;
import com.proriberaapp.ribera.Domain.dto.PaymentDetailDTO;
import com.proriberaapp.ribera.Domain.dto.ReservationReportDto;
import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.services.client.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomImageRepository roomImageRepository;
    private final BedroomRepository bedroomRepository;
    private final RoomOfferRepository roomOfferRepository;
    private final BookingRepository bookingRepository;
    private final CompanionsRepository companionsRepository;
    private final PaymentBookRepository paymentBookRepository;

    @Override
    public Mono<RoomEntity> save(RoomEntity roomEntity) {
        return roomRepository.findByRoomName(roomEntity.getRoomName()).hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalArgumentException("Room already exists"))
                        : roomRepository.save(roomEntity));
    }

    @Override
    public Flux<RoomEntity> saveAll(List<RoomEntity> roomEntity) {
        return roomRepository.findAllByRoomNameIn(roomEntity.stream().map(RoomEntity::getRoomName).toList())
                .collectList()
                .flatMapMany(roomEntities -> roomRepository.saveAll(
                        roomEntity.stream().filter(roomEntity1 -> !roomEntities.contains(roomEntity1)).toList()
                ));
    }

    @Override
    public Mono<RoomEntity> findById(Integer id) {
        return roomRepository.findById(id);
    }

    @Override
    public Flux<RoomEntity> findAll() {
        return roomRepository.findAllByOrderByRoomIdAsc();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return roomRepository.deleteById(id);
    }

    @Override
    public Mono<RoomEntity> update(RoomEntity roomEntity) {
        return roomRepository.save(roomEntity);
    }

    @Override
    public Mono<RoomEntity> createRoom(RoomEntity room) {
        return roomRepository.save(room);
    }

    @Override
    public Mono<RoomEntity> updateRoom(Integer roomId, RoomEntity room) {
        return roomRepository.findById(roomId)
                .flatMap(existingRoom -> {
                    existingRoom.setRoomName(room.getRoomName());
                    existingRoom.setRoomTypeId(room.getRoomTypeId());
                    existingRoom.setStateRoomId(room.getStateRoomId());
                    existingRoom.setRoomDetailId(room.getRoomDetailId());
                    existingRoom.setRoomDescription(room.getRoomDescription());
                    existingRoom.setRoomNumber(room.getRoomNumber());
                    existingRoom.setAdultCapacity(room.getAdultCapacity());
                    existingRoom.setAdultExtra(room.getAdultExtra());
                    existingRoom.setKidCapacity(room.getKidCapacity());
                    existingRoom.setAdultMayorCapacity(room.getAdultMayorCapacity());
                    existingRoom.setInfantCapacity(room.getInfantCapacity());
                    return roomRepository.save(existingRoom);
                });
    }

    @Override
    public Mono<RoomEntity> uploadImage(Integer roomId, String filePath) {
        return roomRepository.findById(roomId)
                .flatMap(existingRoom -> {
                    existingRoom.setImage(filePath);
                    return roomRepository.save(existingRoom);
                });
    }

    @Override
    public Mono<Void> deleteRoom(Integer roomId) {
        return roomOfferRepository.findAllByRoomId(roomId)
                .collectList()
                .flatMap(roomOffers -> {
                    if (roomOffers.isEmpty()) {
                        return roomImageRepository.deleteAllByRoomId(roomId)
                                .then(bedroomRepository.deleteAllByRoomId(roomId))
                                .then(roomRepository.deleteById(roomId));
                    } else {
                        return Mono.error(new IllegalArgumentException("Alojamiento asociado a un servicio, no se puede eliminar"));
                    }
                });
    }

    @Override
    public Flux<RoomEntity> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Mono<RoomEntity> getRoomById(Integer roomId) {
        return roomRepository.findById(roomId);
    }

    @Override
    public Flux<ViewRoomReturn> findAllView() {
        return roomRepository.findAllViewRoomReturn()
                .flatMap(room ->
                        roomRepository.findAllViewBedroomReturn(room.getRoomId())
                                .collectList()
                                .map(bed -> {
                                    room.setListBedroomReturn(bed);
                                    return room;
                                })
                );
    }

    @Override
    public Flux<RoomDashboardDto> findAllViewRoomsDetail(String daybookinginit, String daybookingend, Integer roomtypeid, Integer numberadults, Integer numberchildren, Integer numberbabies, Integer bookingid) {
        return roomRepository.findAllViewRooms().publishOn(Schedulers.boundedElastic()).flatMap(room -> {
            RoomDashboardDto roomDashboard = new RoomDashboardDto();
            roomDashboard.setRoomNumber(room);
            List<RoomDetailDto> listRoomDetail = roomRepository.findAllViewRoomsDetail(daybookinginit, daybookingend, room, roomtypeid, numberadults, numberchildren, numberbabies, bookingid).collectList().block();
            //roomDashboard.setRoomStatus(listRoomDetail.size() > 0 ? "Evaluar" : "Libre");
            if (listRoomDetail.size() > 0) {
                roomDashboard.setRoomStatus(listRoomDetail.get(0).getRoomStatus());
            }
            roomDashboard.setDetails(listRoomDetail);
            return Flux.just(roomDashboard);
        });
    }

    @Override
    public Mono<ReservationReportDto> findDetailById(Integer bookingid) {
        return bookingRepository.findByBookingId(bookingid).publishOn(Schedulers.boundedElastic()).flatMap(bookingEntity -> {
            ReservationReportDto reservationDto = new ReservationReportDto();
            reservationDto.setDayBookingInit(bookingEntity.getCheckIn() != null ? bookingEntity.getCheckIn().toLocalDateTime().format(DateTimeFormatter.ofPattern("EE, dd MMM")) : "");
            reservationDto.setDayBookingEnd(bookingEntity.getCheckout() != null ? bookingEntity.getCheckout().toLocalDateTime().format(DateTimeFormatter.ofPattern("EE, dd MMM")) : "");
            reservationDto.setNumberAdults((bookingEntity.getNumberAdults()+bookingEntity.getNumberAdultsMayor()+bookingEntity.getNumberAdultsExtra())+" Adultos");
            reservationDto.setNumberChildren(bookingEntity.getNumberChildren()+" Niños");
            reservationDto.setNumberBabies(bookingEntity.getNumberBabies()+" Bebés");
            List<ReservationReportDto> listCompanions = new ArrayList<>();
            List<CompanionsDto> items = companionsRepository.getCompanionsByBookingId(bookingEntity.getBookingId()).collectList().block();
            return roomRepository.getRoomNameByBookingId(bookingid).flatMap(roomEntity -> {
                reservationDto.setRoomName(roomEntity.getRoomName());
                reservationDto.setRoomNumber(roomEntity.getRoomNumber());
                reservationDto.setImage(roomEntity.getImage());
                for (CompanionsDto item : items) {
                    if (item.isTitular()) {
                        reservationDto.setDocumentType(item.getDocumenttypedesc());
                        reservationDto.setDocumentNumber(item.getDocumentNumber());
                        reservationDto.setFullname(item.getFirstname() + " " + item.getLastname());
                        reservationDto.setYears(item.getYears() != null ? String.valueOf(item.getYears()) : "");
                        reservationDto.setEmail(item.getEmail() != null ? item.getEmail() : "");
                        reservationDto.setCountrydesc(item.getCountrydesc() != null ? item.getCountrydesc() : "");
                        reservationDto.setCellphone(item.getCellphone() != null ? item.getCellphone() : "");
                    } else {
                        ReservationReportDto reportDto = new ReservationReportDto();
                        reportDto.setDocumentType(item.getDocumenttypedesc() != null ? item.getDocumenttypedesc() : "" );
                        reportDto.setDocumentNumber(item.getDocumentNumber() != null ? item.getDocumentNumber() : "" );
                        reportDto.setFullname(item.getFirstname() + " " + item.getLastname());
                        reportDto.setYears(item.getYears() != null ? ""+item.getYears() : "" );
                        reportDto.setGender(item.getGenderdesc() != null ? item.getGenderdesc() : "" );
                        listCompanions.add(reportDto);
                        reservationDto.setLstCompanions(listCompanions);
                    }
                }
                return Mono.just(reservationDto);
            });
        });
    }

    @Override
    public Mono<PaymentDetailDTO> findPaymentDetailByBookingId(Integer bookingid) {
        return paymentBookRepository.getPaymentDetail(bookingid);
    }

}
