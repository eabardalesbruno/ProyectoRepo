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
@Table("emaillog")
public class EmailLogEntity {
    @Id
    @Column("emaillogid")
    private Integer emailLogId;

    @Column("recipient")
    private String recipient;

    @Column("subject")
    private String subject;

    @Column("body")
    private String body;

    @Column("sentdate")
    private Timestamp sentDate;

    @Column("status")
    private String status;
}
