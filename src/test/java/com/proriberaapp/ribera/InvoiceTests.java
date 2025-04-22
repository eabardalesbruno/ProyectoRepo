package com.proriberaapp.ribera;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import com.proriberaapp.ribera.Domain.entities.BedroomEntity;
import com.proriberaapp.ribera.Domain.entities.CurrencyTypeEntity;
import com.proriberaapp.ribera.Domain.entities.Invoice.InvoiceEntity;
import com.proriberaapp.ribera.Domain.entities.Invoice.InvoiceItemEntity;
import com.proriberaapp.ribera.Domain.entities.Invoice.InvoiceStatusEntity;
import com.proriberaapp.ribera.Domain.entities.Invoice.InvoiceTypeEntity;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceCurrency;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceStatus;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceType;
import com.proriberaapp.ribera.Domain.invoice.InvoiceClientDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceItemDomain;
import com.proriberaapp.ribera.Infraestructure.repository.CurrencyTypeRepository;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.InvoiceItemRepository;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.InvoiceRepository;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.InvoiceStateRepository;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.InvoiceTypeRepsitory;

import reactor.test.StepVerifier;
import reactor.core.publisher.Mono;

@SpringBootTest
public class InvoiceTests {
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

        @Test
        void verifiedInvoiceTypeFacture() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "12345678912", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 19.0, InvoiceCurrency.PEN, InvoiceType.FACTURA,
                                0);
                Mono<InvoiceDomain> invoiceMono = Mono
                                .just(invoice);
                StepVerifier.create(invoiceMono)
                                .expectNextMatches(
                                                invoiceM -> invoiceM.getType() == InvoiceType.FACTURA.name().toString())
                                .verifyComplete();

        }

        @Test
        void verifiedInvoiceTypeWithTypeFactura() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "71837677", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 18, InvoiceCurrency.PEN, InvoiceType.FACTURA,
                                0);
                Mono<InvoiceDomain> invoiceMono = Mono
                                .just(invoice);
                StepVerifier.create(invoiceMono)
                                .expectNextMatches(
                                                invoiceM -> invoiceM.getType() == InvoiceType.FACTURA.name().toString()
                                                                && invoiceM.getSerie().startsWith("F"))

                                .verifyComplete();
        }

        @Test
        void verifiedInvoiceTypeWithTypeBoleta() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "71837677", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 18, InvoiceCurrency.PEN, InvoiceType.BOLETA,
                                0);
                Mono<InvoiceDomain> invoiceMono = Mono
                                .just(invoice);
                StepVerifier.create(invoiceMono)
                                .expectNextMatches(
                                                invoiceM -> invoiceM.getType() == InvoiceType.BOLETA.name().toString()
                                                                && invoiceM.getSerie().startsWith("B"))

                                .verifyComplete();
        }

        @Test
        void verifiedInvoiceTypeBoleta() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "71837677", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 18.0, InvoiceCurrency.PEN, InvoiceType.BOLETA,
                                0);
                Mono<InvoiceDomain> invoiceMono = Mono
                                .just(invoice);
                StepVerifier.create(invoiceMono)
                                .expectNextMatches(
                                                invoiceM -> invoiceM.getType() == InvoiceType.BOLETA.name().toString())
                                .verifyComplete();
        }

        @Test
        void verifiedInvoiceTypeUnknow() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "718376", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 19.0, InvoiceCurrency.PEN, InvoiceType.BOLETA,
                                0);
                Mono<InvoiceDomain> invoiceMono = Mono
                                .just(invoice);
                StepVerifier.create(invoiceMono).verifyErrorMatches(IllegalArgumentException.class::isInstance);
        }

        @Test
        void verfiedCorrelativeFacture() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "71837677121", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 19, InvoiceCurrency.PEN, InvoiceType.BOLETA,
                                0);
                Mono<InvoiceDomain> invoiceMono = Mono
                                .just(invoice);
                StepVerifier.create(invoiceMono).expectNextMatches(invoiceM -> invoiceM.getSerie().equals("F002"))
                                .verifyComplete();
        }

        @Test
        void verfiedCorrelativeBoleta() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "71837677", "Av. Los Pinos",
                                "123456789",
                                "", 1);

                InvoiceDomain invoice = new InvoiceDomain(client, 1, 19.0, InvoiceCurrency.USD, InvoiceType.BOLETA,
                                0);
                Mono<InvoiceDomain> invoiceMono = Mono
                                .just(invoice);
                StepVerifier.create(invoiceMono).expectNextMatches(invoiceM -> invoiceM.getSerie().equals("B012"))
                                .verifyComplete();
        }

        @Test
        void verfiedCorrelativeLessThan10Boleta() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "71837677", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 19.0, InvoiceCurrency.USD, InvoiceType.BOLETA,
                                0);
                Mono<InvoiceDomain> invoiceMono = Mono
                                .just(invoice);
                StepVerifier.create(invoiceMono).expectNextMatches(invoiceM -> invoiceM.getSerie().equals("B009"))
                                .verifyComplete();
        }

        @Test
        void verfiedTotalPayment() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "71837677", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 0.0, InvoiceCurrency.PEN, InvoiceType.BOLETA,
                                0);
                invoice.addItemWithIncludedIgv(
                                new InvoiceItemDomain("Item 1","631210", "aaa", 1, new BigDecimal(50), "DSCTO"));
                invoice.addItemWithIncludedIgv(
                                new InvoiceItemDomain("Item 2","631210", "aaa", 1, new BigDecimal(20), "DSCTO"));
                invoice.addItemWithIncludedIgv(
                                new InvoiceItemDomain("Item 3","631210", "aaa", 1, new BigDecimal(30), "DSCTO"));
                Mono<InvoiceDomain> invoiceMono = Mono
                                .just(invoice);
                StepVerifier.create(invoiceMono)
                                .expectNextMatches(invoiceM -> {
                                        System.out.println(invoiceM.getTotalPayment());
                                        return invoiceM.getTotalPayment().compareTo(new BigDecimal(100)) == 0;
                                })
                                .verifyComplete();
        }

        @Test
        void verfiedPaymentWithIgvNotIncluded() {
                InvoiceClientDomain client = new InvoiceClientDomain(
                                "Juan Perez",
                                "71837677",
                                "Av. Los Pinos 123",
                                "123456789",
                                "juan.perez@example.com", 1);

                // Crear la Boleta
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 18.0, InvoiceCurrency.PEN, InvoiceType.BOLETA,
                                0);
                invoice.addItem(
                                new InvoiceItemDomain("Reserva Habitación Doble","631210", "AAA", 1, new BigDecimal("100.00"), "DSCTO"));
                invoice.addItem(new InvoiceItemDomain("Servicio WiFi","631210", "CCC", 5, new BigDecimal("10.00"), "DSCTO"));
                System.out.println("SUBTOTAL" + invoice.getSubtotal().doubleValue());
                System.out.println("IGV" + invoice.getTotalIgv().doubleValue());
                System.out.println("TOTAL" + invoice.getTotalPayment().doubleValue());
                StepVerifier.create(Mono.just(invoice))
                                .expectNextMatches(
                                                invoiceN -> {
                                                        return invoiceN.getTotalPayment().doubleValue() == 177
                                                                        && invoiceN.getSubtotal().doubleValue() == 150
                                                                        && invoiceN.getTotalIgv().doubleValue() == 27;
                                                })

                                .verifyComplete();
        }

        @Test
        void verfiedPaymentWithIgvIncluded() {
                InvoiceClientDomain client = new InvoiceClientDomain(
                                "Juan Perez",
                                "71837677",
                                "Av. Los Pinos 123",
                                "123456789",
                                "juan.perez@example.com", 1);

                // Crear la Boleta
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 18.0, InvoiceCurrency.PEN, InvoiceType.BOLETA,
                                0);
                invoice.addItemWithIncludedIgv(
                                new InvoiceItemDomain("Reserva Habitación Doble","631210", "AAA", 1, new BigDecimal("100.00"), "DSCTO"));

                invoice.addItemWithIncludedIgv(new InvoiceItemDomain("Reserva Habitación Sencilla","631210", "BBB", 1,
                                new BigDecimal("100.00"), "DSCTO"));
                /* invoice.addItemWithIncludedIgv( */
                /*
                 * new InvoiceItemDomain("Servicio WiFi", "CCC", 3, new BigDecimal("10.00")));
                 */
                System.out.println("SUBTOTAL" + invoice.getSubtotal().doubleValue());
                System.out.println("IGV" + invoice.getTotalIgv().doubleValue());
                System.out.println("TOTAL" + invoice.getTotalPayment().doubleValue());
                StepVerifier.create(Mono.just(invoice))
                                .expectNextMatches(
                                                invoiceN -> {
                                                        return invoiceN.getTotalPayment().doubleValue() == 200
                                                                        && invoiceN.getSubtotal().doubleValue() == 164
                                                                        && invoiceN.getTotalIgv().doubleValue() == 36;
                                                })

                                .verifyComplete();
        }

        @Test
        void verfiedWithIgvIncludedPayment() {
                InvoiceClientDomain client = new InvoiceClientDomain(
                                "Juan Perez",
                                "71837677",
                                "Av. Los Pinos 123",
                                "123456789",
                                "juan.perez@example.com", 1);
                // Crear la Factura
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 18.0, InvoiceCurrency.PEN, InvoiceType.FACTURA,
                                0);
                invoice.addItemWithIncludedIgv(
                                new InvoiceItemDomain("Reserva Habitación Doble","631210", "AAA", 1, new BigDecimal("100.00"), "DSCTO"));
                System.out.println("SUBTOTAL" + invoice.getSubtotal().doubleValue());
                System.out.println("IGV" + invoice.getTotalIgv().doubleValue());
                System.out.println("TOTAL" + invoice.getTotalPayment().doubleValue());
                StepVerifier.create(Mono.just(invoice))
                                .expectNextMatches(
                                                invoiceN -> {
                                                        return invoiceN.getTotalPayment().doubleValue() == 100.0
                                                                        && invoiceN.getSubtotal().doubleValue() == 82.0
                                                                        && invoiceN.getTotalIgv().doubleValue() == 18.0;
                                                })
                                .verifyComplete();
        }

        @Test
        void verfiedSubTotalPayment() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "71837677", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 18, InvoiceCurrency.PEN, InvoiceType.BOLETA,
                                0);
                invoice.addItem(new InvoiceItemDomain("Item 2","631210", "aaa", 1, new BigDecimal(20), "DSCTO"));
                invoice.addItem(new InvoiceItemDomain("Item 3","631210", "aaa", 1, new BigDecimal(30), "DSCTO"));
                invoice.addItem(new InvoiceItemDomain("Item 1","631210", "aaa", 1, new BigDecimal(50), "DSCTO"));
                invoice.calculatedTotals();
                System.out.println(invoice);
                Mono<InvoiceDomain> invoiceMono = Mono
                                .just(invoice);
                StepVerifier.create(invoiceMono)
                                .expectNextMatches(invoiceM -> {
                                        System.out.println(invoiceM.getTotalPayment());
                                        return invoiceM.getTotalPayment().compareTo(new BigDecimal(118)) == 0;
                                })
                                .verifyComplete();
        }

        @Test
        void verfiedTotalWithIgvPayment() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "71837677", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 18, InvoiceCurrency.PEN, InvoiceType.BOLETA,
                                0);
                invoice.addItem(new InvoiceItemDomain("Item 1","631210", "aaa", 1, new BigDecimal(50), "DSCTO"));
                invoice.addItem(new InvoiceItemDomain("Item 2","631210", "aaa", 1, new BigDecimal(20), "DSCTO"));
                invoice.addItem(new InvoiceItemDomain("Item 3","631210", "aaa", 1, new BigDecimal(30), "DSCTO"));
                Mono<InvoiceDomain> invoiceMono = Mono
                                .just(invoice);
                StepVerifier.create(invoiceMono).expectNextMatches(invoiceM -> {
                        System.out.println(invoiceM.getTotalPayment());
                        return invoiceM.getTotalPayment().compareTo(new BigDecimal(118)) == 0;
                })
                                .verifyComplete();
        }

        @Test
        void saveAcceptedInvoice() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "71837677", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoiceDomain = new InvoiceDomain(client, 1, 18, InvoiceCurrency.PEN, InvoiceType.BOLETA,
                                0);
                invoiceDomain.setKeySupplier("wdwdwd");
                invoiceDomain.setSupplierNote("Salio bien");
                invoiceDomain.setStatus(InvoiceStatus.ACEPTED);
                Mono<InvoiceTypeEntity> invoiceTypeEntity = this.invoiceTypeRepsitory
                                .findByName(invoiceDomain.getType())
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid type")));
                Mono<InvoiceStatusEntity> invoiceStatusEntity = this.invoiceStatusRepository
                                .findByName(invoiceDomain.getStatus().getStatus())
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid status")));
                Mono<CurrencyTypeEntity> currencyTypeEntity = this.currencyTypeRepository
                                .findByCurrencyTypeName(invoiceDomain.getCurrency().getCurrency())
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid currency")));
                StepVerifier.create(Mono.zip(invoiceTypeEntity, invoiceStatusEntity, currencyTypeEntity)
                                .flatMap(tuple -> {
                                        InvoiceTypeEntity invoiceType = tuple.getT1();
                                        InvoiceStatusEntity invoiceStatus = tuple.getT2();
                                        CurrencyTypeEntity currencyType = tuple.getT3();
                                        InvoiceEntity entity = invoiceDomain.toEntity(invoiceType.getId(),
                                                        invoiceStatus.getId(), 1,
                                                        currencyType.getCurrencyTypeId());
                                        System.out.println(entity);
                                        return this.invoiceRepsitory.save(entity);
                                })).expectNextCount(1).verifyComplete();

        }

        @Test
        void saveInvoiceDecline() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "71837677", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoiceDomain = new InvoiceDomain(client, 82, 1, InvoiceCurrency.PEN, InvoiceType.FACTURA,
                                0);
                invoiceDomain.setKeySupplier("wdwdwd");
                invoiceDomain.setSupplierNote("No se acepto tu factura");
                invoiceDomain.setStatus(InvoiceStatus.REJECTED);
                Mono<InvoiceTypeEntity> invoiceTypeEntity = this.invoiceTypeRepsitory
                                .findByName(invoiceDomain.getType())
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid type")));
                Mono<InvoiceStatusEntity> invoiceStatusEntity = this.invoiceStatusRepository
                                .findByName(invoiceDomain.getStatus().getStatus())
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid status")));
                Mono<CurrencyTypeEntity> currencyTypeEntity = this.currencyTypeRepository
                                .findByCurrencyTypeName(invoiceDomain.getCurrency().getCurrency())
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid currency")));
                StepVerifier.create(Mono.zip(invoiceTypeEntity, invoiceStatusEntity, currencyTypeEntity)
                                .flatMap(tuple -> {
                                        InvoiceTypeEntity invoiceType = tuple.getT1();
                                        InvoiceStatusEntity invoiceStatus = tuple.getT2();
                                        CurrencyTypeEntity currencyType = tuple.getT3();
                                        InvoiceEntity entity = invoiceDomain.toEntity(invoiceType.getId(),
                                                        invoiceStatus.getId(), 1,
                                                        currencyType.getCurrencyTypeId());
                                        System.out.println(entity);
                                        return this.invoiceRepsitory.save(entity);
                                })).expectNextCount(1).verifyComplete();
        }

        @Test
        void incrementCorrelativeBoleta() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "71837677", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoiceDomain = new InvoiceDomain(client, 82, 18, InvoiceCurrency.USD,
                                InvoiceType.BOLETA,
                                0);
                invoiceDomain.addItem(new InvoiceItemDomain("Item 1","631210", "aaa", 1, new BigDecimal(50), "DSCTO"));
                invoiceDomain.addItem(new InvoiceItemDomain("Item 2","631210", "aaa", 1, new BigDecimal(20), "DSCTO"));
                invoiceDomain.addItem(new InvoiceItemDomain("Item 3","631210", "aaa", 1, new BigDecimal(30), "DSCTO"));
                invoiceDomain.setKeySupplier("wdwdwd");
                invoiceDomain.setSupplierNote("No se acepto tu factura");
                invoiceDomain.setStatus(InvoiceStatus.REJECTED);
                Mono<InvoiceTypeEntity> invoiceTypeEntity = this.invoiceTypeRepsitory
                                .findByName(invoiceDomain.getType())
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid type")));
                Mono<InvoiceStatusEntity> invoiceStatusEntity = this.invoiceStatusRepository
                                .findByName(invoiceDomain.getStatus().getStatus())
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid status")));
                Mono<CurrencyTypeEntity> currencyTypeEntity = this.currencyTypeRepository
                                .findByCurrencyTypeName(invoiceDomain.getCurrency().getCurrency())
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid currency")));
                StepVerifier.create(Mono.zip(invoiceTypeEntity, invoiceStatusEntity, currencyTypeEntity)
                                .flatMap(tuple -> {
                                        InvoiceTypeEntity invoiceType = tuple.getT1();
                                        invoiceDomain.setCorrelative(invoiceType.getCorrelative());
                                        InvoiceStatusEntity invoiceStatus = tuple.getT2();
                                        CurrencyTypeEntity currencyType = tuple.getT3();
                                        InvoiceEntity entity = invoiceDomain.toEntity(invoiceType.getId(),
                                                        invoiceStatus.getId(), 1,
                                                        currencyType.getCurrencyTypeId());
                                        return Mono.zip(this.invoiceRepsitory.save(entity), this.invoiceTypeRepsitory
                                                        .addCorrelative(invoiceDomain.getType()));
                                }).then(this.invoiceTypeRepsitory.findByName(invoiceDomain.getType())))
                                .expectNextMatches(invoiceType -> invoiceType
                                                .getCorrelative() == invoiceDomain.getCorrelative() + 1)
                                .verifyComplete();
        }

        @Test
        void saveInvoiceWithItems() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "71837677", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoiceDomain = new InvoiceDomain(client, 1, 18.0, InvoiceCurrency.PEN,
                                InvoiceType.FACTURA,
                                0);
                invoiceDomain.addItemWithIncludedIgv(
                                new InvoiceItemDomain("Reserva Habitación Doble","631210", "AAA", 2, new BigDecimal("100.00"), "DSCTO"));
                invoiceDomain.addItemWithIncludedIgv(new InvoiceItemDomain("Reserva Habitación Sencilla", "631210","BBB", 1,
                                new BigDecimal("80.00"), "DSCTO"));
                invoiceDomain.addItemWithIncludedIgv(
                                new InvoiceItemDomain("Servicio WiFi","631210", "CCC", 3, new BigDecimal("10.00"), "DSCTO"));
                invoiceDomain.setKeySupplier("wdwdwd");
                invoiceDomain.setSupplierNote("No se acepto tu factura");
                invoiceDomain.setLinkPdf("http://example.com");
                invoiceDomain.setStatus(InvoiceStatus.REJECTED);
                List<InvoiceItemEntity> items = invoiceDomain.getItems()
                                .stream()
                                .map(item -> item.toEntity(invoiceDomain.getId()))
                                .collect(Collectors.toList());
                Mono<InvoiceTypeEntity> invoiceTypeEntity = this.invoiceTypeRepsitory
                                .findByName(invoiceDomain.getType())
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid type")));
                Mono<InvoiceStatusEntity> invoiceStatusEntity = this.invoiceStatusRepository
                                .findByName(invoiceDomain.getStatus().getStatus())
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid status")));
                Mono<CurrencyTypeEntity> currencyTypeEntity = this.currencyTypeRepository
                                .findByCurrencyTypeName(invoiceDomain.getCurrency().getCurrency())
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid currency")));
                Mono<InvoiceEntity> data = Mono.zip(invoiceTypeEntity, invoiceStatusEntity, currencyTypeEntity)
                                .flatMap(tuple -> {
                                        InvoiceTypeEntity invoiceType = tuple.getT1();
                                        invoiceDomain.setCorrelative(invoiceType.getCorrelative());
                                        InvoiceStatusEntity invoiceStatus = tuple.getT2();
                                        CurrencyTypeEntity currencyType = tuple.getT3();
                                        InvoiceEntity entity = invoiceDomain.toEntity(
                                                        invoiceType.getId(),
                                                        invoiceStatus.getId(), 1,
                                                        currencyType.getCurrencyTypeId());
                                        return this.invoiceRepsitory.save(entity)
                                                        .flatMap(savedEntity -> this.invoiceItemRepository
                                                                        .saveAll(items).then(Mono.just(savedEntity)));
                                });
                System.out.println("LLEGO ANTES DE VERIFICAR");
                StepVerifier.create(data)
                                .expectNextCount(1)
                                .verifyComplete();
        }

        @Test
        void saveInvoiceItems() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "71837677", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoiceDomain = new InvoiceDomain(client, 82, 18, InvoiceCurrency.USD,
                                InvoiceType.FACTURA,
                                0);
                invoiceDomain.addItem(new InvoiceItemDomain("Item 1","631210", "aaa1", 1, new BigDecimal(50), "DSCTO"));
                invoiceDomain.addItem(new InvoiceItemDomain("Item 2", "631210","aaa2", 1, new BigDecimal(20), "DSCTO"));
                invoiceDomain.addItem(new InvoiceItemDomain("Item 3", "631210","aaa3", 1, new BigDecimal(30), "DSCTO"));
                invoiceDomain.setKeySupplier("wdwdwd");
                invoiceDomain.setSupplierNote("No se acepto tu factura");
                invoiceDomain.setStatus(InvoiceStatus.REJECTED);
                invoiceDomain.setId(UUID.fromString("decf1542-2c33-4a43-aaa2-af0ae1f203fd"));
                List<InvoiceItemEntity> items = invoiceDomain.getItems()
                                .stream()
                                .map(item -> item.toEntity(invoiceDomain.getId()))
                                .collect(Collectors.toList());
                StepVerifier.create(this.invoiceItemRepository.saveAll(items))
                                .expectNextCount(3).verifyComplete();
        }

        @Test
        void incrementCorrelativeFactura() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "12345678901", "Av. Los Pinos",
                                "123456789",
                                "", 1);
                InvoiceDomain invoiceDomain = new InvoiceDomain(client, 82, 1, InvoiceCurrency.PEN, InvoiceType.BOLETA,
                                0);
                invoiceDomain.setKeySupplier("wdwdwd");
                invoiceDomain.setSupplierNote("No se acepto tu factura");
                invoiceDomain.setStatus(InvoiceStatus.REJECTED);
                Mono<InvoiceTypeEntity> invoiceTypeEntity = this.invoiceTypeRepsitory
                                .findByName(invoiceDomain.getType())
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid type")));
                Mono<InvoiceStatusEntity> invoiceStatusEntity = this.invoiceStatusRepository
                                .findByName(invoiceDomain.getStatus().getStatus())
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid status")));
                Mono<CurrencyTypeEntity> currencyTypeEntity = this.currencyTypeRepository
                                .findByCurrencyTypeName(invoiceDomain.getCurrency().getCurrency())
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid currency")));
                StepVerifier.create(Mono.zip(invoiceTypeEntity, invoiceStatusEntity, currencyTypeEntity)
                                .flatMap(tuple -> {
                                        InvoiceTypeEntity invoiceType = tuple.getT1();
                                        invoiceDomain.setCorrelative(invoiceType.getCorrelative());
                                        InvoiceStatusEntity invoiceStatus = tuple.getT2();
                                        CurrencyTypeEntity currencyType = tuple.getT3();
                                        InvoiceEntity entity = invoiceDomain.toEntity(invoiceType.getId(),
                                                        invoiceStatus.getId(), 1,
                                                        currencyType.getCurrencyTypeId());
                                        return Mono.zip(this.invoiceRepsitory.save(entity), this.invoiceTypeRepsitory
                                                        .addCorrelative(invoiceDomain.getType()));
                                }).then(this.invoiceTypeRepsitory.findByName(invoiceDomain.getType())))
                                .expectNextMatches(invoiceType -> invoiceType
                                                .getCorrelative() == invoiceDomain.getCorrelative() + 1)
                                .verifyComplete();
        }
}
