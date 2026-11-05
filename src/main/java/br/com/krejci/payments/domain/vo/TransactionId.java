package br.com.krejci.payments.domain.vo;

import java.util.Objects;
import java.util.UUID;

public class TransactionId {

    private final String value;

    public TransactionId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Transaction id must not be blank");
        }
        this.value = value;
    }

    public static TransactionId generate() {
        return new TransactionId(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof TransactionId that && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
