package org.PracticaEsfe.Presentacion;

import org.PracticaEsfe.Dominio.Libro;
import org.PracticaEsfe.Dominio.Prestamo;
import org.PracticaEsfe.Dominio.Usuario;
import org.PracticaEsfe.Persistence.LibroDAO;
import org.PracticaEsfe.Persistence.PrestamoDAO;
import org.PracticaEsfe.Persistence.UserDAO;
import org.PracticaEsfe.Utilidades.PaletaColores; // Importar PaletaColores
import org.PracticaEsfe.Main; // Importar la clase Main

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder; // Para el borde con título de la tabla
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Importante: Ahora PrestamoForm extiende JPanel, no JFrame
public class PrestamoForm extends JPanel {
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

    private ImageIcon backgroundImage;
    private Main parentFrame; // Referencia al JFrame principal

    // Constructor modificado para aceptar la referencia al JFrame principal
    public PrestamoForm(Main parentFrame) {
        this.parentFrame = parentFrame;
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
        // Eliminado: setTitle, setSize, setDefaultCloseOperation, setLocationRelativeTo
        // Estas propiedades son gestionadas por el JFrame principal (Main)

        try {
            backgroundImage = new ImageIcon(getClass().getResource("/images/biblioteca.png"));
            if (backgroundImage.getImageLoadStatus() == MediaTracker.ERRORED) {
                System.err.println("Error al cargar la imagen de fondo local 'biblioteca.png' para PrestamoForm: " + getClass().getResource("/images/biblioteca.png"));
                // Fallback image if local fails
                backgroundImage = new ImageIcon("https://placehold.co/900x600/8B4513/FFFFFF?text=Fondo+no+disponible");
            }
        } catch (Exception e) {
            System.err.println("Excepción al cargar la imagen de fondo local 'biblioteca.png': " + e.getMessage());
            // Fallback image if exception occurs
            backgroundImage = new ImageIcon("https://placehold.co/900x600/8B4513/FFFFFF?text=Fondo+no+disponible");
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
        // Importante: El contenido principal del formulario se añade a 'this' (el JPanel de PrestamoForm)
        this.setLayout(new BorderLayout()); // Asegura que el PrestamoForm JPanel tenga un layout
        this.setOpaque(false); // Hace que el JPanel de PrestamoForm sea transparente para mostrar el fondo

        JPanel mainContentWrapper = new JPanel(new BorderLayout());
        mainContentWrapper.setOpaque(false);
        mainContentWrapper.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel formTitle = new JLabel("Gestión de Préstamos", SwingConstants.CENTER);
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

        JLabel lblId = new JLabel("ID del Préstamo:");
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

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setForeground(PaletaColores.CREAM_WHITE);
        lblUsuario.setFont(new Font("Serif", Font.BOLD, 15));
        gbcInput.gridx = 0;
        gbcInput.gridy = 1;
        gbcInput.weightx = 0;
        gbcInput.fill = GridBagConstraints.NONE;
        gbcInput.anchor = GridBagConstraints.EAST;
        inputPanel.add(lblUsuario, gbcInput);

        cmbUsuario = new JComboBox<>();
        cmbUsuario.setBackground(PaletaColores.CREAM_WHITE);
        cmbUsuario.setForeground(PaletaColores.TEXT_DARK);
        cmbUsuario.setFont(new Font("Serif", Font.PLAIN, 15));
        cmbUsuario.setBorder(BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1));
        cmbUsuario.setPreferredSize(new Dimension(180, 35));
        gbcInput.gridx = 1;
        gbcInput.gridy = 1;
        gbcInput.weightx = 1.0;
        gbcInput.fill = GridBagConstraints.HORIZONTAL;
        gbcInput.anchor = GridBagConstraints.CENTER;
        inputPanel.add(cmbUsuario, gbcInput);

        JLabel lblLibro = new JLabel("Libro:");
        lblLibro.setForeground(PaletaColores.CREAM_WHITE);
        lblLibro.setFont(new Font("Serif", Font.BOLD, 15));
        gbcInput.gridx = 0;
        gbcInput.gridy = 2;
        gbcInput.weightx = 0;
        gbcInput.fill = GridBagConstraints.NONE;
        gbcInput.anchor = GridBagConstraints.EAST;
        inputPanel.add(lblLibro, gbcInput);

        cmbLibro = new JComboBox<>();
        cmbLibro.setBackground(PaletaColores.CREAM_WHITE);
        cmbLibro.setForeground(PaletaColores.TEXT_DARK);
        cmbLibro.setFont(new Font("Serif", Font.PLAIN, 15));
        cmbLibro.setBorder(BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1));
        cmbLibro.setPreferredSize(new Dimension(180, 35));
        gbcInput.gridx = 1;
        gbcInput.gridy = 2;
        gbcInput.weightx = 1.0;
        gbcInput.fill = GridBagConstraints.HORIZONTAL;
        gbcInput.anchor = GridBagConstraints.CENTER;
        inputPanel.add(cmbLibro, gbcInput);

