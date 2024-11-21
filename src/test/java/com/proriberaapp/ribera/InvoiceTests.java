package com.proriberaapp.ribera;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

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
import reactor.core.publisher.Flux;
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
                                "");
                List<InvoiceItemDomain> items = List.of();
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 19.0, 1, InvoiceCurrency.PEN, items);
                Mono<InvoiceDomain> invoiceMono = Mono
                                .just(invoice);
                StepVerifier.create(invoiceMono)
                                .expectNextMatches(
                                                invoiceM -> invoiceM.getType() == InvoiceType.FACTURA.name().toString())
                                .verifyComplete();

        }

        @Test
        void verifiedInvoiceTypeBoleta() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "71837677", "Av. Los Pinos",
                                "123456789",
                                "");
                List<InvoiceItemDomain> items = List.of();
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 19.0, 1, InvoiceCurrency.PEN, items);
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
                                "");
                List<InvoiceItemDomain> items = List.of();
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 19.0, 1, InvoiceCurrency.PEN, items);
                Mono<InvoiceDomain> invoiceMono = Mono
                                .just(invoice);
                StepVerifier.create(invoiceMono).verifyErrorMatches(IllegalArgumentException.class::isInstance);
        }

        @Test
        void verfiedCorrelativeFacture() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "71837677121", "Av. Los Pinos",
                                "123456789",
                                "");
                List<InvoiceItemDomain> items = List.of();
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 19.0, 1, InvoiceCurrency.PEN, items);
                Mono<InvoiceDomain> invoiceMono = Mono
                                .just(invoice);
                StepVerifier.create(invoiceMono).expectNextMatches(invoiceM -> invoiceM.getSerie().equals("F002"))
                                .verifyComplete();
        }

        @Test
        void verfiedCorrelativeBoleta() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "71837677", "Av. Los Pinos",
                                "123456789",
                                "");
                List<InvoiceItemDomain> items = List.of();

                InvoiceDomain invoice = new InvoiceDomain(client, 1, 19.0, 11, InvoiceCurrency.USD, items);
                Mono<InvoiceDomain> invoiceMono = Mono
                                .just(invoice);
                StepVerifier.create(invoiceMono).expectNextMatches(invoiceM -> invoiceM.getSerie().equals("B012"))
                                .verifyComplete();
        }

        @Test
        void verfiedCorrelativeLessThan10Boleta() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "71837677", "Av. Los Pinos",
                                "123456789",
                                "");
                List<InvoiceItemDomain> items = List.of();

                InvoiceDomain invoice = new InvoiceDomain(client, 1, 19.0, 8, InvoiceCurrency.USD, items);
                Mono<InvoiceDomain> invoiceMono = Mono
                                .just(invoice);
                StepVerifier.create(invoiceMono).expectNextMatches(invoiceM -> invoiceM.getSerie().equals("B009"))
                                .verifyComplete();
        }

        @Test
        void verfiedTotalPayment() {
                InvoiceClientDomain client = new InvoiceClientDomain("Juan Perez", "71837677", "Av. Los Pinos",
                                "123456789",
                                "");
                List<InvoiceItemDomain> items = new ArrayList<>();
                items.add(new InvoiceItemDomain("Item 1", "aaa", 1, 0.0, new BigDecimal(50)));
                items.add(new InvoiceItemDomain("Item 2", "aaa", 1, 0.0, new BigDecimal(20)));
                items.add(new InvoiceItemDomain("Item 3", "aaa", 1, 0.0, new BigDecimal(30)));
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 0.0, 1, InvoiceCurrency.PEN, items);
                System.out.println(invoice);
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
        void verfiedPayment() {
                InvoiceClientDomain client = new InvoiceClientDomain(
                                "Juan Perez",
                                "71837677",
                                "Av. Los Pinos 123",
                                "123456789",
                                "juan.perez@example.com");

                // Crear la Factura
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 18.0, 1, InvoiceCurrency.PEN);
                invoice.addItem(new InvoiceItemDomain("Reserva Habitación Doble", "AAA", 2, new BigDecimal("100.00")));
                invoice.addItem(new InvoiceItemDomain("Reserva Habitación Sencilla", "BBB", 1,
                                new BigDecimal("80.00")));
                invoice.addItem(new InvoiceItemDomain("Servicio WiFi", "CCC", 3, new BigDecimal("10.00")));
                invoice.calculatedTotals();
                System.out.println("SUBTOTAL" + invoice.getSubtotal().doubleValue());
                System.out.println("IGV" + invoice.getTotalIgv().doubleValue());
                System.out.println("TOTAL" + invoice.getTotalPayment().doubleValue());
                StepVerifier.create(Mono.just(invoice))
                                .expectNextMatches(
                                                invoiceN -> {
                                                        return invoiceN.getTotalPayment().doubleValue() == 365.8
                                                                        && invoiceN.getSubtotal().doubleValue() == 310
                                                                        && invoiceN.getTotalIgv().doubleValue() == 55.8;
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
                                "juan.perez@example.com");
                // Crear la Factura
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 18.0, 1, InvoiceCurrency.PEN);
                invoice.addItem(new InvoiceItemDomain("Reserva Habitación Doble", "AAA", 1, new BigDecimal("100.00")),
                                true);
                invoice.calculatedTotals();
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
                                "");
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 18, 1, InvoiceCurrency.PEN);
                invoice.addItem(new InvoiceItemDomain("Item 2", "aaa", 1, 0.0, new BigDecimal(20)));
                invoice.addItem(new InvoiceItemDomain("Item 3", "aaa", 1, 0.0, new BigDecimal(30)));
                invoice.addItem(new InvoiceItemDomain("Item 1", "aaa", 1, 0.0, new BigDecimal(50)));
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
                                "");
                List<InvoiceItemDomain> items = new ArrayList<>();
                items.add(new InvoiceItemDomain("Item 1", "aaa", 1, 18, new BigDecimal(50)));
                items.add(new InvoiceItemDomain("Item 2", "aaa", 1, 18, new BigDecimal(20)));
                items.add(new InvoiceItemDomain("Item 3", "aaa", 1, 18, new BigDecimal(30)));
                InvoiceDomain invoice = new InvoiceDomain(client, 1, 18, 1, InvoiceCurrency.PEN, items);
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
                                "");
                List<InvoiceItemDomain> items = new ArrayList<>();
                InvoiceDomain invoiceDomain = new InvoiceDomain(client, 82, 1, InvoiceCurrency.PEN, items);
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
                                "");
                List<InvoiceItemDomain> items = new ArrayList<>();
                InvoiceDomain invoiceDomain = new InvoiceDomain(client, 82, 1, InvoiceCurrency.PEN, items);
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
                                "");
                InvoiceDomain invoiceDomain = new InvoiceDomain(client, 82, 18, 1, InvoiceCurrency.USD);
                invoiceDomain.addItem(new InvoiceItemDomain("Item 1", "aaa", 1, new BigDecimal(50)));
                invoiceDomain.addItem(new InvoiceItemDomain("Item 2", "aaa", 1, new BigDecimal(20)));
                invoiceDomain.addItem(new InvoiceItemDomain("Item 3", "aaa", 1, new BigDecimal(30)));
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
                                "");
                InvoiceDomain invoiceDomain = new InvoiceDomain(client, 1, 18.0, 1, InvoiceCurrency.PEN);
                invoiceDomain.addItem(
                                new InvoiceItemDomain("Reserva Habitación Doble", "AAA", 2, new BigDecimal("100.00")));
                invoiceDomain.addItem(new InvoiceItemDomain("Reserva Habitación Sencilla", "BBB", 1,
                                new BigDecimal("80.00")));
                invoiceDomain.addItem(new InvoiceItemDomain("Servicio WiFi", "CCC", 3, new BigDecimal("10.00")));
                invoiceDomain.calculatedTotals();
                invoiceDomain.setKeySupplier("wdwdwd");
                invoiceDomain.setSupplierNote("No se acepto tu factura");
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
                                "");
                InvoiceDomain invoiceDomain = new InvoiceDomain(client, 82, 18, 1, InvoiceCurrency.USD);
                invoiceDomain.addItem(new InvoiceItemDomain("Item 1", "aaa1", 1, new BigDecimal(50)));
                invoiceDomain.addItem(new InvoiceItemDomain("Item 2", "aaa2", 1, new BigDecimal(20)));
                invoiceDomain.addItem(new InvoiceItemDomain("Item 3", "aaa3", 1, new BigDecimal(30)));
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
                                "");
                List<InvoiceItemDomain> items = new ArrayList<>();
                InvoiceDomain invoiceDomain = new InvoiceDomain(client, 82, 1, InvoiceCurrency.PEN, items);
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
