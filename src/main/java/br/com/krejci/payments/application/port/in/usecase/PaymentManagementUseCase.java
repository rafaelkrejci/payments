package br.com.krejci.payments.application.port.in.usecase;

import br.com.krejci.payments.application.port.in.command.CreatePaymentCommand;
import br.com.krejci.payments.application.port.in.query.PaymentPageRequest;
import br.com.krejci.payments.application.port.in.response.PaymentAcceptedResponse;
import br.com.krejci.payments.application.port.in.response.PaymentPageResponse;
import br.com.krejci.payments.application.port.in.response.PaymentResponse;
import br.com.krejci.payments.domain.model.Payment;
import br.com.krejci.payments.domain.vo.TransactionId;

public interface PaymentManagementUseCase {

    PaymentAcceptedResponse createPayment(CreatePaymentCommand command);

    void registerPayment(Payment payment);

    PaymentResponse findPaymentByTransactionId(TransactionId transactionId);

    PaymentPageResponse findAllPayments(PaymentPageRequest pageRequest);
}
