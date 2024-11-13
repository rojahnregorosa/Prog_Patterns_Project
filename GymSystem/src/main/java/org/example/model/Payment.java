package org.example.model;

public interface Payment {
    PaymentType getPaymentType();

    enum PaymentType {
        CASH, CREDIT_CARD, DEBIT_CARD
    }
}


