package org.example.model;

public class CashPayment implements Payment {
    private double cashReceived;

    public CashPayment(double cashReceived) {
        this.cashReceived = cashReceived;
    }

    @Override
    public PaymentType getPaymentType() {
        return PaymentType.CASH;
    }

    /**
     * Calculates change
     *
     * @param amount the amount to subtract from cash received
     * @return the change
     */
    public double calculateChange(double amount) {
        return cashReceived - amount;
    }
}
