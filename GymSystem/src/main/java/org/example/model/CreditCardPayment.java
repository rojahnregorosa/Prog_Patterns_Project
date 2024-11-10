package org.example.model;

public class CreditCardPayment implements Payment{
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
    public boolean processPayment(double amount) {
        return authorizeTransaction(amount);
    }

    @Override
    public PaymentType getPaymentType() {
        return PaymentType.CREDIT_CARD;
    }

    /**
     * authorizes if the information of credit card checks out
     * @param amount to withdraw out of credit card
     * @return if all details are valid or not
     */
    private boolean authorizeTransaction(double amount) {
        // Validate card number (should be 16 digits)
        if (cardNumber == null || !cardNumber.matches("^\\d{16}$")) {
            throw new IllegalArgumentException("Invalid card number. Must be 16 digits.");
        }

        // Validate cardholder name (should contain a space to ensure first and last name)
        if (cardHolderName == null || !cardHolderName.trim().contains(" ")) {
            throw new IllegalArgumentException("Invalid cardholder name. Must include both first and last name.");
        }

        // Validate expiry date (should follow MM/YY format)
        if (expiryDate == null || !expiryDate.matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
            throw new IllegalArgumentException("Invalid expiry date. Must be in MM/YY format.");
        }

        // Validate CVV (should be exactly 3 digits)
        if (String.valueOf(cvv).length() != 3 || !String.valueOf(cvv).matches("^\\d{3}$")) {
            throw new IllegalArgumentException("Invalid CVV. Must be 3 digits.");
        }
        return true;
    }
}
