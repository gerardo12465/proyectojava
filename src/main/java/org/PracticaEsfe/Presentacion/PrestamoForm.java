package org.PracticaEsfe.Presentacion;

import org.PracticaEsfe.Dominio.Libro;
import org.PracticaEsfe.Dominio.Prestamo;
import org.PracticaEsfe.Dominio.Usuario;
import org.PracticaEsfe.Persistence.LibroDAO;
import org.PracticaEsfe.Persistence.PrestamoDAO;
import org.PracticaEsfe.Persistence.UserDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrestamoForm extends JFrame {
    private PrestamoDAO prestamoDAO;
    private UserDAO userDAO;
    private LibroDAO libroDAO;
    private DefaultTableModel tableModel;
    private JTable prestamosTable;

    private JTextField txtId;
    private JComboBox<String> cmbUsuario;
    private JComboBox<String> cmbLibro;
    private JTextField txtFechaPrestamo;
    private JTextField txtFechaDevolucion;

    private Map<String, Integer> usuarioMap;
    private Map<String, Integer> libroMap;

    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnMarcarDevuelto;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public PrestamoForm() {
        prestamoDAO = new PrestamoDAO();
        userDAO = new UserDAO();
        libroDAO = new LibroDAO();
        usuarioMap = new HashMap<>();
        libroMap = new HashMap<>();
        initComponents();
        loadUsuariosToComboBox();
        loadLibrosToComboBox();
        loadPrestamos();
    }

    private void initComponents() {
        setTitle("Gestión de Préstamos");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtId = new JTextField();
        txtId.setEditable(false);
        cmbUsuario = new JComboBox<>();
        cmbLibro = new JComboBox<>();
        txtFechaPrestamo = new JTextField();
        txtFechaDevolucion = new JTextField();

        inputPanel.add(new JLabel("ID del Préstamo:"));
        inputPanel.add(txtId);
        inputPanel.add(new JLabel("Usuario:"));
        inputPanel.add(cmbUsuario);
        inputPanel.add(new JLabel("Libro:"));
        inputPanel.add(cmbLibro);
        inputPanel.add(new JLabel("Fecha de Préstamo (YYYY-MM-DD):"));
        inputPanel.add(txtFechaPrestamo);
        inputPanel.add(new JLabel("Fecha de Devolución (YYYY-MM-DD):"));
        inputPanel.add(txtFechaDevolucion);

        add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnGuardar = new JButton("Guardar Préstamo");
        btnActualizar = new JButton("Actualizar Préstamo");
        btnEliminar = new JButton("Eliminar Préstamo");
        btnLimpiar = new JButton("Limpiar Campos");
        btnMarcarDevuelto = new JButton("Marcar como Devuelto");

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnActualizar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnLimpiar);
        buttonPanel.add(btnMarcarDevuelto);

        tableModel = new DefaultTableModel(new Object[]{"ID", "ID Usuario", "Nombre Usuario", "ID Libro", "Título Libro", "Fecha Préstamo", "Fecha Devolución"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        prestamosTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(prestamosTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Préstamos"));

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.add(buttonPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarPrestamo());
        btnActualizar.addActionListener(e -> actualizarPrestamo());
        btnEliminar.addActionListener(e -> eliminarPrestamo());
        btnLimpiar.addActionListener(e -> clearFields());
        btnMarcarDevuelto.addActionListener(e -> marcarComoDevuelto());

        prestamosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && prestamosTable.getSelectedRow() != -1) {
                int selectedRow = prestamosTable.getSelectedRow();
                txtId.setText(prestamosTable.getValueAt(selectedRow, 0).toString());
                txtFechaPrestamo.setText(prestamosTable.getValueAt(selectedRow, 5).toString());
                Object fechaDev = prestamosTable.getValueAt(selectedRow, 6);
                txtFechaDevolucion.setText(fechaDev != null ? fechaDev.toString() : "");

                String selectedUserName = prestamosTable.getValueAt(selectedRow, 2).toString();
                cmbUsuario.setSelectedItem(selectedUserName);

                String selectedBookTitle = prestamosTable.getValueAt(selectedRow, 4).toString();
                cmbLibro.setSelectedItem(selectedBookTitle);
            }
        });
    }

    private void loadUsuariosToComboBox() {
        try {
            List<Usuario> usuarios = userDAO.getAllUsers();
            cmbUsuario.removeAllItems();
            usuarioMap.clear();
            for (Usuario user : usuarios) {
                cmbUsuario.addItem(user.getNombre());
                usuarioMap.put(user.getNombre(), user.getId());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios para el ComboBox: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadLibrosToComboBox() {
        try {
            List<Libro> libros = libroDAO.getAllLibros();
            cmbLibro.removeAllItems();
            libroMap.clear();
            for (Libro libro : libros) {
                cmbLibro.addItem(libro.getTitulo());
                libroMap.put(libro.getTitulo(), libro.getId());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar libros para el ComboBox: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadPrestamos() {
        tableModel.setRowCount(0);
        try {
            List<Prestamo> prestamos = prestamoDAO.getAllPrestamos();
            for (Prestamo prestamo : prestamos) {
                String userName = "Desconocido";
                Usuario user = userDAO.findById(prestamo.getIdUsuario());
                if (user != null) {
                    userName = user.getNombre();
                }

                String bookTitle = "Desconocido";
                Libro book = libroDAO.findById(prestamo.getIdLibro());
                if (book != null) {
                    bookTitle = book.getTitulo();
                }

                tableModel.addRow(new Object[]{
                        prestamo.getId(),
                        prestamo.getIdUsuario(),
                        userName,
                        prestamo.getIdLibro(),
                        bookTitle,
                        prestamo.getFechaPrestamo(),
                        prestamo.getFechaDevolucion()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar préstamos: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void guardarPrestamo() {
        if (cmbUsuario.getSelectedItem() == null || cmbLibro.getSelectedItem() == null || txtFechaPrestamo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un usuario, un libro y la fecha de préstamo.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Integer idUsuario = usuarioMap.get(cmbUsuario.getSelectedItem().toString());
            Integer idLibro = libroMap.get(cmbLibro.getSelectedItem().toString());
            Date fechaPrestamo = new Date(dateFormat.parse(txtFechaPrestamo.getText()).getTime());
            Date fechaDevolucion = null;

            if (!txtFechaDevolucion.getText().isEmpty()) {
                fechaDevolucion = new Date(dateFormat.parse(txtFechaDevolucion.getText()).getTime());
            }

            if (idUsuario == null || idLibro == null) {
                JOptionPane.showMessageDialog(this, "Usuario o libro no válidos seleccionados.", "Error de Selección", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Prestamo prestamo = new Prestamo(idUsuario, idLibro, fechaPrestamo, fechaDevolucion);
            Prestamo createdPrestamo = prestamoDAO.create(prestamo);

            if (createdPrestamo != null) {
                JOptionPane.showMessageDialog(this, "Préstamo guardado exitosamente con ID: " + createdPrestamo.getId(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                loadPrestamos();
                clearFields();
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. UsebeginPath-MM-DD.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar préstamo: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void actualizarPrestamo() {
        if (txtId.getText().isEmpty() || cmbUsuario.getSelectedItem() == null || cmbLibro.getSelectedItem() == null || txtFechaPrestamo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo de la tabla y complete los campos obligatorios.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText());
            Integer idUsuario = usuarioMap.get(cmbUsuario.getSelectedItem().toString());
            Integer idLibro = libroMap.get(cmbLibro.getSelectedItem().toString());
            Date fechaPrestamo = new Date(dateFormat.parse(txtFechaPrestamo.getText()).getTime());
            Date fechaDevolucion = null;

            if (!txtFechaDevolucion.getText().isEmpty()) {
                fechaDevolucion = new Date(dateFormat.parse(txtFechaDevolucion.getText()).getTime());
            }

            if (idUsuario == null || idLibro == null) {
                JOptionPane.showMessageDialog(this, "Usuario o libro no válidos seleccionados.", "Error de Selección", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Prestamo prestamo = new Prestamo(id, idUsuario, idLibro, fechaPrestamo, fechaDevolucion);
            boolean updated = prestamoDAO.update(prestamo);

            if (updated) {
                JOptionPane.showMessageDialog(this, "Préstamo actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                loadPrestamos();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el préstamo. ID no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. UsebeginPath-MM-DD.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar préstamo: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void eliminarPrestamo() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo de la tabla para eliminar.", "Ningún Préstamo Seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar este préstamo?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText());
            try {
                boolean deleted = prestamoDAO.delete(id);
                if (deleted) {
                    JOptionPane.showMessageDialog(this, "Préstamo eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    loadPrestamos();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el préstamo. ID no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar préstamo: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void marcarComoDevuelto() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo de la tabla para marcar como devuelto.", "Ningún Préstamo Seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(txtId.getText());
        try {
            Prestamo prestamo = prestamoDAO.findById(id);
            if (prestamo != null) {
                if (prestamo.getFechaDevolucion() != null) {
                    JOptionPane.showMessageDialog(this, "Este préstamo ya ha sido marcado como devuelto.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                prestamo.setFechaDevolucion(new Date(System.currentTimeMillis()));
                boolean updated = prestamoDAO.update(prestamo);
                if (updated) {
                    JOptionPane.showMessageDialog(this, "Préstamo marcado como devuelto exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    loadPrestamos();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo marcar el préstamo como devuelto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Préstamo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al marcar préstamo como devuelto: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        txtId.setText("");
        cmbUsuario.setSelectedIndex(-1);
        cmbLibro.setSelectedIndex(-1);
        txtFechaPrestamo.setText("");
        txtFechaDevolucion.setText("");
        prestamosTable.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PrestamoForm().setVisible(true);
        });
    }
}