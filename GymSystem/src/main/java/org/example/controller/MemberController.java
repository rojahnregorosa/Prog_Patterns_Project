package org.example.controller;

import org.example.database.DatabaseConnection;
import org.example.database.MemberDatabase;
import org.example.model.*;
import org.example.utils.LanguageManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MemberController {
    private List<Member> members;

    public MemberController() {
        super();
        members = new ArrayList<>();
    }

    /**
     * Signs in new member to the system
     */
    public void signUpMember() {
        Scanner sc = new Scanner(System.in);
        LanguageManager languageManager = LanguageManager.getInstance();

        System.out.println(languageManager.getMessage("sign_up"));

        String firstName = Validator.validateAlphabetsOnly(languageManager.getMessage("enter_first_name"));
        String lastName = Validator.validateAlphabetsOnly(languageManager.getMessage("enter_last_name"));
        String phoneNumber = Validator.validatePhoneNumber(languageManager.getMessage("enter_phone_number"));
        int streetNumber = Validator.validateStreetNumber(languageManager.getMessage("enter_street_number"));
        String streetName = Validator.validateAlphabetsOnly(languageManager.getMessage("enter_street_name"));
        String city = Validator.validateAlphabetsOnly(languageManager.getMessage("enter_city"));
        String province = Validator.validateAlphabetsOnly(languageManager.getMessage("enter_province"));
        String zipCode = Validator.validateZipCode(languageManager.getMessage("enter_zip_code"));

        Address address = new Address(streetNumber, streetName, city, province, zipCode);

        // Prompt user to select membership type without a separate method

        System.out.println(languageManager.getMessage("select_membership_type"));

        System.out.println("1. " + languageManager.getMessage("membership_regular") + " - " +
                languageManager.getMessage("membership_price_monthly", MembershipType.REGULAR.getMonthlyPrice()) + ", " +
                languageManager.getMessage("membership_price_yearly", MembershipType.REGULAR.getYearlyPrice()));

        System.out.println("2. " + languageManager.getMessage("membership_premium") + " - " +
                languageManager.getMessage("membership_price_monthly", MembershipType.PREMIUM.getMonthlyPrice()) + ", " +
                languageManager.getMessage("membership_price_yearly", MembershipType.PREMIUM.getYearlyPrice()));

        int typeChoice = sc.nextInt();
        sc.nextLine(); // Consume newline

        MembershipType membershipType = (typeChoice == 1) ? MembershipType.REGULAR : MembershipType.PREMIUM;
        Membership membership = new Membership(membershipType);

        // Ask for payment frequency
        System.out.println(languageManager.getMessage("select_payment_frequency"));
        System.out.println("1. " + languageManager.getMessage("payment_frequency_monthly"));
        System.out.println("2. " + languageManager.getMessage("payment_frequency_yearly"));

        int frequencyChoice = sc.nextInt();
        sc.nextLine(); // Consume newline
        boolean isMonthly = (frequencyChoice == 1);

        // Set initial balance based on payment frequency
        double initialBalance;
        if (frequencyChoice == 1) {
            initialBalance = membershipType.getMonthlyPrice();
        } else if (frequencyChoice == 2) {
            initialBalance = membershipType.getYearlyPrice();
        } else {
            System.out.println(languageManager.getMessage("invalid_frequency_message"));
            initialBalance = 0;
        }

        // Creating the new member with an initial balance of 0
        Member newMember = new Member(firstName, lastName, address, phoneNumber, new Membership(membershipType), initialBalance);

        MemberDatabase memberDatabase = null;
        try {
            memberDatabase = MemberDatabase.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Add the member to the database and get the member ID
        int memberId = memberDatabase.addMember(firstName, lastName, phoneNumber, address, membershipType, isMonthly);

        if (memberId != -1) {
            newMember.setMemberId(String.valueOf(memberId)); // Set the generated member ID
            System.out.println(languageManager.getMessage("signup_success") + newMember.getMemberId());
        } else {
            System.out.println(languageManager.getMessage("signup_failure"));
        }
    }

    /**
     * Displays member profile
     * @param memberID the member id to check
     */
    public void displayMemberProfile(String memberID) {
        LanguageManager languageManager = LanguageManager.getInstance();
        Member memberProfile = findMemberByID(memberID); // Directly retrieve the Member without using viewProfile
        if (memberProfile != null) {
            System.out.println(languageManager.getMessage("profile_details"));
            System.out.println(languageManager.getMessage("member_id") + ": " + memberProfile.getMemberId());
            System.out.println(languageManager.getMessage("member_name") + ": " + memberProfile.getFname() + " " + memberProfile.getLname());
            System.out.println(languageManager.getMessage("street_number") + ": " + memberProfile.getAddress().getStreetNumber());
            System.out.println(languageManager.getMessage("street_name") + ": " + memberProfile.getAddress().getStreetName());
            System.out.println(languageManager.getMessage("city") + ": " + memberProfile.getAddress().getCity());
            System.out.println(languageManager.getMessage("province") + ": " + memberProfile.getAddress().getProvince());
            System.out.println(languageManager.getMessage("zip_code") + ": " + memberProfile.getAddress().getZipCode());
            System.out.println(languageManager.getMessage("membership_type") + ": " + memberProfile.getMembershipType().getType());
            System.out.println(languageManager.getMessage("balance") + ": $" + memberProfile.getBalance());
        } else {
            System.out.println(languageManager.getMessage("member_profile_not_found"));
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

        if (User.isPhoneNumberValid(phoneNumber)) {
            member.setPhoneNumber(phoneNumber);
            member.setAddress(address);
            System.out.println("Profile updated successfully.");
        } else {
            System.out.println("Failed to update profile. Check the phone number format.");
        }
    }

    /**
     * Manages membership of member
     * @param member to be managed
     */
    public boolean manageMembership(Member member) {
        Scanner sc = new Scanner(System.in); // Local scanner for input


        System.out.println("Current Membership Type: " + member.getMembershipType().getType());

        if (member.getMembershipType().getType().equals(MembershipType.PREMIUM)) {
            System.out.println("1. Downgrade to Regular");
        } else {
            System.out.println("1. Upgrade to Premium");
        }
        System.out.println("3. Cancel Membership");
        System.out.print("Choose an option: ");

        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline

        try {
            MemberDatabase memberDatabase = MemberDatabase.getInstance(); // Get instance of MemberDatabase

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
                    // Remove the member from the database
                    if (memberDatabase.removeMemberByID(Integer.parseInt(member.getMemberId()))) {
                        System.out.println("Membership canceled and member removed from the database.");
                        return true; // Return true to indicate the session should end
                    } else {
                        System.out.println("Failed to cancel membership or member not found.");
                    }
                }
                default -> {
                    System.out.println("Invalid choice.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error accessing the database: " + e.getMessage());
        }
        return false; // Return false to continue the session
    }


    /**
     * Checks balance of member
     * @param member the member to check balance of
     */
    public void checkPrices(Member member) {
        MembershipType membershipType = member.getMembershipType().getType(); // Get the type (REGULAR or PREMIUM)
        Scanner sc = new Scanner(System.in);

        // Ask user for payment frequency
        System.out.println("Select payment frequency to check prices:");
        System.out.println("1. Monthly");
        System.out.println("2. Yearly");

        int frequencyChoice = sc.nextInt();
        sc.nextLine(); // Consume newline

        double prices;
        if (frequencyChoice == 1) {
            prices = membershipType.getMonthlyPrice();
        } else if (frequencyChoice == 2) {
            prices = membershipType.getYearlyPrice();
        } else {
            System.out.println("Invalid payment frequency.");
            return;
        }

        System.out.println("Prices: $" + prices);
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
                System.out.print("Enter cash received: $");
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
     * Find member by id in database
     * @param memberID member to find
     * @return member
     */
    public Member findMemberByID(String memberID) {
        String memberQuery = "SELECT * FROM Members WHERE id = ?";
        String addressQuery = "SELECT * FROM Addresses WHERE id = ?"; // Adjust if different

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement memberStmt = conn.prepareStatement(memberQuery)) {

            memberStmt.setString(1, memberID);
            ResultSet memberRs = memberStmt.executeQuery();

            if (memberRs.next()) {
                String firstName = memberRs.getString("first_name");
                String lastName = memberRs.getString("last_name");
                String phoneNumber = memberRs.getString("phone_number");
                String membershipType = memberRs.getString("membership_type");
                double balance = memberRs.getDouble("balance");

                // Fetch address ID and query the Addresses table
                int addressId = memberRs.getInt("address_id");
                try (PreparedStatement addressStmt = conn.prepareStatement(addressQuery)) {
                    addressStmt.setInt(1, addressId);
                    ResultSet addressRs = addressStmt.executeQuery();

                    Address address = null;
                    if (addressRs.next()) {
                        int streetNumber = addressRs.getInt("street_number");
                        String streetName = addressRs.getString("street_name");
                        String city = addressRs.getString("city");
                        String province = addressRs.getString("province");
                        String zipCode = addressRs.getString("zip_code");

                        // Construct the Address object
                        address = new Address(streetNumber, streetName, city, province, zipCode);
                    }
                    // If address is null here, handle it accordingly (e.g., log or set a default)

                    // Create the MembershipType and Member object
                    MembershipType type = MembershipType.valueOf(membershipType.toUpperCase());
                    return new Member(firstName, lastName, address, phoneNumber, new Membership(type), balance);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding member by ID: " + e.getMessage());
        }
        System.out.println("Member not found.");
        return null;
    }

}
