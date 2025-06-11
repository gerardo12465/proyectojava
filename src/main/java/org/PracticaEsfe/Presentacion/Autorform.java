package org.PracticaEsfe.Presentacion; // Puedes crear un paquete 'ui' o 'gui' si lo prefieres

import org.PracticaEsfe.Persistence.AutorDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class Autorform extends JFrame {
    private JTextField txtId;
    private JTextField txtNombreCompleto;
    private JTextField txtNacionalidad;
    private JButton btnGuardar;
    private JButton btnBuscar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar; // Nuevo botón para limpiar campos
    private JButton btnVerTodos; // Nuevo botón para ver todos los autores
    private JTable autorTable;
    private DefaultTableModel tableModel;

    private AutorDAO autorDAO; // Instancia del DAO para interactuar con la base de datos

    public Autorform() {
        // Configuración básica del formulario
        setTitle("Gestión de Autores");
        setSize(800, 600); // Aumentamos el tamaño para la tabla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla

        autorDAO = new AutorDAO(); // Inicializa el DAO

        // Panel para los campos de entrada y botones
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Datos del Autor"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo ID (solo lectura para mostrar, no para editar manualmente)
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(15);
        txtId.setEditable(false); // No se puede editar el ID directamente
        inputPanel.add(txtId, gbc);

        // Campo Nombre Completo
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 1;
        txtNombreCompleto = new JTextField(25);
        inputPanel.add(txtNombreCompleto, gbc);

        // Campo Nacionalidad
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Nacionalidad:"), gbc);
        gbc.gridx = 1;
        txtNacionalidad = new JTextField(20);
        inputPanel.add(txtNacionalidad, gbc);

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

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS)); // Apila verticalmente
        topPanel.add(inputPanel); // Primero el panel de entrada
        topPanel.add(buttonPanel);

        // Configuración de la tabla
        String[] columnNames = {"ID", "Nombre Completo", "Nacionalidad"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hace que las celdas de la tabla no sean editables
            }
        };
        autorTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(autorTable); // Para que la tabla sea scrollable

        // Añadir paneles al frame principal
        setLayout(new BorderLayout(10, 10)); // Espaciado entre componentes principales
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);


        // Acción de los botones
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarAutor();
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarAutor();
            }
        });

        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarAutor();
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarAutor();
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
                cargarTodosAutor();
            }
        });

        // Cargar todos los autores al iniciar la aplicación
        cargarTodosAutor();
    }

    // --- Métodos para las operaciones CRUD ---

    private void guardarAutor() {
        String nombre = txtNombreCompleto.getText().trim();
        String nacionalidad = txtNacionalidad.getText().trim();

        if (nombre.isEmpty() || nacionalidad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Autor autor = new Autor(nombre, nacionalidad);
        try {
            if (autorDAO.insertarAutor(autor)) {
                JOptionPane.showMessageDialog(this, "Autor guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarTodosAutor(); // Refresca la tabla
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el autor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos al guardar: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void buscarAutor() {
        String idText = JOptionPane.showInputDialog(this, "Ingrese el ID del autor a buscar:");
        if (idText == null || idText.trim().isEmpty()) {
            return; // El usuario canceló o no ingresó nada
        }

        try {
            int id = Integer.parseInt(idText.trim());
            Autor autor = autorDAO.obtenerAutorPorId(id);
            if (autor != null) {
                txtId.setText(String.valueOf(autor.getId()));
                txtNombreCompleto.setText(autor.getNombreCompleto());
                txtNacionalidad.setText(autor.getNacionalidad());
                JOptionPane.showMessageDialog(this, "Autor encontrado: " + autor.getNombreCompleto(), "Autor Encontrado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "Autor con ID " + id + " no encontrado.", "No Encontrado", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID válido (número entero).", "ID Inválido", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos al buscar: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void actualizarAutor() {
        String idText = txtId.getText().trim();
        String nombre = txtNombreCompleto.getText().trim();
        String nacionalidad = txtNacionalidad.getText().trim();

        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, busque un autor primero o ingrese un ID.", "ID Necesario", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (nombre.isEmpty() || nacionalidad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos para actualizar.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            Autor autor = new Autor(id, nombre, nacionalidad);
            if (autorDAO.actualizarAutor(autor)) {
                JOptionPane.showMessageDialog(this, "Autor actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarTodosAutor(); // Refresca la tabla
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el autor. Asegúrese que el ID exista.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero válido.", "ID Inválido", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos al actualizar: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void eliminarAutor() {
        String idText = txtId.getText().trim(); // Puedes obtener el ID del campo o pedirlo
        if (idText.isEmpty()) {
            // Si el campo ID está vacío, pedimos al usuario que lo ingrese
            idText = JOptionPane.showInputDialog(this, "Ingrese el ID del autor a eliminar:");
            if (idText == null || idText.trim().isEmpty()) {
                return; // El usuario canceló o no ingresó nada
            }
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este autor?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.NO_OPTION) {
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            if (autorDAO.eliminarAutor(id)) {
                JOptionPane.showMessageDialog(this, "Autor eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarTodosAutor(); // Refresca la tabla
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el autor. Asegúrese que el ID exista.", "Error", JOptionPane.ERROR_MESSAGE);
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
        txtNombreCompleto.setText("");
        txtNacionalidad.setText("");
    }

    private void cargarTodosAutor() {
        // Limpia el modelo de la tabla
        tableModel.setRowCount(0);
        try {
            List<Autor> autores = autorDAO.obtenerTodosLosAutores();
            if (autores.isEmpty()) {
                tableModel.addRow(new Object[]{"No hay autores", "", ""}); // Mensaje en la tabla si no hay datos
            } else {
                for (Autor autor : autores) {
                    Object[] rowData = {autor.getId(), autor.getNombreCompleto(), autor.getNacionalidad()};
                    tableModel.addRow(rowData);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar autores: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // --- Método principal para ejecutar el formulario ---
    public static void main(String[] args) {
        // Asegura que la UI se cree en el hilo de despacho de eventos de Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Autorform().setVisible(true);
            }
        });
    }
}
