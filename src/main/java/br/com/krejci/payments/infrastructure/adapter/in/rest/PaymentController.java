package br.com.krejci.payments.infrastructure.adapter.in.rest;

import br.com.krejci.payments.application.port.in.command.CreatePaymentCommand;
import br.com.krejci.payments.application.port.in.query.PaymentPageRequest;
import br.com.krejci.payments.application.port.in.response.PaymentAcceptedResponse;
import br.com.krejci.payments.application.port.in.response.PaymentPageResponse;
import br.com.krejci.payments.application.port.in.response.PaymentResponse;
import br.com.krejci.payments.application.port.in.usecase.PaymentManagementUseCase;
import br.com.krejci.payments.domain.vo.TransactionId;
import br.com.krejci.payments.infrastructure.adapter.in.rest.request.PaymentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentManagementUseCase paymentManagementUseCase;

    public PaymentController(PaymentManagementUseCase paymentManagementUseCase) {
        this.paymentManagementUseCase = paymentManagementUseCase;
    }

    @PostMapping({"", "/"})
    public ResponseEntity<PaymentAcceptedResponse> createPayment(@RequestBody PaymentRequest request) {
        CreatePaymentCommand command = new CreatePaymentCommand(
                request.getPan(),
                request.getCvv(),
                request.getExpirationDate(),
                request.getCardHolderName(),
                request.getCardHolderLastName(),
                request.getCardHolderEmail(),
                request.getCardHolderPhone(),
                request.getSku(),
                request.getAmount()
        );
        return ResponseEntity.ok(paymentManagementUseCase.createPayment(command));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable String transactionId) {
        return ResponseEntity.ok(paymentManagementUseCase.findPaymentByTransactionId(
                new TransactionId(transactionId)
        ));
    }

    @GetMapping({"", "/"})
    public PaymentPageResponse getAll(
            @RequestParam(defaultValue = "" + PaymentPageRequest.DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = "" + PaymentPageRequest.DEFAULT_SIZE) int size
    ) {
        return paymentManagementUseCase.findAllPayments(new PaymentPageRequest(page, size));
    }
}
