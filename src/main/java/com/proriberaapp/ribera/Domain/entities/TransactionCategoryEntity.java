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
@Table("transactioncategory")
public class TransactionCategoryEntity {
    @Id
    @Column("transactioncategoryid")
    private Integer transactionCategoryId;
    @Column("transactioncategoryname")
    private String transactionCategoryName;

}
