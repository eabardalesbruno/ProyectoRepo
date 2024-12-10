package com.proriberaapp.ribera.utils.emails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmailRequest {
    private String recipient;
    private String subject;

}
