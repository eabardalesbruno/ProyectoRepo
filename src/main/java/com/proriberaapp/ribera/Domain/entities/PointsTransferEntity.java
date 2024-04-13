package com.proriberaapp.ribera.Domain.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Data
@Table("pointstransfers")
public class PointsTransferEntity {
    @Id
    private Integer pointstransferid;
    private Integer senderid;
    private Integer requesttypeid;
    private Timestamp datetransfer;
    private Integer receiverid;
    private Double pointstransfered;
    private Integer pointstypeid;
    private Integer sendandreceiveid;
}
