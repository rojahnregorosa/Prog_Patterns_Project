package org.example.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {

    /**
     *
     * @param conn
     */
    public static void createTables(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            String createMembersTable = """
                CREATE TABLE IF NOT EXISTS Members (
                    id INTEGER PRIMARY KEY,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    phone_number TEXT NOT NULL,
                    address_id INTEGER,
                    membership_type TEXT,
                    balance REAL DEFAULT 0,
                    payment_frequency TEXT,
                    FOREIGN KEY (address_id) REFERENCES Addresses(id)
                );
            """;
            stmt.execute(createMembersTable);

            String createEmployeesTable = """
                CREATE TABLE IF NOT EXISTS Employees (
                    id INTEGER PRIMARY KEY,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    position TEXT,
                    email TEXT,
                    phone_number TEXT
                );
            """;
            stmt.execute(createEmployeesTable);

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

            String createMembershipTypesTable = """
                CREATE TABLE IF NOT EXISTS MembershipTypes (
                    type_name TEXT PRIMARY KEY,
                    monthly_price REAL,
                    yearly_price REAL
                );
            """;
            stmt.execute(createMembershipTypesTable);

            String createAddressesTable = """
                CREATE TABLE IF NOT EXISTS Addresses (
                    id INTEGER PRIMARY KEY,
                    street TEXT,
                    city TEXT,
                    province TEXT,
                    zip_code TEXT
                );
            """;
            stmt.execute(createAddressesTable);

            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }
}
