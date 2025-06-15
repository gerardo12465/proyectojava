package org.PracticaEsfe.Presentacion;

import org.PracticaEsfe.Dominio.Usuario;
import org.PracticaEsfe.Main;
import org.PracticaEsfe.Persistence.UserDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class LoginForm extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;
    private UserDAO userDAO;

    private Color primaryBrown = new Color(101, 67, 33);
    private Color lightBrown = new Color(188, 152, 126);
    private Color creamWhite = new Color(245, 245, 220);
    private Color accentGold = new Color(212, 175, 55);
    private Color textDark = new Color(50, 50, 50);

    private ImageIcon backgroundImage;

    public LoginForm() {
        userDAO = new UserDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("Iniciar Sesión / Registrarse");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configuración de la imagen de fondo
        try {
            // CAMBIO: Usar el nombre de archivo 'biblioteca.png'
            backgroundImage = new ImageIcon(getClass().getResource("/images/biblioteca.png"));
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen de fondo local 'biblioteca.png': " + e.getMessage());
            // Fallback: usar una imagen de placeholder si no se puede cargar
            backgroundImage = new ImageIcon("https://placehold.co/700x500/8B4513/FFFFFF?text=Fondo+no+disponible");
        }

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null && backgroundImage.getImage() != null) {
                    g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(150, 100, 50, 255));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        Color semiTransparentLightBrown = new Color(lightBrown.getRed(), lightBrown.getGreen(), lightBrown.getBlue(), 200);
        Color semiTransparentPrimaryBrown = new Color(primaryBrown.getRed(), primaryBrown.getGreen(), primaryBrown.getBlue(), 200);

        JPanel loginContentPanel = new JPanel(new BorderLayout(10, 10));
        loginContentPanel.setBackground(semiTransparentPrimaryBrown);
        loginContentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Bienvenido a la Biblioteca", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Serif", Font.BOLD, 28));
        lblTitle.setForeground(creamWhite);
        lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        loginContentPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel inputAndButtonPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        inputAndButtonPanel.setBackground(semiTransparentLightBrown);
        inputAndButtonPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primaryBrown, 5),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(textDark);
        lblEmail.setFont(new Font("Arial", Font.BOLD, 14));
        inputAndButtonPanel.add(lblEmail);
        txtEmail = new JTextField(20);
        txtEmail.setBackground(creamWhite);
        txtEmail.setForeground(textDark);
        txtEmail.setBorder(BorderFactory.createLineBorder(primaryBrown, 1));
        inputAndButtonPanel.add(txtEmail);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setForeground(textDark);
        lblPassword.setFont(new Font("Arial", Font.BOLD, 14));
        inputAndButtonPanel.add(lblPassword);
        txtPassword = new JPasswordField(20);
        txtPassword.setBackground(creamWhite);
        txtPassword.setForeground(textDark);
        txtPassword.setBorder(BorderFactory.createLineBorder(primaryBrown, 1));
        inputAndButtonPanel.add(txtPassword);

        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBackground(primaryBrown);
        btnLogin.setForeground(creamWhite);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(new EmptyBorder(12, 25, 12, 25));
        inputAndButtonPanel.add(btnLogin);

        btnRegister = new JButton("Registrarse");
        btnRegister.setBackground(primaryBrown);
        btnRegister.setForeground(creamWhite);
        btnRegister.setFont(new Font("Arial", Font.BOLD, 16));
        btnRegister.setFocusPainted(false);
        btnRegister.setBorder(new EmptyBorder(12, 25, 12, 25));
        inputAndButtonPanel.add(btnRegister);

        loginContentPanel.add(inputAndButtonPanel, BorderLayout.CENTER);

        JLabel lblInfo = new JLabel("Ingrese sus credenciales o regístrese para continuar.", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblInfo.setForeground(creamWhite);
        lblInfo.setBorder(new EmptyBorder(10, 0, 0, 0));
        loginContentPanel.add(lblInfo, BorderLayout.SOUTH);

        backgroundPanel.add(loginContentPanel);
        add(backgroundPanel);

        btnLogin.addActionListener(e -> attemptLogin());
        btnRegister.addActionListener(e -> {
            this.setVisible(false);
            UserForm userForm = new UserForm();
            userForm.setVisible(true);

            userForm.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    clearFields();
                    LoginForm.this.setVisible(true);
                }
            });
        });
    }

    private void attemptLogin() {
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            setJOptionPaneColors(new Color(primaryBrown.getRed(), primaryBrown.getGreen(), primaryBrown.getBlue(), 200), creamWhite);
            JOptionPane.showMessageDialog(this, "Por favor, ingrese su email y contraseña.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            resetJOptionPaneColors();
            return;
        }

        try {
            Usuario user = userDAO.findByEmailAndPassword(email, password);
            if (user != null) {
                setJOptionPaneColors(new Color(primaryBrown.getRed(), primaryBrown.getGreen(), primaryBrown.getBlue(), 200), creamWhite);
                JOptionPane.showMessageDialog(this, "¡Bienvenido, " + user.getNombre() + "!", "Inicio de Sesión Exitoso", JOptionPane.INFORMATION_MESSAGE);
                resetJOptionPaneColors();
                this.dispose();
                new Main().setVisible(true);
            } else {
                setJOptionPaneColors(new Color(primaryBrown.getRed(), primaryBrown.getGreen(), primaryBrown.getBlue(), 200), creamWhite);
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas. Verifique su email y contraseña.", "Error de Inicio de Sesión", JOptionPane.ERROR_MESSAGE);
                resetJOptionPaneColors();
            }
        } catch (SQLException ex) {
            setJOptionPaneColors(new Color(primaryBrown.getRed(), primaryBrown.getGreen(), primaryBrown.getBlue(), 200), creamWhite);
            JOptionPane.showMessageDialog(this, "Error de base de datos al iniciar sesión: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            resetJOptionPaneColors();
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        txtEmail.setText("");
        txtPassword.setText("");
    }

    private void setJOptionPaneColors(Color backgroundColor, Color foregroundColor) {
        UIManager.put("OptionPane.background", backgroundColor);
        UIManager.put("Panel.background", backgroundColor);
        UIManager.put("OptionPane.messageForeground", foregroundColor);
        UIManager.put("Button.background", new Color(primaryBrown.getRed(), primaryBrown.getGreen(), primaryBrown.getBlue(), 255));
        UIManager.put("Button.foreground", creamWhite);
        UIManager.put("Button.border", BorderFactory.createLineBorder(accentGold, 1));
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
