package com.proriberaapp.ribera.Domain.dto;

import com.proriberaapp.ribera.Domain.entities.FeedingEntity;
import lombok.Data;

import java.util.List;

@Data
public class ReservationReportDto {
    private String roomName;
    private String dateReservation;
    private String fullname;
    private String documentType;
    private String documentNumber;
    private String years;
    private String gender;
    private String pdfFileName;

    private List<ReservationReportDto> lstCompanions;
}
