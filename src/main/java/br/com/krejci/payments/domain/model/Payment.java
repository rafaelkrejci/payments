package br.com.krejci.payments.domain.model;

import br.com.krejci.payments.domain.vo.PaymentId;
import br.com.krejci.payments.domain.vo.TransactionId;

public class Payment {

    private final PaymentId id;
    private final TransactionId transactionId;
    private final String sku;
    private final Integer pan;
    private final String expirationDate;
    private final Integer cvv;
    private final String cardHolderName;
    private final String cardHolderLastName;
    private final String cardHolderEmail;
    private final String cardHolderPhone;
    private final Integer amount;

    public Payment(
            PaymentId id,
            TransactionId transactionId,
            String sku,
            Integer pan,
            String expirationDate,
            Integer cvv,
            String cardHolderName,
            String cardHolderLastName,
            String cardHolderEmail,
            String cardHolderPhone,
            Integer amount
    ) {
        this.id = id;
        this.transactionId = transactionId;
        this.sku = sku;
        this.pan = pan;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.cardHolderName = cardHolderName;
        this.cardHolderLastName = cardHolderLastName;
        this.cardHolderEmail = cardHolderEmail;
        this.cardHolderPhone = cardHolderPhone;
        this.amount = amount;
    }

    public PaymentId getId() {
        return id;
    }

    public TransactionId getTransactionId() {
        return transactionId;
    }

    public String getSku() {
        return sku;
    }

    public Integer getPan() {
        return pan;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public Integer getCvv() {
        return cvv;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public String getCardHolderLastName() {
        return cardHolderLastName;
    }

    public String getCardHolderEmail() {
        return cardHolderEmail;
    }

    public String getCardHolderPhone() {
        return cardHolderPhone;
    }

    public Integer getAmount() {
        return amount;
    }

    public Payment withId(PaymentId paymentId) {
        return new Payment(
                paymentId,
                transactionId,
                sku,
                pan,
                expirationDate,
                cvv,
                cardHolderName,
                cardHolderLastName,
                cardHolderEmail,
                cardHolderPhone,
                amount
        );
    }
}
