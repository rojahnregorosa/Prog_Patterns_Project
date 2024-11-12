package org.example.controller;

import org.example.model.Member;

import java.util.Scanner;

public class GymSystem {
    private final UserController userController;
    private final MemberController memberController;
    private final EmployeeController employeeController;
    private final Scanner sc;

    public GymSystem() {
        this.userController = new UserController();
        this.memberController = new MemberController();
        this.employeeController = new EmployeeController();
        this.sc = new Scanner(System.in);
    }

    // Example flow:
    // Display options for login as Member or Employee
    // Allow members to view/update their profile, make payments, etc.
    // Allow employees to manage members, process payments, etc.
    // This can be done through command-line input or a graphical interface
    public void start() {
        System.out.println("Welcome to the Gym Management System!");
        boolean running = true;

        while (running) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Login as Member");
            System.out.println("2. Login as Employee");
            System.out.println("3. Exit");

            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    handleMemberLogin();
                    break;
                case 2:
                    handleEmployeeLogin();
                    break;
                case 3:
                    running = false;
                    System.out.println("Exiting the system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleMemberLogin() {

    }

    private void initiatePayment(Member member) {

    }

//    private Payment createPayment(int paymentMethodChoice) {
//
//    }

    private void handleEmployeeLogin() {

    }

//    private MembershipType getMembershipTypeFromInput() {
//
//    }


}
