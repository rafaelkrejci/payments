package br.com.krejci.payments.domain.exception;

import br.com.krejci.payments.domain.vo.TransactionId;

public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException(TransactionId transactionId) {
        super("Payment not found with transaction id: " + transactionId.getValue());
    }
}
