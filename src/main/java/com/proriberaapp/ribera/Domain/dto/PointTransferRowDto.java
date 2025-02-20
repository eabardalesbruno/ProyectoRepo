package com.proriberaapp.ribera.Domain.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointTransferRowDto extends PointTransactionBaseRowDto {
    private double pointsamount;
    private String created_at;
    private Integer userreceiveid;
    private String userreceivename;
    private String userreceivelastname;
    private Integer usersenderid;
    private String usersendername;
    private String usersenderlastname;
}
