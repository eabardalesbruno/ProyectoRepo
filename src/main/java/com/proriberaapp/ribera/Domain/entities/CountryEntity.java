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
@Table("country")
public class CountryEntity {

    @Id
    @Column("countryid")
    private Integer countryId;
    @Column("countrydesc")
    private String countryDesc;
    private String iso;
    @Column("nicename")
    private String niceName;
    private String iso3;
    @Column("numcode")
    private Integer numCode;
    @Column("phonecode")
    private Integer phoneCode;
    private String symbol;
    private String courtesy;

}
