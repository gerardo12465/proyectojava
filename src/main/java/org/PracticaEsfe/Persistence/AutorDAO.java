package org.PracticaEsfe.Persistence;

import org.PracticaEsfe.Dominio.Autor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {
    private ConetionManager conn;

    public AutorDAO() {
        conn = ConetionManager.getInstance();
    }

    // MODIFICADO: Ahora devuelve el objeto Autor con el ID generado.
    public Autor insertarAutor(Autor autor) throws SQLException {
        Autor createdAutor = null; // Variable para almacenar el autor con el ID
        String sql = "INSERT INTO Autor (NombreCompleto, Nacionalidad) VALUES (?, ?)"; // Asegúrate de que las columnas coincidan con tu DB
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, autor.getNombreCompleto());
            ps.setString(2, autor.getNacionalidad());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        autor.setId(generatedKeys.getInt(1)); // Asigna el ID generado al objeto autor
                    }
                }
                createdAutor = autor; // Asigna el autor con ID a la variable de retorno
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al crear el autor: " + ex.getMessage(), ex);
        } finally {
            // Considerar cuándo es apropiado desconectar la conexión (si ConnectionManager no maneja pool)
        }
        return createdAutor; // Devuelve el autor con el ID generado
    }

    public Autor obtenerAutorPorId(int id) throws SQLException {
        Autor autor = null;
        String sql = "SELECT Id, NombreCompleto, Nacionalidad FROM Autor WHERE Id = ?";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) { // Moved executeQuery into try-with-resources if only used once

            ps.setInt(1, id);
            // Removed nested try-with-resources for ResultSet for simplicity,
            // as outer try-with-resources for PreparedStatement already handles closing.
            if (rs.next()) {
                autor = new Autor();
                autor.setId(rs.getInt("Id"));
                autor.setNombreCompleto(rs.getString("NombreCompleto"));
                autor.setNacionalidad(rs.getString("Nacionalidad"));
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar el autor por ID: " + ex.getMessage(), ex);
        } finally {
            // Considerar cuándo es apropiado desconectar
        }
        return autor;
    }

    public List<Autor> obtenerTodosLosAutores() throws SQLException {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT Id, NombreCompleto, Nacionalidad FROM Autor";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Autor autor = new Autor();
                autor.setId(rs.getInt("Id"));
                autor.setNombreCompleto(rs.getString("NombreCompleto"));
                autor.setNacionalidad(rs.getString("Nacionalidad"));
                autores.add(autor);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener todos los autores: " + ex.getMessage(), ex);
        } finally {
            // Considerar cuándo es apropiado desconectar
        }
        return autores;
    }

    public boolean actualizarAutor(Autor autor) throws SQLException {
        String sql = "UPDATE Autor SET NombreCompleto = ?, Nacionalidad = ? WHERE Id = ?";
        boolean updated = false;
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, autor.getNombreCompleto());
            ps.setString(2, autor.getNacionalidad());
            ps.setInt(3, autor.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                updated = true;
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al actualizar el autor: " + ex.getMessage(), ex);
        } finally {
            // Considerar cuándo es apropiado desconectar
        }
        return updated;
    }

    public boolean eliminarAutor(int id) throws SQLException {
        String sql = "DELETE FROM Autor WHERE Id = ?";
        boolean deleted = false;
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                deleted = true;
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el autor: " + ex.getMessage(), ex);
        } finally {
            // Considerar cuándo es apropiado desconectar
        }
        return deleted;
    }

    // Método para obtener autor por nombre completo (útil para pruebas si necesitas ID por nombre)
    public Autor obtenerAutorPorNombreCompleto(String nombreCompleto) throws SQLException {
        Autor autor = null;
        String sql = "SELECT Id, NombreCompleto, Nacionalidad FROM Autor WHERE NombreCompleto = ?";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, nombreCompleto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    autor = new Autor();
                    autor.setId(rs.getInt("Id"));
                    autor.setNombreCompleto(rs.getString("NombreCompleto"));
                    autor.setNacionalidad(rs.getString("Nacionalidad"));
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar autor por nombre completo: " + ex.getMessage(), ex);
        } finally {
            // Considerar cuándo es apropiado desconectar
        }
        return autor;
    }

    // Método para eliminar todos los autores (útil para limpieza de pruebas)
    public void deleteAllAutores() throws SQLException {
        String sql = "DELETE FROM Autor";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar todos los autores: " + ex.getMessage(), ex);
        } finally {
            // Considerar cuándo es apropiado desconectar
        }
    }
}
