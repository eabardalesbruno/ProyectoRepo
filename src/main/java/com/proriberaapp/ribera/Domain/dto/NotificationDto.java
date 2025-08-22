package com.proriberaapp.ribera.Domain.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class NotificationDto {
    private int id;
    private int notificationId;
    private int bookingId;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String notificationTitle;
    private String notificationMessage;
    private String notificationType;
    private int userClientId;
    private boolean notificationIsRead;

    public static NotificationDto getTemplateNotificationConfirm(int userClientId, Integer dayToPay, int bookingId) {
        return NotificationDto.builder()
                .createdAt(LocalDateTime.now())
                .notificationTitle("Tienes un pago pendiente para tu reserva")
                .notificationMessage("Tienes "+dayToPay+" horas para pagar.")
                .notificationType("BOOKING_CONFIRM")
                .notificationIsRead(false)
                .userClientId(userClientId)
                .bookingId(bookingId)
                .build();
    }

    public static NotificationDto getTemplateNotificationPayment(int userClientId, BigDecimal totalCost, String roomName) {
        return NotificationDto.builder()
                .createdAt(LocalDateTime.now())
                .notificationTitle("Confirmación de pago")
                .notificationMessage("Tu reserva "+roomName+",ha sido confirmada (Total pagado: "+totalCost.toString()+").")
                .notificationType("BOOKING_PAYMENT")
                .notificationIsRead(false)
                .userClientId(userClientId)
                .bookingId(0)
                .build();
    }

    public static NotificationDto getTemplateNotificationPaymentConfirm(int userClientId, String roomName) {
        return NotificationDto.builder()
                .createdAt(LocalDateTime.now())
                .notificationTitle("Confirmación de pago")
                .notificationMessage("Tu reserva "+roomName+",el pago ha sido confirmado.")
                .notificationType("BOOKING_CONFIRM_PAYMENT")
                .notificationIsRead(false)
                .userClientId(userClientId)
                .bookingId(0)
                .build();
    }
}
