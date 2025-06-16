package org.PracticaEsfe.Persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.PracticaEsfe.Dominio.Usuario;

public class UserDAO {
    private ConetionManager conn;

    public UserDAO() {
        conn = ConetionManager.getInstance();
    }

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

    public Usuario findByEmailAndPassword(String email, String password) throws SQLException {
        Usuario usuario = null;
        String sql = "SELECT Id, Nombre, Email, Contraseña FROM Usuario WHERE Email = ? AND Contraseña = ?";
        try (PreparedStatement ps = conn.connect().prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario(
                            rs.getInt("Id"),
                            rs.getString("Nombre"),
                            rs.getString("Email"),
                            rs.getString("Contraseña")
                    );
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al autenticar usuario: " + ex.getMessage(), ex);
        }
        return usuario;
    }

    public Usuario findByEmail(String email) throws SQLException {
        Usuario usuario = null;
        String sql = "SELECT Id, Nombre, Email, Contraseña FROM Usuario WHERE Email = ?";
        try (PreparedStatement ps = conn.connect().prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario(
                            rs.getInt("Id"),
                            rs.getString("Nombre"),
                            rs.getString("Email"),
                            rs.getString("Contraseña")
                    );
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar usuario por email: " + ex.getMessage(), ex);
        }
        return usuario;
    }
}
