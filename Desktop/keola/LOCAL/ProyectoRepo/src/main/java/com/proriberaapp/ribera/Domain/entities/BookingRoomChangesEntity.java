package com.proriberaapp.ribera.Domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "booking_room_changes")
@Getter
@Setter
@Builder
public class BookingRoomChangesEntity {

    @Id
    @Column("room_change_id")
    private Integer roomChangeId;

    @Column("booking_id")
    private Integer bookingId;

    @Column("user_client_id")
    private Integer userClientId;

    @Column("change_reason")
    private String changeReason;

    @Column("old_room_offer_id")
    private Integer oldRoomOfferId;

    @Column("new_room_offer_id")
    private Integer newRoomOfferId;

    @Column("additional_cost")
    private BigDecimal additionalCost;

    @Column("change_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime changeDate;

    @Column("receptionist_id")
    private Integer receptionistId;
}
