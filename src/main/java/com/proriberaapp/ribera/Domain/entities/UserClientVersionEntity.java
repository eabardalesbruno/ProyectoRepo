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
@Table("userclientversion")
public class UserClientVersionEntity {

    @Id
    @Column("userclientversionid")
    private Integer userClientVersionId;

    @Column("userclientid")
    private Integer userClientId;

    @Column("versionid")
    private Integer versionId;

    private Boolean active;

    @Column("createdat")
    private Timestamp createdAt;
}