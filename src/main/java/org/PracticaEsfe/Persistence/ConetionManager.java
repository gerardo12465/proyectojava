package org.PracticaEsfe.Persistence;

import java.sql.Connection; // Represents a connection to the database.
import java.sql.DriverManager; // Manages JDBC drivers and establishes connections.
import java.sql.SQLException; // Represents database-specific errors.

/**
 * This class is responsible for managing the connection to the SQL Server database using JDBC.
 * It implements the Singleton pattern to ensure that only a single instance
 * of the class exists and, therefore, a single shared database connection.
 */
public class ConetionManager {
    /**
     * Defines the database connection string. It contains the necessary information
     * to establish communication with the database server.
     * - jdbc:sqlserver://... : Indicates the type of connection (JDBC for SQL Server).
     * - Javaproyecto.mssql.somee.com : Server address.
     * - encrypt=true : Indicates if the connection should be encrypted.
     * - database=Javaproyecto : Specifies the database to connect to.
     * - trustServerCertificate=true : Indicates that the server certificate is trusted (for development environments).
     * - user=papitafrita_SQLLogin_1 : Username for authentication.
     * - password=ttyaep3bmy : Password for authentication.
     */
    private static final String STR_CONNECTION = "jdbc:sqlserver://Javaproyecto.mssql.somee.com;" +
            "encrypt=true;" + // Keep as true for security
            "database=Javaproyecto;" + // Changed from ProyectoJava to Javaproyecto
            "trustServerCertificate=True;" + // Keep as True as per provided details
            "user=papitafrita_SQLLogin_1;" + // Changed user
            "password=ttyaep3bmy;"; // Changed password
    /**
     * Represents the active database connection. Initially it is null.
     */
    private Connection connection;

    /**
     * Unique instance of the JDBCConnectionManager class (for the Singleton pattern).
     * It is initialized to null and created only when needed for the first time.
     */
    private static ConetionManager instance;

    /**
     * Private constructor to prevent direct instantiation from outside the class.
     * This is fundamental for the Singleton pattern.
     */
    private ConetionManager() {
        this.connection = null;
        try {
            // Loads the Microsoft SQL Server JDBC driver. This is necessary for Java to be able
            // to communicate with the SQL Server database.
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            // If the driver is not found, an exception is thrown indicating the error.
            throw new RuntimeException("Error loading the SQL Server JDBC driver", e);
        }
    }

    /**
     * This method is responsible for establishing the connection to the database.
     * It is synchronized to ensure that only one thread at a time can
     * attempt to establish the connection, which is important in multithreaded environments.
     *
     * @return The database connection instance.
     * @throws SQLException If an error occurs while trying to connect to the database.
     */
    public synchronized Connection connect() throws SQLException {
        // Checks if the connection already exists and if it is not closed.
        if (this.connection == null || this.connection.isClosed()) {
            try {
                // Attempts to establish the connection using the connection string.
                this.connection = DriverManager.getConnection(STR_CONNECTION);
            } catch (SQLException exception) {
                // If an error occurs during connection, an SQLException is thrown
                // with a more descriptive message that includes the original exception message.
                throw new SQLException("Error connecting to the database: " + exception.getMessage(), exception);
            }
        }
        // Returns the connection (either the existing one or the newly created one).
        return this.connection;
    }

    /**
     * This method is responsible for closing the database connection.
     * It also throws an SQLException if an error occurs while trying to close the connection.
     *
     * @throws SQLException If an error occurs while trying to close the connection.
     */
    public void disconnect() throws SQLException {
        // Checks if the connection exists (is not null).
        if (this.connection != null) {
            try {
                // Attempts to close the connection.
                this.connection.close();
            } catch (SQLException exception) {
                // If an error occurs while closing the connection, an SQLException is thrown
                // with a more descriptive message.
                throw new SQLException("Error closing the connection: " + exception.getMessage(), exception);
            } finally {
                // The finally block always executes, regardless of whether an exception occurred or not.
                // Here, it ensures that the connection reference is set to null,
                // indicating that there is no longer an active connection managed by this instance.
                this.connection = null;
            }
        }
    }

    /**
     * This static and synchronized method implements the Singleton pattern.
     * It returns the single instance of JDBCConnectionManager. If the instance does not yet exist,
     * it creates a new one before returning it. Synchronization ensures that instance creation
     * is safe in multithreaded environments (so that multiple threads do not try to create the instance at the same time).
     *
     * @return The single instance of JDBCConnectionManager.
     */
    public static synchronized ConetionManager getInstance() {
        // Checks if the instance has already been created.
        if (instance == null) {
            // If it does not exist, a new instance of JDBCConnectionManager is created.
            instance = new ConetionManager();
        }
        // Returns the existing (or newly created) instance.
        return instance;
    }
}