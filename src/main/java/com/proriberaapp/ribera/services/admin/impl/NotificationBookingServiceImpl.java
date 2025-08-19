package com.proriberaapp.ribera.services.admin.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proriberaapp.ribera.Domain.dto.NotificationDto;
import com.proriberaapp.ribera.Domain.mapper.NotificationBookingMapper;
import com.proriberaapp.ribera.Infraestructure.repository.NotificationBookingRepository;
import com.proriberaapp.ribera.services.admin.NotificationBookingService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationBookingServiceImpl implements NotificationBookingService {

    private final Map<String, Sinks.Many<NotificationDto>> userSinks = new ConcurrentHashMap<>();
    private final NotificationBookingRepository notificationBookingRepository;

    NotificationBookingServiceImpl(NotificationBookingRepository notificationBookingRepository){
        this.notificationBookingRepository = notificationBookingRepository;
    }

    @Override
    public Flux<NotificationDto> findAllByUserClientId(int userClientId) {
        return notificationBookingRepository.findAllByUserClientId(userClientId)
                .map(NotificationBookingMapper::toDto);
    }

    @Override
    public Flux<NotificationDto> findLastNotificationsByUserClientId(int userClientId, int limit) {
        return notificationBookingRepository.findLastNotificationsByUserClientId(userClientId, limit)
                .map(NotificationBookingMapper::toDto);
    }

    @Override
    public Mono<NotificationDto> save(NotificationDto notificationDto) {
        System.out.println("Guardando notificación...");
        return notificationBookingRepository.save(NotificationBookingMapper.toEntity(notificationDto))
                .map(NotificationBookingMapper::toDto)
                .doOnNext(saved -> System.out.print("Notificación guardada"))
                .doOnError(error -> System.out.print("Error al guardar notificación" + error.toString()));
    }

    @Override
    public Mono<Void> markAsRead( @RequestBody List<Integer> notificationIds) {
        return notificationBookingRepository.markAsRead(notificationIds);
    }

    public Flux<NotificationDto> getNotifications(String userId) {
        System.out.println("To Client Notification");
        Sinks.Many<NotificationDto> sink = userSinks.computeIfAbsent(
                userId,
                k -> Sinks.many().multicast().onBackpressureBuffer()
        );
        return sink.asFlux().doOnCancel(() -> {
            userSinks.remove(userId);
            System.out.println("Cliente desconectado");
        }).doOnError(e -> {
            userSinks.remove(userId);
            System.out.println("Error en la conexión");
        });
    }

    public Mono<Void> sendNotification(String userId, NotificationDto message) {
        System.out.println("Admin Action");
        Sinks.Many<NotificationDto> sink = userSinks.get(userId);

        if(sink !=null){
                sink.tryEmitNext(message);
        }
        return Mono.empty();
    }
}
