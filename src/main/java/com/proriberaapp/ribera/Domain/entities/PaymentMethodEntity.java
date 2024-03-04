package com.proriberaapp.ribera.Domain.entities;

public class PaymentMethodEntity {
    private Integer paymentMethodId;
    private String description;

    public PaymentMethodEntity() {
    }

    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
