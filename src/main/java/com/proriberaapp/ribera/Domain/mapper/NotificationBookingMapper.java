package com.proriberaapp.ribera.Domain.mapper;

import com.proriberaapp.ribera.Domain.dto.NotificationDto;
import com.proriberaapp.ribera.Domain.entities.NotificationBookingEntity;

public class NotificationBookingMapper {
    public static NotificationDto toDto(NotificationBookingEntity notificationEntity){
        return NotificationDto.builder()
                .id(notificationEntity.getNotificationId())
                .createdAt(notificationEntity.getCreatedAt())
                .notificationTitle(notificationEntity.getNotificationTitle())
                .notificationMessage(notificationEntity.getNotificationMessage())
                .notificationType(notificationEntity.getNotificationType())
                .userClientId(notificationEntity.getUserClientId())
                .notificationIsRead(notificationEntity.isNotificationIsRead())
                .build();
    }

    public static NotificationBookingEntity toEntity(NotificationDto notificationDto){
        return NotificationBookingEntity.builder()
                .createdAt(notificationDto.getCreatedAt())
                .notificationTitle(notificationDto.getNotificationTitle())
                .notificationMessage(notificationDto.getNotificationMessage())
                .notificationType(notificationDto.getNotificationType())
                .userClientId(notificationDto.getUserClientId())
                .notificationIsRead(notificationDto.isNotificationIsRead())
                .build();
    }
}
