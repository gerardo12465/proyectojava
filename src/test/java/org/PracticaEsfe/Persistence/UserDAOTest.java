package org.PracticaEsfe.Persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.PracticaEsfe.Dominio.Usuario;

import java.util.ArrayList;
import java.util.Random;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {
    private UserDAO userDAO;

    @BeforeEach
    void setUp(){

        userDAO = new UserDAO();
    }

    private Usuario create(Usuario usuario) throws SQLException {
        Usuario res = userDAO.create(usuario);

        assertNotNull(res, "El usuario creado no debería ser nulo.");
        assertEquals(usuario.getNombre(), res.getNombre(), "El nombre del usuario creado debe ser igual al original.");
        assertEquals(usuario.getEmail(), res.getEmail(), "El email del usuario creado debe ser igual al original.");
        return res;
    }

    @Test
    void createUser() throws SQLException {
        Usuario usuario = new Usuario(0, "admin", "12345", "admin@gmail.com");
        Usuario res = userDAO.create(usuario);



    }

    private void update(Usuario usuario) throws SQLException{
        // Modifica los atributos del objeto User para simular una actualización.
        usuario.setNombre(usuario.getNombre() + "_u"); // Añade "_u" al final del nombre.
        usuario.setEmail("u" + usuario.getEmail()); // Añade "u" al inicio del email.

        // Llama al método 'update' del UserDAO para actualizar el usuario en la base de datos (simulada).
        boolean res = userDAO.update(usuario);

        // Realiza una aserción para verificar que la actualización fue exitosa.
        assertTrue(res, "La actualización del usuario debería ser exitosa.");


    }


}