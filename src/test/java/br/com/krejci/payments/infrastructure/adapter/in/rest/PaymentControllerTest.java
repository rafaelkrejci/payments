package br.com.krejci.payments.infrastructure.adapter.in.rest;

import br.com.krejci.payments.application.port.in.command.CreatePaymentCommand;
import br.com.krejci.payments.application.port.in.query.PaymentPageRequest;
import br.com.krejci.payments.application.port.in.response.PaymentAcceptedResponse;
import br.com.krejci.payments.application.port.in.response.PaymentPageResponse;
import br.com.krejci.payments.application.port.in.response.PaymentResponse;
import br.com.krejci.payments.application.port.in.usecase.PaymentManagementUseCase;
import br.com.krejci.payments.domain.exception.PaymentNotFoundException;
import br.com.krejci.payments.domain.vo.TransactionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentControllerTest {

    private PaymentManagementUseCase useCase;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        useCase = mock(PaymentManagementUseCase.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new PaymentController(useCase))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void postCreatesPaymentAndReturnsAcceptedTransactionId() throws Exception {
        when(useCase.createPayment(any(CreatePaymentCommand.class)))
                .thenReturn(new PaymentAcceptedResponse("tx-created"));

        mockMvc.perform(post("/api/v1/payments/")
                        .contentType("application/json")
                        .content("""
                                {
                                  "pan": 12345678,
                                  "cvv": 123,
                                  "expirationDate": "12/30",
                                  "cardHolderName": "Maria",
                                  "cardHolderLastName": "Silva",
                                  "cardHolderEmail": "maria@example.com",
                                  "cardHolderPhone": "+5511999999999",
                                  "sku": "SKU-1",
                                  "amount": 500
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("tx-created"))
                .andExpect(jsonPath("$.pan").doesNotExist());

        verify(useCase).createPayment(any(CreatePaymentCommand.class));
    }

    @Test
    void getByTransactionIdReturnsPayment() throws Exception {
        when(useCase.findPaymentByTransactionId(new TransactionId("tx-found")))
                .thenReturn(response("tx-found"));

        mockMvc.perform(get("/api/v1/payments/tx-found"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("tx-found"))
                .andExpect(jsonPath("$.amount").value(500));

        verify(useCase).findPaymentByTransactionId(new TransactionId("tx-found"));
    }

    @Test
    void getByTransactionIdReturnsNotFoundFromExceptionHandler() throws Exception {
        when(useCase.findPaymentByTransactionId(new TransactionId("missing")))
                .thenThrow(new PaymentNotFoundException(new TransactionId("missing")));

        mockMvc.perform(get("/api/v1/payments/missing"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllReturnsPaymentsFromUseCase() throws Exception {
        when(useCase.findAllPayments(new PaymentPageRequest(1, 2)))
                .thenReturn(new PaymentPageResponse(List.of(response("tx-1"), response("tx-2")), 1, 2, 5, 3));

        mockMvc.perform(get("/api/v1/payments/").param("page", "1").param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].transactionId").value("tx-1"))
                .andExpect(jsonPath("$.content[1].transactionId").value("tx-2"))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.totalPages").value(3));

        verify(useCase).findAllPayments(new PaymentPageRequest(1, 2));
    }

    @Test
    void getAllUsesDefaultPageAndSizeWhenOmitted() throws Exception {
        when(useCase.findAllPayments(new PaymentPageRequest(0, 20)))
                .thenReturn(new PaymentPageResponse(List.of(response("tx-1")), 0, 20, 1, 1));

        mockMvc.perform(get("/api/v1/payments/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(useCase).findAllPayments(new PaymentPageRequest(0, 20));
    }

    @Test
    void getAllRejectsInvalidPageAndSize() throws Exception {
        mockMvc.perform(get("/api/v1/payments/").param("page", "-1"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/v1/payments/").param("size", "101"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void unexpectedExceptionsAreMappedToInternalServerError() throws Exception {
        when(useCase.findAllPayments(new PaymentPageRequest(0, 20)))
                .thenThrow(new IllegalStateException("failure"));

        mockMvc.perform(get("/api/v1/payments/"))
                .andExpect(status().isInternalServerError());
    }

    private PaymentResponse response(String transactionId) {
        return new PaymentResponse(
                transactionId, 12345678, 123, "12/30", "Maria", "Silva",
                "maria@example.com", "+5511999999999", "SKU-1", 500
        );
    }
}
