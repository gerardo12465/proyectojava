package org.PracticaEsfe.Persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.PracticaEsfe.Dominio.Autor;

import org.PracticaEsfe.Persistence.AutorDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


class AutoresDAOTest {

    private AutorDAO autorDAO;

    @BeforeEach
    void setUp() {
        autorDAO = new AutorDAO();
    }

    private void assertAutorCreationSuccess(Autor autor) throws SQLException {
        boolean inserted = autorDAO.insertarAutor(autor);
        assertTrue(inserted, "La inserción del autor debería haber sido exitosa.");
    }

    @Test
    void createAutor() throws SQLException {
        Autor autor = new Autor(0, "Gabriel García Márquez", "Colombiana");
        assertAutorCreationSuccess(autor);
    }

    /*
    @Test
    void readAutorById() throws SQLException {
        Autor autorParaBuscar = new Autor(0, "Jane Austen", "Británica");
        autorDAO.insertarAutor(autorParaBuscar);

        Autor autorLeido = autorDAO.obtenerAutorPorId(1);

        assertNotNull(autorLeido, "El autor leído no debería ser nulo.");
    }

    @Test
    void updateAutor() throws SQLException {
        Autor autorOriginal = new Autor(0, "Isaac Asimov", "Rusa");
        autorDAO.insertarAutor(autorOriginal);

        Autor autorDesdeDB = autorDAO.obtenerAutorPorNombreCompleto("Isaac Asimov");
        assertNotNull(autorDesdeDB);

        autorDesdeDB.setNacionalidad("Estadounidense");
        autorDesdeDB.setNombreCompleto("Isaac Asimov (Actualizado)");

        boolean isUpdated = autorDAO.actualizarAutor(autorDesdeDB);
        assertTrue(isUpdated, "El autor debería haberse actualizado correctamente.");

        Autor autorActualizado = autorDAO.obtenerAutorPorId(autorDesdeDB.getId());
        assertNotNull(autorActualizado);
        assertEquals("Estadounidense", autorActualizado.getNacionalidad());
        assertEquals("Isaac Asimov (Actualizado)", autorActualizado.getNombreCompleto());
    }

    @Test
    void deleteAutor() throws SQLException {
        Autor autorParaBorrar = new Autor(0, "Virginia Woolf", "Británica");
        autorDAO.insertarAutor(autorParaBorrar);
        Autor autorDesdeDB = autorDAO.obtenerAutorPorNombreCompleto("Virginia Woolf");
        assertNotNull(autorDesdeDB);

        boolean isDeleted = autorDAO.eliminarAutor(autorDesdeDB.getId());
        assertTrue(isDeleted, "El autor debería haberse eliminado correctamente.");

        Autor autorEliminado = autorDAO.obtenerAutorPorId(autorDesdeDB.getId());
        assertNull(autorEliminado, "El autor eliminado no debería ser encontrado.");
    }

    @Test
    void getAllAutores() throws SQLException {
        List<Autor> autores = autorDAO.obtenerTodosLosAutores();

        assertNotNull(autores, "La lista de autores no debería ser nula.");
    }
    */
}
