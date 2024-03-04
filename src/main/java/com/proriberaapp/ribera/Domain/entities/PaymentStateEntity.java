package com.proriberaapp.ribera.Domain.entities;

public class PaymentStateEntity {
    private Integer paymentStateId;
    private String paymentStateName;

    public PaymentStateEntity() {
    }

    public Integer getPaymentStateId() {
        return paymentStateId;
    }

    public void setPaymentStateId(Integer paymentStateId) {
        this.paymentStateId = paymentStateId;
    }

    public String getPaymentStateName() {
        return paymentStateName;
    }

    public void setPaymentStateName(String paymentStateName) {
        this.paymentStateName = paymentStateName;
    }
}

