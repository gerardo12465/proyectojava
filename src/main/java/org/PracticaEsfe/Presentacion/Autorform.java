package org.PracticaEsfe.Presentacion;

import org.PracticaEsfe.Dominio.Autor;
import org.PracticaEsfe.Persistence.AutorDAO;
import org.PracticaEsfe.Utilidades.PaletaColores;
import org.PracticaEsfe.Main; // Importar la clase Main

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class Autorform extends JFrame {
    private AutorDAO autorDAO;
    private DefaultTableModel tableModel;
    private JTable autoresTable;

    private JTextField txtId;
    private JTextField txtNombreCompleto;
    private JTextField txtNacionalidad;
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnLimpiar;
    private JButton btnEliminar;

    private ImageIcon backgroundImage;

    public Autorform() {
        autorDAO = new AutorDAO();
        initComponents();
        loadAutores();
    }

    private void initComponents() {
        setTitle("Registro de Autores"); // ¡CAMBIO AQUÍ!
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        // this.setResizable(false); // Eliminado para permitir redimensionar la ventana

        try {
            backgroundImage = new ImageIcon(getClass().getResource("/images/biblioteca.png"));
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen de fondo local 'biblioteca.png' para Autorform: " + e.getMessage());
            // Fallback image or a solid color background
            backgroundImage = new ImageIcon("https://placehold.co/800x600/8B4513/FFFFFF?text=Fondo+no+disponible");
        }

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null && backgroundImage.getImage() != null) {
                    g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(PaletaColores.PRIMARY_BROWN);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        JPanel mainContentWrapper = new JPanel(new BorderLayout());
        mainContentWrapper.setOpaque(false);
        mainContentWrapper.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel formTitle = new JLabel("Registro de Autores", SwingConstants.CENTER); // ¡Y TAMBIÉN AQUÍ!
        formTitle.setFont(new Font("Serif", Font.BOLD, 28));
        formTitle.setForeground(PaletaColores.CREAM_WHITE);
        headerPanel.add(formTitle, BorderLayout.CENTER);

        JButton btnRegresar = new JButton("Regresar al Menú");
        btnRegresar.setBackground(PaletaColores.PRIMARY_BROWN);
        btnRegresar.setForeground(PaletaColores.CREAM_WHITE);
        btnRegresar.setFont(new Font("Serif", Font.BOLD, 14));
        btnRegresar.setFocusPainted(false);
        btnRegresar.setBorder(new EmptyBorder(10, 20, 10, 20));
        headerPanel.add(btnRegresar, BorderLayout.EAST);

        mainContentWrapper.add(headerPanel, BorderLayout.NORTH);

        JPanel inputAndTableSection = new JPanel(new GridBagLayout());
        inputAndTableSection.setOpaque(false);
        GridBagConstraints gbcSection = new GridBagConstraints();
        gbcSection.insets = new Insets(15, 15, 15, 15);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbcInput = new GridBagConstraints();
        gbcInput.insets = new Insets(8, 8, 8, 8);
        gbcInput.fill = GridBagConstraints.HORIZONTAL;
        gbcInput.weightx = 1.0;
        gbcInput.anchor = GridBagConstraints.CENTER;

        JLabel lblId = new JLabel("ID:");
        lblId.setForeground(PaletaColores.CREAM_WHITE);
        lblId.setFont(new Font("Serif", Font.BOLD, 15));
        gbcInput.gridx = 0;
        gbcInput.gridy = 0;
        gbcInput.weightx = 0;
        gbcInput.fill = GridBagConstraints.NONE;
        gbcInput.anchor = GridBagConstraints.EAST;
        inputPanel.add(lblId, gbcInput);

        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(PaletaColores.CREAM_WHITE);
        txtId.setForeground(PaletaColores.TEXT_DARK);
        txtId.setFont(new Font("Serif", Font.PLAIN, 15));
        txtId.setBorder(BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1));
        txtId.setPreferredSize(new Dimension(180, 35));
        gbcInput.gridx = 1;
        gbcInput.gridy = 0;
        gbcInput.weightx = 1.0;
        gbcInput.fill = GridBagConstraints.HORIZONTAL;
        gbcInput.anchor = GridBagConstraints.CENTER;
        inputPanel.add(txtId, gbcInput);

        JLabel lblNombreCompleto = new JLabel("Nombre Completo:");
        lblNombreCompleto.setForeground(PaletaColores.CREAM_WHITE);
        lblNombreCompleto.setFont(new Font("Serif", Font.BOLD, 15));
        gbcInput.gridx = 0;
        gbcInput.gridy = 1;
        gbcInput.weightx = 0;
        gbcInput.fill = GridBagConstraints.NONE;
        gbcInput.anchor = GridBagConstraints.EAST;
        inputPanel.add(lblNombreCompleto, gbcInput);

        txtNombreCompleto = new JTextField();
        txtNombreCompleto.setBackground(PaletaColores.CREAM_WHITE);
        txtNombreCompleto.setForeground(PaletaColores.TEXT_DARK);
        txtNombreCompleto.setFont(new Font("Serif", Font.PLAIN, 15));
        txtNombreCompleto.setBorder(BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1));
        txtNombreCompleto.setPreferredSize(new Dimension(180, 35));
        gbcInput.gridx = 1;
        gbcInput.gridy = 1;
        gbcInput.weightx = 1.0;
        gbcInput.fill = GridBagConstraints.HORIZONTAL;
        gbcInput.anchor = GridBagConstraints.CENTER;
        inputPanel.add(txtNombreCompleto, gbcInput);

        JLabel lblNacionalidad = new JLabel("Nacionalidad:");
        lblNacionalidad.setForeground(PaletaColores.CREAM_WHITE);
        lblNacionalidad.setFont(new Font("Serif", Font.BOLD, 15));
        gbcInput.gridx = 0;
        gbcInput.gridy = 2;
        gbcInput.weightx = 0;
        gbcInput.fill = GridBagConstraints.NONE;
        gbcInput.anchor = GridBagConstraints.EAST;
        inputPanel.add(lblNacionalidad, gbcInput);

        txtNacionalidad = new JTextField();
        txtNacionalidad.setBackground(PaletaColores.CREAM_WHITE);
        txtNacionalidad.setForeground(PaletaColores.TEXT_DARK);
        txtNacionalidad.setFont(new Font("Serif", Font.PLAIN, 15));
        txtNacionalidad.setBorder(BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1));
        txtNacionalidad.setPreferredSize(new Dimension(180, 35));
        gbcInput.gridx = 1;
        gbcInput.gridy = 2;
        gbcInput.weightx = 1.0;
        gbcInput.fill = GridBagConstraints.HORIZONTAL;
        gbcInput.anchor = GridBagConstraints.CENTER;
        inputPanel.add(txtNacionalidad, gbcInput);

        gbcSection.gridx = 0;
        gbcSection.gridy = 0;
        gbcSection.gridwidth = 1;
        gbcSection.weightx = 0.3;
        gbcSection.weighty = 1.0;
        gbcSection.fill = GridBagConstraints.NONE;
        gbcSection.anchor = GridBagConstraints.CENTER;
        inputAndTableSection.add(inputPanel, gbcSection);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre Completo", "Nacionalidad"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        autoresTable = new JTable(tableModel);
        autoresTable.setBackground(PaletaColores.CREAM_WHITE);
        autoresTable.setForeground(PaletaColores.TEXT_DARK);
        autoresTable.setFont(new Font("Serif", Font.PLAIN, 12));
        autoresTable.getTableHeader().setBackground(PaletaColores.PRIMARY_BROWN);
        autoresTable.getTableHeader().setForeground(PaletaColores.CREAM_WHITE);
        autoresTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(autoresTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 2),
                "Lista de Autores",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Serif", Font.BOLD, 16),
                PaletaColores.CREAM_WHITE
        ));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setBackground(PaletaColores.CREAM_WHITE);

        gbcSection.gridx = 1;
        gbcSection.gridy = 0;
        gbcSection.gridwidth = 1;
        gbcSection.weightx = 0.7;
        gbcSection.weighty = 1.0;
        gbcSection.fill = GridBagConstraints.BOTH;
        gbcSection.anchor = GridBagConstraints.NORTHEAST;
        inputAndTableSection.add(scrollPane, gbcSection);

        mainContentWrapper.add(inputAndTableSection, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(PaletaColores.PRIMARY_BROWN);
        btnGuardar.setForeground(PaletaColores.CREAM_WHITE);
        btnGuardar.setFont(new Font("Serif", Font.BOLD, 14));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorder(new EmptyBorder(10, 20, 10, 20));

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setBackground(PaletaColores.PRIMARY_BROWN);
        btnActualizar.setForeground(PaletaColores.CREAM_WHITE);
        btnActualizar.setFont(new Font("Serif", Font.BOLD, 14));
        btnActualizar.setFocusPainted(false);
        btnActualizar.setBorder(new EmptyBorder(10, 20, 10, 20));

        btnLimpiar = new JButton("Limpiar Campos");
        btnLimpiar.setBackground(PaletaColores.PRIMARY_BROWN);
        btnLimpiar.setForeground(PaletaColores.CREAM_WHITE);
        btnLimpiar.setFont(new Font("Serif", Font.BOLD, 14));
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setBorder(new EmptyBorder(10, 20, 10, 20));

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(PaletaColores.PRIMARY_BROWN);
        btnEliminar.setForeground(PaletaColores.CREAM_WHITE);
        btnEliminar.setFont(new Font("Serif", Font.BOLD, 14));
        btnEliminar.setFocusPainted(false);
        btnEliminar.setBorder(new EmptyBorder(10, 20, 10, 20));

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnActualizar);
        buttonPanel.add(btnLimpiar);
        buttonPanel.add(btnEliminar);

        mainContentWrapper.add(buttonPanel, BorderLayout.SOUTH);

        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        mainGbc.weightx = 1.0;
        mainGbc.weighty = 1.0;
        mainGbc.fill = GridBagConstraints.BOTH;
        mainGbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(mainContentWrapper, mainGbc);

        add(backgroundPanel);

        btnGuardar.addActionListener(e -> guardarAutor());
        btnActualizar.addActionListener(e -> actualizarAutor());
        btnLimpiar.addActionListener(e -> clearFields());
        btnEliminar.addActionListener(e -> eliminarAutor());

        btnRegresar.addActionListener(e -> {
            dispose();
            new Main().setVisible(true);
        });

        autoresTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && autoresTable.getSelectedRow() != -1) {
                int selectedRow = autoresTable.getSelectedRow();
                txtId.setText(autoresTable.getValueAt(selectedRow, 0).toString());
                txtNombreCompleto.setText(autoresTable.getValueAt(selectedRow, 1).toString());
                txtNacionalidad.setText(autoresTable.getValueAt(selectedRow, 2).toString());
            }
        });
    }

    private void loadAutores() {
        tableModel.setRowCount(0);
        try {
            List<Autor> autores = autorDAO.obtenerTodosLosAutores();
            for (Autor autor : autores) {
                tableModel.addRow(new Object[]{autor.getId(), autor.getNombreCompleto(), autor.getNacionalidad()});
            }
        } catch (SQLException ex) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Error al cargar autores: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            resetJOptionPaneColors();
            ex.printStackTrace();
        }
    }

    private void guardarAutor() {
        if (txtNombreCompleto.getText().isEmpty() || txtNacionalidad.getText().isEmpty()) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            resetJOptionPaneColors();
            return;
        }

        Autor autor = new Autor(txtNombreCompleto.getText(), txtNacionalidad.getText());
        try {
            Autor createdAutor = autorDAO.insertarAutor(autor);
            if (createdAutor != null) {
                setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                JOptionPane.showMessageDialog(this, "Autor guardado exitosamente con ID: " + createdAutor.getId(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                resetJOptionPaneColors();
                loadAutores();
                clearFields();
            }
        } catch (SQLException ex) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Error al guardar autor: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            resetJOptionPaneColors();
            ex.printStackTrace();
        }
    }

    private void actualizarAutor() {
        if (txtId.getText().isEmpty() || txtNombreCompleto.getText().isEmpty() || txtNacionalidad.getText().isEmpty()) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Seleccione un autor de la tabla y complete todos los campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            resetJOptionPaneColors();
            return;
        }

        int id = Integer.parseInt(txtId.getText());
        Autor autor = new Autor(id, txtNombreCompleto.getText(), txtNacionalidad.getText());
        try {
            boolean updated = autorDAO.actualizarAutor(autor);
            if (updated) {
                setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                JOptionPane.showMessageDialog(this, "Autor actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                resetJOptionPaneColors();
                loadAutores();
                clearFields();
            } else {
                setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el autor. ID no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                resetJOptionPaneColors();
            }
        } catch (SQLException ex) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Error al actualizar autor: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            resetJOptionPaneColors();
            ex.printStackTrace();
        }
    }

    private void eliminarAutor() {
        if (txtId.getText().isEmpty()) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Seleccione un autor de la tabla para eliminar.", "Ningún Autor Seleccionado", JOptionPane.WARNING_MESSAGE);
            resetJOptionPaneColors();
            return;
        }

        setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar este autor?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        resetJOptionPaneColors();

        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText());
            try {
                boolean deleted = autorDAO.eliminarAutor(id);
                if (deleted) {
                    setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                    JOptionPane.showMessageDialog(this, "Autor eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    resetJOptionPaneColors();
                    loadAutores();
                    clearFields();
                } else {
                    setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el autor. ID no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    resetJOptionPaneColors();
                }
            } catch (SQLException ex) {
                setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                JOptionPane.showMessageDialog(this, "Error al eliminar autor: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
                resetJOptionPaneColors();
                ex.printStackTrace();
            }
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtNombreCompleto.setText("");
        txtNacionalidad.setText("");
        autoresTable.clearSelection();
    }

    private void setJOptionPaneColors(Color backgroundColor, Color foregroundColor) {
        UIManager.put("OptionPane.background", backgroundColor);
        UIManager.put("Panel.background", backgroundColor);
        UIManager.put("OptionPane.messageForeground", foregroundColor);
        UIManager.put("Button.background", PaletaColores.PRIMARY_BROWN);
        UIManager.put("Button.foreground", PaletaColores.CREAM_WHITE);
        UIManager.put("Button.border", BorderFactory.createLineBorder(PaletaColores.ACCENT_GOLD, 1));
        UIManager.put("Button.font", new Font("Serif", Font.BOLD, 12));
    }

    private void resetJOptionPaneColors() {
        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("OptionPane.messageForeground", null);
        UIManager.put("Button.background", null);
        UIManager.put("Button.foreground", null);
        UIManager.put("Button.border", null);
        UIManager.put("Button.font", null);
    }
}