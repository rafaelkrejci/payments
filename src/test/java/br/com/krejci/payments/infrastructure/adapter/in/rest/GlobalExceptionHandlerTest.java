package br.com.krejci.payments.infrastructure.adapter.in.rest;

import br.com.krejci.payments.domain.exception.PaymentNotFoundException;
import br.com.krejci.payments.domain.vo.TransactionId;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void paymentNotFoundMapsToNotFoundResponse() {
        var response = handler.handlePaymentNotFoundException(
                new PaymentNotFoundException(new TransactionId("tx-missing"))
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Payment not found with transaction id: tx-missing", response.getBody().getMessage());
    }

    @Test
    void unexpectedExceptionMapsToInternalServerErrorResponse() {
        var response = handler.handleGenericException(new IllegalStateException("unexpected"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("An unexpected error occurred: unexpected", response.getBody().getMessage());
    }
}
