package com.proriberaapp.ribera.Infraestructure.invoice;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import com.proriberaapp.ribera.Domain.entities.CurrencyTypeEntity;
import com.proriberaapp.ribera.Domain.entities.Invoice.InvoiceEntity;
import com.proriberaapp.ribera.Domain.entities.Invoice.InvoiceItemEntity;
import com.proriberaapp.ribera.Domain.entities.Invoice.InvoiceStatusEntity;
import com.proriberaapp.ribera.Domain.entities.Invoice.InvoiceTypeEntity;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceCurrency;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceStatus;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceType;
import com.proriberaapp.ribera.Domain.invoice.CompanyDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceClientDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceItemDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceResponse;
import com.proriberaapp.ribera.Domain.invoice.SunatInvoice;
import com.proriberaapp.ribera.Infraestructure.repository.CurrencyTypeRepository;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.InvoiceItemRepository;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.InvoiceRepository;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.InvoiceStateRepository;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.InvoiceTypeRepsitory;
import com.proriberaapp.ribera.services.invoice.InvoiceServiceI;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class VisualContIntegrationTest {

        @Mock
        private WebClient webClient;

        @Mock
        private WebClient.Builder webClientBuilder;

        @Autowired
        private SunatInvoice sunatInvoice;
        @Autowired
        private InvoiceStateRepository invoiceStatusRepository;

        @Autowired
        private InvoiceTypeRepsitory invoiceTypeRepsitory;

        @Autowired
        private InvoiceRepository invoiceRepsitory;

        @Autowired
        private CurrencyTypeRepository currencyTypeRepository;

        @Autowired
        private InvoiceItemRepository invoiceItemRepository;

        @Autowired
        private InvoiceServiceI invoiceService;

        @BeforeEach
        public void setUp() {
                MockitoAnnotations.openMocks(this);
                when(webClientBuilder.build()).thenReturn(webClient);
        }

        @Test
        public void testSendInvoiceWithIgvIncluded() {
                CompanyDomain company = new CompanyDomain("ddd", "1233", "wdwd", "dwdwd", "wdwdw", "wdwdw", "wdwd");
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "78804372", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 18.0, InvoiceCurrency.PEN, InvoiceType.BOLETA,
                                new BigDecimal(0));
                invoice.addItemWithIncludedIgv(new InvoiceItemDomain("Item 1", "aaa", 1, new BigDecimal(58)));
                invoice.calculatedTotals();
                Mono<InvoiceResponse> response = sunatInvoice.sendInvoice(invoice, company);
                StepVerifier.create(response)
                                .expectNextMatches(invoiceResponse -> {
                                        System.out.println(invoiceResponse);
                                        return !invoiceResponse.getKey().isEmpty();
                                })
                                .verifyComplete();
        }

        @Test
        public void testSendInvoice() {
                CompanyDomain company = new CompanyDomain("ddd", "1233", "wdwd", "dwdwd", "wdwdw", "wdwdw", "wdwd");
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "78804372", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 18.0, InvoiceCurrency.PEN, InvoiceType.BOLETA,
                                new BigDecimal(0));
                invoice.addItem(new InvoiceItemDomain("Item 1", "aaa", 1, new BigDecimal(50)));
                invoice.addItem(new InvoiceItemDomain("Item 2", "aaa", 1, new BigDecimal(20)));
                invoice.addItem(new InvoiceItemDomain("Item 3", "aaa", 1, new BigDecimal(30)));
                invoice.calculatedTotals();
                Mono<InvoiceResponse> response = sunatInvoice.sendInvoice(invoice, company);

                StepVerifier.create(response)
                                .expectNextMatches(invoiceResponse -> {
                                        System.out.println(invoiceResponse);
                                        return !invoiceResponse.getKey().isEmpty();
                                })
                                .verifyComplete();
        }

        @Test
        public void testFormatJsonFacture() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "12345678912", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 19.0, InvoiceCurrency.PEN, InvoiceType.BOLETA,
                                new BigDecimal(0));
                invoice.addItem(new InvoiceItemDomain("Item 1", "aaa", 1, new BigDecimal(50)));
                invoice.addItem(new InvoiceItemDomain("Item 2", "aaa", 1, new BigDecimal(20)));
                invoice.addItem(new InvoiceItemDomain("Item 3", "aaa", 1, new BigDecimal(30)));
                JSONObject json = sunatInvoice.formatJson(invoice);
                StepVerifier.create(Mono.just(json))
                                .expectNextMatches(jsonObject -> {
                                        try {
                                                return jsonObject.getString("type").equals("send_electronic_invoice")
                                                                && jsonObject.getJSONObject("invoice").getString("tipo")
                                                                                .equals("1")
                                                                && jsonObject.getJSONObject("invoice")
                                                                                .getString("serie").equals("F001")
                                                                && jsonObject.getJSONObject("invoice")
                                                                                .getString("entidad_tipo_de_documento")
                                                                                .equals("6");
                                        } catch (JSONException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                                return false;
                                        }
                                })
                                .verifyComplete();

                // Add assertions to verify the JSON structure
        }

        @Test
        public void testFormatJsonBoleta() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "71837677", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 19.0, InvoiceCurrency.PEN, InvoiceType.BOLETA,
                                new BigDecimal(0));
                invoice.addItem(new InvoiceItemDomain("Item 1", "aaa", 1, new BigDecimal(50)));
                invoice.addItem(new InvoiceItemDomain("Item 2", "aaa", 1, new BigDecimal(20)));
                invoice.addItem(new InvoiceItemDomain("Item 3", "aaa", 1, new BigDecimal(30)));
                JSONObject json = sunatInvoice.formatJson(invoice);
                StepVerifier.create(Mono.just(json))
                                .expectNextMatches(jsonObject -> {
                                        try {
                                                return jsonObject.getString("type").equals("send_electronic_invoice")
                                                                && jsonObject.getJSONObject("invoice").getString("tipo")
                                                                                .equals("2")
                                                                && jsonObject.getJSONObject("invoice")
                                                                                .getString("serie").equals("B001")
                                                                && jsonObject.getJSONObject("invoice")
                                                                                .getString("entidad_tipo_de_documento")
                                                                                .equals("1");
                                        } catch (JSONException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                                return false;
                                        }
                                })
                                .verifyComplete();
        }

        @Test
        public void testFormatBaseJson() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "71837677", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 19.0, InvoiceCurrency.PEN, InvoiceType.BOLETA,
                                new BigDecimal(0));
                invoice.addItem(new InvoiceItemDomain("Item 1", "aaa", 1, new BigDecimal(50)));
                invoice.addItem(new InvoiceItemDomain("Item 2", "aaa", 1, new BigDecimal(20)));
                invoice.addItem(new InvoiceItemDomain("Item 3", "aaa", 1, new BigDecimal(30)));
                invoice.calculatedTotals();
                JSONObject json = sunatInvoice.formatJson(invoice);
                StepVerifier.create(Mono.just(json))
                                .expectNextMatches(jsonObject -> {
                                        try {
                                                return jsonObject.getString("type").equals("send_electronic_invoice")
                                                                && jsonObject.getJSONObject("invoice")
                                                                                .getString("moneda").equals("1")
                                                                && jsonObject.getJSONObject("invoice")
                                                                                .getString("entidad_denominacion")
                                                                                .equals("Juan Perez")
                                                                && jsonObject.getJSONObject("invoice")
                                                                                .getString("entidad_direccion")
                                                                                .equals("Av. Los Pinos")
                                                                && jsonObject.getJSONObject("invoice")
                                                                                .getString("entidad_email").equals("")
                                                                && jsonObject.getJSONObject("invoice")
                                                                                .getString("entidad_codigo")
                                                                                .equals("71837677")
                                                                && jsonObject.getJSONObject("invoice").getString(
                                                                                "entidad_numero_de_documento")
                                                                                .equals("71837677")
                                                                && jsonObject.getJSONObject("invoice")
                                                                                .getJSONArray("invoice_lines")
                                                                                .length() == 3
                                                                && jsonObject.getJSONObject("invoice")
                                                                                .getJSONArray("invoice_lines")
                                                                                .getJSONObject(0)
                                                                                .getString("descripcion").equals("aaa")
                                                                && jsonObject.getJSONObject("invoice")
                                                                                .getJSONArray("invoice_lines")
                                                                                .getJSONObject(0)
                                                                                .getString("unidad_de_medida")
                                                                                .equals("UND");
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                                return false;
                                        }
                                })
                                .verifyComplete();
        }

        @Test
        public void savedAndSendInvoice() {
                CompanyDomain company = new CompanyDomain("ddd", "1233", "wdwd", "dwdwd", "wdwdw", "wdwdw", "wdwd");
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "78804372", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoiceDomain = new InvoiceDomain(client, 1, 18.0, InvoiceCurrency.PEN,
                                InvoiceType.BOLETA, new BigDecimal(0));
                invoiceDomain.addItemWithIncludedIgv(
                                new InvoiceItemDomain("Item 1", "aaa", 1, new BigDecimal(100)));
                Mono<InvoiceTypeEntity> invoiceTypeEntity = this.invoiceTypeRepsitory
                                .findByName(invoiceDomain.getType())
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid type")));
                Mono<CurrencyTypeEntity> currencyTypeEntity = this.currencyTypeRepository
                                .findByCurrencyTypeName(invoiceDomain.getCurrency().getCurrency())
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid currency")));
                Mono<InvoiceEntity> data = invoiceTypeEntity.flatMap(invoiceType -> {
                        invoiceDomain.setCorrelative(invoiceType.getCorrelative());
                        invoiceDomain.setSerie(invoiceType.getSerie());
                        return Mono.zip(Mono.just(invoiceDomain), Mono.just(invoiceType));
                }).flatMap(tuple -> Mono.zip(this.sunatInvoice.sendInvoice(
                                tuple.getT1(), company),
                                Mono.just(tuple.getT1()), Mono.just(tuple.getT2())))
                                .flatMap(tuple -> {
                                        InvoiceResponse responseInvoice = tuple.getT1();
                                        InvoiceDomain invoiceDomainD = tuple.getT2();
                                        InvoiceTypeEntity invoiceType = tuple.getT3();
                                        invoiceDomainD.setKeySupplier(responseInvoice.getKey());
                                        invoiceDomainD.setSupplierNote(responseInvoice.getSunat_note());
                                        invoiceDomainD.setStatus(responseInvoice.isAceptada_por_sunat()
                                                        ? InvoiceStatus.ACEPTED
                                                        : InvoiceStatus.REJECTED);
                                        invoiceDomain.setLinkPdf(responseInvoice.getLink_pdf());
                                        return Mono.zip(Mono.just(invoiceDomainD), Mono.just(invoiceType));
                                }).flatMap(tuple -> Mono.zip(Mono.just(tuple.getT1()),
                                                this.invoiceStatusRepository
                                                                .findByName(invoiceDomain.getStatus().getStatus()),
                                                Mono.just(tuple.getT2()),
                                                currencyTypeEntity))
                                .flatMap(tuple -> {
                                        InvoiceDomain invoiceDomainD = tuple.getT1();
                                        InvoiceStatusEntity invoiceStatus = tuple.getT2();
                                        InvoiceTypeEntity invoiceType = tuple.getT3();
                                        CurrencyTypeEntity currencyType = tuple.getT4();
                                        InvoiceEntity entity = invoiceDomainD.toEntity(invoiceType.getId(),
                                                        invoiceStatus.getId(), 1,
                                                        currencyType.getCurrencyTypeId());
                                        List<InvoiceItemEntity> itemsD = invoiceDomainD.getItems()
                                                        .stream()
                                                        .map(item -> item.toEntity(invoiceDomain.getId()))
                                                        .collect(Collectors.toList());
                                        return this.invoiceRepsitory.save(entity)
                                                        .flatMap(savedEntity -> this.invoiceItemRepository
                                                                        .saveAll(itemsD)
                                                                        .then(this.invoiceTypeRepsitory.addCorrelative(
                                                                                        invoiceDomainD.getType()))
                                                                        .then(Mono.just(savedEntity)));
                                });
                /*
                 * Mono<InvoiceEntity> data = response.flatMap(responseInvoice -> {
                 * invoiceDomain.setKeySupplier(responseInvoice.getKey());
                 * invoiceDomain.setSupplierNote(responseInvoice.getSunat_note());
                 * invoiceDomain.setStatus(responseInvoice.isAceptada_por_sunat() ?
                 * InvoiceStatus.ACEPTED
                 * : InvoiceStatus.REJECTED);
                 * return Mono.just(invoiceDomain);
                 * }).flatMap(invoiceD -> {
                 * return Mono.zip(invoiceTypeEntity, this.invoiceStatusRepository
                 * .findByName(invoiceDomain.getStatus().getStatus())
                 * .switchIfEmpty(Mono.error(new IllegalArgumentException(
                 * "Invalid status"))),
                 * currencyTypeEntity).flatMap(tuple -> {
                 * InvoiceTypeEntity invoiceType = tuple.getT1();
                 * invoiceDomain.setCorrelative(invoiceType.getCorrelative());
                 * InvoiceStatusEntity invoiceStatus = tuple.getT2();
                 * CurrencyTypeEntity currencyType = tuple.getT3();
                 * InvoiceEntity entity = invoiceDomain.toEntity(invoiceType.getId(),
                 * invoiceStatus.getId(), 1,
                 * currencyType.getCurrencyTypeId());
                 * return this.invoiceRepsitory.save(entity)
                 * .flatMap(savedEntity -> this.invoiceItemRepository
                 * .saveAll(items)
                 * .then(this.invoiceTypeRepsitory
                 * .addCorrelative(invoiceDomain
                 * .getType()))
                 * .then(Mono.just(savedEntity)));
                 * });
                 * });
                 */

                System.out.println("LLEGO ANTES DE VERIFICAR");
                StepVerifier.create(data)
                                .expectNextCount(1)
                                .verifyComplete();
        }

        @Test
        public void testSendInvoiceWithSave() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "78804372", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoiceDomain = new InvoiceDomain(client, 1, 18.0, InvoiceCurrency.PEN,
                                InvoiceType.BOLETA, new BigDecimal(0));
                invoiceDomain.addItemWithIncludedIgv(
                                new InvoiceItemDomain("Item 1", "aaa", 1, new BigDecimal(100)));

                StepVerifier.create(this.invoiceService.save(invoiceDomain)).verifyComplete();
                ;
        }
}