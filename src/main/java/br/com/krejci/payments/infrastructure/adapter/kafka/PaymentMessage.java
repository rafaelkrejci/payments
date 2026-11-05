package br.com.krejci.payments.infrastructure.adapter.kafka;

import br.com.krejci.payments.domain.model.Payment;
import br.com.krejci.payments.domain.vo.TransactionId;

public record PaymentMessage(
        String transactionId,
        String sku,
        Integer pan,
        String expirationDate,
        Integer cvv,
        String cardHolderName,
        String cardHolderLastName,
        String cardHolderEmail,
        String cardHolderPhone,
        Integer amount
) {

    public static PaymentMessage from(Payment payment) {
        return new PaymentMessage(
                payment.getTransactionId().getValue(),
                payment.getSku(),
                payment.getPan(),
                payment.getExpirationDate(),
                payment.getCvv(),
                payment.getCardHolderName(),
                payment.getCardHolderLastName(),
                payment.getCardHolderEmail(),
                payment.getCardHolderPhone(),
                payment.getAmount()
        );
    }

    public Payment toDomain() {
        return new Payment(
                null,
                new TransactionId(transactionId),
                sku,
                pan,
                expirationDate,
                cvv,
                cardHolderName,
                cardHolderLastName,
                cardHolderEmail,
                cardHolderPhone,
                amount
        );
    }
}
