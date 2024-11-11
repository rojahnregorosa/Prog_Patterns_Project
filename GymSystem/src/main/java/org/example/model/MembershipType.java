package org.example.model;

import lombok.Getter;

@Getter
public enum MembershipType {
    REGULAR(19.99, 259.75),
    PREMIUM(29.99, 389.75);

    private final double monthlyPrice;
    private final double yearlyPrice;

    MembershipType(double monthlyPrice, double yearlyPrice) {
        this.monthlyPrice = monthlyPrice;
        this.yearlyPrice = yearlyPrice;
    }
}
