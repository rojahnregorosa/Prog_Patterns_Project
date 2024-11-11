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
        // Validate card number (should be 16 digits)
        if (cardNumber == null || !cardNumber.matches("^\\d{16}$")) {
            throw new IllegalArgumentException("Invalid card number. Must be 16 digits.");
        }

        // Validate cardholder name (should contain a space to ensure first and last name)
        if (cardHolderName == null || !cardHolderName.trim().contains(" ")) {
            throw new IllegalArgumentException("Invalid cardholder name. Must include both first and last name.");
        }

        // Validate pin (should be exactly 4 digits)
        if (String.valueOf(pin).length() != 3 || !String.valueOf(pin).matches("^\\d{4}$")) {
            throw new IllegalArgumentException("Invalid pin. Must be 4 digits.");
        }

        return true;
    }

}
