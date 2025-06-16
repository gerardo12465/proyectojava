package org.PracticaEsfe.Presentacion;

import org.PracticaEsfe.Dominio.Autor;
import org.PracticaEsfe.Dominio.Libro;
import org.PracticaEsfe.Persistence.AutorDAO;
import org.PracticaEsfe.Persistence.LibroDAO;
import org.PracticaEsfe.Utilidades.PaletaColores;
import org.PracticaEsfe.Main; // Importar la clase Main

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Importante: Ahora LibroForm extiende JPanel, no JFrame
public class LibroForm extends JPanel {
    private LibroDAO libroDAO;
    private AutorDAO autorDAO;
    private DefaultTableModel tableModel;
    private JTable librosTable;

    private JTextField txtId;
    private JTextField txtTitulo;
    private JTextField txtFechaPublicacion;
    private JComboBox<String> cmbAutor;
    private Map<String, Integer> autorMap;

    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private ImageIcon backgroundImage;
    private Main parentFrame; // Referencia al JFrame principal

    // Constructor modificado para aceptar la referencia al JFrame principal
    public LibroForm(Main parentFrame) {
        this.parentFrame = parentFrame;
        libroDAO = new LibroDAO();
        autorDAO = new AutorDAO();
        autorMap = new HashMap<>();
        initComponents();
        loadAutoresToComboBox();
        loadLibros();
    }

    private void initComponents() {
        // Eliminado: setTitle, setSize, setDefaultCloseOperation, setLocationRelativeTo
        // Estas propiedades son gestionadas por el JFrame principal (Main)

        try {
            backgroundImage = new ImageIcon(getClass().getResource("/images/biblioteca.png"));
            if (backgroundImage.getImageLoadStatus() == MediaTracker.ERRORED) {
                System.err.println("Error al cargar la imagen de fondo local 'biblioteca.png' para LibroForm: " + getClass().getResource("/images/biblioteca.png"));
                // Fallback image if local fails
                backgroundImage = new ImageIcon("https://placehold.co/800x600/8B4513/FFFFFF?text=Fondo+no+disponible");
            }
        } catch (Exception e) {
            System.err.println("Excepción al cargar la imagen de fondo local 'biblioteca.png': " + e.getMessage());
            // Fallback image if exception occurs
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
        // Importante: El contenido principal del formulario se añade a 'this' (el JPanel de LibroForm)
        this.setLayout(new BorderLayout()); // Asegura que el LibroForm JPanel tenga un layout
        this.setOpaque(false); // Hace que el JPanel de LibroForm sea transparente para mostrar el fondo

        JPanel mainContentWrapper = new JPanel(new BorderLayout());
        mainContentWrapper.setOpaque(false);
        mainContentWrapper.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel formTitle = new JLabel("Gestión de Libros", SwingConstants.CENTER);
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

        JLabel lblTitulo = new JLabel("Título:");
        lblTitulo.setForeground(PaletaColores.CREAM_WHITE);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 15));
        gbcInput.gridx = 0;
        gbcInput.gridy = 1;
        gbcInput.weightx = 0;
        gbcInput.fill = GridBagConstraints.NONE;
        gbcInput.anchor = GridBagConstraints.EAST;
        inputPanel.add(lblTitulo, gbcInput);

