package org.example.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {

    /**
     * Creates tables in the database
     */
    public static void createTables() {
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {

            String createAddressesTable = """
            CREATE TABLE IF NOT EXISTS Addresses (
                id INTEGER PRIMARY KEY,
                street_number INTEGER,
                street_name TEXT,
                city TEXT,
                province TEXT,
                zip_code TEXT
            );
        """;
            stmt.execute(createAddressesTable);

            String createMembersTable = """
            CREATE TABLE IF NOT EXISTS Members (
                id INTEGER PRIMARY KEY,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                phone_number TEXT NOT NULL,
                membership_type TEXT,
                balance REAL DEFAULT 0,
                address_id INTEGER,
                FOREIGN KEY (address_id) REFERENCES Addresses(id)
            );
        """;
            stmt.execute(createMembersTable);

            String createPaymentsTable = """
            CREATE TABLE IF NOT EXISTS Payments (
                id INTEGER PRIMARY KEY,
                member_id INTEGER,
                amount REAL,
                payment_date TEXT,
                payment_method TEXT,
                FOREIGN KEY (member_id) REFERENCES Members(id)
            );
        """;
            stmt.execute(createPaymentsTable);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *
     */
    public static void dropTables() {
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {

            String dropEmployeeTable = "DROP TABLE IF EXISTS Employees;";
//            String dropMemberTable = "DROP TABLE IF EXISTS Members;";
            String dropMembershipTypeTable = "DROP TABLE IF EXISTS MembershipTypes;";
            String dropAddressesTable = "DROP TABLE IF EXISTS Addresses;";

            stmt.execute(dropAddressesTable);
            stmt.execute(dropEmployeeTable);
//            stmt.execute(dropMemberTable);
            stmt.execute(dropMembershipTypeTable);

            System.out.println("Tables deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting tables: " + e.getMessage());
        }
    }
}
