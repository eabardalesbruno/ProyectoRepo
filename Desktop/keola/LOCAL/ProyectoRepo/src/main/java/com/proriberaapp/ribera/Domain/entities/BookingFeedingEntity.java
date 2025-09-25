package com.proriberaapp.ribera.Domain.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("booking_feeding")
public class BookingFeedingEntity {
    @Id
    @Column("bookingFeedingId")
    private Long bookingFeedingId;
    @Column("bookingId")
    private Long bookingId;
    @Column("feedingId")
    private Long feedingId;
    @Column("bookingfeedingamout")
    private Float bookingfeedingamout;
}