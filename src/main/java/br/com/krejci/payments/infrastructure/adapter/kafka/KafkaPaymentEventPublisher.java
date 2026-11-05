package br.com.krejci.payments.infrastructure.adapter.kafka;

import br.com.krejci.payments.application.port.out.event.PaymentEventPublisher;
import br.com.krejci.payments.config.KafkaConfiguration;
import br.com.krejci.payments.domain.model.Payment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaPaymentEventPublisher implements PaymentEventPublisher {

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    public KafkaPaymentEventPublisher(KafkaTemplate<Object, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(Payment payment) {
        kafkaTemplate.send(KafkaConfiguration.TOPIC_NAME, PaymentMessage.from(payment)).join();
    }
}
