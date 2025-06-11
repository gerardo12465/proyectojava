package org.PracticaEsfe.Persistence; // Es importante que esta clase esté en el paquete Persistence, no en presentacion

import org.PracticaEsfe.Dominio.Autor; // <--- ¡IMPORTACIÓN AÑADIDA/CORREGIDA!
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase implementa el patrón DAO (Data Access Object) para la entidad Autor.
 * Se encarga de todas las operaciones de persistencia (CRUD) relacionadas con la tabla de autores en la base de datos.
 * Utiliza la clase ConetionManager para obtener y gestionar la conexión a la base de datos.
 */
public class AutorDAO {

    /**
     * Inserta un nuevo autor en la base de datos.
     *
     * @param autor El objeto Autor a insertar. Se espera que el ID sea autogenerado por la base de datos.
     * @return true si la inserción fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public boolean insertarAutor(Autor autor) throws SQLException {
        // MODIFICADO: Cambiado 'Autores' a 'Autor'
        String sql = "INSERT INTO Autor (nombreCompleto, nacionalidad) VALUES (?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean inserted = false;

        try {
            connection = ConetionManager.getInstance().connect(); // Obtiene la conexión de la base de datos
            statement = connection.prepareStatement(sql);
            statement.setString(1, autor.getNombreCompleto());
            statement.setString(2, autor.getNacionalidad());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                inserted = true;
            }
        } finally {
            // Asegura que los recursos se cierren, incluso si hay una excepción
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                ConetionManager.getInstance().disconnect(); // Desconecta la base de datos
            }
        }
        return inserted;
    }

    /**
     * Obtiene un autor de la base de datos por su ID.
     *
     * @param id El ID del autor a buscar.
     * @return El objeto Autor encontrado, o null si no se encuentra ningún autor con ese ID.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public Autor obtenerAutorPorId(int id) throws SQLException {
        // MODIFICADO: Cambiado 'Autores' a 'Autor'
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

    /**
     * Obtiene una lista de todos los autores de la base de datos.
     *
     * @return Una lista de objetos Autor. Si no hay autores, la lista estará vacía.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public List<Autor> obtenerTodosLosAutores() throws SQLException {
        // Ya estaba correcto: 'Autor'
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

    /**
     * Actualiza la información de un autor existente en la base de datos.
     *
     * @param autor El objeto Autor con la información actualizada. El ID del autor debe existir en la base de datos.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public boolean actualizarAutor(Autor autor) throws SQLException {
        // MODIFICADO: Cambiado 'Autores' a 'Autor'
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

    /**
     * Elimina un autor de la base de datos por su ID.
     *
     * @param id El ID del autor a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public boolean eliminarAutor(int id) throws SQLException {
        // MODIFICADO: Cambiado 'Autores' a 'Autor'
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