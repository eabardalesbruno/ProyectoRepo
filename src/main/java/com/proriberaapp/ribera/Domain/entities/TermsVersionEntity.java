package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@Table("termsversion")
public class TermsVersionEntity {

    @Id
    @Column("versionid")
    private Integer versionId;

    @Column("s3url")
    private String s3Url;

    @Column("createdat")
    private Timestamp createdAt;
}