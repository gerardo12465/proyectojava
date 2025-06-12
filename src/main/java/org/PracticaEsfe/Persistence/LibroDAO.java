package org.PracticaEsfe.Persistence;

import org.PracticaEsfe.Dominio.Libro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {
    private ConetionManager conn;

    public LibroDAO() {
        conn = ConetionManager.getInstance();
    }

    public Libro create(Libro libro) throws SQLException {
        Libro newLibro = null;
        String sql = "INSERT INTO Libro (Titulo, FechaPublicacion, IdAutor) VALUES (?, ?, ?)";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, libro.getTitulo());
            ps.setDate(2, libro.getFechaPublicacion());
            ps.setInt(3, libro.getIdAutor());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        libro.setId(generatedKeys.getInt(1));
                    }
                }
                newLibro = libro;
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al crear el libro: " + ex.getMessage(), ex);
        } finally {
            // Considerar cuándo es apropiado desconectar
        }
        return newLibro;
    }

    public Libro findById(int id) throws SQLException {
        Libro libro = null;
        String sql = "SELECT Id, Titulo, FechaPublicacion, IdAutor FROM Libro WHERE Id = ?";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    libro = new Libro();
                    libro.setId(rs.getInt("Id"));
                    libro.setTitulo(rs.getString("Titulo"));
                    libro.setFechaPublicacion(rs.getDate("FechaPublicacion"));
                    libro.setIdAutor(rs.getInt("IdAutor"));
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar el libro por ID: " + ex.getMessage(), ex);
        } finally {
            // Considerar cuándo es apropiado desconectar
        }
        return libro;
    }

    public boolean update(Libro libro) throws SQLException {
        String sql = "UPDATE Libro SET Titulo = ?, FechaPublicacion = ?, IdAutor = ? WHERE Id = ?";
        boolean updated = false;
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setDate(2, libro.getFechaPublicacion());
            ps.setInt(3, libro.getIdAutor());
            ps.setInt(4, libro.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                updated = true;
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al actualizar el libro: " + ex.getMessage(), ex);
        } finally {
            // Considerar cuándo es apropiado desconectar
        }
        return updated;
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM Libro WHERE Id = ?";
        boolean deleted = false;
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                deleted = true;
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el libro: " + ex.getMessage(), ex);
        } finally {
            // Considerar cuándo es apropiado desconectar
        }
        return deleted;
    }

    public List<Libro> getAllLibros() throws SQLException {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT Id, Titulo, FechaPublicacion, IdAutor FROM Libro";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Libro libro = new Libro();
                libro.setId(rs.getInt("Id"));
                libro.setTitulo(rs.getString("Titulo"));
                libro.setFechaPublicacion(rs.getDate("FechaPublicacion"));
                libro.setIdAutor(rs.getInt("IdAutor"));
                libros.add(libro);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener todos los libros: " + ex.getMessage(), ex);
        } finally {
            // Considerar cuándo es apropiado desconectar
        }
        return libros;
    }

    // Nuevo método para eliminar todos los libros (útil para limpieza de pruebas)
    public void deleteAllLibros() throws SQLException {
        String sql = "DELETE FROM Libro";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar todos los libros: " + ex.getMessage(), ex);
        }
    }
}