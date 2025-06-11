package org.PracticaEsfe.Persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.PracticaEsfe.Dominio.Usuario;

import java.util.ArrayList;

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
}