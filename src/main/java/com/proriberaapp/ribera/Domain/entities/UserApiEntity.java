package com.proriberaapp.ribera.Domain.entities;

import com.proriberaapp.ribera.Domain.enums.Role;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@Table("userclient")
public class UserApiEntity implements UserDetails {
    @Id
    @Column("userclientid")
    private Integer userClientId;
    @Column("registertypeid")
    private Integer registerTypeId;
    @Column("userlevelid")
    private Integer userLevelId;
    @Column("codeuser")
    private Integer codeUser;
    @Column("firstname")
    private String firstName;
    @Column("lastname")
    private String lastName;
    @Column("nationalityid")
    private Integer nationalityId;
    @Column("genderid")
    private Integer genderId;
    @Column("areazoneid")
    private Integer areazoneId;
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
    private String username;
    public UserApiEntity() {}

    public UserApiEntity(Integer userClientId, Integer registerTypeId, Integer userLevelId, Integer codeUser,
                         String firstName, String lastName, Integer nationalityId, Integer genderId,
                         Integer areazoneId, Integer documenttypeId, String documentNumber,
                         Timestamp birthDate, Integer role, String civilStatus, String city,
                         String address, String cellNumber, String email, String password,
                         String googleAuth, String googleId, String googleEmail, String username) {
        this.userClientId = userClientId;
        this.registerTypeId = registerTypeId;
        this.userLevelId = userLevelId;
        this.codeUser = codeUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationalityId = nationalityId;
        this.genderId = genderId;
        this.areazoneId = areazoneId;
        this.documenttypeId = documenttypeId;
        this.documentNumber = documentNumber;
        this.birthDate = birthDate;
        this.role = role;
        this.civilStatus = civilStatus;
        this.city = city;
        this.address = address;
        this.cellNumber = cellNumber;
        this.email = email;
        this.password = password;
        this.googleAuth = googleAuth;
        this.googleId = googleId;
        this.googleEmail = googleEmail;
        this.username = username;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority((role.toString())));
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
