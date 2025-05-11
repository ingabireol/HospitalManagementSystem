package olim.com.hospitalmanagementsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    // JDBC URL, username and password
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/HospitalManagementSystemDb";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "078868";

    // Connection pool to reuse connections
    private static Connection connection = null;

    /**
     * Get a connection to the database
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load PostgreSQL JDBC Driver
            Class.forName("org.postgresql.Driver");
            
            // Create a connection if it doesn't exist or is closed
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                connection.setAutoCommit(false); // For transaction control
            }
            return connection;
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found", e);
        } catch (SQLException e) {
            System.err.println("Connection Error: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Close the database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    /**
     * Commit the current transaction
     * @throws SQLException if a database access error occurs
     */
    public static void commitTransaction() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.commit();
        }
    }

    /**
     * Rollback the current transaction
     */
    public static void rollbackTransaction() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
            }
        } catch (SQLException e) {
            System.err.println("Error during rollback: " + e.getMessage());
        }
    }
}