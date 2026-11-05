package br.com.krejci.payments.domain;

import br.com.krejci.payments.domain.model.Payment;
import br.com.krejci.payments.domain.vo.PaymentId;
import br.com.krejci.payments.domain.vo.TransactionId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentValueObjectsTest {

    @Test
    void generatedTransactionIdsAreNonblankAndUnique() {
        TransactionId first = TransactionId.generate();
        TransactionId second = TransactionId.generate();

        assertTrue(!first.getValue().isBlank());
        assertNotEquals(first, second);
    }

    @Test
    void transactionIdRejectsNullAndBlankValues() {
        assertThrows(IllegalArgumentException.class, () -> new TransactionId(null));
        assertThrows(IllegalArgumentException.class, () -> new TransactionId(" "));
    }

    @Test
    void paymentIdRejectsNullAndBlankValues() {
        assertThrows(IllegalArgumentException.class, () -> new PaymentId(null));
        assertThrows(IllegalArgumentException.class, () -> new PaymentId(""));
    }

    @Test
    void valueObjectsCompareByValue() {
        assertEquals(new TransactionId("tx-1"), new TransactionId("tx-1"));
        assertEquals(new PaymentId("payment-1"), new PaymentId("payment-1"));
        assertEquals(new TransactionId("tx-1").hashCode(), new TransactionId("tx-1").hashCode());
    }

    @Test
    void withIdReturnsPaymentCopyAndKeepsExistingFields() {
        Payment payment = new Payment(
                null, new TransactionId("tx-1"), "SKU-1", 1234, "12/30",
                123, "Maria", "Silva", "maria@example.com", "+5511999999999", 500
        );

        Payment identified = payment.withId(new PaymentId("mongo-1"));

        assertEquals(new PaymentId("mongo-1"), identified.getId());
        assertEquals(payment.getTransactionId(), identified.getTransactionId());
        assertEquals(payment.getSku(), identified.getSku());
        assertEquals(payment.getAmount(), identified.getAmount());
    }
}
