package org.example.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Membership {
    private MembershipType type;

    public Membership(MembershipType type) {
        this.type = type;
    }

    /**
     * Chooses price depending on the membership type
     *
     * @param frequencyType how frequent they want to pay
     * @return the price
     */
    public double getPrice(String frequencyType) {
        if ("monthly".equalsIgnoreCase(frequencyType)) {
            return type.getMonthlyPrice();
        } else if ("yearly".equalsIgnoreCase(frequencyType)) {
            return type.getYearlyPrice();
        }
        throw new IllegalArgumentException("Invalid frequency: " + frequencyType);
    }

    public MembershipType getType() {
        return type;
    }

    public void setType(MembershipType type) {
        this.type = type;
    }
}