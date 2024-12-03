package com.proriberaapp.ribera.services.invoice;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proriberaapp.ribera.Domain.dto.CompanyDataDto;
import com.proriberaapp.ribera.Domain.entities.CurrencyTypeEntity;
import com.proriberaapp.ribera.Domain.entities.Invoice.InvoiceEntity;
import com.proriberaapp.ribera.Domain.entities.Invoice.InvoiceItemEntity;
import com.proriberaapp.ribera.Domain.entities.Invoice.InvoiceStatusEntity;
import com.proriberaapp.ribera.Domain.entities.Invoice.InvoiceTypeEntity;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceStatus;
import com.proriberaapp.ribera.Domain.enums.invoice.InvoiceType;
import com.proriberaapp.ribera.Domain.invoice.CompanyDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceDomain;
import com.proriberaapp.ribera.Domain.invoice.InvoiceResponse;
import com.proriberaapp.ribera.Domain.invoice.SunatInvoice;
import com.proriberaapp.ribera.Infraestructure.repository.CurrencyTypeRepository;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.InvoiceItemRepository;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.InvoiceRepository;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.InvoiceStateRepository;
import com.proriberaapp.ribera.Infraestructure.repository.Invoice.InvoiceTypeRepsitory;
import com.proriberaapp.ribera.services.client.UserClientService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class InvoiceService implements InvoiceServiceI {
        @Autowired
        private InvoiceTypeRepsitory invoiceTypeRepsitory;
        @Autowired
        private InvoiceRepository invoiceRepsitory;
        @Autowired
        private CurrencyTypeRepository currencyTypeRepository;
        @Autowired
        private InvoiceItemRepository invoiceItemRepository;

        @Autowired
        private InvoiceStateRepository invoiceStatusRepository;

        @Autowired
        private SunatInvoice sunatInvoice;

        @Autowired
        private UserClientService userClientService;

        @Override
        public Mono<Void> save(InvoiceDomain invoiceDomain) {
                invoiceDomain.calculatedTotals();
                CompanyDomain company = new CompanyDomain("ddd", "1233", "wdwd", "dwdwd", "wdwdw", "wdwdw", "wdwd");
                Mono<InvoiceDomain> invoiceDomainaMono = Mono.just(invoiceDomain);
                List<InvoiceItemEntity> items = invoiceDomain.getItems()
                                .stream()
                                .map(item -> item.toEntity(invoiceDomain.getId()))
                                .collect(Collectors.toList());
                if (invoiceDomain.getType() == InvoiceType.FACTURA.name()) {
                        invoiceDomainaMono = invoiceDomainaMono
                                        .zipWith(this.userClientService
                                                        .loadDataRuc(invoiceDomain.getClient().getIdentifier()))
                                        .map(tuple -> {
                                                CompanyDataDto companyData = tuple.getT2();
                                                invoiceDomain.getClient().setAddress(companyData.getDireccion());
                                                invoiceDomain.getClient().setName(companyData.getRazonSocial());
                                                return invoiceDomain;
                                        });
                }
                Mono<InvoiceTypeEntity> invoiceTypeEntity = this.invoiceTypeRepsitory
                                .findByName(invoiceDomain.getType())
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid type")));
                Mono<CurrencyTypeEntity> currencyTypeEntity = this.currencyTypeRepository
                                .findByCurrencyTypeName(invoiceDomain.getCurrency().getCurrency())
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid currency")));
                Mono<InvoiceResponse> response = Mono.zip(invoiceDomainaMono, invoiceTypeEntity).flatMap(tuple -> {
                        InvoiceDomain invoice = tuple.getT1();
                        InvoiceTypeEntity invoiceType = tuple.getT2();
                        invoice.setCorrelative(invoiceType.getCorrelative());
                        invoice.setSerie(invoiceType.getSerie());
                        return sunatInvoice.sendInvoice(invoice, company);
                }); /*
                     * invoiceTypeEntity.flatMap(invoiceType -> {
                     * invoiceDomain.setCorrelative(invoiceType.getCorrelative());
                     * invoiceDomain.setSerie(invoiceType.getSerie());
                     * return sunatInvoice.sendInvoice(invoiceDomain, company);
                     * });
                     */
                return response.flatMap(responseInvoice -> {
                        invoiceDomain.setKeySupplier(responseInvoice.getKey());
                        invoiceDomain.setSupplierNote(responseInvoice.getSunat_note());
                        invoiceDomain.setStatus(responseInvoice.isAceptada_por_sunat() ? InvoiceStatus.ACEPTED
                                        : InvoiceStatus.REJECTED);
                        invoiceDomain.setLinkPdf(responseInvoice.getLink_pdf());
                        return Mono.just(invoiceDomain);
                }).flatMap(invoiceD -> {
                        return Mono.zip(invoiceTypeEntity, this.invoiceStatusRepository
                                        .findByName(invoiceDomain.getStatus().getStatus())
                                        .switchIfEmpty(Mono.error(new IllegalArgumentException(
                                                        "Invalid status"))),
                                        currencyTypeEntity).flatMap(tuple -> {
                                                InvoiceTypeEntity invoiceType = tuple.getT1();

                                                InvoiceStatusEntity invoiceStatus = tuple.getT2();
                                                CurrencyTypeEntity currencyType = tuple.getT3();
                                                InvoiceEntity entity = invoiceDomain.toEntity(invoiceType.getId(),
                                                                invoiceStatus.getId(), 1,
                                                                currencyType.getCurrencyTypeId());
                                                return this.invoiceRepsitory.save(entity)
                                                                .flatMap(savedEntity -> this.invoiceItemRepository
                                                                                .saveAll(items)
                                                                                .then(this.invoiceTypeRepsitory
                                                                                                .addCorrelative(invoiceDomain
                                                                                                                .getType()))
                                                                                .then(Mono.just(savedEntity)));
                                        });
                }).then();

        }

        @Override
        public Mono<InvoiceDomain> findById(String id) {
                // TODO Auto-generated method stub
                return Mono.empty();
        }

        @Override
        public Flux<InvoiceDomain> finAll() {
                // TODO Auto-generated method stub
                return Flux.empty();

        }

        @Override
        public Flux<InvoiceDomain> findByClientId(String clientId) {
                // TODO Auto-generated method stub
                return Flux.empty();
        }

}
