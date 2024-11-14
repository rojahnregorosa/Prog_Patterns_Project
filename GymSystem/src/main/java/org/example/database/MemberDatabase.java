package org.example.database;

import org.example.model.Address;
import org.example.model.MembershipType;

import java.sql.*;

public class MemberDatabase {
    private static MemberDatabase instance;
    private Connection connection;

    private MemberDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/database/gym_database.db");
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
     * @param isMonthly      (monthly/yearly)
     * @return true if the member was added successfully, false otherwise
     */
    public int addMember(String firstName, String lastName, String phoneNumber, Address address, MembershipType membershipType, boolean isMonthly) {
        double balance = isMonthly ? membershipType.getMonthlyPrice() : membershipType.getYearlyPrice();
        int memberId = -1; // Default to failure

        try {
            // Step 1: Insert the address into the Addresses table
            String addressSQL = "INSERT INTO Addresses (street_number, street_name, city, province, zip_code) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement addressStmt = connection.prepareStatement(addressSQL, Statement.RETURN_GENERATED_KEYS)) {
                addressStmt.setInt(1, address.getStreetNumber());
                addressStmt.setString(2, address.getStreetName());
                addressStmt.setString(3, address.getCity());
                addressStmt.setString(4, address.getProvince());
                addressStmt.setString(5, address.getZipCode());
                addressStmt.executeUpdate();

                // Retrieve the generated address ID
                ResultSet addressKeys = addressStmt.getGeneratedKeys();
                if (addressKeys.next()) {
                    int addressId = addressKeys.getInt(1);

                    // Step 2: Insert the member into the Members table, using the address ID
                    String memberSQL = "INSERT INTO Members (first_name, last_name, phone_number, membership_type, balance, address_id) VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement memberStmt = connection.prepareStatement(memberSQL, Statement.RETURN_GENERATED_KEYS)) {
                        memberStmt.setString(1, firstName);
                        memberStmt.setString(2, lastName);
                        memberStmt.setString(3, phoneNumber);
                        memberStmt.setString(4, membershipType.name());
                        memberStmt.setDouble(5, balance); // Set balance based on frequency choice
                        memberStmt.setInt(6, addressId); // Link to the Addresses table

                        memberStmt.executeUpdate();

                        // Retrieve the generated member ID
                        ResultSet memberKeys = memberStmt.getGeneratedKeys();
                        if (memberKeys.next()) {
                            memberId = memberKeys.getInt(1); // Get the generated member ID
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding member to the database: " + e.getMessage());
        }
        return memberId; // Return the member ID (or -1 if failed)
    }

    /**
     * Removes member in database by ID
     *
     * @param memberId the member to remove
     * @return if removed successfully
     */
    public boolean removeMemberByID(int memberId) {
        String findAddressIdQuery = "SELECT address_id FROM Members WHERE id = ?";
        String deleteMemberQuery = "DELETE FROM Members WHERE id = ?";
        String deleteAddressQuery = "DELETE FROM Addresses WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect()) {
            conn.setAutoCommit(false); // Start transaction

            // Step 1: Retrieve the address ID associated with the member
            int addressId = -1;
            try (PreparedStatement findAddressStmt = conn.prepareStatement(findAddressIdQuery)) {
                findAddressStmt.setInt(1, memberId);
                ResultSet rs = findAddressStmt.executeQuery();
                if (rs.next()) {
                    addressId = rs.getInt("address_id");
                }
            }

            // Step 2: Delete the member
            try (PreparedStatement deleteMemberStmt = conn.prepareStatement(deleteMemberQuery)) {
                deleteMemberStmt.setInt(1, memberId);
                deleteMemberStmt.executeUpdate();
            }

            // Step 3: Delete the address only if we found an address ID
            if (addressId != -1) {
                try (PreparedStatement deleteAddressStmt = conn.prepareStatement(deleteAddressQuery)) {
                    deleteAddressStmt.setInt(1, addressId);
                    deleteAddressStmt.executeUpdate();
                }
            }

            conn.commit(); // Commit transaction
            System.out.println("Member and associated address removed from the database successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error removing member and address from the database: " + e.getMessage());
            return false;
        }
    }
}
