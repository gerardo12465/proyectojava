package org.PracticaEsfe.Presentacion;

import org.PracticaEsfe.Dominio.Usuario;
import org.PracticaEsfe.Persistence.UserDAO;
import org.PracticaEsfe.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginForm extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;
    private UserDAO userDAO;

    public LoginForm() {
        userDAO = new UserDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("Iniciar Sesión / Registrarse");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color primaryBrown = new Color(101, 67, 33);
        Color lightBrown = new Color(188, 152, 126);
        Color creamWhite = new Color(245, 245, 220);
        Color accentGold = new Color(212, 175, 55);
        Color textDark = new Color(50, 50, 50);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(primaryBrown);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Bienvenido a la Biblioteca", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Serif", Font.BOLD, 24));
        lblTitle.setForeground(creamWhite);
        lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel inputAndButtonPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        inputAndButtonPanel.setBackground(lightBrown);
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
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(new EmptyBorder(10, 20, 10, 20));
        inputAndButtonPanel.add(btnLogin);

        btnRegister = new JButton("Registrarse");
        btnRegister.setBackground(primaryBrown);
        btnRegister.setForeground(creamWhite);
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegister.setFocusPainted(false);
        btnRegister.setBorder(new EmptyBorder(10, 20, 10, 20));
        inputAndButtonPanel.add(btnRegister);

        mainPanel.add(inputAndButtonPanel, BorderLayout.CENTER);

        JLabel lblInfo = new JLabel("Ingrese sus credenciales o regístrese para continuar.", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblInfo.setForeground(creamWhite);
        lblInfo.setBorder(new EmptyBorder(10, 0, 0, 0));
        mainPanel.add(lblInfo, BorderLayout.SOUTH);

        add(mainPanel);

        btnLogin.addActionListener(e -> attemptLogin());
        btnRegister.addActionListener(e -> {
            this.setVisible(false);
            UserForm userForm = new UserForm(); // Correcto: Llama al constructor sin argumentos
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
            JOptionPane.showMessageDialog(this, "Por favor, ingrese su email y contraseña.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Usuario user = userDAO.findByEmailAndPassword(email, password);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "¡Bienvenido, " + user.getNombre() + "!", "Inicio de Sesión Exitoso", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                new Main().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas. Verifique su email y contraseña.", "Error de Inicio de Sesión", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos al iniciar sesión: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        txtEmail.setText("");
        txtPassword.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}

