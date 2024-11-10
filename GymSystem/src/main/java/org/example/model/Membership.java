package org.example.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Membership {
    private String type; // Should be "regular" or "premium"
    private double price;

    public Membership(double price, String type) {
        this.price = price;
        this.type = type;
    }

    public double calculatePriceDifference(Membership otherMembership) {
        return Math.abs(this.price - otherMembership.getPrice());
    }
}