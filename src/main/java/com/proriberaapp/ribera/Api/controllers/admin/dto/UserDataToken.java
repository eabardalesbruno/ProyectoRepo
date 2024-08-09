package com.proriberaapp.ribera.Api.controllers.admin.dto;

import lombok.*;

import java.security.Principal;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDataToken implements Principal {

    private Integer id;

    private String name;
    private String document;
    private String roles;
    private String permissions;
    private String state;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDataToken that = (UserDataToken) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
