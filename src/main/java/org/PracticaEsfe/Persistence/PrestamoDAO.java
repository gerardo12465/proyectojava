package org.PracticaEsfe.Persistence;

import org.PracticaEsfe.Dominio.Prestamo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

/**
 * Data Access Object (DAO) for the Prestamo (Loan) entity.
 * This class handles database operations for Prestamo objects,
 * including creation, reading, updating, and deleting.
 * It assumes a ConnectionManager class is available for managing database connections.
 */
public class PrestamoDAO {
    private ConetionManager conn;

    /**
     * Constructor for PrestamoDAO.
     * Initializes the ConnectionManager instance.
     */
    public PrestamoDAO() {
        conn = ConetionManager.getInstance();
    }

    /**
     * Creates a new loan record in the database.
     * Inserts a Prestamo object into the 'Prestamo' table and retrieves
     * the auto-generated ID, updating the Prestamo object.
     *
     * @param prestamo The Prestamo object to be created.
     * @return The Prestamo object with its generated ID, or null if creation failed.
     * @throws SQLException If a database access error occurs.
     */
    public Prestamo create(Prestamo prestamo) throws SQLException {
        Prestamo newPrestamo = null;
        String sql = "INSERT INTO Prestamo (IdUsuario, IdLibro, FechaPrestamo, FechaDevolucion) VALUES (?, ?, ?, ?)";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, prestamo.getIdUsuario());
            ps.setInt(2, prestamo.getIdLibro());
            ps.setDate(3, prestamo.getFechaPrestamo());
            if (prestamo.getFechaDevolucion() != null) {
                ps.setDate(4, prestamo.getFechaDevolucion());
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        prestamo.setId(generatedKeys.getInt(1));
                    }
                }
                newPrestamo = prestamo;
            }
        } catch (SQLException ex) {
            throw new SQLException("Error creating the loan: " + ex.getMessage(), ex);
        }
        return newPrestamo;
    }

    /**
     * Finds a loan by its ID in the database.
     *
     * @param id The ID of the loan to find.
     * @return The Prestamo object if found, null otherwise.
     * @throws SQLException If a database access error occurs.
     */
    public Prestamo findById(int id) throws SQLException {
        Prestamo prestamo = null;
        String sql = "SELECT Id, IdUsuario, IdLibro, FechaPrestamo, FechaDevolucion FROM Prestamo WHERE Id = ?";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    prestamo = new Prestamo();
                    prestamo.setId(rs.getInt("Id"));
                    prestamo.setIdUsuario(rs.getInt("IdUsuario"));
                    prestamo.setIdLibro(rs.getInt("IdLibro"));
                    prestamo.setFechaPrestamo(rs.getDate("FechaPrestamo"));
                    prestamo.setFechaDevolucion(rs.getDate("FechaDevolucion"));
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error finding loan by ID: " + ex.getMessage(), ex);
        }
        return prestamo;
    }

    /**
     * Updates an existing loan record in the database.
     * Modifies the IdUsuario, IdLibro, FechaPrestamo, and FechaDevolucion of a loan identified by its ID.
     *
     * @param prestamo The Prestamo object containing the updated information and the ID.
     * @return true if the update was successful, false otherwise.
     * @throws SQLException If a database access error occurs.
     */
    public boolean update(Prestamo prestamo) throws SQLException {
        boolean updated = false;
        String sql = "UPDATE Prestamo SET IdUsuario = ?, IdLibro = ?, FechaPrestamo = ?, FechaDevolucion = ? WHERE Id = ?";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, prestamo.getIdUsuario());
            ps.setInt(2, prestamo.getIdLibro());
            ps.setDate(3, prestamo.getFechaPrestamo());
            if (prestamo.getFechaDevolucion() != null) {
                ps.setDate(4, prestamo.getFechaDevolucion());
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }
            ps.setInt(5, prestamo.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                updated = true;
            }
        } catch (SQLException ex) {
            throw new SQLException("Error updating the loan: " + ex.getMessage(), ex);
        }
        return updated;
    }

    /**
     * Deletes a loan from the database by its ID.
     *
     * @param id The ID of the loan to delete.
     * @return true if the deletion was successful, false otherwise.
     * @throws SQLException If a database access error occurs.
     */
    public boolean delete(int id) throws SQLException {
        boolean deleted = false;
        String sql = "DELETE FROM Prestamo WHERE Id = ?";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                deleted = true;
            }
        } catch (SQLException ex) {
            throw new SQLException("Error deleting the loan: " + ex.getMessage(), ex);
        }
        return deleted;
    }

    /**
     * Retrieves all loans from the database.
     *
     * @return A List of Prestamo objects, or an empty list if no loans are found.
     * @throws SQLException If a database access error occurs.
     */
    public List<Prestamo> getAllPrestamos() throws SQLException {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT Id, IdUsuario, IdLibro, FechaPrestamo, FechaDevolucion FROM Prestamo";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Prestamo prestamo = new Prestamo();
                prestamo.setId(rs.getInt("Id"));
                prestamo.setIdUsuario(rs.getInt("IdUsuario"));
                prestamo.setIdLibro(rs.getInt("IdLibro"));
                prestamo.setFechaPrestamo(rs.getDate("FechaPrestamo"));
                prestamo.setFechaDevolucion(rs.getDate("FechaDevolucion"));
                prestamos.add(prestamo);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error retrieving all loans: " + ex.getMessage(), ex);
        }
        return prestamos;
    }

    /**
     * Deletes all loan records from the database.
     * Useful for cleanup or testing purposes.
     *
     * @throws SQLException If a database access error occurs.
     */
    public void deleteAllPrestamos() throws SQLException {
        String sql = "DELETE FROM Prestamo";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Error deleting all loans: " + ex.getMessage(), ex);
        }
    }
}