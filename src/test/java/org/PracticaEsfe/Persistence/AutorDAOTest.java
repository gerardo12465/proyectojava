package org.PracticaEsfe.Persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.PracticaEsfe.Dominio.Autor;

import org.PracticaEsfe.Persistence.AutorDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class AutoresDAOTest {

    private AutorDAO autorDAO;

    @BeforeEach
    void setUp() {
        autorDAO = new AutorDAO();
    }

    private Autor assertAutorCreationSuccess(Autor autor) throws SQLException {
        Autor createdAutor = autorDAO.insertarAutor(autor);

        assertNotNull(createdAutor, "El autor creado no debería ser nulo.");
        assertTrue(createdAutor.getId() > 0, "El ID del autor creado debe ser mayor que 0 (autoincrementado).");
        assertEquals(autor.getNombreCompleto(), createdAutor.getNombreCompleto(), "El nombre completo debe coincidir.");
        assertEquals(autor.getNacionalidad(), createdAutor.getNacionalidad(), "La nacionalidad debe coincidir.");

        return createdAutor;
    }

    @Test
    void createAutor() throws SQLException {
        String uniqueName = "Autor Test " + UUID.randomUUID().toString().substring(0, 8);
        Autor autor = new Autor(0, uniqueName, "Colombiana");
        Autor createdAutor = assertAutorCreationSuccess(autor);
    }



    @Test
    void readAutorById() throws SQLException {
        String uniqueName = "Autor Read Test " + UUID.randomUUID().toString().substring(0, 8);
        Autor autorParaCrear = new Autor(0, uniqueName, "Británica");
        Autor autorCreado = autorDAO.insertarAutor(autorParaCrear);
        assertNotNull(autorCreado, "El autor debería ser creado para la prueba de lectura.");
        assertTrue(autorCreado.getId() > 0, "El autor creado debería tener un ID.");

        Autor autorLeido = autorDAO.obtenerAutorPorId(autorCreado.getId());

        assertNotNull(autorLeido, "El autor leído no debería ser nulo.");
        assertEquals(autorCreado.getId(), autorLeido.getId(), "Los IDs deben coincidir.");
        assertEquals(autorCreado.getNombreCompleto(), autorLeido.getNombreCompleto(), "Los nombres deben coincidir.");
        assertEquals(autorCreado.getNacionalidad(), autorLeido.getNacionalidad(), "Las nacionalidades deben coincidir.");

    }

    @Test
    void updateAutor() throws SQLException {
        String uniqueName = "Autor Update Test " + UUID.randomUUID().toString().substring(0, 8);
        Autor autorParaCrear = new Autor(0, uniqueName, "Rusa");
        Autor autorOriginal = autorDAO.insertarAutor(autorParaCrear);
        assertNotNull(autorOriginal, "El autor debería ser creado para la prueba de actualización.");
        assertTrue(autorOriginal.getId() > 0, "El autor creado debería tener un ID.");

        String updatedName = "Isaac Asimov (Actualizado) " + UUID.randomUUID().toString().substring(0, 4);
        autorOriginal.setNacionalidad("Estadounidense");
        autorOriginal.setNombreCompleto(updatedName);

        boolean isUpdated = autorDAO.actualizarAutor(autorOriginal);
        assertTrue(isUpdated, "El autor debería haberse actualizado correctamente.");

        Autor autorActualizado = autorDAO.obtenerAutorPorId(autorOriginal.getId());
        assertNotNull(autorActualizado);
        assertEquals(updatedName, autorActualizado.getNombreCompleto(), "El nombre completo actualizado debe coincidir.");
        assertEquals("Estadounidense", autorActualizado.getNacionalidad(), "La nacionalidad actualizada debe coincidir.");

    }

    @Test
    void deleteAutor() throws SQLException {
        String uniqueName = "Autor Delete Test " + UUID.randomUUID().toString().substring(0, 8);
        Autor autorParaBorrar = new Autor(0, uniqueName, "Británica");
        Autor createdAutor = autorDAO.insertarAutor(autorParaBorrar);
        assertNotNull(createdAutor, "El autor debería ser creado para la prueba de eliminación.");
        assertTrue(createdAutor.getId() > 0, "El autor creado debería tener un ID.");

        boolean isDeleted = autorDAO.eliminarAutor(createdAutor.getId());
        assertTrue(isDeleted, "El autor debería haberse eliminado correctamente.");

        Autor autorEliminado = autorDAO.obtenerAutorPorId(createdAutor.getId());
        assertNull(autorEliminado, "El autor eliminado no debería ser encontrado.");
    }

    @Test
    void getAllAutores() throws SQLException {
        List<Autor> autores = autorDAO.obtenerTodosLosAutores();
        assertNotNull(autores, "La lista de autores no debería ser nula.");
    }
//jazmin lue, hola buenas noches participe en el proyecto
}
