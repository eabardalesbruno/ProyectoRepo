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

@Table(name = "booking_cancellations")
@Getter
@Setter
@Builder
public class BookingCancellationsEntity {

    @Id
    @Column("cancellation_id")
    private Integer cancellationId;

    @Column("booking_id")
    private Integer bookingId;

    @Column("user_client_id")
    private Integer userClientId;

    @Column("cancellation_reason")
    private String cancellationReason;

    @Column("additional_cost")
    private BigDecimal additionalCost;

    @Column("cancellation_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime cancellationDate;

    @Column("receptionist_id")
    private Integer receptionistId;
}
