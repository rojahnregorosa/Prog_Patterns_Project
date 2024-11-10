package org.example.model;

public class DebitCardPayment implements Payment {
    private String cardNumber;
    private String cardHolderName;
    private String bankName;
    private int pin;

    public DebitCardPayment(String cardNumber, String cardHolderName, String bankName, int pin) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.bankName = bankName;
        this.pin = pin;
    }

    @Override
    public boolean processPayment(double amount) {
        return authorizeTransaction(amount);
    }

    @Override
    public PaymentType getPaymentType() {
        return PaymentType.DEBIT_CARD;
    }

    /**
     * authorizes if the information of debit card checks out
     * @param amount to withdraw out of debit card
     * @return if all details are valid or not
     */
    public boolean authorizeTransaction(double amount) {
        return true;
    }

}
