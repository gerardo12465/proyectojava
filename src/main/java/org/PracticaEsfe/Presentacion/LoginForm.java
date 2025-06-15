package org.PracticaEsfe.Presentacion;

import org.PracticaEsfe.Dominio.Usuario;
import org.PracticaEsfe.Persistence.UserDAO;
import org.PracticaEsfe.Main;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

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
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panel.add(txtEmail);

        panel.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        btnLogin = new JButton("Iniciar Sesión");
        btnRegister = new JButton("Registrarse");

        panel.add(btnLogin);
        panel.add(btnRegister);

        // Mensaje para el usuario
        JLabel lblInfo = new JLabel("Ingrese sus credenciales o regístrese.", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 12));

        setLayout(new BorderLayout());
        add(lblInfo, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        // Action Listeners
        btnLogin.addActionListener(e -> attemptLogin());
        btnRegister.addActionListener(e -> attemptRegister());
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
                // Si el inicio de sesión es exitoso, cierra esta ventana y abre la principal
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

    private void attemptRegister() {
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un email y contraseña para registrarse.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = email.substring(0, email.indexOf('@'));

        Usuario newUser = new Usuario(0, nombre, email, password);

        try {

            Usuario existingUser = userDAO.findByEmail(email);
            if (existingUser != null) {
                JOptionPane.showMessageDialog(this, "Ya existe un usuario con este email. Por favor, inicie sesión o use otro email.", "Error de Registro", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Usuario registeredUser = userDAO.create(newUser);
            if (registeredUser != null) {
                JOptionPane.showMessageDialog(this, "Cuenta creada exitosamente para " + registeredUser.getNombre() + ". ¡Ya puede iniciar sesión!", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                clearFields(); // Limpia los campos después del registro exitoso
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo crear la cuenta. Inténtelo de nuevo.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos al registrar: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        txtEmail.setText("");
        txtPassword.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
