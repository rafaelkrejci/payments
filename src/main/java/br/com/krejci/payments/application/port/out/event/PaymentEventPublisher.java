package br.com.krejci.payments.application.port.out.event;

import br.com.krejci.payments.domain.model.Payment;

public interface PaymentEventPublisher {

    void publish(Payment payment);
}
