package org.PracticaEsfe.Presentacion;

import org.PracticaEsfe.Dominio.Usuario;
import org.PracticaEsfe.Main;
import org.PracticaEsfe.Persistence.UserDAO;
import org.PracticaEsfe.Utilidades.PaletaColores;
import org.PracticaEsfe.Utilidades.RoundedBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class LoginForm extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private UserDAO userDAO;

    private ImageIcon backgroundImage;

    public LoginForm() {
        userDAO = new UserDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("Iniciar Sesión");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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
                if (backgroundImage != null && backgroundImage.getImage() != null) {
                    g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(PaletaColores.PRIMARY_BROWN);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        JPanel loginContentPanel = new JPanel(new BorderLayout(10, 10));
        loginContentPanel.setBackground(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN);
        loginContentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Bienvenido a la Biblioteca", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Serif", Font.BOLD, 28));
        lblTitle.setForeground(PaletaColores.CREAM_WHITE);
        lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        loginContentPanel.add(lblTitle, BorderLayout.NORTH);

        // **INICIO DE CAMBIO: Usar GridBagLayout para los campos de entrada**
        JPanel inputFieldsPanel = new JPanel(new GridBagLayout());
        inputFieldsPanel.setOpaque(false); // Hacer transparente para que se vea el fondo
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0); // Espaciado entre componentes

        // Configuración común para las etiquetas
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Color labelColor = PaletaColores.CREAM_WHITE;
        Dimension fieldSize = new Dimension(250, 35); // Ancho y alto deseado para los campos

        // Email
        gbc.gridx = 0; // Columna 0
        gbc.gridy = 0; // Fila 0
        gbc.anchor = GridBagConstraints.EAST; // Alinea a la derecha
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(labelColor);
        lblEmail.setFont(labelFont);
        inputFieldsPanel.add(lblEmail, gbc);

        gbc.gridx = 1; // Columna 1
        gbc.gridy = 0; // Fila 0
        gbc.anchor = GridBagConstraints.WEST; // Alinea a la izquierda
        txtEmail = new JTextField(20);
        txtEmail.setBackground(PaletaColores.CREAM_WHITE);
        txtEmail.setForeground(PaletaColores.TEXT_DARK);
        txtEmail.setBorder(BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1));
        txtEmail.setPreferredSize(fieldSize); // Aplicar tamaño
        inputFieldsPanel.add(txtEmail, gbc);

        // Contraseña
        gbc.gridx = 0; // Columna 0
        gbc.gridy = 1; // Fila 1
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setForeground(labelColor);
        lblPassword.setFont(labelFont);
        inputFieldsPanel.add(lblPassword, gbc);

        gbc.gridx = 1; // Columna 1
        gbc.gridy = 1; // Fila 1
        gbc.anchor = GridBagConstraints.WEST;
        txtPassword = new JPasswordField(20);
        txtPassword.setBackground(PaletaColores.CREAM_WHITE);
        txtPassword.setForeground(PaletaColores.TEXT_DARK);
        txtPassword.setBorder(BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1));
        txtPassword.setPreferredSize(fieldSize); // Aplicar tamaño
        inputFieldsPanel.add(txtPassword, gbc);

        // Panel para el botón de login (ahora en su propio panel para centrarlo)
        JPanel loginButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginButtonPanel.setOpaque(false);
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBackground(PaletaColores.PRIMARY_BROWN);
        btnLogin.setForeground(PaletaColores.CREAM_WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(new RoundedBorder(25, PaletaColores.PRIMARY_BROWN, 2));
        btnLogin.setPreferredSize(new Dimension(250, 50));
        loginButtonPanel.add(btnLogin);

        // Usar un JPanel para agrupar los campos y el botón de login en el centro
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); // Apila verticalmente
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(15, 0, 15, 0)); // Espaciado vertical

        centerPanel.add(inputFieldsPanel); // Agrega el panel con los campos
        centerPanel.add(Box.createVerticalStrut(20)); // Espacio entre campos y botón
        centerPanel.add(loginButtonPanel); // Agrega el botón de login

        loginContentPanel.add(centerPanel, BorderLayout.CENTER);
        // **FIN DE CAMBIO**

        // Footer Panel con el link de registro y el link de "Olvidó su contraseña"
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Link "¿Olvidó su contraseña?"
        JLabel lblForgotPassword = new JLabel("¿Olvidó su contraseña?");
        lblForgotPassword.setForeground(PaletaColores.CREAM_WHITE);
        lblForgotPassword.setFont(new Font("Arial", Font.ITALIC, 12));
        lblForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblForgotPassword.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                // **CAMBIO AQUÍ: Abre la nueva ventana de recuperación de contraseña**
                dispose(); // Cierra el LoginForm
                new ForgotPasswordForm().setVisible(true); // Abre la nueva ventana
            }
        });
        footerPanel.add(lblForgotPassword);


        // Nueva pregunta y link de registro
        JLabel lblQuestion = new JLabel("¿Es tu primera vez? ");
        lblQuestion.setForeground(PaletaColores.CREAM_WHITE);
        lblQuestion.setFont(new Font("Arial", Font.PLAIN, 12));
        footerPanel.add(lblQuestion);

        JLabel lblRegisterLink = new JLabel("<html><u>Regístrate aquí</u></html>");
        lblRegisterLink.setForeground(PaletaColores.ACCENT_GOLD);
        lblRegisterLink.setFont(new Font("Arial", Font.BOLD, 12));
        lblRegisterLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblRegisterLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                UserForm userForm = new UserForm();
                userForm.setVisible(true);
                userForm.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        clearFields();
                        LoginForm.this.setVisible(true);
                    }
                });
            }
        });
        footerPanel.add(lblRegisterLink);

        loginContentPanel.add(footerPanel, BorderLayout.SOUTH);

        backgroundPanel.add(loginContentPanel);
        add(backgroundPanel);

        btnLogin.addActionListener(e -> attemptLogin());
    }

    private void attemptLogin() {
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Por favor, ingrese su email y contraseña.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            resetJOptionPaneColors();
            return;
        }

        try {
            Usuario user = userDAO.findByEmailAndPassword(email, password);
            if (user != null) {
                setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                JOptionPane.showMessageDialog(this, "¡Bienvenido, " + user.getNombre() + "!", "Inicio de Sesión Exitoso", JOptionPane.INFORMATION_MESSAGE);
                resetJOptionPaneColors();
                this.dispose();
                new Main().setVisible(true);
            } else {
                setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas. Verifique su email y contraseña.", "Error de Inicio de Sesión", JOptionPane.ERROR_MESSAGE);
                resetJOptionPaneColors();
            }
        } catch (SQLException ex) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Error de base de datos al iniciar sesión: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            resetJOptionPaneColors();
        }
    }

    public void clearFields() {
        txtEmail.setText("");
        txtPassword.setText("");
    }

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}