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
@Table("rankpromoter")
public class RankPromoterEntity  {

   /* @Id
    @Column("rankpromoterid")
    private Integer rankPromoterId ;

    @Column("rankname")
    private String rankName;

    @Column("rankdescription")
    private String rankDescription;


    //si se puede subir imagenes ala base de datos
    @Column("rankimage")
    private String rankImage;

    @Column("level")
    private Integer level;
    */
}
