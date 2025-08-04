package com.proriberaapp.ribera.Api.controllers.client.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar el balance de rewards cuando se realizan transacciones desde la wallet
 * Permite mantener sincronizados los saldos entre wallet y rewards
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletBalanceUpdateRequest {
    
    /**
     * ID del usuario
     */
    private Integer userId;
    
    /**
     * Cantidad a actualizar (positivo para incrementar, negativo para decrementar)
     */
    private Integer rewardsAmount;
    
    /**
     * Nombre del paquete familiar (ej: "Ribera", "Inclub", etc.)
     */
    private String familyPackageName;
    
    /**
     * Detalle de la transacción (ej: "Pago de reserva", "Compra de puntos", etc.)
     */
    private String detail;
    
    /**
     * Tipo de transacción (ej: "PAYMENT", "PURCHASE", "TRANSFER", etc.)
     */
    private String transactionType;
    
    /**
     * ID de la transacción en la wallet (opcional, para trazabilidad)
     */
    private String walletTransactionId;
} 