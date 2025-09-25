package com.proriberaapp.ribera.Domain.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.annotation.Transient;

import com.proriberaapp.ribera.Api.controllers.client.dto.quotationDayDto;

@Getter
@Setter
public class PointQuotationRawDto {
    private Integer id;
    private Integer pointstypeid;
    private String pointstypedesc;
    private Integer offertypeid;
    private String offertypename;
    private double costPerNight;
    private Integer status;
}
