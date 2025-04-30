package com.proriberaapp.ribera.Domain.dto;

import com.proriberaapp.ribera.Domain.entities.CategoryRoomType;
import com.proriberaapp.ribera.Domain.entities.RoomStateEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Getter
@Setter
public class RoomTypeDto {

    private Integer roomTypeId;

    private RoomType roomType;

    private String roomTypeName;

    private String roomTypeDescription;

    private Integer roomstateid;

    private RoomStateEntity roomState;

    private CategoryRoomType category;

    private List<RoomDto> roomnumbers;

    @Getter
    public enum RoomType {
        DEPARTAMENTO("Departamento"),
        HABITACION("Habitación"),
        SUITE("Suite");

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
