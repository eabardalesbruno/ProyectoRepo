package com.proriberaapp.ribera.utils;

public class PaymeCodeErrorsWithMessage {

    private static final String STATUS_PENDING = "01"; // Transaction pending
    private static final String STATUS_DECLINED = "02"; // Transaction declined
    private static final String STATUS_ERROR = "03"; // System error
    private static final String STATUS_INVALID_CARD = "04"; // Invalid card
    private static final String STATUS_INSUFFICIENT_FUNDS = "05"; // Insufficient funds
    private static final String STATUS_EXPIRED_CARD = "06"; // Expired card
    private static final String STATUS_INVALID_DATA = "07"; // Invalid transaction data
    private static final String STATUS_TIMEOUT = "08"; // Transaction timeout
    private static final String STATUS_FRAUD = "09"; // Suspicious transaction
    private static final String STATUS_DECLINE_CVV2 = "N7"; // Suspicious transaction

    public static String getMessage(String code) {
        switch (code) {
            case STATUS_PENDING:
                return "Transacción pendiente";
            case STATUS_DECLINED:
                return "Transacción declinada";
            case STATUS_ERROR:
                return "Error del sistema";
            case STATUS_INVALID_CARD:
                return "Tarjeta inválida";
            case STATUS_INSUFFICIENT_FUNDS:
                return "Fondos insuficientes";
            case STATUS_EXPIRED_CARD:
                return "Tarjeta vencida";
            case STATUS_INVALID_DATA:
                return "Datos de transacción inválidos";
            case STATUS_TIMEOUT:
                return "Tiempo de transacción agotado";
            case STATUS_FRAUD:
                return "Transacción sospechosa";
            case STATUS_DECLINE_CVV2:
                return "Decline for CVV2 failure";
            default:
                return "Error desconocido";
        }
    }
}
