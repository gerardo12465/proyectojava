package org.PracticaEsfe.Presentacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import org.PracticaEsfe.Dominio.Usuario;
import org.PracticaEsfe.Persistence.UserDAO;
import org.PracticaEsfe.Utilidades.PaletaColores;
import org.PracticaEsfe.Utilidades.RoundedBorder;

public class UserForm extends JFrame {

    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel nameLabel;
    private JTextField nameField;
    private JLabel emailLabel;
    private JTextField emailField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JButton saveButton;
    private JButton cancelButton;

    private ImageIcon backgroundImage; // Declarada como campo de clase

    private LoginForm parentLoginForm; // Referencia al LoginForm padre

    // Constructor sin argumentos (para pruebas directas)
    public UserForm() {
        this(null); // Llama al constructor principal con null
    }

    // Constructor principal que acepta el LoginForm padre
    public UserForm(LoginForm parentLoginForm) {
        super("Registro de Usuario");
        this.parentLoginForm = parentLoginForm; // Guarda la referencia

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500); // Mismo tamaño que LoginForm
        setLocationRelativeTo(null);

        // Carga la imagen de fondo una sola vez y la asigna a la variable de instancia
        try {
            backgroundImage = new ImageIcon(getClass().getResource("/images/biblioteca.png"));
            if (backgroundImage.getImageLoadStatus() == MediaTracker.ERRORED) {
                System.err.println("Error al cargar la imagen de fondo desde recursos: " + getClass().getResource("/images/biblioteca.png"));
                backgroundImage = new ImageIcon("https://placehold.co/700x500/8B4513/FFFFFF?text=Fondo+no+disponible");
            }
        } catch (Exception e) {
            System.err.println("Excepción al intentar cargar la imagen de fondo 'biblioteca.png': " + e.getMessage());
            backgroundImage = new ImageIcon("https://placehold.co/700x500/8B4513/FFFFFF?text=Fondo+no+disponible");
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

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        titleLabel = new JLabel("Registro de Nuevo Usuario", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(PaletaColores.CREAM_WHITE);
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel inputFieldsPanel = new JPanel(new GridBagLayout());
        inputFieldsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Color labelColor = PaletaColores.CREAM_WHITE;
        Dimension fieldSize = new Dimension(250, 35);

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        nameLabel = new JLabel("Nombre:");
        nameLabel.setForeground(labelColor);
        nameLabel.setFont(labelFont);
        inputFieldsPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        nameField = new JTextField(20);
        nameField.setBackground(PaletaColores.CREAM_WHITE);
        nameField.setForeground(PaletaColores.TEXT_DARK);
        nameField.setBorder(BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1));
        nameField.setPreferredSize(fieldSize);
        inputFieldsPanel.add(nameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        emailLabel = new JLabel("Email:");
        emailLabel.setForeground(labelColor);
        emailLabel.setFont(labelFont);
        inputFieldsPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        emailField = new JTextField(20);
        emailField.setBackground(PaletaColores.CREAM_WHITE);
        emailField.setForeground(PaletaColores.TEXT_DARK);
        emailField.setBorder(BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1));
        emailField.setPreferredSize(fieldSize);
        inputFieldsPanel.add(emailField, gbc);

        // Contraseña
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setForeground(labelColor);
        passwordLabel.setFont(labelFont);
        inputFieldsPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(20);
        passwordField.setBackground(PaletaColores.CREAM_WHITE);
        passwordField.setForeground(PaletaColores.TEXT_DARK);
        passwordField.setBorder(BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1));
        passwordField.setPreferredSize(fieldSize);
        inputFieldsPanel.add(passwordField, gbc);

        // Panel para los botones (centrados y con estilo)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        saveButton = new JButton("Guardar");
        saveButton.setBackground(PaletaColores.PRIMARY_BROWN); // Color de fondo del botón
        saveButton.setForeground(PaletaColores.CREAM_WHITE);   // Color del texto del botón
        saveButton.setFont(new Font("Arial", Font.BOLD, 16));
        saveButton.setFocusPainted(false);
        saveButton.setBorder(new RoundedBorder(25, PaletaColores.PRIMARY_BROWN, 2)); // Borde redondeado
        saveButton.setPreferredSize(new Dimension(150, 50));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonPanel.add(saveButton);

        cancelButton = new JButton("Cancelar");
        cancelButton.setBackground(PaletaColores.PRIMARY_BROWN); // Color de fondo del botón
        cancelButton.setForeground(PaletaColores.CREAM_WHITE);   // Color del texto del botón
        cancelButton.setFont(new Font("Arial", Font.BOLD, 16));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(new RoundedBorder(25, PaletaColores.PRIMARY_BROWN, 2)); // Borde redondeado
        cancelButton.setPreferredSize(new Dimension(150, 50));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonPanel.add(cancelButton);

