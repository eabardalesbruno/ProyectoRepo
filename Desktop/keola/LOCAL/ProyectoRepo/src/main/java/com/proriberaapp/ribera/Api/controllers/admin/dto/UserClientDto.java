package com.proriberaapp.ribera.Api.controllers.admin.dto;

import com.proriberaapp.ribera.Domain.enums.StatesUser;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

import java.sql.Timestamp;

@Data
public class UserClientDto {
    @Column("userclientid")
    private Integer userClientId;
    @Column("registertypeid")
    private Integer registerTypeId;
    @Column("userlevelid")
    private Integer userLevelId;
    @Column("countryid")
    private Integer countryId;
    @Column("codeuser")
    private Integer codeUser;
    @Column("firstname")
    private String firstName;
    @Column("lastname")
    private String lastName;
    @Column("genderid")
    private Integer genderId;

    @Column("documenttypeid")
    private Integer documenttypeId;
    @Column("documentnumber")
    private String documentNumber;
    @Column("birthdate")
    private Timestamp birthDate;

    private Integer role;
    @Column("civilstatus")
    private String civilStatus;
    @Column("city")
    private String city;
    @Column("address")
    private String address;
    @Column("cellnumber")
    private String cellNumber;
    private String email;
    private String password;
    @Column("googleauth")
    private String googleAuth;
    @Column("googleid")
    private String googleId;
    @Column("googleemail")
    private String googleEmail;
    @Column("username")
    private String username;

    @Column("walletid")
    private Integer walletId;

    private StatesUser status;
    private Timestamp createdat;

    private String genderdesc;
    private String countrydesc;
    private String levelname;
    private String statusdesc;
    private Integer nationalityId;

}
