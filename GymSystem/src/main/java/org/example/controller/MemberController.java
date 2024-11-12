package org.example.controller;

import org.example.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class MemberController extends UserController {
    private List<Member> members;

    public MemberController() {
        super();
        members = new ArrayList<>();
    }

    public boolean signupMember() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Sign up as a new member");

        String firstName = Validator.validateAlphabetsOnly("Enter first name: ");
        String lastName = Validator.validateAlphabetsOnly("Enter last name: ");
        String phoneNumber = Validator.validatePhoneNumber("Enter phone number: ");
        int streetNumber = Validator.validateStreetNumber("Enter street number: ");
        String streetName = Validator.validateAlphabetsOnly("Enter street name: ");
        String city = Validator.validateAlphabetsOnly("Enter city: ");
        String province = Validator.validateAlphabetsOnly("Enter province: ");
        String zipCode = Validator.validateZipCode("Enter zip code: ");

        Address address = new Address(streetNumber, streetName, city, province, zipCode);

        // Prompt user to select membership type without a separate method
        System.out.println("Select membership type:");
        System.out.println("1. Regular - Monthly: $" + MembershipType.REGULAR.getMonthlyPrice() + ", Yearly: $" + MembershipType.REGULAR.getYearlyPrice());
        System.out.println("2. Premium - Monthly: $" + MembershipType.PREMIUM.getMonthlyPrice() + ", Yearly: $" + MembershipType.PREMIUM.getYearlyPrice());

        int typeChoice = sc.nextInt();
        sc.nextLine(); // Consume newline

        MembershipType membershipType = (typeChoice == 1) ? MembershipType.REGULAR : MembershipType.PREMIUM;

        // Creating the new member with an initial balance of 0
        Member newMember = new Member(firstName, lastName, address, phoneNumber, new Membership(membershipType), 0);

        // Add the new member to the in-memory list
        members.add(newMember);

        System.out.println("Signup successful! You can now log in using your Member ID: " + newMember.getMemberId());
        return true;
    }

    /**
     * Displays member profile
     * @param memberID the member id to check
     */
    public void displayMemberProfile(String memberID) {
        Member memberProfile = findMemberByID(memberID); // Directly retrieve the Member without using viewProfile
        if (memberProfile != null) {
            System.out.println("Profile Details:");
            System.out.println("ID: " + memberProfile.getMemberId());
            System.out.println("Name: " + memberProfile.getFname() + " " + memberProfile.getLname());
            System.out.println("Street Number: " + memberProfile.getAddress().getStreetNumber());
            System.out.println("Street Name: " + memberProfile.getAddress().getStreetName());
            System.out.println("City: " + memberProfile.getAddress().getCity());
            System.out.println("Province: " + memberProfile.getAddress().getProvince());
            System.out.println("Zip Code: " + memberProfile.getAddress().getZipCode());
            System.out.println("Membership Type: " + memberProfile.getMembershipType().getType());
            System.out.println("Balance: $" + memberProfile.getBalance());
            // Add any other member details you want to display here
        } else {
            System.out.println("Member profile not found.");
        }
    }

    /**
     * Updates profile of member
     * @param member the member to be updated
     */
    public void updateMemberProfile(Member member) {
        Scanner sc = new Scanner(System.in); // Local scanner instance for this method

        String firstName = Validator.validateAlphabetsOnly("Enter first name: ");
        String lastName = Validator.validateAlphabetsOnly("Enter last name: ");
        String phoneNumber = Validator.validatePhoneNumber("Enter phone number: ");
        int streetNumber = Validator.validateStreetNumber("Enter street number: ");
        String streetName = Validator.validateAlphabetsOnly("Enter street name: ");
        String city = Validator.validateAlphabetsOnly("Enter city: ");
        String province = Validator.validateAlphabetsOnly("Enter province: ");
        String zipCode = Validator.validateZipCode("Enter zip code: ");

        // Creating a new Address object with all four arguments
        Address address = new Address(streetNumber, streetName, city, province, zipCode);
    }

    /**
     * Manages membership of member
     * @param member to be managed
     */
    public void manageMembership(Member member) {
        Scanner sc = new Scanner(System.in); // Local scanner for input

        System.out.println("Current Membership Type: " + member.getMembershipType().getType());

        if (member.getMembershipType().getType() == MembershipType.REGULAR) {
            System.out.println("1. Upgrade to Premium");
            System.out.println("2. Cancel Membership");
        } else {
            System.out.println("1. Downgrade to Regular");
            System.out.println("2. Cancel Membership");
        }
        System.out.print("Choose an option: ");

        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline

        switch (choice) {
            case 1 -> {
                member.getMembershipType().setType(MembershipType.PREMIUM); // Upgrade to Premium
                System.out.println("Membership upgraded to Premium.");
            }
            case 2 -> {
                member.getMembershipType().setType(MembershipType.REGULAR); // Downgrade to Regular
                System.out.println("Membership downgraded to Regular.");
            }
            case 3 -> {
                member.setMembershipType(null); // Cancels membership by setting it to null
                System.out.println("Membership canceled.");
            }
            default -> {
                System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * Checks balance of member
     * @param member the member to check balance of
     */
    public void checkBalance(Member member) {
        double balance = member.getBalance();
        System.out.println("Account Balance: $" + balance);
    }

    /**
     * Makes payment based on their membership type
     * @param memberID of the member
     * @param frequencyType how often they pay
     * @return if payment was successful
     */
    public boolean makePayment(String memberID, String frequencyType) {
        Member member = findMemberByID(memberID);

        if (member != null) {
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

            // Call the helper method to select and process payment method
            Payment payment = selectPaymentMethod(requiredAmount);
            if (payment == null) {
                System.out.println("Payment process failed.");
                return false;
            }

            // Deduct balance if payment was successful
            member.setBalance(member.getBalance() - requiredAmount);
            System.out.println("Payment successful. " + frequencyType + " payment of $" + requiredAmount + " made.");
            return true;
        }

        System.out.println("Member not found.");
        return false;
    }

    /**
     * Helper method to select payment method
     * @param requiredAmount amount to pay off
     * @return type of payment
     */
    private Payment selectPaymentMethod(double requiredAmount) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Choose payment method:");
        System.out.println("1. Cash");
        System.out.println("2. Credit Card");
        System.out.println("3. Debit Card");
        System.out.print("Enter choice: ");
        int paymentChoice = sc.nextInt();
        sc.nextLine(); // Consume newline

        Payment payment = null;

        switch (paymentChoice) {
            case 1 -> { // Cash payment
                System.out.print("Enter cash received: ");
                double cashReceived = sc.nextDouble();
                sc.nextLine(); // Consume newline

                payment = new CashPayment(cashReceived);
            }
            case 2 -> { // Credit Card payment

                String cardNumber = Validator.validateCardNumber("Enter card number: ");
                String cardHolderName = Validator.validateCardHolderName("Enter card holder name: ");
                String expiryDate = Validator.validateExpiryDate("Enter expiration date (MM/YY): ");
                int cvv = Validator.validateCVV("Enter CVV: ");

                payment = new CreditCardPayment(cardNumber, cardHolderName, expiryDate, cvv);

            }
            case 3 -> { // Debit Card payment

                String cardNumber = Validator.validateCardNumber("Enter card number: ");
                String cardHolderName = Validator.validateCardHolderName("Enter card holder name: ");
                String bankName = Validator.validateBankName("Enter bank name: ");
                int pin = Validator.validatePinNumber("Enter PIN: ");

                payment = new DebitCardPayment(cardNumber, cardHolderName, bankName, pin);
            }
            default -> {
                System.out.println("Invalid payment method selected.");
                return null;
            }
        }
        return payment;
    }

    /**
     * Views notification of member form gym system
     * @param member member to view notification
     */
    public void viewNotifications(Member member) {
        List<Notification> notifications = member.getNotifications();
        if (notifications.isEmpty()) {
            System.out.println("No new notifications.");
        } else {
            System.out.println("Notifications:");
            for (Notification notification : notifications) {
                System.out.println("- " + notification);
            }
        }
    }

    /**
     * Find member by id
     * @param memberID member to find
     * @return member
     */
    public Member findMemberByID(String memberID) {
        for (Member member : members) {
            if (Objects.equals(member.getMemberId(), memberID)) {
                return member;
            }
        }
        return null;
    }
}
