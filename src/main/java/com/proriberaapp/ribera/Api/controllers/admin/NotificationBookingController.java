package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.dto.NotificationDto;
import com.proriberaapp.ribera.services.admin.NotificationBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationBookingController {

    private final NotificationBookingService service;

    @GetMapping(value = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NotificationDto> subscribe(@PathVariable String userId) {
        return service.getNotifications(userId);
    }

    @PostMapping("/send/{userId}")
    public void send(@PathVariable String userId, @RequestBody NotificationDto message){
        service.sendNotification(userId, message);
    }

    @GetMapping("/last/{userClientId}")
    public Flux<NotificationDto> findLastNotificationsByUserClientId(
            @PathVariable int userClientId,
            @RequestParam(name = "limit", defaultValue = "5") int limit){
        return service.findLastNotificationsByUserClientId(userClientId, limit);
    }

    @GetMapping("/{userClientId}")
    public Flux<NotificationDto> getAllByUserClientId(@PathVariable int userClientId){
        return service.findAllByUserClientId(userClientId);
    }

    @PostMapping("/")
    public void save(@RequestBody NotificationDto notificationDto){
        service.save(notificationDto).subscribe();
    }

}
