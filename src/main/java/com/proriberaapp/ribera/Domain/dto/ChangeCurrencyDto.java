package com.proriberaapp.ribera.Domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeCurrencyDto {
    private double precioCompra;
    private double precioVenta;

    @Override
    public String toString() {
        return "ChangeCurrencyDto{" +
                "precioCompra=" + precioCompra +
                ", precioVenta=" + precioVenta +
                '}';
    }
}
