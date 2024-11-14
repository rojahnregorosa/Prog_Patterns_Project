package org.example.controller;

import org.example.database.DatabaseConnection;
import org.example.database.MemberDatabase;
import org.example.model.*;

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
        Membership membership = new Membership(membershipType);

        // Ask for payment frequency
        System.out.println("Select payment frequency:");
        System.out.println("1. Monthly");
        System.out.println("2. Yearly");

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
            System.out.println("Invalid payment frequency. Defaulting to $0 balance.");
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
            System.out.println("Signup successful! You can now log in using your Member ID : " + newMember.getMemberId());
        } else {
            System.out.println("Signup failed. Unable to add member to the database.");
        }
    }

    /**
     * Displays member profile
     *
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
     *
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

        System.out.println("Profile updated successfully.");

        Notification paymentNotification = NotificationFactory.createNotification("email", "Profile updated successfully.");
        addNotification(paymentNotification);
        notificationService.sendNotification(paymentNotification);
    }

    /**
     * Manages membership of member
     *
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
        System.out.println("2. Cancel Membership");
        System.out.print("Choose an option: ");

        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline

        try {
            MemberDatabase memberDatabase = MemberDatabase.getInstance(); // Get instance of MemberDatabase

            switch (choice) {
                case 1 -> {
                    if (member.getMembershipType().getType().equals(MembershipType.PREMIUM)) {
                        member.getMembershipType().setType(MembershipType.REGULAR);
                        System.out.println("Membership downgraded to Regular.");

                        Notification paymentNotification = NotificationFactory.createNotification("email", "Membership downgraded to Regular.");
                        addNotification(paymentNotification);
                        notificationService.sendNotification(paymentNotification);
                    } else {
                        member.getMembershipType().setType(MembershipType.PREMIUM);
                        System.out.println("Membership upgraded to Premium.");

                        Notification paymentNotification = NotificationFactory.createNotification("email", "Membership upgraded to Premium.");
                        addNotification(paymentNotification);
                        notificationService.sendNotification(paymentNotification);
                    }
                }
                case 2 -> {
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
     *
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
     *
     * @param memberID      of the member
     * @param frequencyType how often they pay
     * @return if payment was successful
     */
    public boolean makePayment(String memberID, String frequencyType) {
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

            Notification paymentNotification = NotificationFactory.createNotification("sms", "Payment of $"
                    + requiredAmount + " was successful.");
            addNotification(paymentNotification);
            notificationService.sendNotification(paymentNotification);

            // Add this line to prevent further code execution in case of success
            return true;
        }

        System.out.println("Member not found.");
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
     *
     * @param member member to view notification
     */
    public void viewNotifications(Member member) {
        System.out.println("Notifications for Member ID: " + member.getMemberId());

        for (Notification notification : getNotifications()) {
            notificationService.sendNotification(notification);  // Each notification carries its own message
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
