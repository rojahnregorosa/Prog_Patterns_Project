package org.example.controller;

import lombok.Getter;
import org.example.database.DatabaseConnection;
import org.example.model.Employee;
import org.example.model.Member;
import org.example.model.MembershipType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        MemberController memberController = new MemberController();
        memberController.signUpMember();
    }

    /**
     * helper method to add member
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
     * helper method to remove member
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
     * helps main method update member
     * @param memberID to find
     * @param updatedMember to update
     */
    public void updateMember(String memberID, Member updatedMember) {
        MemberController memberController = new MemberController();
        if (memberID.equals(updatedMember.getMemberId())) {
            memberController.updateMemberProfile(updatedMember);
        }
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
