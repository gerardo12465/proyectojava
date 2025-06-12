package org.PracticaEsfe.Persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.PracticaEsfe.Dominio.Usuario;
import java.util.List;

import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private UserDAO userDAO;

    @BeforeEach
    void setUp(){
        userDAO = new UserDAO();
    }

    @Test
    void createUser() throws SQLException {
        String uniqueId = UUID.randomUUID().toString();

        String username = "test_user_" + uniqueId.substring(0, 8);
        String password = "password_" + uniqueId.substring(0, 4);
        String email = "test_" + uniqueId + "@example.com";

        Usuario usuario = new Usuario(0, username, email, password);

        Usuario createdUser = userDAO.create(usuario);

        assertNotNull(createdUser, "El usuario creado no debería ser nulo después de la inserción.");
        assertTrue(createdUser.getId() > 0, "El ID del usuario creado debe ser mayor que 0 (autoincrementado).");
        assertEquals(username, createdUser.getNombre(), "El nombre del usuario creado debe ser igual al generado.");
        assertEquals(email, createdUser.getEmail(), "El email del usuario creado debe ser igual al generado.");
        assertEquals(password, createdUser.getContraseña(), "La contraseña del usuario creado debe ser igual a la generada.");
    }


    @Test
    void readUserById() throws SQLException {
        String uniqueId = UUID.randomUUID().toString();
        Usuario userToCreate = new Usuario(0, "read_user_" + uniqueId.substring(0,4), "read_" + uniqueId + "@example.com", "pass_" + uniqueId.substring(0,4));
        Usuario createdUser = userDAO.create(userToCreate);
        assertNotNull(createdUser, "User should be created for reading.");
        assertTrue(createdUser.getId() > 0, "Created user should have an ID.");

        Usuario readUser = userDAO.findById(createdUser.getId());

        assertNotNull(readUser, "The read user should not be null.");
        assertEquals(createdUser.getId(), readUser.getId(), "IDs should match.");
        assertEquals(createdUser.getNombre(), readUser.getNombre(), "Names should match.");
        assertEquals(createdUser.getEmail(), readUser.getEmail(), "Emails should match.");
    }

    @Test
    void updateUser() throws SQLException {
        String uniqueId = UUID.randomUUID().toString();
        Usuario userToCreate = new Usuario(0, "update_user_" + uniqueId.substring(0,4), "update_" + uniqueId + "@example.com", "old_pass_" + uniqueId.substring(0,4));
        Usuario createdUser = userDAO.create(userToCreate);
        assertNotNull(createdUser, "User should be created for updating.");
        assertTrue(createdUser.getId() > 0, "Created user should have an ID.");

        createdUser.setNombre("Updated Name " + uniqueId.substring(0,4));
        createdUser.setContraseña("new_pass_" + uniqueId.substring(0,4));
        createdUser.setEmail("updated_" + uniqueId + "@example.com");

        boolean isUpdated = userDAO.update(createdUser);

        assertTrue(isUpdated, "The user should have been updated successfully.");

        Usuario updatedUser = userDAO.findById(createdUser.getId());
        assertNotNull(updatedUser);
        assertEquals(createdUser.getNombre(), updatedUser.getNombre(), "Updated name should match.");
        assertEquals(createdUser.getEmail(), updatedUser.getEmail(), "Updated email should match.");
        assertEquals(createdUser.getContraseña(), updatedUser.getContraseña(), "Updated password should match.");
    }

    @Test
    void deleteUser() throws SQLException {
        String uniqueId = UUID.randomUUID().toString();
        Usuario userToCreate = new Usuario(0, "delete_user_" + uniqueId.substring(0,4), "delete_" + uniqueId + "@example.com", "pass_" + uniqueId.substring(0,4));
        Usuario createdUser = userDAO.create(userToCreate);
        assertNotNull(createdUser, "User should be created for deletion.");
        assertTrue(createdUser.getId() > 0, "Created user should have an ID.");

        boolean isDeleted = userDAO.delete(createdUser.getId());

        assertTrue(isDeleted, "The user should have been deleted successfully.");

        Usuario deletedUser = userDAO.findById(createdUser.getId());
        assertNull(deletedUser, "The deleted user should not be found.");
    }

    @Test
    void getAllUsers() throws SQLException {
        List<Usuario> users = userDAO.getAllUsers();

        assertNotNull(users, "The list of users should not be null.");
    }

}