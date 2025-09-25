package com.proriberaapp.ribera.Domain.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Table("company")
@Getter
@Setter
@AllArgsConstructor
public class CompanyEntity {
    @Id
    private int id;
    @Column("name")
    private String name;
    @Column("address")
    private String address;
    @Column("phone")
    private String phone;
    @Column("email")
    private String email;
    @Column("ruc")
    private String ruc;
    private String createdAt;
    private String updatedAt;

}
