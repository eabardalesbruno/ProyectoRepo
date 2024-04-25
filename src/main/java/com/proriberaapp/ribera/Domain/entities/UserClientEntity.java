package com.proriberaapp.ribera.Domain.entities;

import com.proriberaapp.ribera.Domain.enums.StatesUser;
import lombok.Builder;
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
public class UserClientEntity implements UserDetails {
    @Id
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
    private String username;

    private StatesUser status;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority((role.toString())));
    }

    @Override
    public String getUsername() {
        return username;
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
