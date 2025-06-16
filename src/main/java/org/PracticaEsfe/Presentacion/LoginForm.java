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

    private ImageIcon backgroundImage; // Declarada como campo de clase

    public LoginForm() {
        userDAO = new UserDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("Iniciar Sesión");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Carga la imagen de fondo una sola vez y la asigna a la variable de instancia
        try {
            backgroundImage = new ImageIcon(getClass().getResource("/images/biblioteca.png"));
            // Verifica si la imagen se cargó correctamente
            if (backgroundImage.getImageLoadStatus() == MediaTracker.ERRORED) {
                System.err.println("Error al cargar la imagen de fondo desde recursos: " + getClass().getResource("/images/biblioteca.png"));
                // Fallback a una imagen de placeholder si no se encuentra o hay error
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
                // Dibuja la imagen de fondo o un color si la imagen no está disponible
                if (backgroundImage != null && backgroundImage.getImage() != null) {
                    g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(PaletaColores.PRIMARY_BROWN); // Color de fallback
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        JPanel loginContentPanel = new JPanel(new BorderLayout(10, 10));
        loginContentPanel.setBackground(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN); // Color semitransparente
        loginContentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Bienvenido a la Biblioteca", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Serif", Font.BOLD, 28));
        lblTitle.setForeground(PaletaColores.CREAM_WHITE);
        lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        loginContentPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel inputFieldsPanel = new JPanel(new GridBagLayout());
        inputFieldsPanel.setOpaque(false); // Transparente para que se vea el fondo
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0); // Espaciado entre componentes

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Color labelColor = PaletaColores.CREAM_WHITE;
        Dimension fieldSize = new Dimension(250, 35); // Tamaño estándar para campos

        // Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(labelColor);
        lblEmail.setFont(labelFont);
        inputFieldsPanel.add(lblEmail, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        txtEmail = new JTextField(20);
        txtEmail.setBackground(PaletaColores.CREAM_WHITE);
        txtEmail.setForeground(PaletaColores.TEXT_DARK);
        txtEmail.setBorder(BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1));
        txtEmail.setPreferredSize(fieldSize);
        inputFieldsPanel.add(txtEmail, gbc);

        // Contraseña
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setForeground(labelColor);
        lblPassword.setFont(labelFont);
        inputFieldsPanel.add(lblPassword, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        txtPassword = new JPasswordField(20);
        txtPassword.setBackground(PaletaColores.CREAM_WHITE);
        txtPassword.setForeground(PaletaColores.TEXT_DARK);
        txtPassword.setBorder(BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1));
        txtPassword.setPreferredSize(fieldSize);
        inputFieldsPanel.add(txtPassword, gbc);

        // Panel para el botón de login
        JPanel loginButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginButtonPanel.setOpaque(false);
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBackground(PaletaColores.PRIMARY_BROWN); // Color de fondo del botón
        btnLogin.setForeground(PaletaColores.CREAM_WHITE); // Color del texto del botón
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(new RoundedBorder(25, PaletaColores.PRIMARY_BROWN, 2)); // Borde redondeado
        btnLogin.setPreferredSize(new Dimension(250, 50));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cambia el cursor al pasar el mouse
        loginButtonPanel.add(btnLogin);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

        centerPanel.add(inputFieldsPanel);
        centerPanel.add(Box.createVerticalStrut(20)); // Espacio entre campos y botón
        centerPanel.add(loginButtonPanel);

        loginContentPanel.add(centerPanel, BorderLayout.CENTER);

        // Footer Panel con los links
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
                dispose(); // Cierra el LoginForm
                // Abre ForgotPasswordForm, pasándole la instancia actual de LoginForm
                new ForgotPasswordForm(LoginForm.this).setVisible(true);
            }
        });
        footerPanel.add(lblForgotPassword);

        // Pregunta de registro y link
        JLabel lblQuestion = new JLabel("¿Es tu primera vez? ");
        lblQuestion.setForeground(PaletaColores.CREAM_WHITE);
        lblQuestion.setFont(new Font("Arial", Font.PLAIN, 12));
        footerPanel.add(lblQuestion);

        JLabel lblRegisterLink = new JLabel("<html><u>Regístrate aquí</u></html>");
        lblRegisterLink.setForeground(PaletaColores.ACCENT_GOLD); // Color de acento para el link
        lblRegisterLink.setFont(new Font("Arial", Font.BOLD, 12));
        lblRegisterLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblRegisterLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose(); // Cierra el LoginForm
                // Abre UserForm, pasándole la instancia actual de LoginForm
                UserForm userForm = new UserForm(LoginForm.this);
                userForm.setVisible(true);
                // No necesitamos WindowListener aquí, ya que UserForm maneja el retorno.
            }
        });
        footerPanel.add(lblRegisterLink);

        loginContentPanel.add(footerPanel, BorderLayout.SOUTH);

        backgroundPanel.add(loginContentPanel);
        add(backgroundPanel);

        // Acción del botón de login
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
                // Abre la ventana principal de la aplicación
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

    // Método público para limpiar los campos, llamado desde UserForm y ForgotPasswordForm
    public void clearFields() {
        txtEmail.setText("");
        txtPassword.setText("");
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}