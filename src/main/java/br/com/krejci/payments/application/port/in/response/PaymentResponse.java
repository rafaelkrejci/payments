package br.com.krejci.payments.application.port.in.response;

public record PaymentResponse(
        String transactionId,
        Integer pan,
        Integer cvv,
        String expirationDate,
        String cardHolderName,
        String cardHolderLastName,
        String cardHolderEmail,
        String cardHolderPhone,
        String sku,
        Integer amount
) {
}
