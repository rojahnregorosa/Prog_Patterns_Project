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
            System.out.println("3. Sign up as a new Member");
            System.out.println("4. Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> handleMemberLogin();
                case 2 -> handleEmployeeLogin();
                case 3 -> memberController.signupMember();
                case 4 -> {
                    running = false;
                    System.out.println("Exiting the system. Goodbye!");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * handles the log in for the members
     */
    private void handleMemberLogin() {
        System.out.print("Enter Member ID: ");
        String memberID = sc.nextLine();
        Member member = memberController.findMemberByID(memberID);

        if (member != null) {
            System.out.println("Login successful! Welcome, " + member.getFname());
            boolean memberSession = true;

            while (memberSession) {
                System.out.println("\nMember Options");
                System.out.println("1. View Profile");
                System.out.println("2. Update Profile");
                System.out.println("3. Manage Membership");
                System.out.println("4. Check Balance");
                System.out.println("5. Make Payment");
                System.out.println("6. View Notifications");
                System.out.println("7. Log out.");

                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> memberController.displayMemberProfile(member.getMemberId()); // Calls displayMemberProfile
                    case 2 -> memberController.updateMemberProfile(member);
                    case 3 -> memberController.manageMembership(member);
                    case 4 -> memberController.checkBalance(member);
                    case 5 -> initiatePayment(member);
                    case 6 -> memberController.viewNotifications(member);
                    case 7 -> {
                        memberSession = false;
                        System.out.println("Logging out...");
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        } else {
            System.out.println("Member not found. Please check your ID and try again.");
        }
    }

    /**
     * handles the login for the employees
     */
    private void initiatePayment(Member member) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter payment frequency (monthly/yearly): ");
        String frequencyType = sc.nextLine();
        // Call the makePayment method in MemberController to process the payment
        if (!memberController.makePayment(member.getMemberId(), frequencyType)) {
            System.out.println("Payment process failed.");
        }
    }

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
