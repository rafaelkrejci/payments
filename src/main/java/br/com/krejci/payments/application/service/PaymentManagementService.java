package br.com.krejci.payments.application.service;

import br.com.krejci.payments.application.port.in.command.CreatePaymentCommand;
import br.com.krejci.payments.application.port.in.query.PaymentPageRequest;
import br.com.krejci.payments.application.port.in.response.PaymentAcceptedResponse;
import br.com.krejci.payments.application.port.in.response.PaymentPageResponse;
import br.com.krejci.payments.application.port.in.response.PaymentResponse;
import br.com.krejci.payments.application.port.in.usecase.PaymentManagementUseCase;
import br.com.krejci.payments.application.port.out.PaymentPage;
import br.com.krejci.payments.application.port.out.PaymentRepository;
import br.com.krejci.payments.application.port.out.event.PaymentEventPublisher;
import br.com.krejci.payments.domain.exception.PaymentNotFoundException;
import br.com.krejci.payments.domain.model.Payment;
import br.com.krejci.payments.domain.vo.TransactionId;
import org.springframework.stereotype.Service;

@Service
public class PaymentManagementService implements PaymentManagementUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentEventPublisher paymentEventPublisher;

    public PaymentManagementService(
            PaymentRepository paymentRepository,
            PaymentEventPublisher paymentEventPublisher
    ) {
        this.paymentRepository = paymentRepository;
        this.paymentEventPublisher = paymentEventPublisher;
    }

    @Override
    public PaymentAcceptedResponse createPayment(CreatePaymentCommand command) {
        TransactionId transactionId = TransactionId.generate();
        Payment payment = new Payment(
                null,
                transactionId,
                command.getSku(),
                command.getPan(),
                command.getExpirationDate(),
                command.getCvv(),
                command.getCardHolderName(),
                command.getCardHolderLastName(),
                command.getCardHolderEmail(),
                command.getCardHolderPhone(),
                command.getAmount()
        );

        paymentEventPublisher.publish(payment);
        return new PaymentAcceptedResponse(transactionId.getValue());
    }

    @Override
    public void registerPayment(Payment payment) {
        paymentRepository.save(payment);
    }

    @Override
    public PaymentResponse findPaymentByTransactionId(TransactionId transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new PaymentNotFoundException(transactionId));
        return PaymentMapper.toResponse(payment);
    }

    @Override
    public PaymentPageResponse findAllPayments(PaymentPageRequest pageRequest) {
        PaymentPage result = paymentRepository.findAll(pageRequest.page(), pageRequest.size());
        return new PaymentPageResponse(
                result.content().stream().map(PaymentMapper::toResponse).toList(),
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages()
        );
    }
}
