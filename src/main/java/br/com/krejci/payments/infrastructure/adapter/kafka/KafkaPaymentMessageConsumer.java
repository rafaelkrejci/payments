package br.com.krejci.payments.infrastructure.adapter.kafka;

import br.com.krejci.payments.application.port.in.usecase.PaymentManagementUseCase;
import br.com.krejci.payments.config.KafkaConfiguration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaPaymentMessageConsumer {

    private final PaymentManagementUseCase paymentManagementUseCase;

    public KafkaPaymentMessageConsumer(PaymentManagementUseCase paymentManagementUseCase) {
        this.paymentManagementUseCase = paymentManagementUseCase;
    }

    @KafkaListener(
            topics = KafkaConfiguration.TOPIC_NAME,
            groupId = KafkaConfiguration.GROUP_ID
    )
    public void consume(PaymentMessage message) {
        paymentManagementUseCase.registerPayment(message.toDomain());
    }
}
