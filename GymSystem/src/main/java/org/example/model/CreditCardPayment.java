package org.example.model;

public class CreditCardPayment implements Payment {
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private int cvv;

    public CreditCardPayment(String cardNumber, String cardHolderName, String expiryDate, int cvv) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    @Override
    public PaymentType getPaymentType() {
        return PaymentType.CREDIT_CARD;
    }
}
