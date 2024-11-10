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
        return null;
    }

    /**
     * authorizes if the information of credit card checkout out
     * @param amount to withdraw out of credit card
     * @return if all details are valid or not
     */
    private boolean authorizeTransaction(double amount) {
        // Logic to authorize credit card transaction
        // For example, check card details, expiry, etc.
        return true; // Assume authorization is successful for this example
    }
}
