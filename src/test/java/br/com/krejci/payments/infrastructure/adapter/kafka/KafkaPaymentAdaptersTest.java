package br.com.krejci.payments.infrastructure.adapter.kafka;

import br.com.krejci.payments.application.port.in.usecase.PaymentManagementUseCase;
import br.com.krejci.payments.config.KafkaConfiguration;
import br.com.krejci.payments.domain.model.Payment;
import br.com.krejci.payments.domain.vo.TransactionId;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KafkaPaymentAdaptersTest {

    @Test
    void publisherSendsMappedMessageAndWaitsForBrokerAcknowledgement() {
        @SuppressWarnings("unchecked")
        KafkaTemplate<Object, Object> kafkaTemplate = mock(KafkaTemplate.class);
        CompletableFuture<SendResult<Object, Object>> sendResult = CompletableFuture.completedFuture(null);
        when(kafkaTemplate.send(eq(KafkaConfiguration.TOPIC_NAME), any(PaymentMessage.class)))
                .thenReturn(sendResult);
        KafkaPaymentEventPublisher publisher = new KafkaPaymentEventPublisher(kafkaTemplate);
        Payment payment = payment();

        publisher.publish(payment);

        verify(kafkaTemplate).send(eq(KafkaConfiguration.TOPIC_NAME), eq(PaymentMessage.from(payment)));
    }

    @Test
    void publisherPropagatesBrokerFailure() {
        @SuppressWarnings("unchecked")
        KafkaTemplate<Object, Object> kafkaTemplate = mock(KafkaTemplate.class);
        CompletableFuture<SendResult<Object, Object>> sendResult =
                CompletableFuture.failedFuture(new IllegalStateException("broker unavailable"));
        when(kafkaTemplate.send(eq(KafkaConfiguration.TOPIC_NAME), any(PaymentMessage.class)))
                .thenReturn(sendResult);
        KafkaPaymentEventPublisher publisher = new KafkaPaymentEventPublisher(kafkaTemplate);

        CompletionException exception = assertThrows(
                CompletionException.class,
                () -> publisher.publish(payment())
        );

        assertEquals("broker unavailable", exception.getCause().getMessage());
    }

    @Test
    void consumerSendsDomainPaymentToUseCase() {
        PaymentManagementUseCase useCase = mock(PaymentManagementUseCase.class);
        KafkaPaymentMessageConsumer consumer = new KafkaPaymentMessageConsumer(useCase);
        PaymentMessage message = PaymentMessage.from(payment());

        consumer.consume(message);

        org.mockito.ArgumentCaptor<Payment> paymentCaptor = org.mockito.ArgumentCaptor.forClass(Payment.class);
        verify(useCase).registerPayment(paymentCaptor.capture());
        assertEquals(message.toDomain().getTransactionId(), paymentCaptor.getValue().getTransactionId());
        assertEquals(message.toDomain().getPan(), paymentCaptor.getValue().getPan());
    }

    @Test
    void consumerPropagatesUseCaseFailure() {
        PaymentManagementUseCase useCase = mock(PaymentManagementUseCase.class);
        KafkaPaymentMessageConsumer consumer = new KafkaPaymentMessageConsumer(useCase);
        PaymentMessage message = PaymentMessage.from(payment());
        doThrow(new IllegalStateException("persistence failed"))
                .when(useCase).registerPayment(any(Payment.class));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> consumer.consume(message)
        );

        assertEquals("persistence failed", exception.getMessage());
    }

    private Payment payment() {
        return new Payment(
                null, new TransactionId("tx-kafka"), "SKU-1", 1234, "12/30",
                123, "Maria", "Silva", "maria@example.com", "+5511999999999", 500
        );
    }
}
