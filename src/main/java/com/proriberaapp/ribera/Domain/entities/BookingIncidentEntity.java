package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@Table("bookingincident")
public class BookingIncidentEntity {
    @Id
    @Column("bookingincidentid")
    private Integer bookingIncidentId;
    @Column("bookingid")
    private Integer bookingId;
    @Column("useradminid")
    private Integer userAdminId;
    @Column("evicenceimage")
    private String evidenceImage;
    @Column("description")
    private String description;
    @Column("observation")
    private String observation;
    @Column("actionstake")
    private String actionStake;
    @Column("createdat")
    private Timestamp createdAt;

}
