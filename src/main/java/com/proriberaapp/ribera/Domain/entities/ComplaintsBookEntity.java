package com.proriberaapp.ribera.Domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("complaintsbook")
public class ComplaintsBookEntity {
    @Id
    private Integer id;
    private String personType;
    private String businessName;
    private String ruc;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Boolean isAdult;
    private String address;
    private Boolean acceptedTerms;
}
