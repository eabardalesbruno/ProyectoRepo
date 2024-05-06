package com.proriberaapp.ribera.Domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("termsversion")
public class TermsVersionEntity {

    @Id
    private Integer versionId;

    @Column("userclientid")
    private Integer userClientId;

    @Column("s3_url")
    private String s3Url;

    private Boolean active;

    @Column("createdat")
    private Timestamp createdAt;
}
