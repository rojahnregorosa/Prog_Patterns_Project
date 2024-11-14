package org.example.controller;

import java.util.Scanner;

public class Validator {
    private static final Scanner sc = new Scanner(System.in);

    /**
     * Validates if the phone number is typed correctly
     * @return if the phone number is typed correctly
     */
    public static String validatePhoneNumber(String prompt) {
        while (true) {
            System.out.print(prompt);
            String phoneNumber = sc.nextLine();
            if (phoneNumber.trim().matches("\\d{10}")) return phoneNumber;
            else System.out.println("Invalid input. Phone number must be exactly 10 digits.");
        }
    }

    /**
     * Validates if the street number only contains numbers
     * @return if the street number only contains numbers
     */
    public static int validateStreetNumber(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                int streetNumber = sc.nextInt();
                sc.nextLine(); // Consume newline
                if (Integer.toString(streetNumber).length() <= 4) return streetNumber;
                else System.out.println("Invalid input. Street number must be up to 4 digits.");
            } else {
                System.out.println("Invalid input. Street number must be numeric.");
                sc.next(); // Clear invalid input
            }
        }
    }

    /**
     * Validates if the string only contains letters
     * @param prompt to tell you what to enter
     * @return if the input only contains letters or not
     */
    public static String validateAlphabetsOnly(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine();
            if (input.matches("[a-zA-Z ]+")) return input.substring(0,1).toUpperCase() + input.substring(1).toLowerCase();
            else System.out.println("Invalid input. Must only contain letters.");
        }
    }

    /**
     * Validates the zip code
     * @return if the zip code is valid or not
     */
    public static String validateZipCode(String prompt) {
        while (true) {
            System.out.print(prompt);
            String zipCode = sc.nextLine();
            if (zipCode.trim().matches("[A-Z]\\d[A-Z]\\d[A-Z]\\d$")) return zipCode;
            else System.out.println("Invalid input. Zip code must contain alphanumeric uppercase characters only.");
        }
    }

    /**
     * Validates the information of card number
     * @return if the card number is valid or not
     */
    public static String validateCardNumber(String prompt) {
        while (true) {
            // Validate card number (should be 16 digits)
            System.out.println(prompt);
            String cardNumber = sc.nextLine();
            if (cardNumber == null || cardNumber.trim().matches("^\\d{16}$")) return cardNumber;
            else System.out.println("Invalid card number. Must be 16 digits.");
        }
    }

    /**
     * Validates the information of cardholder number
     * @return if the cardholder number is valid or not
     */
    public static String validateCardHolderName(String prompt) {
        while (true) {
            System.out.println(prompt);
            String cardHolderName = sc.nextLine();
            if (cardHolderName == null || cardHolderName.contains(" ")) return cardHolderName;
            else System.out.println("Invalid cardholder name. Must include both first and last name with a space in between.");
        }
    }

    /**
     * Validates the information of bank name
     * @return if the bank name is valid or not
     */
    public static String validateBankName(String prompt) {
        while (true) {
            // Validate card number (should be 16 digits)
            System.out.println(prompt);
            String bankName = sc.nextLine();
            if (bankName != null || bankName.trim().matches("[a-zA-Z]+")) return bankName;
            else System.out.println("Invalid bank name. Must only contain letters.");
        }
    }

    /**
     * Validates the information of pin
     * @return if the pin is valid or not
     */
    public static int validatePinNumber(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                int pin = sc.nextInt();
                sc.nextLine(); // Consume newline
                if (Integer.toString(pin).length() == 4 || String.valueOf(pin).trim().matches("^\\d{4}$")) return pin;
                else System.out.println("Invalid pin. Must be 4 digits.");
            } else {
                System.out.println("Invalid input. Pin must be numeric.");
                System.out.println("Card payment failed.");
                sc.next(); // Clear invalid input
            }
        }
    }

    /**
     * Validates the information of the expiry date of the credit card
     * @return if the expiry date is valid or not
     */
    public static String validateExpiryDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String expiryDate = sc.nextLine();

            // Check if expiry date is in MM/YY format
            if (expiryDate.matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
                return expiryDate;
            } else {
                System.out.println("Invalid expiry date. Must be in MM/YY format (e.g., 01/23).");
            }
        }
    }

    /**
     * Validates the information of cvv
     * @return if the cvv is valid or not
     */
    public static int validateCVV(String prompt) {
        while (true) {
            // Validate cvv (should be 3 digits)
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                int cvv = sc.nextInt();
                sc.nextLine(); // Consume newline
                if (String.valueOf(cvv).length() == 3 || String.valueOf(cvv).trim().matches("^\\d{3}$")) return cvv;
                else System.out.println("Invalid cvv. Must be 3 digits.");
            } else {
                System.out.println("Invalid input. CVV must be numeric.");
                System.out.println("Card payment failed.");
                sc.next(); // Clear invalid input
            }
        }
    }
}
