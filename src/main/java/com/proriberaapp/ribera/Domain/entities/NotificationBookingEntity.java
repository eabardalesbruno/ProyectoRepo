package com.proriberaapp.ribera.Domain.entities;

import com.proriberaapp.ribera.Domain.dto.NotificationDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Table("notification")
public class NotificationBookingEntity {
    @Id
    @Column("id")
    private int notificationId;
    @Column("createdat")
    private LocalDateTime createdAt;
    @Column("title")
    private String  notificationTitle;
    @Column("message")
    private String  notificationMessage;
    @Column("notificationtype")
    private String  notificationType;
    @Column("isread")
    private boolean notificationIsRead;
    @Column("userclientid")
    private int userClientId;
}
