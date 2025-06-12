package org.PracticaEsfe.Persistence;

import org.PracticaEsfe.Dominio.Autor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {

    public boolean insertarAutor(Autor autor) throws SQLException {
        String sql = "INSERT INTO Autor (nombreCompleto, nacionalidad) VALUES (?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean inserted = false;

        try {
            connection = ConetionManager.getInstance().connect();
            statement = connection.prepareStatement(sql);
            statement.setString(1, autor.getNombreCompleto());
            statement.setString(2, autor.getNacionalidad());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                inserted = true;
            }
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                ConetionManager.getInstance().disconnect();
            }
        }
        return inserted;
    }

    public Autor obtenerAutorPorId(int id) throws SQLException {
        String sql = "SELECT id, nombreCompleto, nacionalidad FROM Autor WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Autor autor = null;

        try {
            connection = ConetionManager.getInstance().connect();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                autor = new Autor();
                autor.setId(resultSet.getInt("id"));
                autor.setNombreCompleto(resultSet.getString("nombreCompleto"));
                autor.setNacionalidad(resultSet.getString("nacionalidad"));
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                ConetionManager.getInstance().disconnect();
            }
        }
        return autor;
    }

    public List<Autor> obtenerTodosLosAutores() throws SQLException {
        String sql = "SELECT id, nombreCompleto, nacionalidad FROM Autor";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Autor> autores = new ArrayList<>();

        try {
            connection = ConetionManager.getInstance().connect();
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Autor autor = new Autor();
                autor.setId(resultSet.getInt("id"));
                autor.setNombreCompleto(resultSet.getString("nombreCompleto"));
                autor.setNacionalidad(resultSet.getString("nacionalidad"));
                autores.add(autor);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                ConetionManager.getInstance().disconnect();
            }
        }
        return autores;
    }

    public boolean actualizarAutor(Autor autor) throws SQLException {
        String sql = "UPDATE Autor SET nombreCompleto = ?, nacionalidad = ? WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean updated = false;

        try {
            connection = ConetionManager.getInstance().connect();
            statement = connection.prepareStatement(sql);
            statement.setString(1, autor.getNombreCompleto());
            statement.setString(2, autor.getNacionalidad());
            statement.setInt(3, autor.getId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                updated = true;
            }
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                ConetionManager.getInstance().disconnect();
            }
        }
        return updated;
    }

    public boolean eliminarAutor(int id) throws SQLException {
        String sql = "DELETE FROM Autor WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean deleted = false;

        try {
            connection = ConetionManager.getInstance().connect();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                deleted = true;
            }
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                ConetionManager.getInstance().disconnect();
            }
        }
        return deleted;
    }
}
