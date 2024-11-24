package com.proriberaapp.ribera.Domain.entities;

import com.proriberaapp.ribera.Domain.enums.Permission;
import com.proriberaapp.ribera.Domain.enums.Role;
import com.proriberaapp.ribera.Domain.enums.StatesUser;
import com.proriberaapp.ribera.Domain.enums.TypeDocument;
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
@Table("userpromoter")
public class UserPromoterEntity implements UserDetails {
    @Id
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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority((role.name())));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
