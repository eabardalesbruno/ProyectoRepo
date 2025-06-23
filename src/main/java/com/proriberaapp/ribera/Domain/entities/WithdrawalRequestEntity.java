package com.proriberaapp.ribera.Domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("withdrawal_requests")
public class WithdrawalRequestEntity {

    @Id
    @Column("withdrawal_request_id")
    private Integer withdrawalRequestId;

    @Column("operation_number")
    private String operationNumber;

    @Column("user_id")
    private Integer userId;

    @Column("wallet_id")
    private Integer walletId;

    @Column("amount")
    private BigDecimal amount;

    @Column("destination_bank")
    private String destinationBank;
    
    @Column("account_number")
    private String accountNumber;

    @Column("account_holder_name")
    private String accountHolderName;

    @Column("account_holder_document")
    private String accountHolderDocument;
    
    @Column("country")
    private String country;

    @Column("document_type")
    private String documentType;

    @Column("observation")
    private String observation;

    @Column("status")
    private String status; 

    @Column("creation_date")
    private Timestamp creationDate;

    @Column("update_date")
    private Timestamp updateDate;

    @Column("reject_reason")
    private String rejectReason;

    @Column("reject_message")
    private String rejectMessage;
} 