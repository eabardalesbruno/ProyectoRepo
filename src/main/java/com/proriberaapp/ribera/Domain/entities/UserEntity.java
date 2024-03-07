package com.proriberaapp.ribera.Domain.entities;

import com.proriberaapp.ribera.Domain.enums.Role;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
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
@Table("public.user")
public class UserEntity implements UserDetails {
    @Id
    private Integer userId;
    private Integer registerTypeId;
    private Integer userLevelId;
    private Integer codeUser;
    private String firstName;
    private String lastName;
    private String nationality;
    private String documentType;
    private String documentNumber;
    private Timestamp birthDate;
    private String sex;

    private Role role;

    private String civilStatus;
    private String city;
    private String address;
    private String cellNumber;
    private String email;
    private String password;
    private String googleAuth;

    private String googleId;
    private String googleEmail;
    private String googleName;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority((role.name())));
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
