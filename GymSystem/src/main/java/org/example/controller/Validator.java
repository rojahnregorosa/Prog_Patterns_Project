package org.example.controller;

import java.util.Scanner;

public class Validator {
    private static final Scanner sc = new Scanner(System.in);

    public static String validateName(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine();
            if (input.matches("[a-zA-Z]+")) return input;
            System.out.println("Invalid input. Name must only contain letters.");
        }
    }

    public static String validatePhoneNumber() {
        while (true) {
            System.out.print("Enter phone number: ");
            String phoneNumber = sc.nextLine();
            if (phoneNumber.matches("\\d{10}")) return phoneNumber;
            System.out.println("Invalid input. Phone number must be exactly 10 digits.");
        }
    }

    public static int validateStreetNumber() {
        while (true) {
            System.out.print("Enter street number: ");
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

    public static String validateAlphabetsOnly(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine();
            if (input.matches("[a-zA-Z ]+")) return input;
            System.out.println("Invalid input. Field must only contain letters.");
        }
    }

    public static String validateZipCode() {
        while (true) {
            System.out.print("Enter zip code: ");
            String zipCode = sc.nextLine();
            if (zipCode.matches("[A-Za-z0-9]+")) return zipCode;
            System.out.println("Invalid input. Zip code must contain alphanumeric characters only.");
        }
    }
}
