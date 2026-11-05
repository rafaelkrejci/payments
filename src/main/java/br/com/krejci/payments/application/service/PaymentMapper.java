package br.com.krejci.payments.application.service;

import br.com.krejci.payments.application.port.in.response.PaymentResponse;
import br.com.krejci.payments.domain.model.Payment;

public class PaymentMapper {

    public static PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getTransactionId().getValue(),
                payment.getPan(),
                payment.getCvv(),
                payment.getExpirationDate(),
                payment.getCardHolderName(),
                payment.getCardHolderLastName(),
                payment.getCardHolderEmail(),
                payment.getCardHolderPhone(),
                payment.getSku(),
                payment.getAmount()
        );
    }
}
