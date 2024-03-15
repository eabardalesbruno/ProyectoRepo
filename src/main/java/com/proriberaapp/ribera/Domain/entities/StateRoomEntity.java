package com.proriberaapp.ribera.Domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("stateroom")
public class StateRoomEntity {
    @Id
    @Column("stateroomid")
    private Integer stateRoomId;
    @Column("stateroomname")
    private String stateRoomName;
}
