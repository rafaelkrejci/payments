package br.com.krejci.payments.application.port.out;

import br.com.krejci.payments.domain.model.Payment;

import java.util.List;

public record PaymentPage(List<Payment> content, int page, int size, long totalElements) {

    public PaymentPage {
        content = List.copyOf(content);
        if (page < 0 || size < 1 || totalElements < 0) {
            throw new IllegalArgumentException("Invalid payment page metadata");
        }
    }

    public int totalPages() {
        long pages = totalElements / size + (totalElements % size == 0 ? 0 : 1);
        return Math.toIntExact(pages);
    }
}
