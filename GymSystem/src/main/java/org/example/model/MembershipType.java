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

    /**
     * Gets the price based on the frequency type.
     *
     * @param frequency The frequency type (either "monthly" or "yearly")
     * @return The price corresponding to the frequency
     * @throws IllegalArgumentException If an invalid frequency type is provided
     */
    public double getPrice(String frequency) {
        if ("monthly".equalsIgnoreCase(frequency)) {
            return monthlyPrice;
        } else if ("yearly".equalsIgnoreCase(frequency)) {
            return yearlyPrice;
        }
        throw new IllegalArgumentException("Invalid frequency type: " + frequency);
    }
}
