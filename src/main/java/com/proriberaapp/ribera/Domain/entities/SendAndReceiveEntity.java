package com.proriberaapp.ribera.Domain.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("sendandreceive")
public class SendAndReceiveEntity {
    @Id
    private Integer sendandreceiveid;
    private String sendandreceivedesc;
}