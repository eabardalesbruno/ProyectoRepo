package com.proriberaapp.ribera.Domain.invoice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InvoiceResponse {
    private String key;
    private String numero;
    private boolean aceptada_por_sunat;
    private String sunat_description;
    private String sunat_note;
    private String sunat_responsecode;
    private String link_pdf;

}
