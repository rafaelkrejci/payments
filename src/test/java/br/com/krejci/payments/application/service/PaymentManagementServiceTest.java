package br.com.krejci.payments.application.service;

import br.com.krejci.payments.application.port.in.command.CreatePaymentCommand;
import br.com.krejci.payments.application.port.in.query.PaymentPageRequest;
import br.com.krejci.payments.application.port.out.PaymentPage;
import br.com.krejci.payments.application.port.out.PaymentRepository;
import br.com.krejci.payments.application.port.out.event.PaymentEventPublisher;
import br.com.krejci.payments.domain.exception.PaymentNotFoundException;
import br.com.krejci.payments.domain.model.Payment;
import br.com.krejci.payments.domain.vo.TransactionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentManagementServiceTest {

    private PaymentRepository paymentRepository;
    private PaymentEventPublisher paymentEventPublisher;
    private PaymentManagementService service;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        paymentEventPublisher = mock(PaymentEventPublisher.class);
        service = new PaymentManagementService(paymentRepository, paymentEventPublisher);
    }

    @Test
    void createPaymentGeneratesTransactionIdAndPublishesPayment() {
        CreatePaymentCommand command = command();

        var response = service.createPayment(command);

        assertNotNull(response.transactionId());
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentEventPublisher).publish(paymentCaptor.capture());
        assertEquals(response.transactionId(), paymentCaptor.getValue().getTransactionId().getValue());
        assertEquals(command.getSku(), paymentCaptor.getValue().getSku());
    }

    @Test
    void registerPaymentPersistsConsumedPayment() {
        Payment payment = payment("tx-123");

        service.registerPayment(payment);

        verify(paymentRepository).save(payment);
    }

    @Test
    void findPaymentMapsRepositoryResultToResponse() {
        Payment payment = payment("tx-123");
        when(paymentRepository.findByTransactionId(payment.getTransactionId()))
                .thenReturn(Optional.of(payment));

        var response = service.findPaymentByTransactionId(payment.getTransactionId());

        assertEquals("tx-123", response.transactionId());
        assertEquals(payment.getAmount(), response.amount());
    }

    @Test
    void findPaymentThrowsWhenTransactionDoesNotExist() {
        TransactionId transactionId = new TransactionId("missing");
        when(paymentRepository.findByTransactionId(transactionId)).thenReturn(Optional.empty());

        assertThrows(PaymentNotFoundException.class,
                () -> service.findPaymentByTransactionId(transactionId));
    }

    @Test
    void findAllMapsPaymentsAndPreservesPageMetadata() {
        PaymentPageRequest pageRequest = new PaymentPageRequest(1, 2);
        when(paymentRepository.findAll(1, 2))
                .thenReturn(new PaymentPage(List.of(payment("tx-123"), payment("tx-456")), 1, 2, 5));

        var response = service.findAllPayments(pageRequest);

        assertEquals(List.of("tx-123", "tx-456"),
                response.content().stream().map(payment -> payment.transactionId()).toList());
        assertEquals(1, response.page());
        assertEquals(2, response.size());
        assertEquals(5, response.totalElements());
        assertEquals(3, response.totalPages());
    }

    @Test
    void pageRequestRejectsNegativePageAndInvalidSize() {
        assertThrows(IllegalArgumentException.class, () -> new PaymentPageRequest(-1, 20));
        assertThrows(IllegalArgumentException.class, () -> new PaymentPageRequest(0, 0));
        assertThrows(IllegalArgumentException.class, () -> new PaymentPageRequest(0, 101));
    }

    private CreatePaymentCommand command() {
        return new CreatePaymentCommand(
                12345678,
                123,
                "12/30",
                "Maria",
                "Silva",
                "maria@example.com",
                "+5511999999999",
                "SKU-001",
                1500
        );
    }

    private Payment payment(String transactionId) {
        return new Payment(
                null,
                new TransactionId(transactionId),
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
