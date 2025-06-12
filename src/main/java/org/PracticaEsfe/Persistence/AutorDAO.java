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

    public Autor insertarAutor(Autor autor) throws SQLException {
        Autor createdAutor = null;
        String sql = "INSERT INTO Autor (NombreCompleto, Nacionalidad) VALUES (?, ?)";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, autor.getNombreCompleto());
            ps.setString(2, autor.getNacionalidad());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        autor.setId(generatedKeys.getInt(1));
                    }
                }
                createdAutor = autor;
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al crear el autor: " + ex.getMessage(), ex);
        } finally {
            // Considerar cuándo es apropiado desconectar
        }
        return createdAutor;
    }

    public Autor obtenerAutorPorId(int id) throws SQLException {
        Autor autor = null;
        String sql = "SELECT Id, NombreCompleto, Nacionalidad FROM Autor WHERE Id = ?";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // CORRECCIÓN: Mover ps.setInt() antes de executeQuery()
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    autor = new Autor();
                    autor.setId(rs.getInt("Id"));
                    autor.setNombreCompleto(rs.getString("NombreCompleto"));
                    autor.setNacionalidad(rs.getString("Nacionalidad"));
                }
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

    public void deleteAllAutores() throws SQLException {
        String sql = "DELETE FROM Autor";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar todos los autores: " + ex.getMessage(), ex);
        }
    }
}
