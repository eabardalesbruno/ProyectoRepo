package com.proriberaapp.ribera.Infraestructure.invoice;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import com.proriberaapp.ribera.Domain.invoice.SunatInvoice;
import reactor.core.publisher.Mono;

import com.nimbusds.jose.shaded.gson.JsonArray;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceCurrency;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceType;
import com.proriberaapp.ribera.Domain.invoice.CompanyDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceBaseProcess;
import com.proriberaapp.ribera.Domain.invoice.InvoiceDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceItemDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceResponse;
import com.proriberaapp.ribera.Domain.invoice.InvoiceResponseStatus;

public class VisualContIntegration extends InvoiceBaseProcess implements SunatInvoice {
    private String url;
    private String token;

    public VisualContIntegration(@Value("sunat.api.url") String url, @Value("sunat.api.token") String token) {
        this.url = url;
        this.token = token;
    }

    @Override
    public Mono<InvoiceResponse> sendInvoice(InvoiceDomain invoice, CompanyDomain company) {
        JSONObject invoiceJson = new JSONObject();
        invoiceJson.put("type", "send_electronic_invoice");
        Map<String, Object> invoiceMap = new HashMap<>();
        JsonArray items = new JsonArray();
        invoiceMap.put("tipo", invoice.getType());
        invoiceMap.put("serie", invoice.getSerie());
        invoiceMap.put("tipo_transaccion", "1");
        invoiceMap.put("entidad_codigo", invoice.getClient().getIdentifier());
        invoiceMap.put("entidad_tipo_de_documento",
                InvoiceType.getInvoiceTypeByLenght(invoice.getClient().getIdentifier().length()));
        invoiceMap.put("entidad_numero_de_documento", invoice.getClient().getIdentifier());
        invoiceMap.put("entidad_denominacion", invoice.getClient().getName());
        invoiceMap.put("entidad_direccion", invoice.getClient().getAddress());
        invoiceMap.put("entidad_email", invoice.getClient().getEmail());
        invoiceMap.put("moneda", invoice.getCurrency().getDecimalPlaces());
        if (invoice.getCurrency().equals(InvoiceCurrency.USD)) {
            invoiceMap.put("tipo_cambio", invoice.getTc());
        }
        invoiceMap.put("porcentaje_igv", invoice.getTc());
        invoiceMap.put("total_igv", invoice.getTotalIgv());
        invoiceMap.put("total", invoice.getTotalPayment());
        invoiceMap.put("fecha_de_emision", DateFormat.getInstance().format("dd-MM-yyyy"));
        invoiceMap.put("items", items);
        invoiceJson.put("invoice", invoiceMap);
        invoice.getItems().stream().map(item -> {
            JSONObject itemJson = new JSONObject();
            itemJson.put("descripcion", item.getDescription());
            itemJson.put("cantidad", item.getQuantity());
            itemJson.put("precio_unitario", item.getPriceUnit().toString());
            itemJson.put("total", item.getTotal());
            itemJson.put("igv", item.getIgv());
            return itemJson;
        });

        WebClient client = this.configureFetchVisualCont();
        Mono<InvoiceResponse> response = client.post()
                .bodyValue(invoiceJson)
                .retrieve().bodyToMono(String.class).map(json -> {
                    JSONObject jsonResponse = new JSONObject(json);
                    JSONObject invoiceResponse = jsonResponse.getJSONObject("invoice");
                    String key = invoiceResponse.getString("key");
                    boolean aceptada_por_sunat = invoiceResponse.getBoolean("aceptada_por_sunat");
                    String numero = invoiceResponse.getString("numero");
                    String sunat_description = invoiceResponse.getString("sunat_description");
                    String sunat_note = invoiceResponse.getString("sunat_note");
                    String sunat_responsecode = invoiceResponse.getString("sunat_responsecode");
                    String link_pdf = invoiceResponse.getString("link_pdf");
                    return new InvoiceResponse(key,
                            numero, aceptada_por_sunat, sunat_description, sunat_note,
                            sunat_responsecode, link_pdf);
                });
        return response;
    }

    private WebClient configureFetchVisualCont() {
        String token = new StringBuilder().append("Token ").append(this.token).toString();
        WebClient client = super.configureFetch();
        client.head().header("authorization", token);

        return client;
    }

    @Override
    public Mono<InvoiceResponseStatus> getInvoiceStatus(String key) {
        JSONObject invoiceStatusJson = new JSONObject();
        WebClient client = this.configureFetchVisualCont();
        Map<String, String> invoice = Map.of("key", key);
        invoiceStatusJson.put("type", "status_invoice");
        invoiceStatusJson.put("invoice", invoice);
        Mono<InvoiceResponseStatus> response = client.post()
                .bodyValue(invoiceStatusJson)
                .retrieve()
                .bodyToMono(String.class)
                .map(body -> {
                    JSONObject jsonResponse = new JSONObject(body);
                    JSONObject invoiceJson = jsonResponse.getJSONObject("invoice");
                    boolean aceptada_por_sunat = invoiceJson.getBoolean("aceptada_por_sunat");
                    String sunat_description = invoiceJson.getString("sunat_description");
                    String sunat_note = invoiceJson.getString("sunat_note");
                    String sunat_responsecode = invoiceJson.getString("sunat_responsecode");
                    String link_pdf = invoiceJson.getString("link_pdf");
                    return new InvoiceResponseStatus(aceptada_por_sunat, sunat_description, sunat_note,
                            sunat_responsecode, link_pdf);
                });
        return response;
    }

    @Override
    protected String getToken() {
        return this.token;
    }

    @Override
    protected String getUrl() {
        return this.url;
    }

}
