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
            System.out.println(LanguageManager.getInstance().getMessage("select_option"));
            System.out.println("1. " + LanguageManager.getInstance().getMessage("login_member"));
            System.out.println("2. " + LanguageManager.getInstance().getMessage("login_employee"));
            System.out.println("3. " + LanguageManager.getInstance().getMessage("sign_up"));
            System.out.println("4. " + LanguageManager.getInstance().getMessage("exit"));

            int choice = sc.nextInt();
            sc.nextLine();

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
        System.out.println("Choose language:");
        System.out.println("1. English");
        System.out.println("2. Français");

        int languageChoice = sc.nextInt();
        sc.nextLine(); // Consume newline

        if (languageChoice == 1) {
            LanguageManager.getInstance().setLanguage("en");
        } else if (languageChoice == 2) {
            LanguageManager.getInstance().setLanguage("fr");
        } else {
            System.out.println("Invalid choice. Defaulting to English.");
            LanguageManager.getInstance().setLanguage("en");
        }
    }

    /**
     * handles the log in for the members
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
     * handles the login for the employees
     */
    private void initiatePayment(Member member) {
        Scanner sc = new Scanner(System.in);

        System.out.print(LanguageManager.getInstance().getMessage("enter_payment_frequency"));
        String frequencyType = sc.nextLine();
        // Call the makePayment method in MemberController to process the payment
        if (!memberController.makePayment(member.getMemberId(), frequencyType)) {
            System.out.println(LanguageManager.getInstance().getMessage("payment_failed"));
        }
    }

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
