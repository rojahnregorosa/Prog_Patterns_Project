package org.example.model;

public interface Payment {
    boolean processPayment(double amount);
    PaymentType getPaymentType();
}
