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
    private String persontype;
    private String businessname;
    private String ruc;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private Boolean isadult;
    private String address;
    private Boolean acceptedterms;
}
