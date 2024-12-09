package org.example.controller;

import lombok.Getter;
import org.example.database.DatabaseConnection;
import org.example.model.Address;
import org.example.model.Employee;
import org.example.model.Member;
import org.example.model.MembershipType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Getter
public class EmployeeController {
    private static List<Member> members;
    private List<Employee> employees;

    public EmployeeController() {
        super();
        members = new ArrayList<>();
        employees = new ArrayList<>();
    }

    /**
     * Finds the employee by their id
     *
     * @param employeeID to search
     * @return employee with that id
     */
    public Employee findEmployeeByID(String employeeID) {
        String employeeQuery = "SELECT * FROM Employees WHERE id = ?";
        String addressQuery = "SELECT * FROM Addresses WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement employeeStmt = conn.prepareStatement(employeeQuery)) {

            employeeStmt.setString(1, employeeID);
            ResultSet employeeRs = employeeStmt.executeQuery();

            if (employeeRs.next()) {
                String firstName = employeeRs.getString("first_name");
                String lastName = employeeRs.getString("last_name");
                String phoneNumber = employeeRs.getString("phone_number");

                // Fetch address ID and query the Addresses table
                try (PreparedStatement addressStmt = conn.prepareStatement(addressQuery)) {
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
                    return new Employee(firstName, lastName, address, phoneNumber, employeeID);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding employee by ID: " + e.getMessage());
        }
        System.out.println("Employee not found.");
        return null;
    }

    /**
     * Adds member to the system
     */
    public void promptAddMember() {
        MemberController memberController = new MemberController();
        memberController.signUpMember();
    }

    /**
     * Helper method to add member
     *
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
     * Deletes a member from the system
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
     * Helper method to remove member
     *
     * @param memberID to find to remove
     * @return removed member
     */
    private static boolean removeMember(String memberID) {
        String sql = "DELETE FROM Members WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(memberID));
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Member deleted successfully.");
                return true;
            } else {
                System.out.println("Member not found.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error deleting member: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update member
     *
     */
    public void promptUpdateMember() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Member ID: ");
        String id = scanner.nextLine();
        for (Member member : members) {
            if (member.getMemberId().equals(id)) {
                updateMember(id, member);
            }
        }
    }

    /**
     * Helps main method update member
     *
     * @param memberID      to find
     * @param updatedMember to update
     */
    public void updateMember(String memberID, Member updatedMember) {
        MemberController memberController = new MemberController();
        if (memberID.equals(updatedMember.getMemberId())) {
            memberController.updateMemberProfile(updatedMember);
        }
    }

    /**
     * Gets the membership type from the input of the user
     *
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
