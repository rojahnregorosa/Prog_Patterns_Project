package org.example.database;

import org.example.model.Address;
import org.example.model.MembershipType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MemberDatabase {
    private static MemberDatabase instance;
    private Connection connection;

    private MemberDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:your_database_path");
    }

    public static MemberDatabase getInstance() throws SQLException {
        if (instance == null) {
            instance = new MemberDatabase();
        }
        return instance;
    }

    /**
     * Adds a new member to the database.
     *
     * @param firstName      Member's first name
     * @param lastName       Member's last name
     * @param phoneNumber    Member's phone number
     * @param membershipType Member's membership type
     * @param isMonthly (monthly/yearly)
     * @return true if the member was added successfully, false otherwise
     */
    public boolean addMember(String firstName, String lastName, String phoneNumber, Address address, MembershipType membershipType, boolean isMonthly) {
        String sql = "INSERT INTO Members (first_name, last_name, phone_number, membership_type, balance) VALUES (?, ?, ?, ?, ?)";
        double balance = isMonthly ? membershipType.getMonthlyPrice() : membershipType.getYearlyPrice();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, phoneNumber);
            pstmt.setString(4, membershipType.name());
            pstmt.setDouble(5, balance); // Set balance based on frequency choice

            pstmt.executeUpdate();
            System.out.println("Member added to the database successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding member to the database: " + e.getMessage());
            return false;
        }
    }
}
