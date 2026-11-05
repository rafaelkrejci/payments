package br.com.krejci.payments.application.port.in.command;

public class CreatePaymentCommand {

    private Integer pan;
    private Integer cvv;
    private String expirationDate;
    private String cardHolderName;
    private String cardHolderLastName;
    private String cardHolderEmail;
    private String cardHolderPhone;
    private String sku;
    private Integer amount;

    public CreatePaymentCommand(
            Integer pan,
            Integer cvv,
            String expirationDate,
            String cardHolderName,
            String cardHolderLastName,
            String cardHolderEmail,
            String cardHolderPhone,
            String sku,
            Integer amount
    ) {
        this.pan = pan;
        this.cvv = cvv;
        this.expirationDate = expirationDate;
        this.cardHolderName = cardHolderName;
        this.cardHolderLastName = cardHolderLastName;
        this.cardHolderEmail = cardHolderEmail;
        this.cardHolderPhone = cardHolderPhone;
        this.sku = sku;
        this.amount = amount;
    }

    public Integer getPan() {
        return pan;
    }

    public Integer getCvv() {
        return cvv;
    }

    public String getExpirationDate() {
        return expirationDate;
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

    public String getSku() {
        return sku;
    }

    public Integer getAmount() {
        return amount;
    }
}
