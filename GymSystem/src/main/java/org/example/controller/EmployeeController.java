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
    private MemberController memberController = new MemberController();

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
        // First, delete the associated address from the Addresses table
        String addressSql = "DELETE FROM Addresses WHERE id = (SELECT address_id FROM Members WHERE id = ?)";

        try (Connection conn = DatabaseConnection.connect()) {
            // Remove address first
            try (PreparedStatement pstmtAddress = conn.prepareStatement(addressSql)) {
                pstmtAddress.setInt(1, Integer.parseInt(memberID));
                pstmtAddress.executeUpdate();
            }

            // Now remove the member from the Members table
            String memberSql = "DELETE FROM Members WHERE id = ?";
            try (PreparedStatement pstmtMember = conn.prepareStatement(memberSql)) {
                pstmtMember.setInt(1, Integer.parseInt(memberID));
                int affectedRows = pstmtMember.executeUpdate();

                if (affectedRows > 0) {
                    return true; // Member and address removed successfully
                } else {
                    System.out.println("Member not found.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error deleting member and associated address: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates a member
     *
     */
    public void promptUpdateMember() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Member ID: ");
        String id = scanner.nextLine();

        // Call the findMemberByID method from MemberController to fetch the member by ID
        Member memberToUpdate = memberController.findMemberByID(id);

        // If the member is found, proceed to update their profile
        if (memberToUpdate != null) {
            updateMember(memberToUpdate);  // Pass the member directly for updating
        } else {
            System.out.println("Member ID not found.");  // If member doesn't exist, notify the employee
        }
    }

    /**
     * Helper method to update member
     *
     * @param updatedMember
     */
    private void updateMember(Member updatedMember) {
        // Directly update the member's profile without creating a new instance of MemberController
        memberController.updateMemberProfile(updatedMember);
        System.out.println("Member profile updated successfully.");
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
