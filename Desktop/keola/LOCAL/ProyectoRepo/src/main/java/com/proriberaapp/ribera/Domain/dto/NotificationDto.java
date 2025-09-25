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

    public static NotificationDto getTemplateNotificationPayment(int userClientId, int bookingId) {
        return NotificationDto.builder()
                .createdAt(LocalDateTime.now())
                .notificationTitle("Validaremos el pago de tu reserva")
                .notificationMessage("Tu reserva #"+bookingId+", será validada en un plazo máximo de 48 horas, te notificaremos cuando esté revisada.")
                .notificationType("BOOKING_PAYMENT")
                .notificationIsRead(false)
                .userClientId(userClientId)
                .bookingId(bookingId)
                .build();
    }

    public static NotificationDto getTemplateNotificationPaymentConfirm(int userClientId, String roomName, int bookingId) {
        return NotificationDto.builder()
                .createdAt(LocalDateTime.now())
                .notificationTitle(roomName+" confirmada")
                .notificationMessage("Reserva #"+bookingId+" confirmada. ¡Que lo disfrutes!")
                .notificationType("BOOKING_CONFIRM_PAYMENT")
                .notificationIsRead(false)
                .userClientId(userClientId)
                .bookingId(bookingId)
                .build();
    }

    public static NotificationDto getTemplateNotificationReject(int userClientId, String userClientName, int bookingId) {
        return NotificationDto.builder()
                .createdAt(LocalDateTime.now())
                .notificationTitle("¡Lo sentimos, "+userClientName+"! Rechazamos tu pago")
                .notificationMessage("Tienes hasta 48 horas para completar el pago y asegurar tu reserva en Ribera.")
                .notificationType("BOOKING_REJECT_PAYMENT")
                .notificationIsRead(false)
                .userClientId(userClientId)
                .bookingId(bookingId)
                .build();
    }
}
