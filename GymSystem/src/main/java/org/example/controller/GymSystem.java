package org.example.controller;

import org.example.model.Employee;
import org.example.model.Member;
import org.example.utils.LanguageManager;

import java.util.Scanner;

public class GymSystem {
    private final MemberController memberController;
    private final EmployeeController employeeController;
    private final Scanner sc;

    public GymSystem() {
        this.memberController = new MemberController();
        this.employeeController = new EmployeeController();
        this.sc = new Scanner(System.in);
    }

    /**
     * Runs the program
     */
    public void start() {
        chooseLanguage();
        boolean running = true;

        while (running) {
            // Print the options to the user
            System.out.println(LanguageManager.getInstance().getMessage("select_option"));
            System.out.println("1. " + LanguageManager.getInstance().getMessage("login_member"));
            System.out.println("2. " + LanguageManager.getInstance().getMessage("login_employee"));
            System.out.println("3. " + LanguageManager.getInstance().getMessage("sign_up"));
            System.out.println("4. " + LanguageManager.getInstance().getMessage("exit"));

            int choice = -1;

            // Loop until a valid integer choice is entered
            while (true) {
                if (sc.hasNextInt()) {  // Check if input is an integer
                    choice = sc.nextInt();
                    sc.nextLine();  // Consume the newline character
                    break;  // Exit the loop if a valid integer is entered
                } else {
                    System.out.println(LanguageManager.getInstance().getMessage("invalid_choice"));
                    sc.nextLine();  // Consume invalid input

                    // Reprint the options after invalid input
                    System.out.println(LanguageManager.getInstance().getMessage("select_option"));
                    System.out.println("1. " + LanguageManager.getInstance().getMessage("login_member"));
                    System.out.println("2. " + LanguageManager.getInstance().getMessage("login_employee"));
                    System.out.println("3. " + LanguageManager.getInstance().getMessage("sign_up"));
                    System.out.println("4. " + LanguageManager.getInstance().getMessage("exit"));
                }
            }

            // Now that we have a valid choice, process it
            switch (choice) {
                case 1 -> handleMemberLogin();
                case 2 -> handleEmployeeLogin();
                case 3 -> memberController.signUpMember();
                case 4 -> {
                    running = false;
                    System.out.println(LanguageManager.getInstance().getMessage("exit_message"));
                }
                default -> System.out.println(LanguageManager.getInstance().getMessage("invalid_choice"));
            }
        }
    }

    /**
     * Prompts the user to choose a language
     */
    private void chooseLanguage() {
        int languageChoice = 0; // Initialize variable for language choice

        while (languageChoice != 1 && languageChoice != 2) {
            System.out.println("Choose language:");
            System.out.println("1. English");
            System.out.println("2. Français");

            if (sc.hasNextInt()) {  // Check if input is an integer
                languageChoice = sc.nextInt();
                sc.nextLine(); // Consume newline

                if (languageChoice == 1) {
                    LanguageManager.getInstance().setLanguage("en");
                } else if (languageChoice == 2) {
                    LanguageManager.getInstance().setLanguage("fr");
                } else {
                    System.out.println("Invalid choice. Please select 1 or 2.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); // Consume invalid input
            }
        }
    }

    /**
     * Handles the log in for the members
     */
    private void handleMemberLogin() {
        System.out.println(LanguageManager.getInstance().getMessage("enter_member_id"));
        String memberID = sc.nextLine();
        Member member = memberController.findMemberByID(memberID);

        if (member != null) {
            System.out.println(LanguageManager.getInstance().getMessage("login_success") + " " + member.getFname() + " " + member.getLname());
            boolean memberSession = true;

            while (memberSession) {
                System.out.println("\n" + LanguageManager.getInstance().getMessage("member_options"));
                System.out.println("1. " + LanguageManager.getInstance().getMessage("view_profile"));
                System.out.println("2. " + LanguageManager.getInstance().getMessage("update_profile"));
                System.out.println("3. " + LanguageManager.getInstance().getMessage("manage_membership"));
                System.out.println("4. " + LanguageManager.getInstance().getMessage("check_prices"));
                System.out.println("5. " + LanguageManager.getInstance().getMessage("make_payment"));
                System.out.println("6. " + LanguageManager.getInstance().getMessage("view_notifications"));
                System.out.println("7. " + LanguageManager.getInstance().getMessage("log_out"));

                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> memberController.displayMemberProfile(member.getMemberId());
                    case 2 -> memberController.updateMemberProfile(member);
                    case 3 -> {
                        if (memberController.manageMembership(member)) {
                            memberSession = false; // End the session if membership is canceled
                        }
                    }
                    case 4 -> memberController.checkPrices(member);
                    case 5 -> initiatePayment(member);
                    case 6 -> memberController.viewNotifications(member);
                    case 7 -> {
                        memberSession = false;
                        System.out.println(LanguageManager.getInstance().getMessage("logging_out"));
                    }
                    default -> System.out.println(LanguageManager.getInstance().getMessage("invalid_choice"));
                }
            }
        } else {
            System.out.println(LanguageManager.getInstance().getMessage("member_not_found"));
        }
    }

    /**
     * Make member pay
     */
    private void initiatePayment(Member member) {
        Scanner sc = new Scanner(System.in);

        // Ask user for payment frequency
        System.out.println(LanguageManager.getInstance().getMessage("enter_payment_frequency"));
        System.out.println("1. " + LanguageManager.getInstance().getMessage("payment_frequency_monthly"));
        System.out.println("2. " + LanguageManager.getInstance().getMessage("payment_frequency_yearly"));
        int frequencyChoice = sc.nextInt();

        String frequencyType;
        switch (frequencyChoice) {
            case 1 -> frequencyType = "monthlyPrice";
            case 2 -> frequencyType = "yearlyPrice";
            default -> {
                System.out.println(LanguageManager.getInstance().getMessage("invalid_payment_frequency"));
                return;
            }
        }

        // Call the makePayment method with the correct frequency type
        if (!memberController.makePayment(member.getMemberId(), frequencyType)) {
            System.out.println(LanguageManager.getInstance().getMessage("payment_failed"));
        }
    }

    /**
     * Handles employee login
     */
    private void handleEmployeeLogin() {
        System.out.print(LanguageManager.getInstance().getMessage("enter_employee_id"));
        String employeeID = sc.nextLine();
        Employee employee = employeeController.findEmployeeByID(employeeID);

        if (employee != null) {
            System.out.println(LanguageManager.getInstance().getMessage("login_success") + " " + employee.getFname() + " " + employee.getLname());
            boolean employeeSession = true;

            while (employeeSession) {
                System.out.println("\n" + LanguageManager.getInstance().getMessage("employee_options"));
                System.out.println("1. " + LanguageManager.getInstance().getMessage("add_member"));
                System.out.println("2. " + LanguageManager.getInstance().getMessage("update_member"));
                System.out.println("3. " + LanguageManager.getInstance().getMessage("remove_member"));
                System.out.println("4. " + LanguageManager.getInstance().getMessage("log_out"));

                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> employeeController.promptAddMember();
                    case 2 -> employeeController.promptUpdateMember();
                    case 3 -> employeeController.promptRemoveMember();
                    case 4 -> {
                        employeeSession = false;
                        System.out.println(LanguageManager.getInstance().getMessage("logging_out"));
                    }
                    default -> System.out.println(LanguageManager.getInstance().getMessage("invalid_choice"));
                }
            }
        } else {
            System.out.println(LanguageManager.getInstance().getMessage("employee_not_found"));
        }
    }
}
