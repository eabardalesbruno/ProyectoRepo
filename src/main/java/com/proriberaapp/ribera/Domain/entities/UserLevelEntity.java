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
@Table("userlevel")
public class UserLevelEntity {
    @Id
    @Column("userlevelid")
    private Integer userLevelId;
    @Column("levelname")
    private String levelName;
    @Column("leveldescription")
    private String levelDescription;
}
