package com.proriberaapp.ribera.Domain.entities;


import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("transactioncategorywallet")
public class TransactionCategoryEntity {
    @Id
    @Column("transactioncategoryid")
    private Integer transactionCategoryId;
    @Column("transactioncategoryname")
    private String transactionCategoryName;
    @Column("transactioncategorydescription")
    private String transactionCategoryDescription;










}
