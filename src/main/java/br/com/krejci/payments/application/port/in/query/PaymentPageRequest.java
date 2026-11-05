package br.com.krejci.payments.application.port.in.query;

public record PaymentPageRequest(int page, int size) {

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 20;
    public static final int MAX_SIZE = 100;

    public PaymentPageRequest {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must be zero or greater");
        }
        if (size < 1 || size > MAX_SIZE) {
            throw new IllegalArgumentException("Page size must be between 1 and " + MAX_SIZE);
        }
    }
}