        JLabel lblFechaPrestamo = new JLabel("Fecha Préstamo:");
        lblFechaPrestamo.setForeground(PaletaColores.CREAM_WHITE);
        lblFechaPrestamo.setFont(new Font("Serif", Font.BOLD, 15));
        gbcInput.gridx = 0;
        gbcInput.gridy = 3;
        gbcInput.weightx = 0;
        gbcInput.fill = GridBagConstraints.NONE;
        gbcInput.anchor = GridBagConstraints.EAST;
        inputPanel.add(lblFechaPrestamo, gbcInput);

        txtFechaPrestamo = new JTextField();
        txtFechaPrestamo.setBackground(PaletaColores.CREAM_WHITE);
        txtFechaPrestamo.setForeground(PaletaColores.TEXT_DARK);
        txtFechaPrestamo.setFont(new Font("Serif", Font.PLAIN, 15));
        txtFechaPrestamo.setBorder(BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1));
        txtFechaPrestamo.setPreferredSize(new Dimension(180, 35));
        gbcInput.gridx = 1;
        gbcInput.gridy = 3;
        gbcInput.weightx = 1.0;
        gbcInput.fill = GridBagConstraints.HORIZONTAL;
        gbcInput.anchor = GridBagConstraints.CENTER;
        inputPanel.add(txtFechaPrestamo, gbcInput);

        JLabel lblFechaDevolucion = new JLabel("Fecha Devolución:");
        lblFechaDevolucion.setForeground(PaletaColores.CREAM_WHITE);
        lblFechaDevolucion.setFont(new Font("Serif", Font.BOLD, 15));
        gbcInput.gridx = 0;
        gbcInput.gridy = 4;
        gbcInput.weightx = 0;
        gbcInput.fill = GridBagConstraints.NONE;
        gbcInput.anchor = GridBagConstraints.EAST;
        inputPanel.add(lblFechaDevolucion, gbcInput);

        txtFechaDevolucion = new JTextField();
        txtFechaDevolucion.setBackground(PaletaColores.CREAM_WHITE);
        txtFechaDevolucion.setForeground(PaletaColores.TEXT_DARK);
        txtFechaDevolucion.setFont(new Font("Serif", Font.PLAIN, 15));
        txtFechaDevolucion.setBorder(BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1));
        txtFechaDevolucion.setPreferredSize(new Dimension(180, 35));
        gbcInput.gridx = 1;
        gbcInput.gridy = 4;
        gbcInput.weightx = 1.0;
        gbcInput.fill = GridBagConstraints.HORIZONTAL;
        gbcInput.anchor = GridBagConstraints.CENTER;
        inputPanel.add(txtFechaDevolucion, gbcInput);

        gbcSection.gridx = 0;
        gbcSection.gridy = 0;
        gbcSection.gridwidth = 1;
        gbcSection.weightx = 0.3;
        gbcSection.weighty = 1.0;
        gbcSection.fill = GridBagConstraints.NONE;
        gbcSection.anchor = GridBagConstraints.CENTER;
        inputAndTableSection.add(inputPanel, gbcSection);

        tableModel = new DefaultTableModel(new Object[]{"ID", "ID Usuario", "Nombre Usuario", "ID Libro", "Título Libro", "Fecha Préstamo", "Fecha Devolución"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        prestamosTable = new JTable(tableModel);
        prestamosTable.setBackground(PaletaColores.CREAM_WHITE);
        prestamosTable.setForeground(PaletaColores.TEXT_DARK);
        prestamosTable.setFont(new Font("Serif", Font.PLAIN, 12));
        prestamosTable.getTableHeader().setBackground(PaletaColores.PRIMARY_BROWN);
        prestamosTable.getTableHeader().setForeground(PaletaColores.CREAM_WHITE);
        prestamosTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(prestamosTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 2),
                "Lista de Préstamos",
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

        btnGuardar = new JButton("Guardar Préstamo");
        btnGuardar.setBackground(PaletaColores.PRIMARY_BROWN);
        btnGuardar.setForeground(PaletaColores.CREAM_WHITE);
        btnGuardar.setFont(new Font("Serif", Font.BOLD, 14));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorder(new EmptyBorder(10, 20, 10, 20));

        btnActualizar = new JButton("Actualizar Préstamo");
        btnActualizar.setBackground(PaletaColores.PRIMARY_BROWN);
        btnActualizar.setForeground(PaletaColores.CREAM_WHITE);
        btnActualizar.setFont(new Font("Serif", Font.BOLD, 14));
        btnActualizar.setFocusPainted(false);
        btnActualizar.setBorder(new EmptyBorder(10, 20, 10, 20));

        btnEliminar = new JButton("Eliminar Préstamo");
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

        btnMarcarDevuelto = new JButton("Marcar como Devuelto");
        btnMarcarDevuelto.setBackground(PaletaColores.ACCENT_GOLD); // Un color diferente para destacar
        btnMarcarDevuelto.setForeground(PaletaColores.TEXT_DARK);
        btnMarcarDevuelto.setFont(new Font("Serif", Font.BOLD, 14));
        btnMarcarDevuelto.setFocusPainted(false);
        btnMarcarDevuelto.setBorder(new EmptyBorder(10, 20, 10, 20));

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnActualizar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnLimpiar);
        buttonPanel.add(btnMarcarDevuelto);

        mainContentWrapper.add(buttonPanel, BorderLayout.SOUTH);

        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        mainGbc.weightx = 1.0;
        mainGbc.weighty = 1.0;
        mainGbc.fill = GridBagConstraints.BOTH;
        mainGbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(mainContentWrapper, mainGbc);

        // Añade el backgroundPanel al PrestamoForm JPanel
        this.add(backgroundPanel, BorderLayout.CENTER);


        btnGuardar.addActionListener(e -> guardarPrestamo());
        btnActualizar.addActionListener(e -> actualizarPrestamo());
        btnEliminar.addActionListener(e -> eliminarPrestamo());
        btnLimpiar.addActionListener(e -> clearFields());
        btnMarcarDevuelto.addActionListener(e -> marcarComoDevuelto());

        // Acción para el botón "Regresar al Menú"
        btnRegresar.addActionListener(e -> {
            parentFrame.showPanel("MainMenu"); // Vuelve a la tarjeta del menú principal
        });

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
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios para el ComboBox: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            resetJOptionPaneColors();
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
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Error al cargar libros para el ComboBox: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            resetJOptionPaneColors();
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
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Error al cargar préstamos: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            resetJOptionPaneColors();
            ex.printStackTrace();
        }
    }

    private void guardarPrestamo() {
        if (cmbUsuario.getSelectedItem() == null || cmbLibro.getSelectedItem() == null || txtFechaPrestamo.getText().isEmpty()) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un usuario, un libro y la fecha de préstamo.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            resetJOptionPaneColors();
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
                setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                JOptionPane.showMessageDialog(this, "Usuario o libro no válidos seleccionados.", "Error de Selección", JOptionPane.ERROR_MESSAGE);
                resetJOptionPaneColors();
                return;
            }

            Prestamo prestamo = new Prestamo(idUsuario, idLibro, fechaPrestamo, fechaDevolucion);
            Prestamo createdPrestamo = prestamoDAO.create(prestamo);

            if (createdPrestamo != null) {
                setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                JOptionPane.showMessageDialog(this, "Préstamo guardado exitosamente con ID: " + createdPrestamo.getId(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                resetJOptionPaneColors();
                loadPrestamos();
                clearFields();
            }
        } catch (ParseException e) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. UsebeginPath-MM-DD.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            resetJOptionPaneColors();
            e.printStackTrace();
        } catch (SQLException ex) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Error al guardar préstamo: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            resetJOptionPaneColors();
            ex.printStackTrace();
        }
    }

    private void actualizarPrestamo() {
        if (txtId.getText().isEmpty() || cmbUsuario.getSelectedItem() == null || cmbLibro.getSelectedItem() == null || txtFechaPrestamo.getText().isEmpty()) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo de la tabla y complete los campos obligatorios.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            resetJOptionPaneColors();
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
                setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                JOptionPane.showMessageDialog(this, "Usuario o libro no válidos seleccionados.", "Error de Selección", JOptionPane.ERROR_MESSAGE);
                resetJOptionPaneColors();
                return;
            }

            Prestamo prestamo = new Prestamo(id, idUsuario, idLibro, fechaPrestamo, fechaDevolucion);
            boolean updated = prestamoDAO.update(prestamo);

            if (updated) {
                setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                JOptionPane.showMessageDialog(this, "Préstamo actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                resetJOptionPaneColors();
                loadPrestamos();
                clearFields();
            } else {
                setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el préstamo. ID no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                resetJOptionPaneColors();
            }
        } catch (ParseException e) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. UsebeginPath-MM-DD.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            resetJOptionPaneColors();
            e.printStackTrace();
        } catch (SQLException ex) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Error al actualizar préstamo: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            resetJOptionPaneColors();
            ex.printStackTrace();
        }
    }

    private void eliminarPrestamo() {
        if (txtId.getText().isEmpty()) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo de la tabla para eliminar.", "Ningún Préstamo Seleccionado", JOptionPane.WARNING_MESSAGE);
            resetJOptionPaneColors();
            return;
        }

        setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar este préstamo?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        resetJOptionPaneColors();

        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText());
            try {
                boolean deleted = prestamoDAO.delete(id);
                if (deleted) {
                    setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                    JOptionPane.showMessageDialog(this, "Préstamo eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    resetJOptionPaneColors();
                    loadPrestamos();
                    clearFields();
                } else {
                    setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el préstamo. ID no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    resetJOptionPaneColors();
                }
            } catch (SQLException ex) {
                setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                JOptionPane.showMessageDialog(this, "Error al eliminar préstamo: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
                resetJOptionPaneColors();
                ex.printStackTrace();
            }
        }
    }

    private void marcarComoDevuelto() {
        if (txtId.getText().isEmpty()) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo de la tabla para marcar como devuelto.", "Ningún Préstamo Seleccionado", JOptionPane.WARNING_MESSAGE);
            resetJOptionPaneColors();
            return;
        }

        int id = Integer.parseInt(txtId.getText());
        try {
            Prestamo prestamo = prestamoDAO.findById(id);
            if (prestamo != null) {
                if (prestamo.getFechaDevolucion() != null) {
                    setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                    JOptionPane.showMessageDialog(this, "Este préstamo ya ha sido marcado como devuelto.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    resetJOptionPaneColors();
                    return;
                }
                prestamo.setFechaDevolucion(new Date(System.currentTimeMillis()));
                boolean updated = prestamoDAO.update(prestamo);
                if (updated) {
                    setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                    JOptionPane.showMessageDialog(this, "Préstamo marcado como devuelto exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    resetJOptionPaneColors();
                    loadPrestamos();
                    clearFields();
                } else {
                    setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                    JOptionPane.showMessageDialog(this, "No se pudo marcar el préstamo como devuelto.", "Error", JOptionPane.ERROR_MESSAGE);
                    resetJOptionPaneColors();
                }
            } else {
                setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                JOptionPane.showMessageDialog(this, "Préstamo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                resetJOptionPaneColors();
            }
        } catch (SQLException ex) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Error al marcar préstamo como devuelto: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            resetJOptionPaneColors();
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

    // Métodos para establecer y resetear colores de JOptionPane
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

    // Eliminado el método main, ya que el inicio se gestiona en la clase Main principal
    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(() -> {
    //         new PrestamoForm().setVisible(true);
    //     });
    // }
}
