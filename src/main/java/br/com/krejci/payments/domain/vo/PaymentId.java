package br.com.krejci.payments.domain.vo;

import java.util.Objects;

public class PaymentId {

    private final String value;

    public PaymentId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Payment id must not be blank");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof PaymentId that && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
