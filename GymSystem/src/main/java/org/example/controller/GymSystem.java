package org.example.controller;

import org.example.model.Employee;
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

    /**
     * handles the login for the employees
     */
    private void handleEmployeeLogin() {
        System.out.print("Enter Employee ID: ");
        String employeeID = sc.nextLine();
        Employee employee = employeeController.findEmployeeByID(employeeID);

        if (employee != null) {
            System.out.println("Login successful! Welcome, " + employee.getFname() + " " + employee.getLname());
            boolean employeeSession = true;

            while (employeeSession) {
                System.out.println("\nEmployee Options:");
                System.out.println("1. Add Member");
                System.out.println("2. Remove Member");
                System.out.println("3. Update Member");
                System.out.println("4. Logout");

                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> employeeController.promptAddMember();
                    case 2 -> employeeController.promptRemoveMember();
                    case 3 -> employeeController.promptUpdateMember();
                    case 4 -> {
                        employeeSession = false;
                        System.out.println("Logging out...");
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        } else {
            System.out.println("Employee not found. Please check your ID and try again.");
        }
    }
}
