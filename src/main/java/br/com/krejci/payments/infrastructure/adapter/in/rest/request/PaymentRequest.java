package br.com.krejci.payments.infrastructure.adapter.in.rest.request;

public class PaymentRequest {

    private Integer pan;
    private Integer cvv;
    private String expirationDate;
    private String cardHolderName;
    private String cardHolderLastName;
    private String cardHolderEmail;
    private String cardHolderPhone;
    private String sku;
    private Integer amount;

    public Integer getPan() {
        return pan;
    }

    public void setPan(Integer pan) {
        this.pan = pan;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
