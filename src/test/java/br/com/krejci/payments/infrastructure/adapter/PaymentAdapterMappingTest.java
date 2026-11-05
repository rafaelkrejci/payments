package br.com.krejci.payments.infrastructure.adapter;

import br.com.krejci.payments.domain.model.Payment;
import br.com.krejci.payments.domain.vo.PaymentId;
import br.com.krejci.payments.domain.vo.TransactionId;
import br.com.krejci.payments.infrastructure.adapter.kafka.PaymentMessage;
import br.com.krejci.payments.infrastructure.adapter.out.persistence.entity.PaymentEntity;
import br.com.krejci.payments.infrastructure.adapter.out.persistence.mapper.PaymentEntityMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentAdapterMappingTest {

    private final PaymentEntityMapper entityMapper = new PaymentEntityMapper();

    @Test
    void persistenceMapperPreservesPaymentFieldsAndIdentifiers() {
        Payment payment = payment(new PaymentId("mongo-id"));

        PaymentEntity entity = entityMapper.toEntity(payment);
        Payment restoredPayment = entityMapper.toDomain(entity);

        assertEquals("mongo-id", entity.getId());
        assertEquals(payment.getId(), restoredPayment.getId());
        assertEquals(payment.getTransactionId(), restoredPayment.getTransactionId());
        assertEquals(payment.getPan(), restoredPayment.getPan());
        assertEquals(payment.getCvv(), restoredPayment.getCvv());
        assertEquals(payment.getAmount(), restoredPayment.getAmount());
    }

    @Test
    void kafkaMessageRoundTripPreservesPaymentFields() {
        Payment payment = payment(null);

        Payment restoredPayment = PaymentMessage.from(payment).toDomain();

        assertEquals(payment.getTransactionId(), restoredPayment.getTransactionId());
        assertEquals(payment.getSku(), restoredPayment.getSku());
        assertEquals(payment.getPan(), restoredPayment.getPan());
        assertEquals(payment.getCvv(), restoredPayment.getCvv());
        assertEquals(payment.getAmount(), restoredPayment.getAmount());
    }

    private Payment payment(PaymentId id) {
        return new Payment(
                id,
                new TransactionId("tx-123"),
                "SKU-001",
                12345678,
                "12/30",
                123,
                "Maria",
                "Silva",
                "maria@example.com",
                "+5511999999999",
                1500
        );
    }
}
