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
@Table("roomtype")
public class RoomTypeEntity {
    @Id
    @Column("roomtypeid")
    private Integer roomTypeId;
    @Column("roomtype")
    private RoomType roomType;
    @Column("roomtypename")
    private String roomTypeName;
    @Column("roomtypedescription")
    private String roomTypeDescription;
    @Column("roomstateid")
    private Integer roomstateid;

    @Getter
    public enum RoomType {
        DEPARTAMENTO("Departamento"),
        HABITACION("Habitación");

        private final String value;

        RoomType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
