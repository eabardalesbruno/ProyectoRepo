package com.proriberaapp.ribera.Domain.entities;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("typewallettransaction")
public class TypeWalletTransactionEntity {

    @Id
    @Column("typewallettransactionid")
    private Integer typeWalletTransactionId;

    @Column("transactioncategoryid")
    private Integer transactionCategoryId;

    @Column("description")
    private String description;

}
