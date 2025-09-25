package com.proriberaapp.ribera.utils.emails;

import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@AllArgsConstructor
@Getter
public class bookingRejectUserEmailDto {
    @Column("roomname")
    private String roomName;
    @Column("clientname")
    private String clientName;

    @Column("clientemail")
    private String clientEmail;
}
