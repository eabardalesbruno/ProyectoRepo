package com.proriberaapp.ribera.services.admin;

import com.proriberaapp.ribera.Domain.dto.NotificationDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface NotificationBookingService {

    Flux<NotificationDto> findAllByUserClientId(int userClientId);
    public Flux<NotificationDto> findLastNotificationsByUserClientId(int userClientId, int limit);
    Mono<NotificationDto> save(NotificationDto notificationDto);
    Mono<Void> markAsRead( List<Integer> notificationIds);
    Flux<NotificationDto> getNotifications(String userId);
    Mono<Void> sendNotification(String userId, NotificationDto message);
}
