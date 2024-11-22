package com.proriberaapp.ribera.Infraestructure.invoice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import com.proriberaapp.ribera.Domain.invoice.SunatInvoice;
import reactor.core.publisher.Mono;

import com.nimbusds.jose.shaded.gson.JsonObject;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceClientTypeDocument;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceCurrency;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceType;
import com.proriberaapp.ribera.Domain.invoice.CompanyDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceBaseProcess;
import com.proriberaapp.ribera.Domain.invoice.InvoiceDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceItemDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceResponse;
import com.proriberaapp.ribera.Domain.invoice.InvoiceResponseStatus;
import org.springframework.http.MediaType;

public class VisualContIntegration extends InvoiceBaseProcess implements SunatInvoice {
    @Value("${sunat.api.url}")
    private String url;
    @Value("${sunat.api.token}")
    private String token;

    /* public VisualContIntegration() { */
    /* this.url = "https://e-vf.softwareintegrado.com/vc-cpe/api/v1"; */
    /*
     * this.token =
     * "4a4ea1cca6c51af7f30b567cd7fa0bd5bc22abaea0bac119bdc4a00bc5edc6fd";
     */
    /* } */

    @Override
    public Mono<InvoiceResponse> sendInvoice(InvoiceDomain invoice, CompanyDomain company) {
        WebClient client = this.configureFetchVisualCont();
        JSONObject invoiceJson = this.formatJson(invoice);
        Mono<InvoiceResponse> response = client.post()
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Token token=" + this.token)
                .bodyValue(invoiceJson.toString())
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), clientResponse -> {
                    return clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                        System.err.println("Error response: " + errorBody);
                        return Mono.error(new RuntimeException("Failed to send invoice: " + errorBody));
                    });
                })
                .bodyToMono(String.class).map(json -> {
                    JSONObject jsonResponse = new JSONObject(json);
                    JSONObject invoiceResponse = jsonResponse.getJSONObject("invoice");
                    String key = invoiceResponse.getString("key");
                    boolean aceptada_por_sunat = invoiceResponse.getBoolean("aceptada_por_sunat");
                    String numero = String.valueOf(invoiceResponse.getInt("numero"));
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

    @Override
    public JSONObject formatJson(InvoiceDomain invoice) {
        JSONObject invoiceJson = new JSONObject();
        invoiceJson.put("type", "send_electronic_invoice");
        JSONObject invoiceMap = new JSONObject();
        JSONArray items = new JSONArray();
        invoiceMap.put("tipo", InvoiceType.getInvoiceTypeByName(invoice.getType()).getCode().toString());
        invoiceMap.put("serie", invoice.getSerie());
        invoiceMap.put("tipo_transaccion", "1");
        invoiceMap.put("fecha_de_vencimiento", "");
        invoiceMap.put("tipo_cambio", invoice.getTc());
        invoiceMap.put("porcentaje_igv", String.valueOf(invoice.getTaxPercentaje()));
        invoiceMap.put("fecha_de_emision", new SimpleDateFormat("dd-MM-yyyy").format(Date.from(Instant.now())));
        invoiceMap.put("entidad_codigo", invoice.getClient().getIdentifier());
        invoiceMap.put("entidad_tipo_de_documento",
                InvoiceClientTypeDocument
                        .getInvoiceClientTypeDocumentByLenght(invoice.getClient().getIdentifier().length())
                        .getCode());
        invoiceMap.put("entidad_numero_de_documento", invoice.getClient().getIdentifier());
        invoiceMap.put("entidad_denominacion", invoice.getClient().getName());
        invoiceMap.put("entidad_direccion", invoice.getClient().getAddress());
        invoiceMap.put("entidad_email", invoice.getClient().getEmail());
        invoiceMap.put("moneda", invoice.getCurrency().getDecimalPlaces());
        invoiceMap.put("number", String.valueOf(invoice.getCorrelative()));
        invoiceMap.put("codigo_unico", String.valueOf(invoice.getCorrelative()).concat("-").concat(
                invoice.getClient().getIdentifier()));
        invoiceMap.put("descuento_global", 0);
        invoiceMap.put("total_descuento", 0);
        invoiceMap.put("total_anticipo", 0);
        invoiceMap.put("total_anticipo", 0);
        invoiceMap.put("total_exportacion", 0);
        invoiceMap.put("total_gravada", invoice.getSubtotal().doubleValue());
        invoiceMap.put("total_inafecta", 0);
        invoiceMap.put("total_exonerada", 0);
        invoiceMap.put("total_igv", invoice.getTotalIgv().doubleValue());
        invoiceMap.put("total_isc", 0);
        invoiceMap.put("operacion_gratuita", "false");
        invoiceMap.put("total_gratuita", 0);
        invoiceMap.put("total_otros_cargos", 0);
        invoiceMap.put("total_icbper", 0);
        invoiceMap.put("total", invoice.getTotalPayment().doubleValue());
        invoiceMap.put("total_percepcion", 0);
        invoiceMap.put("percepcion_tipo", "0");
        invoiceMap.put("total_incluido_percepcion", 0);
        invoiceMap.put("total_percepcion", 0);
        invoiceMap.put("percepcion_base_imponible", 0);
        invoiceMap.put("observaciones", "");
        invoiceMap.put("documento_que_se_modifica_tipo", "");
        invoiceMap.put("documento_que_se_modifica_serie", "");
        invoiceMap.put("documento_que_se_modifica_numero", "");
        invoiceMap.put("tipo_de_nota_de_credito", "");
        invoiceMap.put("tipo_de_nota_de_debito", "");
        invoiceMap.put("enviar_automaticamente_a_la_sunat", "false");
        invoiceMap.put("enviar_automaticamente_al_cliente", "false");
        invoiceMap.put("cancelado", "true");
        invoiceMap.put("condiciones_de_pago", "");
        invoiceMap.put("condicion_de_pago_tipo", "1");
        invoiceMap.put("condicion_de_pago_dias", "");
        // Aca falta el tema de pago con tarjeta para llenar el numero de operacion que
        // viene por la pasarela de pago
        invoiceMap.put("numero_operacion", "FAKE_NUMBER");
        invoiceMap.put("orden_compra_servicio", "");
        invoiceMap.put("vendedor_codigo", "");
        invoiceMap.put("tarjeta_bonus", "");

        for (int index = 0; index < invoice.getItems().size(); index++) {
            JSONObject itemJson = new JSONObject();
            InvoiceItemDomain item = invoice.getItems().get(index);
            itemJson.put("item", index + 1);
            itemJson.put("unit_code", item.getUnitOfMeasurement());
            itemJson.put("codigo_producto", item.getCodProductSunat());
            itemJson.put("unidad_de_medida", item.getUnitOfMeasurement());
            itemJson.put("codigo_detraccion", "");
            itemJson.put("descripcion_producto", item.getDescription());
            itemJson.put("codigo_plu", "");
            itemJson.put("codigo_categoria", "");
            itemJson.put("nombre_categoria", "");
            itemJson.put("codigo_producto_sunat", "90111800");
            itemJson.put("cantidad", item.getQuantity());
            itemJson.put("valor_unitario", item.getValorUnitario().doubleValue()); // Precio sin IGV);
            itemJson.put("precio_unitario", item.getPriceUnit().doubleValue());
            itemJson.put("igv", item.getIgv().doubleValue());
            itemJson.put("tipo_de_igv", "1");
            itemJson.put("descuento", 0);
            itemJson.put("subtotal", item.getSubtotal().doubleValue());
            itemJson.put("total", item.getTotal().doubleValue());
            itemJson.put("icbper", 0);
            items.put(itemJson);
        }
        invoiceMap.put("invoice_lines", items);
        invoiceMap.put("invoice_guides", new JSONArray());
        invoiceJson.put("invoice", invoiceMap);
        return invoiceJson;
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
        JSONObject invoice = new JSONObject();
        invoiceStatusJson.put("type", "status_invoice");
        invoice.put("key", key);
        invoiceStatusJson.put("invoice", invoice);
        Mono<InvoiceResponseStatus> response = client.post()
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Token " + this.token)
                .bodyValue(invoiceStatusJson)
                .retrieve()
                .bodyToMono(String.class)
                .map(body -> {
                    System.out.println(body);
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
