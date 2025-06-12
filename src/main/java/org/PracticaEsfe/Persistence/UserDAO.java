package org.PracticaEsfe.Persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.PracticaEsfe.Dominio.Usuario;

public class UserDAO {
    private ConetionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public UserDAO() {
        conn = ConetionManager.getInstance();
    }

    public Usuario create(Usuario usuario) throws SQLException {
        Usuario res = null;
        // intente {  // No es un bloque try-with-resources para la conexión aquí, asumo que ConetionManager la maneja
        try (PreparedStatement ps = conn.connect().prepareStatement( // Asegúrate que connect() retorna una Connection
                "INSERT INTO Usuario (Nombre, Email, Contraseña) VALUES (?, ?, ?)",
                java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getContraseña());

            int rowsAffected = ps.executeUpdate(); // ¡AHORA SÍ EJECUTA!

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        usuario.setId(generatedKeys.getInt(1)); // Asumiendo que el ID es la primera columna generada
                    }
                }
                res = usuario; // Asigna el objeto Usuario (con ID si fue generado) a 'res'
            }
        } catch (SQLException ex) {
            // Re-lanzar la excepción con un mensaje más descriptivo es bueno
            throw new SQLException("Error al crear el usuario: " + ex.getMessage(), ex);
        } finally {
            // conn.disconnect(); // Solo si es realmente necesario cerrar la conexión aquí,
            // de lo contrario, déjalo al ConnectionManager o al final de la aplicación
        }
        return res;
    }


    public boolean update(Usuario usuario) throws SQLException{
        boolean res = false;
        try{

            ps = conn.connect().prepareStatement(
                    "UPDATE Usuario " +
                            "SET Nombre = ?, Email = ?" +
                            "WHERE id = ?"
            );

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setInt(3, usuario.getId());


            if(ps.executeUpdate() > 0){
                res = true;
            }
            ps.close();
        }catch (SQLException ex){

            throw new SQLException("Error al modificar el usuario: " + ex.getMessage(), ex);
        } finally {

            ps = null;
            conn.disconnect();
        }

        return res;
    }



}