package org.PracticaEsfe.Presentacion;

import org.PracticaEsfe.Persistence.AutorDAO;
import org.PracticaEsfe.Dominio.Autor;
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
    private JButton btnLimpiar;
    private JButton btnVerTodos;
    private JTable autorTable;
    private DefaultTableModel tableModel;

    private AutorDAO autorDAO;

    public Autorform() {
        setTitle("Gestión de Autores");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        autorDAO = new AutorDAO();

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Datos del Autor"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(15);
        txtId.setEditable(false);
        inputPanel.add(txtId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 1;
        txtNombreCompleto = new JTextField(25);
        inputPanel.add(txtNombreCompleto, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Nacionalidad:"), gbc);
        gbc.gridx = 1;
        txtNacionalidad = new JTextField(20);
        inputPanel.add(txtNacionalidad, gbc);

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
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(inputPanel);
        topPanel.add(buttonPanel);

        String[] columnNames = {"ID", "Nombre Completo", "Nacionalidad"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        autorTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(autorTable);

        setLayout(new BorderLayout(10, 10));
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);


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

        cargarTodosAutor();
    }

    private void guardarAutor() {
        String nombre = txtNombreCompleto.getText().trim();
        String nacionalidad = txtNacionalidad.getText().trim();

        if (nombre.isEmpty() || nacionalidad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Autor autor = new Autor(0, nombre, nacionalidad);
        try {
            // CORRECCIÓN APLICADA AQUÍ: Se captura el objeto Autor devuelto por insertarAutor
            Autor createdAutor = autorDAO.insertarAutor(autor);

            // Se verifica si la creación fue exitosa (el objeto no es nulo y tiene un ID válido)
            if (createdAutor != null && createdAutor.getId() > 0) {
                JOptionPane.showMessageDialog(this, "Autor guardado exitosamente con ID: " + createdAutor.getId(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarTodosAutor();
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
            return;
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
            boolean updated = autorDAO.actualizarAutor(autor);

            if (updated) {
                JOptionPane.showMessageDialog(this, "Autor actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarTodosAutor();
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
        String idText = txtId.getText().trim();
        if (idText.isEmpty()) {
            idText = JOptionPane.showInputDialog(this, "Ingrese el ID del autor a eliminar:");
            if (idText == null || idText.trim().isEmpty()) {
                return;
            }
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este autor?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.NO_OPTION) {
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            boolean deleted = autorDAO.eliminarAutor(id);

            if (deleted) {
                JOptionPane.showMessageDialog(this, "Autor eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarTodosAutor();
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
        tableModel.setRowCount(0);
        try {
            List<Autor> autores = autorDAO.obtenerTodosLosAutores();
            if (autores.isEmpty()) {
                tableModel.addRow(new Object[]{"No hay autores", "", ""});
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Autorform().setVisible(true);
            }
        });
    }
}