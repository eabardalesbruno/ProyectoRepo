package com.proriberaapp.ribera.Domain.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Data
@Table("roomimages")
public class RoomImagesEntity {

    @Id
    @Column("id")
    private Integer id;

    @Column("roomid")
    private Integer roomId;

    @Column("imagepath")
    private String imagePath;
}
