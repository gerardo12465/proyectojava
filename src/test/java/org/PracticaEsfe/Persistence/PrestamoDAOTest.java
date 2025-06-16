package org.PracticaEsfe.Persistence;

import org.PracticaEsfe.Dominio.Libro;
import org.PracticaEsfe.Dominio.Prestamo;
import org.PracticaEsfe.Dominio.Usuario;
import org.PracticaEsfe.Dominio.Autor;
import org.PracticaEsfe.Persistence.ConetionManager;
import org.PracticaEsfe.Persistence.PrestamoDAO;
import org.PracticaEsfe.Persistence.UserDAO;
import org.PracticaEsfe.Persistence.LibroDAO;
import org.PracticaEsfe.Persistence.AutorDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.sql.Date;
import java.util.List;

public class PrestamoDAOTest {

    private PrestamoDAO prestamoDAO;
    private UserDAO userDAO;
    private LibroDAO libroDAO;
    private AutorDAO autorDAO;
    private ConetionManager connectionManager;

    private int testUserId;
    private int testAutorId;
    private int testLibroId;

    @BeforeEach
    void setUp() throws SQLException {
        connectionManager = ConetionManager.getInstance();
        prestamoDAO = new PrestamoDAO();
        userDAO = new UserDAO();
        libroDAO = new LibroDAO();
        autorDAO = new AutorDAO();

        try {
            connectionManager.disconnect();
        } catch (Exception e) {
            // Ignore
        }

        prestamoDAO.deleteAllPrestamos();
        libroDAO.deleteAllLibros();
        userDAO.deleteAllUsers();
        autorDAO.deleteAllAutores();

        Usuario testUser = new Usuario(0, "Test User", "testuser@example.com", "password123");
        Usuario createdUser = userDAO.create(testUser);
        assertNotNull(createdUser, "Test user should be created successfully.");
        testUserId = createdUser.getId();

        Autor testAutor = new Autor("Test Autor", "Nacionalidad Test");
        Autor createdAutor = autorDAO.insertarAutor(testAutor);
        assertNotNull(createdAutor, "Test author should be created successfully.");
        testAutorId = createdAutor.getId();

        Libro testLibro = new Libro("Test Libro", new Date(System.currentTimeMillis()), testAutorId);
        Libro createdLibro = libroDAO.create(testLibro);
        assertNotNull(createdLibro, "Test book should be created successfully.");
        testLibroId = createdLibro.getId();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connectionManager.disconnect();
    }

    @Test
    void testCreatePrestamo() throws SQLException {
        Prestamo prestamo = new Prestamo(testUserId, testLibroId, new Date(System.currentTimeMillis()), null);
        Prestamo createdPrestamo = prestamoDAO.create(prestamo);

        assertNotNull(createdPrestamo, "The created Prestamo should not be null.");
        assertTrue(createdPrestamo.getId() > 0, "Prestamo ID should be greater than 0.");
        assertEquals(testUserId, createdPrestamo.getIdUsuario(), "User ID should match.");
        assertEquals(testLibroId, createdPrestamo.getIdLibro(), "Book ID should match.");
        assertNotNull(createdPrestamo.getFechaPrestamo(), "Loan date should not be null.");
        assertNull(createdPrestamo.getFechaDevolucion(), "Return date should be null initially.");
    }

    @Test
    void testFindById() throws SQLException {
        Prestamo originalPrestamo = new Prestamo(testUserId, testLibroId, new Date(System.currentTimeMillis()), null);
        Prestamo createdPrestamo = prestamoDAO.create(originalPrestamo);
        assertNotNull(createdPrestamo, "Original loan should be created.");

        Prestamo foundPrestamo = prestamoDAO.findById(createdPrestamo.getId());

        assertNotNull(foundPrestamo, "Found Prestamo should not be null.");
        assertEquals(createdPrestamo.getId(), foundPrestamo.getId(), "Prestamo IDs should match.");
        assertEquals(createdPrestamo.getIdUsuario(), foundPrestamo.getIdUsuario(), "User ID should match.");
        assertEquals(createdPrestamo.getIdLibro(), foundPrestamo.getIdLibro(), "Book ID should match.");
        assertEquals(createdPrestamo.getFechaPrestamo().toString(), foundPrestamo.getFechaPrestamo().toString(), "Loan dates should match.");
        assertEquals(createdPrestamo.getFechaDevolucion(), foundPrestamo.getFechaDevolucion(), "Return dates should match (null).");
    }

