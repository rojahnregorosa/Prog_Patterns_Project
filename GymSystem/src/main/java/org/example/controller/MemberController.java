package org.example.controller;

import org.example.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemberController extends UserController {
    private List<Member> members;

    public MemberController() {
        super();
        members = new ArrayList<>();
    }

    // Method to update a member's address
    public boolean updateAddress(String memberID, Address newAddress) {
        Member member = findMemberByID(memberID);
        if (member != null) {
            member.setAddress(newAddress);
            return true;
        }
        return false;
    }

    // Method to update a member's membership type
    public boolean updateMembership(String memberID, Membership newMembership) {
        Member member = findMemberByID(memberID);
        if (member != null) {
            member.setMembershipType(newMembership);
            return true;
        }
        return false;
    }

    // Method to validate a phone number format
    public boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.matches("\\d{10}"); // Example validation for a 10-digit phone number
    }

    // Method to update a member's balance
    public boolean updateBalance(String memberID, double balance) {
        Member member = findMemberByID(memberID);
        if (member != null) {
            member.setBalance(balance);
            return true;
        }
        return false;
    }

    // Method to view a member's profile
    public Member viewProfile(String memberID) {
        return findMemberByID(memberID);
    }

    // Method to make a payment for a member
    public boolean makePayment(String memberID, Payment payment, String frequencyType) {
        Member member = findMemberByID(memberID);
        if (member != null && payment != null) {
            MembershipType membershipType = member.getMembershipType().getType();
            double requiredAmount = 0;

            // Determine the required payment amount based on membership type and frequency
            if ("monthly".equalsIgnoreCase(frequencyType)) {
                requiredAmount = membershipType.getMonthlyPrice();
            } else if ("yearly".equalsIgnoreCase(frequencyType)) {
                requiredAmount = membershipType.getYearlyPrice();
            } else {
                System.out.println("Invalid payment frequency: " + frequencyType);
                return false;
            }

            // Check if the payment amount matches the required amount
            if (payment.processPayment(requiredAmount)) {
                member.setBalance(member.getBalance() - requiredAmount);
                System.out.println("Payment successful. " + frequencyType + " payment of $" + requiredAmount + " made.");
                return true;
            } else {
                System.out.println("Payment failed: Payment amount must match the " + frequencyType + " price for the membership.");
            }
        }
        return false;
    }

    // Helper method to find a member by ID
    private Member findMemberByID(String memberID) {
        for (Member member : members) {
            if (Objects.equals(member.getMemberId(), memberID)) {
                return member;
            }
        }
        return null;
    }
}
