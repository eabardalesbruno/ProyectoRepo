package com.proriberaapp.ribera.Domain.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Data
@Table("pointsexchange")
public class PointsExchangeEntity {
    @Id
    private Integer pointsexchangeid;
    private Integer userclientid;
    private Integer exchangetypeid;
    private Timestamp dateuse;
    private String exchangecode;
    private Integer serviceid;
    private String description;
    private Timestamp checkin;
    private Timestamp checkout;
    private Integer nights;
    private Double pointsquantity;
    private Double pointsused;
    private Integer pointstypeid;
    private Integer bookingid;
}