    @Test
    void testFindByIdNotFound() throws SQLException {
        Prestamo foundPrestamo = prestamoDAO.findById(99999);
        assertNull(foundPrestamo, "Finding a non-existent Prestamo should return null.");
    }

    @Test
    void testUpdatePrestamo() throws SQLException {
        Prestamo originalPrestamo = new Prestamo(testUserId, testLibroId, new Date(System.currentTimeMillis()), null);
        Prestamo createdPrestamo = prestamoDAO.create(originalPrestamo);
        assertNotNull(createdPrestamo, "Original loan should be created.");

        Prestamo prestamoToUpdate = prestamoDAO.findById(createdPrestamo.getId());
        assertNotNull(prestamoToUpdate, "Prestamo to update should be found.");

        Date newReturnDate = new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7));
        prestamoToUpdate.setFechaDevolucion(newReturnDate);

        boolean updated = prestamoDAO.update(prestamoToUpdate);
        assertTrue(updated, "Prestamo should be updated successfully.");

        Prestamo updatedPrestamo = prestamoDAO.findById(createdPrestamo.getId());
        assertNotNull(updatedPrestamo, "Updated Prestamo should not be null.");
        assertEquals(newReturnDate.toString(), updatedPrestamo.getFechaDevolucion().toString(), "Return date should be updated.");
    }

    @Test
    void testDeletePrestamo() throws SQLException {
        Prestamo originalPrestamo = new Prestamo(testUserId, testLibroId, new Date(System.currentTimeMillis()), null);
        Prestamo createdPrestamo = prestamoDAO.create(originalPrestamo);
        assertNotNull(createdPrestamo, "Original loan should be created.");

        boolean deleted = prestamoDAO.delete(createdPrestamo.getId());
        assertTrue(deleted, "Prestamo should be deleted successfully.");

        Prestamo foundPrestamo = prestamoDAO.findById(createdPrestamo.getId());
        assertNull(foundPrestamo, "Deleted Prestamo should not be found.");
    }

    @Test
    void testDeletePrestamoNotFound() throws SQLException {
        boolean deleted = prestamoDAO.delete(99999);
        assertFalse(deleted, "Deleting a non-existent Prestamo should return false.");
    }

    @Test
    void testGetAllPrestamos() throws SQLException {
        prestamoDAO.deleteAllPrestamos();

        Prestamo prestamo1 = new Prestamo(testUserId, testLibroId, new Date(System.currentTimeMillis()), null);
        prestamoDAO.create(prestamo1);

        Usuario testUser2 = new Usuario(0, "Test User 2", "testuser2@example.com", "password456");
        Usuario createdUser2 = userDAO.create(testUser2);
        assertNotNull(createdUser2, "Second test user should be created.");

        Autor testAutor2 = new Autor("Test Autor 2", "Nacionalidad Test 2");
        Autor createdAutor2 = autorDAO.insertarAutor(testAutor2);
        assertNotNull(createdAutor2, "Second test author should be created.");

        Libro testLibro2 = new Libro("Test Libro 2", new Date(System.currentTimeMillis()), createdAutor2.getId());
        Libro createdLibro2 = libroDAO.create(testLibro2);
        assertNotNull(createdLibro2, "Second test book should be created.");


        Prestamo prestamo2 = new Prestamo(createdUser2.getId(), createdLibro2.getId(), new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24)), new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 10)));
        prestamoDAO.create(prestamo2);

        List<Prestamo> prestamos = prestamoDAO.getAllPrestamos();
        assertNotNull(prestamos, "List of prestamos should not be null.");
        assertEquals(2, prestamos.size(), "Should retrieve two prestamos.");

        assertTrue(prestamos.stream().anyMatch(p -> p.getId() == prestamo1.getId()), "List should contain prestamo1.");
        assertTrue(prestamos.stream().anyMatch(p -> p.getId() == prestamo2.getId()), "List should contain prestamo2.");
    }

    @Test
    void testGetAllPrestamosEmpty() throws SQLException {
        prestamoDAO.deleteAllPrestamos();
        List<Prestamo> prestamos = prestamoDAO.getAllPrestamos();
        assertNotNull(prestamos, "List of prestamos should not be null.");
        assertTrue(prestamos.isEmpty(), "List of prestamos should be empty.");
    }
}