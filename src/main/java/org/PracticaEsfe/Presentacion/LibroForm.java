package org.PracticaEsfe.Presentacion;

import org.PracticaEsfe.Persistence.LibroDAO; // Importar LibroDAO
import org.PracticaEsfe.Dominio.Libro;     // Importar Libro del dominio
import org.PracticaEsfe.Dominio.Autor;     // Posiblemente necesario para mostrar el autor en la tabla o selección
import org.PracticaEsfe.Persistence.AutorDAO; // Posiblemente necesario para obtener nombres de autor

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date; // Para manejar java.sql.Date para FechaPublicacion
import java.sql.SQLException;
import java.time.LocalDate; // Para facilitar la creación de fechas
import java.util.List;

public class LibroForm extends JDialog {
    private JPanel MainPanel; // Renombrado a MainPanel si es el panel principal de JDialog
    private JTextField txtId; // Campo para el ID del libro (no editable)
    private JTextField txtTitulo; // Campo para el título del libro
    private JTextField txtFechaPublicacion; // Campo para la fecha de publicación
    private JTextField txtIdAutor; // Campo para el ID del autor
    private JButton btnGuardar;
    private JButton btnBuscar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnVerTodos;
    private JTable libroTable;
    private DefaultTableModel tableModel;

    private LibroDAO libroDAO; // Instancia del DAO para interactuar con la base de datos
    private AutorDAO autorDAO; // Opcional: para obtener nombres de autores si se muestran en la tabla

