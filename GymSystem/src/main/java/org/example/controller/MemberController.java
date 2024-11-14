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

import static org.example.model.Member.*;

public class MemberController {
    private final List<Member> members;
    private final NotificationService notificationService;

    public MemberController() {
        this.members = new ArrayList<>();
        this.notificationService = new NotificationService();
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
     *
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
     *
     * @param member the member to be updated
     */
    public void updateMemberProfile(Member member) {
        Scanner sc = new Scanner(System.in); // Local scanner instance for this method
        LanguageManager languageManager = LanguageManager.getInstance();

        String firstName = Validator.validateAlphabetsOnly(languageManager.getMessage("enter_first_name"));
        String lastName = Validator.validateAlphabetsOnly(languageManager.getMessage("enter_last_name"));
        String phoneNumber = Validator.validatePhoneNumber(languageManager.getMessage("enter_phone_number"));
        int streetNumber = Validator.validateStreetNumber(languageManager.getMessage("enter_street_number"));
        String streetName = Validator.validateAlphabetsOnly(languageManager.getMessage("enter_street_name"));
        String city = Validator.validateAlphabetsOnly(languageManager.getMessage("enter_city"));
        String province = Validator.validateAlphabetsOnly(languageManager.getMessage("enter_province"));
        String zipCode = Validator.validateZipCode(languageManager.getMessage("enter_zip_code"));

        // Creating a new Address object with all four arguments
        Address address = new Address(streetNumber, streetName, city, province, zipCode);

        if (User.isPhoneNumberValid(phoneNumber)) {
            member.setPhoneNumber(phoneNumber);
            member.setAddress(address);
            System.out.println(languageManager.getMessage("profile_updated_successfully"));

            Notification paymentNotification = NotificationFactory.createNotification("email", "Profile updated successfully.");
            addNotification(paymentNotification);
            notificationService.sendNotification(paymentNotification);
        } else {
            System.out.println(languageManager.getMessage("profile_update_failed"));
        }
    }

    /**
     * Manages membership of member
     *
     * @param member to be managed
     */
    public boolean manageMembership(Member member) {
        Scanner sc = new Scanner(System.in); // Local scanner for input
        LanguageManager languageManager = LanguageManager.getInstance();

        System.out.println(languageManager.getMessage("current_membership_type") + ": " + member.getMembershipType().getType());

        if (member.getMembershipType().getType().equals(MembershipType.PREMIUM)) {
            System.out.println("1. " + languageManager.getMessage("downgrade_to_regular"));
        } else {
            System.out.println("1. " + languageManager.getMessage("upgrade_to_premium"));
        }
        System.out.println("2. " + languageManager.getMessage("cancel_membership"));
        System.out.print(languageManager.getMessage("choose_option"));

        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline

        try {
            MemberDatabase memberDatabase = MemberDatabase.getInstance(); // Get instance of MemberDatabase

            switch (choice) {
                case 1 -> {
                    if (member.getMembershipType().getType().equals(MembershipType.PREMIUM)) {
                        member.getMembershipType().setType(MembershipType.REGULAR); // Downgrade to Regular
                        System.out.println(languageManager.getMessage("membership_downgraded"));

                        Notification paymentNotification = NotificationFactory.createNotification("email", "Membership downgraded to Regular.");
                        addNotification(paymentNotification);
                        notificationService.sendNotification(paymentNotification);
                    } else {
                        member.getMembershipType().setType(MembershipType.PREMIUM); // Upgrade to Premium
                        System.out.println(languageManager.getMessage("membership_upgraded"));

                        Notification paymentNotification = NotificationFactory.createNotification("email", "Membership upgraded to Premium.");
                        addNotification(paymentNotification);
                        notificationService.sendNotification(paymentNotification);
                    }
                }
                case 2 -> {
                    // Remove the member from the database
                    if (memberDatabase.removeMemberByID(Integer.parseInt(member.getMemberId()))) {
                        System.out.println(languageManager.getMessage("membership_canceled"));
                        return true; // Return true to indicate the session should end
                    } else {
                        System.out.println(languageManager.getMessage("cancel_membership_failed"));
                    }
                }
                default -> {
                    System.out.println(languageManager.getMessage("invalid_choice"));
                }
            }
        } catch (SQLException e) {
            System.out.println(languageManager.getMessage("database_error") + ": " + e.getMessage());
        }
        return false; // Return false to continue the session
    }


    /**
     * Checks balance of member
     *
     * @param member the member to check balance of
     */
    public void checkPrices(Member member) {
        MembershipType membershipType = member.getMembershipType().getType(); // Get the type (REGULAR or PREMIUM)
        Scanner sc = new Scanner(System.in);
        LanguageManager languageManager = LanguageManager.getInstance();

        // Ask user for payment frequency
        System.out.println(languageManager.getMessage("select_payment_frequency_check"));
        System.out.println("1. " + languageManager.getMessage("payment_frequency_monthly"));
        System.out.println("2. " + languageManager.getMessage("payment_frequency_yearly"));

        int frequencyChoice = sc.nextInt();
        sc.nextLine(); // Consume newline

        double prices;
        if (frequencyChoice == 1) {
            prices = membershipType.getMonthlyPrice();
        } else if (frequencyChoice == 2) {
            prices = membershipType.getYearlyPrice();
        } else {
            System.out.println(languageManager.getMessage("invalid_payment_frequency"));
            return;
        }

        System.out.println(languageManager.getMessage("prices") + ": $" + prices);
    }

    /**
     * Makes payment based on their membership type
     *
     * @param memberID      of the member
     * @param frequencyType how often they pay
     * @return if payment was successful
     */
    public boolean makePayment(String memberID, String frequencyType) {
        LanguageManager languageManager = LanguageManager.getInstance();
        Member member = findMemberByID(memberID);

        if (member != null) {
            MembershipType membershipType = member.getMembershipType().getType();
            double requiredAmount = 0;

            // Determine the required payment amount based on membership type and frequency
            if ("monthlyPrice".equalsIgnoreCase(frequencyType)) {
                requiredAmount = membershipType.getMonthlyPrice();
            } else if ("yearlyPrice".equalsIgnoreCase(frequencyType)) {
                requiredAmount = membershipType.getYearlyPrice();
            } else {
                System.out.println(languageManager.getMessage("invalid_payment_frequency") + ": " + frequencyType);
                return false;
            }

            // Call the helper method to select and process payment method
            Payment payment = selectPaymentMethod(requiredAmount);
            if (payment == null) {
                System.out.println(languageManager.getMessage("payment_process_failed"));
                return false;
            }

            // Deduct balance if payment was successful
            member.setBalance(member.getBalance() - requiredAmount);
            System.out.println("Payment successful. " + frequencyType + " payment of $" + requiredAmount + " made.");

            Notification paymentNotification = NotificationFactory.createNotification("sms", "Payment of $"
                    + requiredAmount + " was successful.");
            addNotification(paymentNotification);
            notificationService.sendNotification(paymentNotification);

            // Add this line to prevent further code execution in case of success
            return true;
        }

        System.out.println(languageManager.getMessage("member_not_found"));
        return false;
    }

    /**
     * Helper method to select payment method
     *
     * @param requiredAmount amount to pay off
     * @return type of payment
     */
    private Payment selectPaymentMethod(double requiredAmount) {
        Scanner sc = new Scanner(System.in);
        LanguageManager languageManager = LanguageManager.getInstance();

        System.out.println(languageManager.getMessage("choose_payment_method"));
        System.out.println("1. " + languageManager.getMessage("cash"));
        System.out.println("2. " + languageManager.getMessage("credit_card"));
        System.out.println("3. " + languageManager.getMessage("debit_card"));
        System.out.print(languageManager.getMessage("enter_choice"));
        int paymentChoice = sc.nextInt();
        sc.nextLine(); // Consume newline

        Payment payment = null;

        switch (paymentChoice) {
            case 1 -> { // Cash payment
                System.out.print(languageManager.getMessage("enter_cash_received"));
                double cashReceived = sc.nextDouble();
                sc.nextLine(); // Consume newline

                payment = new CashPayment(cashReceived);
            }
            case 2 -> { // Credit Card payment
                String cardNumber = Validator.validateCardNumber(languageManager.getMessage("enter_card_number"));
                String cardHolderName = Validator.validateCardHolderName(languageManager.getMessage("enter_card_holder_name"));
                String expiryDate = Validator.validateExpiryDate(languageManager.getMessage("enter_expiration_date"));
                int cvv = Validator.validateCVV(languageManager.getMessage("enter_cvv"));

                payment = new CreditCardPayment(cardNumber, cardHolderName, expiryDate, cvv);
            }
            case 3 -> { // Debit Card payment
                String cardNumber = Validator.validateCardNumber(languageManager.getMessage("enter_card_number"));
                String cardHolderName = Validator.validateCardHolderName(languageManager.getMessage("enter_card_holder_name"));
                String bankName = Validator.validateBankName(languageManager.getMessage("enter_bank_name"));
                int pin = Validator.validatePinNumber(languageManager.getMessage("enter_pin"));

                payment = new DebitCardPayment(cardNumber, cardHolderName, bankName, pin);
            }
            default -> {
                System.out.println(languageManager.getMessage("invalid_payment_method"));
                return null;
            }
        }
        return payment;
    }

    /**
     * Views notification of member form gym system
     *
     * @param member member to view notification
     */
    public void viewNotifications(Member member) {
        System.out.println("Notifications for Member ID: " + member.getMemberId());
        List<Notification> notifications = getNotifications();
        LanguageManager languageManager = LanguageManager.getInstance();

        if (notifications.isEmpty()) {
            System.out.println(languageManager.getMessage("no_new_notifications"));
        } else {
            System.out.println(languageManager.getMessage("notifications"));
            for (Notification notification : getNotifications()) {
                notificationService.sendNotification(notification);
            }
        }
    }

    /**
     * Find member by id in database
     *
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
