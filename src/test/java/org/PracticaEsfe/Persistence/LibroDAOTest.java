package org.PracticaEsfe.Persistence;

import org.junit.jupiter.api.AfterEach; // Import para la limpieza después de cada test
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.PracticaEsfe.Dominio.Autor; // Necesario para crear autores de prueba
import org.PracticaEsfe.Dominio.Libro; // Importa tu clase Libro del dominio

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba unitaria para LibroDAO.
 * Utiliza JUnit 5 para verificar el comportamiento de las operaciones CRUD
 * (Crear, Leer, Actualizar, Eliminar) en la base de datos para la entidad Libro.
 */
class LibroDAOTest {

    private LibroDAO libroDAO;
    private AutorDAO autorDAO; // Instancia para gestionar autores de prueba
    private Autor testAutor; // Para almacenar el autor creado para cada test

    /**
     * Este método se ejecuta antes de cada prueba (@Test).
     * Se utiliza para inicializar los DAOs y crear un autor de prueba,
     * asegurando que cada prueba comience con un estado limpio y consistente.
     */
    @BeforeEach
    void setUp(){
        libroDAO = new LibroDAO();
        autorDAO = new AutorDAO();

        // Limpiar todas las tablas antes de cada test para asegurar un estado limpio
        try {
            libroDAO.deleteAllLibros();
            autorDAO.deleteAllAutores();
        } catch (SQLException e) {
            System.err.println("Error al limpiar la base de datos antes de la prueba: " + e.getMessage());
            // Si la limpieza falla, no podemos confiar en los tests, así que se lanza una excepción.
            fail("Falló la limpieza de la base de datos antes de la prueba: " + e.getMessage());
        }

        // Crear un autor de prueba antes de cada test para satisfacer la restricción de clave foránea
        try {
            String uniqueAuthorName = "Test Author " + UUID.randomUUID().toString().substring(0, 8);
            testAutor = autorDAO.insertarAutor(new Autor(0, uniqueAuthorName, "Testland"));
            assertNotNull(testAutor, "El autor de prueba no debería ser nulo.");
            assertTrue(testAutor.getId() > 0, "El autor de prueba debería tener un ID válido.");
        } catch (SQLException e) {
            System.err.println("Error al crear el autor de prueba: " + e.getMessage());
            fail("Falló la creación del autor de prueba: " + e.getMessage());
        }
    }

    /**
     * Este método se ejecuta después de cada prueba (@Test).
     * Se utiliza para limpiar los datos de prueba insertados por cada test.
     */
    @AfterEach
    void tearDown() {
        // Limpiar la base de datos después de cada test
        try {
            libroDAO.deleteAllLibros();
            autorDAO.deleteAllAutores();
        } catch (SQLException e) {
            System.err.println("Error al limpiar la base de datos después de la prueba: " + e.getMessage());
            // No lanzar una excepción aquí a menos que quieras que fallen los tests si la limpieza falla.
        }
        testAutor = null; // Resetear la referencia al autor de prueba
    }

    /**
     * Prueba el método 'create' de LibroDAO.
     * Verifica que un nuevo libro se pueda persistir correctamente en la base de datos
     * y que los datos devueltos son correctos, incluyendo el ID generado por la DB.
     * @throws SQLException Si ocurre un error de SQL.
     */
    @Test
    void createLibro() throws SQLException {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String titulo = "El Libro de Prueba " + uniqueId;
        Date fechaPublicacion = Date.valueOf(LocalDate.now());

        // Usamos el ID del autor de prueba creado en setUp()
        Libro libro = new Libro(0, titulo, fechaPublicacion, testAutor.getId());

        Libro createdLibro = libroDAO.create(libro);

        assertNotNull(createdLibro, "El libro creado no debería ser nulo después de la inserción.");
        assertTrue(createdLibro.getId() > 0, "El ID del libro creado debe ser mayor que 0 (autoincrementado).");
        assertEquals(titulo, createdLibro.getTitulo(), "El título del libro creado debe ser igual al generado.");
        assertEquals(fechaPublicacion, createdLibro.getFechaPublicacion(), "La fecha de publicación debe coincidir.");
        assertEquals(testAutor.getId(), createdLibro.getIdAutor(), "El ID del autor del libro creado debe ser igual al del autor de prueba.");
    }

