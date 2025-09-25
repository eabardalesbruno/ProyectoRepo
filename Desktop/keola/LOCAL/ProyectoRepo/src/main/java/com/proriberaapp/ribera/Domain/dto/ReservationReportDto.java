package com.proriberaapp.ribera.Domain.dto;

import lombok.Data;

import java.sql.Timestamp;
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
    private String email;
    private String countrydesc;
    private String roomNumber;
    private String dayBookingInit;
    private String dayBookingEnd;
    private String numberAdults;
    private String numberChildren;
    private String numberBabies;
    private String methodPayment;
    private String cellphone;
    private String image;
    private String address;
    private String birthdate;
    private String courtesy;

    private List<ReservationReportDto> lstCompanions;
}
