package com.proriberaapp.ribera.entities.admin;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Table("beneficiary_checkin")
public class BeneficiaryCheckin {
    @Id
    private Long id;
    @org.springframework.data.relational.core.mapping.Column("id_beneficiary")
    private String idBeneficiary;
    @org.springframework.data.relational.core.mapping.Column("name")
    private String name;
    @org.springframework.data.relational.core.mapping.Column("lastname")
    private String lastname;
    @org.springframework.data.relational.core.mapping.Column("idsuscription")
    private String idsuscription;
    @org.springframework.data.relational.core.mapping.Column("membership")
    private String membership;
    @org.springframework.data.relational.core.mapping.Column("document_number")
    private String documentNumber;
    @org.springframework.data.relational.core.mapping.Column("checkin_at")
    private LocalDateTime checkinAt;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdBeneficiary() {
        return idBeneficiary;
    }

    public void setIdBeneficiary(String idBeneficiary) {
        this.idBeneficiary = idBeneficiary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getIdsuscription() {
        return idsuscription;
    }

    public void setIdsuscription(String idsuscription) {
        this.idsuscription = idsuscription;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public LocalDateTime getCheckinAt() {
        return checkinAt;
    }

    public void setCheckinAt(LocalDateTime checkinAt) {
        this.checkinAt = checkinAt;
    }
}
