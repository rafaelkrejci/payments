package br.com.krejci.payments.application.port.out;

import br.com.krejci.payments.domain.model.Payment;
import br.com.krejci.payments.domain.vo.TransactionId;

import java.util.Optional;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findByTransactionId(TransactionId transactionId);

    PaymentPage findAll(int page, int size);
}
