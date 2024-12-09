package org.example.database;

import java.sql.*;

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

            String createEmployeesTable = """
                        CREATE TABLE IF NOT EXISTS Employees (
                            id INTEGER PRIMARY KEY,
                            first_name TEXT NOT NULL,
                            last_name TEXT NOT NULL,
                            phone_number TEXT NOT NULL
                        );
                    """;
            stmt.execute(createEmployeesTable);

            insertDefaultEmployeesIfNeeded(conn);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Inserts 5 default employees if the Employees table is empty.
     *
     * @param conn The database connection.
     */
    private static void insertDefaultEmployeesIfNeeded(Connection conn) throws SQLException {
        String checkEmployeesQuery = "SELECT COUNT(*) FROM Employees";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkEmployeesQuery)) {

            // Check if there are already any employees in the Employees table
            if (rs.next() && rs.getInt(1) == 0) {
                // No employees in the table, insert 5 default employees
                String insertEmployeesQuery = """
                        INSERT INTO Employees (first_name, last_name, phone_number) 
                        VALUES 
                        ('John', 'Doe', '1234567890'),
                        ('Jane', 'Smith', '0987654321'),
                        ('Robert', 'Johnson', '1122334455'),
                        ('Emily', 'Davis', '2233445566'),
                        ('Michael', 'Miller', '3344556677');
                    """;
                stmt.execute(insertEmployeesQuery);
                System.out.println("5 default employees have been added to the Employees table.");
            }
        }
    }
}