    /**
     * Prueba el método 'findById' de LibroDAO.
     * Verifica que un libro existente pueda ser recuperado correctamente por su ID.
     * @throws SQLException Si ocurre un error de SQL.
     */
    @Test
    void findLibroById() throws SQLException {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        Date fechaPublicacion = Date.valueOf(LocalDate.now().minusDays(1));
        Libro bookToCreate = new Libro(0, "Libro para Buscar " + uniqueId, fechaPublicacion, testAutor.getId());
        Libro createdLibro = libroDAO.create(bookToCreate);
        assertNotNull(createdLibro, "El libro debería ser creado para la prueba de lectura.");
        assertTrue(createdLibro.getId() > 0, "El libro creado debería tener un ID.");

        Libro foundLibro = libroDAO.findById(createdLibro.getId());

        assertNotNull(foundLibro, "El libro encontrado no debería ser nulo.");
        assertEquals(createdLibro.getId(), foundLibro.getId(), "Los IDs deben coincidir.");
        assertEquals(createdLibro.getTitulo(), foundLibro.getTitulo(), "Los títulos deben coincidir.");
        assertEquals(createdLibro.getFechaPublicacion(), foundLibro.getFechaPublicacion(), "Las fechas de publicación deben coincidir.");
        assertEquals(createdLibro.getIdAutor(), foundLibro.getIdAutor(), "Los IDs de autor deben coincidir.");
    }

    /**
     * Prueba el método 'update' de LibroDAO.
     * Verifica que un libro existente pueda ser actualizado correctamente en la base de datos.
     * @throws SQLException Si ocurre un error de SQL.
     */
    @Test
    void updateLibro() throws SQLException {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        Date originalFechaPublicacion = Date.valueOf(LocalDate.of(2020, 1, 1));
        Libro bookToCreate = new Libro(0, "Libro para Actualizar " + uniqueId, originalFechaPublicacion, testAutor.getId());
        Libro createdLibro = libroDAO.create(bookToCreate);
        assertNotNull(createdLibro, "El libro debería ser creado para la prueba de actualización.");
        assertTrue(createdLibro.getId() > 0, "El libro creado debería tener un ID.");

        String updatedTitulo = "Título Actualizado " + uniqueId;
        Date updatedFechaPublicacion = Date.valueOf(LocalDate.of(2023, 5, 10));

        createdLibro.setTitulo(updatedTitulo);
        createdLibro.setFechaPublicacion(updatedFechaPublicacion);

        boolean isUpdated = libroDAO.update(createdLibro);

        assertTrue(isUpdated, "El libro debería haberse actualizado correctamente.");

        Libro updatedLibro = libroDAO.findById(createdLibro.getId());
        assertNotNull(updatedLibro);
        assertEquals(updatedTitulo, updatedLibro.getTitulo(), "El título actualizado debe coincidir.");
        assertEquals(updatedFechaPublicacion, updatedLibro.getFechaPublicacion(), "La fecha de publicación actualizada debe coincidir.");
        assertEquals(testAutor.getId(), updatedLibro.getIdAutor(), "El autor ID actualizado debe coincidir con el original de prueba.");
    }

    /**
     * Prueba el método 'delete' de LibroDAO.
     * Verifica que un libro pueda ser eliminado correctamente de la base de datos.
     * @throws SQLException Si ocurre un error de SQL.
     */
    @Test
    void deleteLibro() throws SQLException {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        Libro bookToCreate = new Libro(0, "Libro para Eliminar " + uniqueId, Date.valueOf(LocalDate.of(2021, 7, 20)), testAutor.getId());
        Libro createdLibro = libroDAO.create(bookToCreate);
        assertNotNull(createdLibro, "El libro debería ser creado para la prueba de eliminación.");
        assertTrue(createdLibro.getId() > 0, "El libro creado debería tener un ID.");

        boolean isDeleted = libroDAO.delete(createdLibro.getId());

        assertTrue(isDeleted, "El libro debería haberse eliminado correctamente.");

        Libro deletedLibro = libroDAO.findById(createdLibro.getId());
        assertNull(deletedLibro, "El libro eliminado no debería ser encontrado.");
    }

    /**
     * Prueba el método 'getAllLibros' de LibroDAO.
     * Verifica que se puedan obtener todos los libros de la base de datos.
     * @throws SQLException Si ocurre un error de SQL.
     */
    @Test
    void getAllLibros() throws SQLException {
        // Se insertan dos libros para asegurar que la prueba de "obtener todos" tenga datos.
        String uniqueId1 = UUID.randomUUID().toString().substring(0, 4);
        libroDAO.create(new Libro(0, "Libro A " + uniqueId1, Date.valueOf(LocalDate.of(2019, 3, 15)), testAutor.getId()));
        String uniqueId2 = UUID.randomUUID().toString().substring(0, 4);
        libroDAO.create(new Libro(0, "Libro B " + uniqueId2, Date.valueOf(LocalDate.of(2022, 11, 1)), testAutor.getId()));

        List<Libro> libros = libroDAO.getAllLibros();

        assertNotNull(libros, "La lista de libros no debería ser nula.");
        assertTrue(libros.size() >= 2, "Debería haber al menos dos libros en la lista después de la inserción de prueba.");
    }
}