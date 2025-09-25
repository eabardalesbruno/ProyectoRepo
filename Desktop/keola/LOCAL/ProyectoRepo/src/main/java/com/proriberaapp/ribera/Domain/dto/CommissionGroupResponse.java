package com.proriberaapp.ribera.Domain.dto;

import com.proriberaapp.ribera.Domain.entities.CommissionEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Data
public class CommissionGroupResponse {
    private BigDecimal totalCommissionAmount;
    private int numberOfCommissions;
    private Timestamp firstDisbursementDate;
    private Timestamp lastDisbursementDate;

    private BigDecimal totalFirstHalfCommissionAmount;
    private BigDecimal totalSecondHalfCommissionAmount;
    private int numberOfFirstHalfCommissions;
    private int numberOfSecondHalfCommissions;

    private List<CommissionEntity> firstHalfCommissions;
    private List<CommissionEntity> secondHalfCommissions;
}
