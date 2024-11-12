package org.example.controller;

import lombok.Getter;
import org.example.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

@Getter
public class EmployeeController extends UserController {
    private static List<Member> members;
    private List<Employee> employees;

    public EmployeeController() {
        super();
        members = new ArrayList<>();
        employees = new ArrayList<>();
    }

    /**
     * finds the employee by their id
     * @param employeeID to search
     * @return employee with that id
     */
    public Employee findEmployeeByID(String employeeID) {
        for (Employee employee : employees) {
            if (Objects.equals(employee.getEmployeeId(), employeeID)) {
                return employee;
            }
        }
        return null;
    }

    /**
     * adds member to the system
     */
    public void promptAddMember() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter member first name: ");
        String fname = scanner.nextLine();
        System.out.print("Enter member first name: ");
        String lname = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        MembershipType type = getMembershipTypeFromInput();
        Address address = new Address("123 Main St", "City", "Province", "12345"); // Example address

        Member newMember = new Member(fname, lname, address, phoneNumber, new Membership(type), 0);
        boolean success = EmployeeController.addMember(newMember);

        if (success) {
            System.out.println("Member added successfully.");
        } else {
            System.out.println("Failed to add member.");
        }
    }

    /**
     * helps the main method to add member
     * @param member to add
     * @return added member
     */
    private static boolean addMember(Member member) {
        if (member != null && !members.contains(member)) {
            members.add(member);
            return true;
        }
        return false;
    }

    /**
     * deletes a member from the system
     */
    public void promptRemoveMember() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter member ID to remove: ");
        String memberID = scanner.nextLine();
        boolean success = EmployeeController.removeMember(memberID);

        if (success) {
            System.out.println("Member removed successfully.");
        } else {
            System.out.println("Failed to remove member.");
        }
    }

    /**
     * helps main method remove member
     * @param memberID to find to remove
     * @return removed member
     */
    private static boolean removeMember(String memberID) {
        return members.removeIf(member -> Objects.equals(member.getMemberId(), memberID));
    }
    public void promptUpdateMember() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter member ID to update: ");
        String memberID = scanner.nextLine();
        System.out.println("Feature under construction.");
    }

    /**
     * helps main method update member
     * @param memberID to find
     * @param updatedMember to update
     * @return updated member
     */
    private boolean updateMember(String memberID, Member updatedMember) {
        for (int i = 0; i < members.size(); i++) {
            if (Objects.equals(members.get(i).getMemberId(), memberID)) {
                members.set(i, updatedMember);
                return true;
            }
        }
        return false;
    }

    /**
     * gets the membership type from the input of the user
     * @return the membership type
     */
    public MembershipType getMembershipTypeFromInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select membership type:");
        System.out.println("1. Regular");
        System.out.println("2. Premium");

        int typeChoice = scanner.nextInt();
        scanner.nextLine();
        return (typeChoice == 1) ? MembershipType.REGULAR : MembershipType.PREMIUM;
    }
}
