package org.example.model;

public interface Payment {
    boolean processPayment(double amount);
    PaymentType getPaymentType();

    enum PaymentType {
        CASH, CREDIT_CARD, DEBIT_CARD
    }
}