        JPanel centerContentPanel = new JPanel();
        centerContentPanel.setLayout(new BoxLayout(centerContentPanel, BoxLayout.Y_AXIS));
        centerContentPanel.setOpaque(false);
        centerContentPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

        centerContentPanel.add(inputFieldsPanel);
        centerContentPanel.add(Box.createVerticalStrut(30));
        centerContentPanel.add(buttonPanel);

        mainPanel.add(centerContentPanel, BorderLayout.CENTER);

        backgroundPanel.add(mainPanel);
        add(backgroundPanel);

        // Acción del botón Guardar
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                    JOptionPane.showMessageDialog(mainPanel,
                            "Todos los campos son obligatorios.",
                            "Error de Validación",
                            JOptionPane.WARNING_MESSAGE);
                    resetJOptionPaneColors();
                    return;
                }

                UserDAO userDAO = new UserDAO(); // Instancia de UserDAO

                try {
                    Usuario existingUser = userDAO.findByEmail(email);
                    if (existingUser != null) {
                        setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                        JOptionPane.showMessageDialog(mainPanel,
                                "Ya existe un usuario con este email. Por favor, use otro email.",
                                "Error de Registro",
                                JOptionPane.WARNING_MESSAGE);
                        resetJOptionPaneColors();
                        return;
                    }

                    Usuario newUser = new Usuario(0, name, email, password);
                    Usuario createdUser = userDAO.create(newUser);

                    if (createdUser != null && createdUser.getId() > 0) {
                        setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                        JOptionPane.showMessageDialog(mainPanel,
                                "Usuario '" + createdUser.getNombre() + "' registrado con éxito. Ahora puede iniciar sesión.",
                                "Registro Exitoso",
                                JOptionPane.INFORMATION_MESSAGE);
                        resetJOptionPaneColors();
                        dispose(); // Cierra esta ventana (UserForm)
                        // Si el LoginForm padre existe, lo hace visible y limpia sus campos
                        if (parentLoginForm != null) {
                            parentLoginForm.setVisible(true);
                            parentLoginForm.clearFields();
                        }

                    } else {
                        setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                        JOptionPane.showMessageDialog(mainPanel,
                                "Error al registrar el usuario. No se pudo obtener el ID.",
                                "Error de Registro",
                                JOptionPane.ERROR_MESSAGE);
                        resetJOptionPaneColors();
                    }
                } catch (SQLException ex) {
                    setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                    JOptionPane.showMessageDialog(mainPanel,
                            "Error de base de datos al registrar el usuario: " + ex.getMessage(),
                            "Error de Base de Datos",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                    resetJOptionPaneColors();
                }
            }
        });

        // Acción del botón Cancelar
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra esta ventana (UserForm)
                // Si el LoginForm padre existe, lo hace visible y limpia sus campos
                if (parentLoginForm != null) {
                    parentLoginForm.setVisible(true);
                    parentLoginForm.clearFields();
                }
            }
        });

        // Añadir WindowListener para manejar el cierre de la ventana UserForm (por la 'X' o Alt+F4)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // Si el LoginForm padre existe, lo hace visible y limpia sus campos
                if (parentLoginForm != null) {
                    parentLoginForm.setVisible(true);
                    parentLoginForm.clearFields();
                }
            }
        });
    }

    // Configura los colores para los JOptionPane
    private void setJOptionPaneColors(Color backgroundColor, Color foregroundColor) {
        UIManager.put("OptionPane.background", backgroundColor);
        UIManager.put("Panel.background", backgroundColor);
        UIManager.put("OptionPane.messageForeground", foregroundColor);
        UIManager.put("Button.background", PaletaColores.PRIMARY_BROWN);
        UIManager.put("Button.foreground", PaletaColores.CREAM_WHITE);
        UIManager.put("Button.border", BorderFactory.createLineBorder(PaletaColores.ACCENT_GOLD, 1));
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 12));
    }

    // Restablece los colores predeterminados de JOptionPane
    private void resetJOptionPaneColors() {
        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("OptionPane.messageForeground", null);
        UIManager.put("Button.background", null);
        UIManager.put("Button.foreground", null);
        UIManager.put("Button.border", null);
        UIManager.put("Button.font", null);
    }

    // Métodos para obtener los datos de entrada (si se necesitan desde fuera de la clase)
    public String getNameInput() {
        return nameField.getText();
    }

    public String getEmailInput() {
        return emailField.getText();
    }

    public String getPasswordInput() {
        return new String(passwordField.getPassword());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UserForm().setVisible(true);
        });
    }
}