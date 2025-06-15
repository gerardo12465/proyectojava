package org.PracticaEsfe.Persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List; // Ensure this import is present for List type

import org.PracticaEsfe.Dominio.Usuario;

/**
 * Data Access Object (DAO) for the Usuario (User) entity.
 * This class handles the database operations for User objects,
 * including creation, reading, updating, and deleting.
 * It assumes a ConnectionManager class is available for managing database connections.
 */
public class UserDAO {
    private ConetionManager conn;

    /**
     * Constructor for UserDAO.
     * Initializes the ConnectionManager instance.
     */
    public UserDAO() {
        conn = ConetionManager.getInstance();
    }

    /**
     * Creates a new user record in the database.
     * Inserts a Usuario object into the 'Usuario' table and retrieves
     * the auto-generated ID, updating the Usuario object.
     *
     * @param usuario The Usuario object to be created.
     * @return The Usuario object with its generated ID, or null if creation failed.
     * @throws SQLException If a database access error occurs.
     */
    public Usuario create(Usuario usuario) throws SQLException {
        Usuario res = null;
        try (PreparedStatement ps = conn.connect().prepareStatement(
                "INSERT INTO Usuario (Nombre, Email, Contraseña) VALUES (?, ?, ?)",
                java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getContraseña());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        usuario.setId(generatedKeys.getInt(1));
                    }
                }
                res = usuario;
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al crear el usuario: " + ex.getMessage(), ex);
        }
        return res;
    }

    /**
     * Updates an existing user record in the database.
     * Modifies the name, email, and password of a user identified by its ID.
     *
     * @param usuario The Usuario object containing the updated information and the ID.
     * @return true if the update was successful, false otherwise.
     * @throws SQLException If a database access error occurs.
     */
    public boolean update(Usuario usuario) throws SQLException {
        boolean res = false;
        try (PreparedStatement ps = conn.connect().prepareStatement(
                "UPDATE Usuario SET Nombre = ?, Email = ?, Contraseña = ? WHERE id = ?")) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getContraseña());
            ps.setInt(4, usuario.getId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el usuario: " + ex.getMessage(), ex);
        }
        return res;
    }

    /**
     * Finds a user by their ID in the database.
     *
     * @param id The ID of the user to find.
     * @return The Usuario object if found, null otherwise.
     * @throws SQLException If a database access error occurs.
     */
    public Usuario findById(int id) throws SQLException {
        Usuario usuario = null;
        try (PreparedStatement ps = conn.connect().prepareStatement("SELECT id, Nombre, Email, Contraseña FROM Usuario WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario(
                            rs.getInt("id"),
                            rs.getString("Nombre"),
                            rs.getString("Email"),
                            rs.getString("Contraseña")
                    );
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar el usuario por ID: " + ex.getMessage(), ex);
        }
        return usuario;
    }

    /**
     * Deletes a user from the database by their ID.
     *
     * @param id The ID of the user to delete.
     * @return true if the deletion was successful, false otherwise.
     * @throws SQLException If a database access error occurs.
     */
    public boolean delete(int id) throws SQLException {
        boolean res = false;
        try (PreparedStatement ps = conn.connect().prepareStatement("DELETE FROM Usuario WHERE id = ?")) {
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                res = true;
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el usuario: " + ex.getMessage(), ex);
        }
        return res;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A List of Usuario objects, or an empty list if no users are found.
     * @throws SQLException If a database access error occurs.
     */
    public List<Usuario> getAllUsers() throws SQLException {
        List<Usuario> users = new ArrayList<>();
        try (PreparedStatement ps = conn.connect().prepareStatement("SELECT id, Nombre, Email, Contraseña FROM Usuario")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(new Usuario(
                            rs.getInt("id"),
                            rs.getString("Nombre"),
                            rs.getString("Email"),
                            rs.getString("Contraseña")
                    ));
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener todos los usuarios: " + ex.getMessage(), ex);
        }
        return users;
    }
    public void deleteAllUsers() throws SQLException {
        String sql = "DELETE FROM Usuario";
        try (PreparedStatement ps = conn.connect().prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar todos los usuarios: " + ex.getMessage(), ex);
        }
    }
}