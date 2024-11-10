package org.example.controller;

public class GymSystem {
    private UserController userController;
    private MemberController memberController;
    private EmployeeController employeeController;

    public GymSystem() {
        // Initialize controllers and any other necessary data
        userController = new UserController();
        memberController = new MemberController();
        employeeController = new EmployeeController();
    }

    public void start() {
        // Main system loop or a simple menu system for the user to interact with
        System.out.println("Welcome to the Gym Management System!");

        // Example flow:
        // Display options for login as Member or Employee
        // Allow members to view/update their profile, make payments, etc.
        // Allow employees to manage members, process payments, etc.
        // This can be done through command-line input or a graphical interface
    }
}
