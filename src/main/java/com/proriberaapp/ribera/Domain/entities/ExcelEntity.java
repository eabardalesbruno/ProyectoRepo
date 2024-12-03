package com.proriberaapp.ribera.Domain.entities;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@Table("excel_view")
@AllArgsConstructor
@Builder
public class ExcelEntity {

    @Column("keysupplier")
    private String keySupplier;

    @Column("id")
    private UUID id;

    @Column("serie")
    private String serie;

    @Column("identifierclient")
    private String identifierClient;

    @Column("createdat")
    @CreatedDate
    // private Date createdAt;
    private LocalDateTime createdAt;

    @Column("idcurrency")
    private int idCurrency;

    @Column("tc")
    private double tc;

    @Column("totaligv")
    private double totalIgv;

    @Column("totalpayment")
    private double totalPayment;

    @Override
    public String toString() {
        return "ExcelEntity [keySupplier=" + keySupplier + ", id=" + id + ", serie=" + serie + 
               ", identifierClient=" + identifierClient + ", createdAt=" + createdAt + 
               ", idCurrency=" + idCurrency + ", tc=" + tc + ", totalIgv=" + totalIgv + 
               ", totalPayment=" + totalPayment + "]";
    }
}

