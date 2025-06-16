package org.PracticaEsfe.Presentacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private ImageIcon backgroundImage; // <--- MOVIDO AQUÍ: Declaración como campo de la clase

    // Agrega una referencia al LoginForm si necesitas volver a mostrarlo
    private LoginForm parentLoginForm;

    public UserForm() {
        this(null); // Llama al constructor que acepta un LoginForm y pasa null
    }

    public UserForm(LoginForm parentLoginForm) {
        super("Registro de Usuario");
        this.parentLoginForm = parentLoginForm; // Guarda la referencia

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500); // Mismo tamaño que LoginForm
        setLocationRelativeTo(null);

        // Intenta cargar la imagen de fondo, similar a LoginForm
        // AQUI SOLO SE ASIGNA EL VALOR A LA VARIABLE DE INSTANCIA YA DECLARADA
        try {
            backgroundImage = new ImageIcon(getClass().getResource("/images/biblioteca.png"));
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen de fondo local 'biblioteca.png': " + e.getMessage());
            backgroundImage = new ImageIcon("https://placehold.co/700x500/8B4513/FFFFFF?text=Fondo+no+disponible");
        }

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Ahora backgroundImage es un campo de clase, por lo tanto, es accesible y efectivamente final.
                if (backgroundImage != null && backgroundImage.getImage() != null) {
                    g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(PaletaColores.PRIMARY_BROWN);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        mainPanel = new JPanel(new BorderLayout(10, 10)); // Mismo layout que loginContentPanel
        mainPanel.setBackground(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN); // Color semitransparente
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Mismo borde

        titleLabel = new JLabel("Registro de Nuevo Usuario", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28)); // Mismo tamaño de fuente que el título de LoginForm
        titleLabel.setForeground(PaletaColores.CREAM_WHITE); // Mismo color de fuente
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0)); // Mismo borde
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel para los campos de entrada usando GridBagLayout para centrar y alinear
        JPanel inputFieldsPanel = new JPanel(new GridBagLayout());
        inputFieldsPanel.setOpaque(false); // Hacer transparente para que se vea el fondo
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0); // Espaciado entre componentes

        // Configuración común para las etiquetas
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Color labelColor = PaletaColores.CREAM_WHITE;
        Dimension fieldSize = new Dimension(250, 35); // Ancho y alto deseado para los campos

        // Nombre
        gbc.gridx = 0; // Columna 0
        gbc.gridy = 0; // Fila 0
        gbc.anchor = GridBagConstraints.EAST; // Alinea a la derecha
        nameLabel = new JLabel("Nombre:");
        nameLabel.setForeground(labelColor);
        nameLabel.setFont(labelFont);
        inputFieldsPanel.add(nameLabel, gbc);

        gbc.gridx = 1; // Columna 1
        gbc.gridy = 0; // Fila 0
        gbc.anchor = GridBagConstraints.WEST; // Alinea a la izquierda
        nameField = new JTextField(20);
        nameField.setBackground(PaletaColores.CREAM_WHITE);
        nameField.setForeground(PaletaColores.TEXT_DARK);
        nameField.setBorder(BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1));
        nameField.setPreferredSize(fieldSize); // Aplicar tamaño
        inputFieldsPanel.add(nameField, gbc);

        // Email
        gbc.gridx = 0; // Columna 0
        gbc.gridy = 1; // Fila 1
        gbc.anchor = GridBagConstraints.EAST;
        emailLabel = new JLabel("Email:");
        emailLabel.setForeground(labelColor);
        emailLabel.setFont(labelFont);
        inputFieldsPanel.add(emailLabel, gbc);

        gbc.gridx = 1; // Columna 1
        gbc.gridy = 1; // Fila 1
        gbc.anchor = GridBagConstraints.WEST;
        emailField = new JTextField(20);
        emailField.setBackground(PaletaColores.CREAM_WHITE);
        emailField.setForeground(PaletaColores.TEXT_DARK);
        emailField.setBorder(BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1));
        emailField.setPreferredSize(fieldSize); // Aplicar tamaño
        inputFieldsPanel.add(emailField, gbc);

        // Contraseña
        gbc.gridx = 0; // Columna 0
        gbc.gridy = 2; // Fila 2
        gbc.anchor = GridBagConstraints.EAST;
        passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setForeground(labelColor);
        passwordLabel.setFont(labelFont);
        inputFieldsPanel.add(passwordLabel, gbc);

        gbc.gridx = 1; // Columna 1
        gbc.gridy = 2; // Fila 2
        gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(20);
        passwordField.setBackground(PaletaColores.CREAM_WHITE);
        passwordField.setForeground(PaletaColores.TEXT_DARK);
        passwordField.setBorder(BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1));
        passwordField.setPreferredSize(fieldSize); // Aplicar tamaño
        inputFieldsPanel.add(passwordField, gbc);

        // Panel para los botones (centrados y con estilo)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0)); // Centrado, 20px de espacio entre botones
        buttonPanel.setOpaque(false); // Transparente

        saveButton = new JButton("Guardar");
        saveButton.setBackground(PaletaColores.PRIMARY_BROWN);
        saveButton.setForeground(PaletaColores.CREAM_WHITE);
        saveButton.setFont(new Font("Arial", Font.BOLD, 16)); // Mismo tamaño de fuente que el botón de login
        saveButton.setFocusPainted(false);
        saveButton.setBorder(new RoundedBorder(25, PaletaColores.PRIMARY_BROWN, 2)); // Borde redondeado
        saveButton.setPreferredSize(new Dimension(150, 50)); // Tamaño de botón
        buttonPanel.add(saveButton);

        cancelButton = new JButton("Cancelar");
        cancelButton.setBackground(PaletaColores.PRIMARY_BROWN);
        cancelButton.setForeground(PaletaColores.CREAM_WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 16)); // Mismo tamaño de fuente que el botón de login
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(new RoundedBorder(25, PaletaColores.PRIMARY_BROWN, 2)); // Borde redondeado
        cancelButton.setPreferredSize(new Dimension(150, 50)); // Tamaño de botón
        buttonPanel.add(cancelButton);

        // Panel central que agrupa los campos de entrada y los botones
        JPanel centerContentPanel = new JPanel();
        centerContentPanel.setLayout(new BoxLayout(centerContentPanel, BoxLayout.Y_AXIS));
        centerContentPanel.setOpaque(false);
        centerContentPanel.setBorder(new EmptyBorder(15, 0, 15, 0)); // Espaciado vertical

        centerContentPanel.add(inputFieldsPanel);
        centerContentPanel.add(Box.createVerticalStrut(30)); // Espacio entre campos y botones
        centerContentPanel.add(buttonPanel);

        mainPanel.add(centerContentPanel, BorderLayout.CENTER);

        backgroundPanel.add(mainPanel); // Añade el panel principal al panel de fondo
        add(backgroundPanel); // Añade el panel de fondo al frame

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

                UserDAO userDAO = new UserDAO();

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
                        dispose();
                        // Si se pasó un LoginForm, muéstralo al cerrar esta ventana
                        if (parentLoginForm != null) {
                            parentLoginForm.setVisible(true);
                            parentLoginForm.clearFields(); // Limpiar campos del LoginForm
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
                dispose();
                // Si se pasó un LoginForm, muéstralo al cerrar esta ventana
                if (parentLoginForm != null) {
                    parentLoginForm.setVisible(true);
                    parentLoginForm.clearFields(); // Limpiar campos del LoginForm
                }
            }
        });
    }

    // Métodos para cambiar los colores de JOptionPane
    private void setJOptionPaneColors(Color backgroundColor, Color foregroundColor) {
        UIManager.put("OptionPane.background", backgroundColor);
        UIManager.put("Panel.background", backgroundColor);
        UIManager.put("OptionPane.messageForeground", foregroundColor);
        UIManager.put("Button.background", PaletaColores.PRIMARY_BROWN);
        UIManager.put("Button.foreground", PaletaColores.CREAM_WHITE);
        UIManager.put("Button.border", BorderFactory.createLineBorder(PaletaColores.ACCENT_GOLD, 1));
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 12));
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