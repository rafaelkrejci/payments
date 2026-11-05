package br.com.krejci.payments.infrastructure.adapter.out.persistence.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "payments")
public class PaymentEntity {

    @org.springframework.data.annotation.Id
    private String id;
    private String transactionId;
    private String sku;
    private Integer pan;
    private String expirationDate;
    private Integer cvv;
    private String cardHolderName;
    private String cardHolderLastName;
    private String cardHolderEmail;
    private String cardHolderPhone;
    private Integer amount;

    public PaymentEntity() {
    }
    private PaymentEntity(Builder builder) {
        id = builder.id;
        transactionId = builder.transactionId;
        amount = builder.amount;
        sku = builder.sku;
        pan = builder.pan;
        expirationDate = builder.expirationDate;
        cvv = builder.cvv;
        cardHolderName = builder.cardHolderName;
        cardHolderLastName = builder.cardHolderLastName;
        cardHolderEmail = builder.cardHolderEmail;
        cardHolderPhone = builder.cardHolderPhone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getPan() {
        return pan;
    }

    public void setPan(Integer pan) {
        this.pan = pan;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardHolderLastName() {
        return cardHolderLastName;
    }

    public void setCardHolderLastName(String cardHolderLastName) {
        this.cardHolderLastName = cardHolderLastName;
    }

    public String getCardHolderEmail() {
        return cardHolderEmail;
    }

    public void setCardHolderEmail(String cardHolderEmail) {
        this.cardHolderEmail = cardHolderEmail;
    }

    public String getCardHolderPhone() {
        return cardHolderPhone;
    }

    public void setCardHolderPhone(String cardHolderPhone) {
        this.cardHolderPhone = cardHolderPhone;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    public Integer getAmount() {
        return amount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String transactionId;
        private Integer amount;
        private String sku;
        private Integer pan;
        private String expirationDate;
        private Integer cvv;
        private String cardHolderName;
        private String cardHolderLastName;
        private String cardHolderEmail;
        private String cardHolderPhone;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder amount(Integer amount) {
            this.amount = amount;
            return this;
        }

        public Builder sku(String sku) {
            this.sku = sku;
            return this;
        }

        public Builder pan(Integer pan) {
            this.pan = pan;
            return this;
        }

        public Builder expirationDate(String expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public Builder cvv(Integer cvv) {
            this.cvv = cvv;
            return this;
        }

        public Builder cardHolderName(String cardHolderName) {
            this.cardHolderName = cardHolderName;
            return this;
        }

        public Builder cardHolderLastName(String cardHolderLastName) {
            this.cardHolderLastName = cardHolderLastName;
            return this;
        }

        public Builder cardHolderEmail(String cardHolderEmail) {
            this.cardHolderEmail = cardHolderEmail;
            return this;
        }

        public Builder cardHolderPhone(String cardHolderPhone) {
            this.cardHolderPhone = cardHolderPhone;
            return this;
        }

        public PaymentEntity build() {
            return new PaymentEntity(this);
        }
    }


}
