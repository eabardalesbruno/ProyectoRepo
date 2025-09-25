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
@Table("roomstate")
public class RoomStateEntity {
    @Id
    @Column("roomstateid")
    private Integer roomStateId;

    @Column("roomstatename")
    private String roomStateName;

    @Column("roomstatedescription")
    private String roomStateDescription;
}
