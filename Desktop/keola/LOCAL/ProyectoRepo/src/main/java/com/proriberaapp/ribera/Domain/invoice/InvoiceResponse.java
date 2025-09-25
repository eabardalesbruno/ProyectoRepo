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

    @Override
    public String toString() {
        return "InvoiceResponse{" +
                "key='" + key + '\'' +
                ", numero='" + numero + '\'' +
                ", aceptada_por_sunat=" + aceptada_por_sunat +
                ", sunat_description='" + sunat_description + '\'' +
                ", sunat_note='" + sunat_note + '\'' +
                ", sunat_responsecode='" + sunat_responsecode + '\'' +
                ", link_pdf='" + link_pdf + '\'' +
                '}';
    }

    public static InvoiceResponse dummyData(){
        return new InvoiceResponse("key","numero",false,"sunat_description","sunat_note","3","link_pdf");
    }
}
