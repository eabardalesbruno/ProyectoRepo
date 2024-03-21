package com.proriberaapp.ribera.Api.controllers;
import com.proriberaapp.ribera.Domain.entities.BookingDetailEntity;
import com.proriberaapp.ribera.Infraestructure.services.BookingDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@RestController
@RequestMapping("/api/v1/booking-details")
public class BookingDetailController {

    private final BookingDetailService bookingDetailService;

    @Autowired
    public BookingDetailController(BookingDetailService bookingDetailService) {
        this.bookingDetailService = bookingDetailService;
    }

    @GetMapping
    public ResponseEntity<Flux<BookingDetailEntity>> getAllBookingDetails() {
        Flux<BookingDetailEntity> bookingDetails = bookingDetailService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(bookingDetails);
    }

    @PostMapping
    public ResponseEntity<Mono<BookingDetailEntity>> createBookingDetail(@RequestBody BookingDetailEntity bookingDetailEntity) {
        Mono<BookingDetailEntity> createdBookingDetail = bookingDetailService.save(bookingDetailEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBookingDetail);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mono<BookingDetailEntity>> updateBookingDetail(@PathVariable("id") String id, @RequestBody BookingDetailEntity bookingDetailEntity) {
        Mono<BookingDetailEntity> updatedBookingDetail = bookingDetailService.update(bookingDetailEntity);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBookingDetail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Mono<Void>> deleteBookingDetail(@PathVariable("id") String id) {
        Mono<Void> result = bookingDetailService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(result);
    }
    @GetMapping("/by-room-type/{roomType}")
    public ResponseEntity<Flux<BookingDetailEntity>> findByRoomType(@PathVariable String roomType) {
        Flux<BookingDetailEntity> bookingDetails = bookingDetailService.findByRoomType(roomType);
        return ResponseEntity.status(HttpStatus.OK).body(bookingDetails);
    }
}