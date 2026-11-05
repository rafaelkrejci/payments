package br.com.krejci.payments.application.port.in.response;

import java.util.List;

public record PaymentPageResponse(
        List<PaymentResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {

    public PaymentPageResponse {
        content = List.copyOf(content);
    }
}
