package com.proriberaapp.ribera.Api.controllers.admin.dto;

import com.proriberaapp.ribera.Domain.enums.Permission;
import com.proriberaapp.ribera.Domain.enums.Role;
import com.proriberaapp.ribera.Domain.enums.StatesUser;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

import java.sql.Timestamp;
import java.util.List;

@Data
public class UserPromoterDto {

    @Column("userpromoterid")
    private Integer userPromoterId;

    private String email;
    private String password;
    @Column("username")
    private String username;
    @Column("firstname")
    private String firstName;
    @Column("lastname")
    private String lastName;
    private String phone;
    private String address;

    @Column("documenttypeid")
    private Integer documenttypeId;
    @Column("documentnumber")
    private String documentNumber;

    private Role role;
    private StatesUser status;
    private List<Permission> permission;
    @Column("createdat")
    private Timestamp createdAt;
    @Column("createdid")
    private Integer createdId;
    @Column("updatedat")
    private Timestamp updatedAt;
    @Column("updatedid")
    private Integer updatedId;

    @Column("googleauth")
    private String googleAuth;
    @Column("googleid")
    private String googleId;
    @Column("googleemail")
    private String googleEmail;

    @Column("genderid")
    private Integer genderId;

    @Column("walletid")
    private Integer walletId;

    private String genderdesc;

}