    public LibroForm() {
        setTitle("Gestión de Libros");
        setSize(900, 700); // Aumentamos el tamaño para más campos y la tabla
        setModal(true); // Hace el JDialog modal, bloqueando la ventana padre
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana
        setLocationRelativeTo(null);

        libroDAO = new LibroDAO(); // Inicializa el DAO de libros
        autorDAO = new AutorDAO(); // Inicializa el DAO de autores (si se necesita para mostrar nombres)

        // Configuración de los paneles
        MainPanel = new JPanel(new BorderLayout(10, 10));
        MainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margen alrededor del panel

        // Panel de entrada de datos
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Datos del Libro"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo ID
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(15);
        txtId.setEditable(false);
        inputPanel.add(txtId, gbc);

        // Campo Título
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        txtTitulo = new JTextField(30);
        inputPanel.add(txtTitulo, gbc);

        // Campo Fecha Publicación
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Fecha Publicación (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        txtFechaPublicacion = new JTextField(20);
        inputPanel.add(txtFechaPublicacion, gbc);

        // Campo ID Autor
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("ID Autor:"), gbc);
        gbc.gridx = 1;
        txtIdAutor = new JTextField(15);
        inputPanel.add(txtIdAutor, gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnGuardar = new JButton("Guardar");
        btnBuscar = new JButton("Buscar por ID");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        btnVerTodos = new JButton("Ver Todos");

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnBuscar);
        buttonPanel.add(btnActualizar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnLimpiar);
        buttonPanel.add(btnVerTodos);

        // Combinar inputPanel y buttonPanel en un panel superior
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(inputPanel);
        topPanel.add(buttonPanel);

        MainPanel.add(topPanel, BorderLayout.NORTH);

        // Configuración de la tabla de libros
        String[] columnNames = {"ID", "Título", "Fecha Publicación", "ID Autor", "Nombre Autor"}; // Añadimos Nombre Autor
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Las celdas de la tabla no son editables
            }
        };
        libroTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(libroTable);
        MainPanel.add(scrollPane, BorderLayout.CENTER);

        // Añadir MainPanel al JDialog
        add(MainPanel);


        // Acciones de los botones
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarLibro();
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarLibro();
            }
        });

        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarLibro();
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarLibro();
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });

        btnVerTodos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarTodosLibros();
            }
        });

        // Cargar todos los libros al iniciar el formulario
        cargarTodosLibros();
    }

    // --- Métodos para las operaciones CRUD ---

    private void guardarLibro() {
        String titulo = txtTitulo.getText().trim();
        String fechaPublicacionStr = txtFechaPublicacion.getText().trim();
        String idAutorStr = txtIdAutor.getText().trim();

        if (titulo.isEmpty() || fechaPublicacionStr.isEmpty() || idAutorStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Date fechaPublicacion = Date.valueOf(LocalDate.parse(fechaPublicacionStr)); // Convierte String a Date
            int idAutor = Integer.parseInt(idAutorStr);

            Libro libro = new Libro(titulo, fechaPublicacion, idAutor); // ID 0 para nuevo libro
            Libro createdLibro = libroDAO.create(libro); // El DAO debería devolver el libro con el ID generado

            if (createdLibro != null && createdLibro.getId() > 0) {
                JOptionPane.showMessageDialog(this, "Libro guardado exitosamente con ID: " + createdLibro.getId(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarTodosLibros(); // Refresca la tabla
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el libro.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID Autor inválido. Por favor, ingrese un número entero.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use YYYY-MM-DD.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos al guardar: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void buscarLibro() {
        String idText = JOptionPane.showInputDialog(this, "Ingrese el ID del libro a buscar:");
        if (idText == null || idText.trim().isEmpty()) {
            return;
        }

        try {
            int id = Integer.parseInt(idText.trim());
            Libro libro = libroDAO.findById(id);
            if (libro != null) {
                txtId.setText(String.valueOf(libro.getId()));
                txtTitulo.setText(libro.getTitulo());
                txtFechaPublicacion.setText(libro.getFechaPublicacion().toString());
                txtIdAutor.setText(String.valueOf(libro.getIdAutor()));
                JOptionPane.showMessageDialog(this, "Libro encontrado: " + libro.getTitulo(), "Libro Encontrado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "Libro con ID " + id + " no encontrado.", "No Encontrado", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID válido (número entero).", "ID Inválido", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos al buscar: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void actualizarLibro() {
        String idText = txtId.getText().trim();
        String titulo = txtTitulo.getText().trim();
        String fechaPublicacionStr = txtFechaPublicacion.getText().trim();
        String idAutorStr = txtIdAutor.getText().trim();

        if (idText.isEmpty() || titulo.isEmpty() || fechaPublicacionStr.isEmpty() || idAutorStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos y asegúrese de que el ID esté presente.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            Date fechaPublicacion = Date.valueOf(LocalDate.parse(fechaPublicacionStr));
            int idAutor = Integer.parseInt(idAutorStr);

            Libro libro = new Libro(id, titulo, fechaPublicacion, idAutor);
            boolean updated = libroDAO.update(libro);

            if (updated) {
                JOptionPane.showMessageDialog(this, "Libro actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarTodosLibros(); // Refresca la tabla
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el libro. Asegúrese que el ID exista.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID o ID Autor inválido. Por favor, ingrese números enteros.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use YYYY-MM-DD.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos al actualizar: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void eliminarLibro() {
        String idText = txtId.getText().trim();
        if (idText.isEmpty()) {
            idText = JOptionPane.showInputDialog(this, "Ingrese el ID del libro a eliminar:");
            if (idText == null || idText.trim().isEmpty()) {
                return;
            }
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este libro?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.NO_OPTION) {
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            boolean deleted = libroDAO.delete(id);

            if (deleted) {
                JOptionPane.showMessageDialog(this, "Libro eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarTodosLibros(); // Refresca la tabla
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el libro. Asegúrese que el ID exista.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID válido (número entero).", "ID Inválido", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos al eliminar: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtTitulo.setText("");
        txtFechaPublicacion.setText("");
        txtIdAutor.setText("");
    }

    private void cargarTodosLibros() {
        tableModel.setRowCount(0); // Limpia el modelo de la tabla
        try {
            List<Libro> libros = libroDAO.getAllLibros();
            if (libros.isEmpty()) {
                tableModel.addRow(new Object[]{"No hay libros", "", "", "", ""}); // Mensaje en la tabla
            } else {
                for (Libro libro : libros) {
                    // Opcional: Obtener el nombre del autor para mostrarlo en la tabla
                    String nombreAutor = "Desconocido";
                    try {
                        Autor autor = autorDAO.obtenerAutorPorId(libro.getIdAutor());
                        if (autor != null) {
                            nombreAutor = autor.getNombreCompleto();
                        }
                    } catch (SQLException e) {
                        System.err.println("Error al obtener el nombre del autor para ID " + libro.getIdAutor() + ": " + e.getMessage());
                        // No es crítico para el funcionamiento de LibroForm, pero se registra el error.
                    }

                    Object[] rowData = {
                            libro.getId(),
                            libro.getTitulo(),
                            libro.getFechaPublicacion(),
                            libro.getIdAutor(),
                            nombreAutor // Muestra el nombre del autor
                    };
                    tableModel.addRow(rowData);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar libros: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Método principal para ejecutar el formulario
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LibroForm().setVisible(true);
            }
        });
    }
}