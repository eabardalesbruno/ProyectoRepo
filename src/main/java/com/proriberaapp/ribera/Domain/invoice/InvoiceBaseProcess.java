package com.proriberaapp.ribera.Domain.invoice;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public abstract class InvoiceBaseProcess {
    protected abstract String getToken();

    protected abstract String getUrl();

    public WebClient configureFetch() {
        WebClient client = WebClient.create(this.getUrl());
        client.head().header("authorization", getToken());
        client.head().header("content-type ", MediaType.APPLICATION_JSON_VALUE);
        return client;
    }
}

record CompanyData(String ruc, String name, String address, String phone, String email) {
    public CompanyData {
        if (name == null || address == null || phone == null || email == null) {
            throw new IllegalArgumentException("Company data cannot be null");
        }
    }
}
