package com.proriberaapp.ribera.Domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "reservation_time_type")
@Getter
@Setter
@Builder
public class ReservationTimeTypeEntity {
    @Id
    @Column("id_reservation_time_type")
    private Integer idReservationTimeType;

    @Column("type_name")
    private String typeName;

    @Column("standby_hours")
    private Integer standbyHours;

    @Column("status")
    private Integer status;

    @Column("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;

    @Column("created_by")
    private String createdBy;

    @Column("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime updatedAt;

    @Column("updated_by")
    private String updatedBy;
}