        txtTitulo = new JTextField();
        txtTitulo.setBackground(PaletaColores.CREAM_WHITE);
        txtTitulo.setForeground(PaletaColores.TEXT_DARK);
        txtTitulo.setFont(new Font("Serif", Font.PLAIN, 15));
        txtTitulo.setBorder(BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1));
        txtTitulo.setPreferredSize(new Dimension(180, 35));
        gbcInput.gridx = 1;
        gbcInput.gridy = 1;
        gbcInput.weightx = 1.0;
        gbcInput.fill = GridBagConstraints.HORIZONTAL;
        gbcInput.anchor = GridBagConstraints.CENTER;
        inputPanel.add(txtTitulo, gbcInput);

        JLabel lblFechaPublicacion = new JLabel("Fecha Publicación:");
        lblFechaPublicacion.setForeground(PaletaColores.CREAM_WHITE);
        lblFechaPublicacion.setFont(new Font("Serif", Font.BOLD, 15));
        gbcInput.gridx = 0;
        gbcInput.gridy = 2;
        gbcInput.weightx = 0;
        gbcInput.fill = GridBagConstraints.NONE;
        gbcInput.anchor = GridBagConstraints.EAST;
        inputPanel.add(lblFechaPublicacion, gbcInput);

        txtFechaPublicacion = new JTextField();
        txtFechaPublicacion.setBackground(PaletaColores.CREAM_WHITE);
        txtFechaPublicacion.setForeground(PaletaColores.TEXT_DARK);
        txtFechaPublicacion.setFont(new Font("Serif", Font.PLAIN, 15));
        txtFechaPublicacion.setBorder(BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1));
        txtFechaPublicacion.setPreferredSize(new Dimension(180, 35));
        gbcInput.gridx = 1;
        gbcInput.gridy = 2;
        gbcInput.weightx = 1.0;
        gbcInput.fill = GridBagConstraints.HORIZONTAL;
        gbcInput.anchor = GridBagConstraints.CENTER;
        inputPanel.add(txtFechaPublicacion, gbcInput);

        JLabel lblAutor = new JLabel("Autor:");
        lblAutor.setForeground(PaletaColores.CREAM_WHITE);
        lblAutor.setFont(new Font("Serif", Font.BOLD, 15));
        gbcInput.gridx = 0;
        gbcInput.gridy = 3;
        gbcInput.weightx = 0;
        gbcInput.fill = GridBagConstraints.NONE;
        gbcInput.anchor = GridBagConstraints.EAST;
        inputPanel.add(lblAutor, gbcInput);

        cmbAutor = new JComboBox<>();
        cmbAutor.setBackground(PaletaColores.CREAM_WHITE);
        cmbAutor.setForeground(PaletaColores.TEXT_DARK);
        cmbAutor.setFont(new Font("Serif", Font.PLAIN, 15));
        cmbAutor.setBorder(BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1));
        cmbAutor.setPreferredSize(new Dimension(180, 35));
        gbcInput.gridx = 1;
        gbcInput.gridy = 3;
        gbcInput.weightx = 1.0;
        gbcInput.fill = GridBagConstraints.HORIZONTAL;
        gbcInput.anchor = GridBagConstraints.CENTER;
        inputPanel.add(cmbAutor, gbcInput);


        gbcSection.gridx = 0;
        gbcSection.gridy = 0;
        gbcSection.gridwidth = 1;
        gbcSection.weightx = 0.3;
        gbcSection.weighty = 1.0;
        gbcSection.fill = GridBagConstraints.NONE;
        gbcSection.anchor = GridBagConstraints.CENTER;
        inputAndTableSection.add(inputPanel, gbcSection);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Título", "Fecha Publicación", "ID Autor", "Nombre Autor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        librosTable = new JTable(tableModel);
        librosTable.setBackground(PaletaColores.CREAM_WHITE);
        librosTable.setForeground(PaletaColores.TEXT_DARK);
        librosTable.setFont(new Font("Serif", Font.PLAIN, 12));
        librosTable.getTableHeader().setBackground(PaletaColores.PRIMARY_BROWN);
        librosTable.getTableHeader().setForeground(PaletaColores.CREAM_WHITE);
        librosTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(librosTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 2),
                "Lista de Libros",
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

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(PaletaColores.PRIMARY_BROWN);
        btnEliminar.setForeground(PaletaColores.CREAM_WHITE);
        btnEliminar.setFont(new Font("Serif", Font.BOLD, 14));
        btnEliminar.setFocusPainted(false);
        btnEliminar.setBorder(new EmptyBorder(10, 20, 10, 20));

        btnLimpiar = new JButton("Limpiar Campos");
        btnLimpiar.setBackground(PaletaColores.PRIMARY_BROWN);
        btnLimpiar.setForeground(PaletaColores.CREAM_WHITE);
        btnLimpiar.setFont(new Font("Serif", Font.BOLD, 14));
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setBorder(new EmptyBorder(10, 20, 10, 20));

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

        // Añade el backgroundPanel al LibroForm JPanel
        this.add(backgroundPanel, BorderLayout.CENTER);

        // --- Event Listeners ---
        btnGuardar.addActionListener(e -> guardarLibro());
        btnActualizar.addActionListener(e -> actualizarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnLimpiar.addActionListener(e -> clearFields());

        btnRegresar.addActionListener(e -> {
            // Ya no dispose() esta ventana, sino que le indicamos al Main que cambie al menú principal
            parentFrame.showPanel("MainMenu");
        });

        librosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && librosTable.getSelectedRow() != -1) {
                int selectedRow = librosTable.getSelectedRow();
                txtId.setText(librosTable.getValueAt(selectedRow, 0).toString());
                txtTitulo.setText(librosTable.getValueAt(selectedRow, 1).toString());
                txtFechaPublicacion.setText(librosTable.getValueAt(selectedRow, 2).toString());
                String selectedAutorNombre = librosTable.getValueAt(selectedRow, 4).toString();
                cmbAutor.setSelectedItem(selectedAutorNombre);
            }
        });
    }

    private void loadAutoresToComboBox() {
        try {
            List<Autor> autores = autorDAO.obtenerTodosLosAutores();
            cmbAutor.removeAllItems();
            autorMap.clear();
            for (Autor autor : autores) {
                cmbAutor.addItem(autor.getNombreCompleto());
                autorMap.put(autor.getNombreCompleto(), autor.getId());
            }
        } catch (SQLException ex) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Error al cargar autores para el ComboBox: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            resetJOptionPaneColors();
            ex.printStackTrace();
        }
    }

    private void loadLibros() {
        tableModel.setRowCount(0);
        try {
            List<Libro> libros = libroDAO.getAllLibros();
            for (Libro libro : libros) {
                String autorNombre = "Desconocido";
                Autor autor = autorDAO.obtenerAutorPorId(libro.getIdAutor());
                if (autor != null) {
                    autorNombre = autor.getNombreCompleto();
                }
                tableModel.addRow(new Object[]{
                        libro.getId(),
                        libro.getTitulo(),
                        libro.getFechaPublicacion(),
                        libro.getIdAutor(),
                        autorNombre
                });
            }
        } catch (SQLException ex) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Error al cargar libros: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            resetJOptionPaneColors();
            ex.printStackTrace();
        }
    }

    private void guardarLibro() {
        if (txtTitulo.getText().isEmpty() || txtFechaPublicacion.getText().isEmpty() || cmbAutor.getSelectedItem() == null) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            resetJOptionPaneColors();
            return;
        }

        try {
            String titulo = txtTitulo.getText();
            Date fechaPublicacion = new Date(dateFormat.parse(txtFechaPublicacion.getText()).getTime());
            String autorSeleccionado = (String) cmbAutor.getSelectedItem();
            Integer idAutor = autorMap.get(autorSeleccionado);

            if (idAutor == null) {
                setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                JOptionPane.showMessageDialog(this, "Autor no válido seleccionado.", "Error de Autor", JOptionPane.ERROR_MESSAGE);
                resetJOptionPaneColors();
                return;
            }

            Libro libro = new Libro(titulo, fechaPublicacion, idAutor);
            Libro createdLibro = libroDAO.create(libro);
            if (createdLibro != null) {
                setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                JOptionPane.showMessageDialog(this, "Libro guardado exitosamente con ID: " + createdLibro.getId(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                resetJOptionPaneColors();
                loadLibros();
                clearFields();
            }
        } catch (ParseException ex) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use YYYY-MM-DD.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            resetJOptionPaneColors();
            ex.printStackTrace();
        } catch (SQLException ex) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Error al guardar libro: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            resetJOptionPaneColors();
            ex.printStackTrace();
        }
    }

    private void actualizarLibro() {
        if (txtId.getText().isEmpty() || txtTitulo.getText().isEmpty() || txtFechaPublicacion.getText().isEmpty() || cmbAutor.getSelectedItem() == null) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Seleccione un libro de la tabla y complete todos los campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            resetJOptionPaneColors();
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText());
            String titulo = txtTitulo.getText();
            Date fechaPublicacion = new Date(dateFormat.parse(txtFechaPublicacion.getText()).getTime());
            String autorSeleccionado = (String) cmbAutor.getSelectedItem();
            Integer idAutor = autorMap.get(autorSeleccionado);

            if (idAutor == null) {
                setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                JOptionPane.showMessageDialog(this, "Autor no válido seleccionado.", "Error de Autor", JOptionPane.ERROR_MESSAGE);
                resetJOptionPaneColors();
                return;
            }

            Libro libro = new Libro(id, titulo, fechaPublicacion, idAutor);
            boolean updated = libroDAO.update(libro);
            if (updated) {
                setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                JOptionPane.showMessageDialog(this, "Libro actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                resetJOptionPaneColors();
                loadLibros();
                clearFields();
            } else {
                setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el libro. ID no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                resetJOptionPaneColors();
            }
        } catch (ParseException ex) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use YYYY-MM-DD.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            resetJOptionPaneColors();
            ex.printStackTrace();
        } catch (SQLException ex) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Error al actualizar libro: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            resetJOptionPaneColors();
            ex.printStackTrace();
        }
    }

    private void eliminarLibro() {
        if (txtId.getText().isEmpty()) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Seleccione un libro de la tabla para eliminar.", "Ningún Libro Seleccionado", JOptionPane.WARNING_MESSAGE);
            resetJOptionPaneColors();
            return;
        }

        setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar este libro?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        resetJOptionPaneColors();

        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText());
            try {
                boolean deleted = libroDAO.delete(id);
                if (deleted) {
                    setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                    JOptionPane.showMessageDialog(this, "Libro eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    resetJOptionPaneColors();
                    loadLibros();
                    clearFields();
                } else {
                    setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el libro. ID no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    resetJOptionPaneColors();
                }
            } catch (SQLException ex) {
                setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                JOptionPane.showMessageDialog(this, "Error al eliminar libro: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
                resetJOptionPaneColors();
                ex.printStackTrace();
            }
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtTitulo.setText("");
        txtFechaPublicacion.setText("");
        cmbAutor.setSelectedIndex(-1); // Deseleccionar cualquier ítem
        librosTable.clearSelection();
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
