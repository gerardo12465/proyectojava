package org.PracticaEsfe.Presentacion;

import org.PracticaEsfe.Dominio.Usuario;
import org.PracticaEsfe.Persistence.UserDAO;
import org.PracticaEsfe.Utilidades.PaletaColores;
import org.PracticaEsfe.Utilidades.RoundedBorder; // Para los botones, si quieres el mismo estilo

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class ForgotPasswordForm extends JFrame {

    private JTextField txtEmail;
    private JButton btnRecoverPassword;
    private JButton btnBackToLogin;
    private UserDAO userDAO;
    private ImageIcon backgroundImage;

    public ForgotPasswordForm() {
        userDAO = new UserDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("Recuperar Contraseña");
        setSize(550, 400); // Ajusta el tamaño según necesidad
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana
        setLocationRelativeTo(null); // Centra la ventana

        // Cargar la imagen de fondo
        try {
            backgroundImage = new ImageIcon(getClass().getResource("/images/biblioteca.png"));
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen de fondo local 'biblioteca.png' para ForgotPasswordForm: " + e.getMessage());
            backgroundImage = new ImageIcon("https://placehold.co/550x400/8B4513/FFFFFF?text=Fondo+no+disponible");
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
        backgroundPanel.setLayout(new GridBagLayout()); // Para centrar el contenido

        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN);
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel lblTitle = new JLabel("Recuperar Contraseña", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Serif", Font.BOLD, 26));
        lblTitle.setForeground(PaletaColores.CREAM_WHITE);
        contentPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false); // Transparente
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5); // Espaciado

        // Instrucciones
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ocupa dos columnas
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel lblInstructions = new JLabel("Ingrese su email para recuperar su contraseña.");
        lblInstructions.setForeground(PaletaColores.CREAM_WHITE);
        lblInstructions.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(lblInstructions, gbc);

        // Email field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Vuelve a una columna
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(PaletaColores.CREAM_WHITE);
        lblEmail.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(lblEmail, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        txtEmail = new JTextField(25); // Un poco más ancho
        txtEmail.setBackground(PaletaColores.CREAM_WHITE);
        txtEmail.setForeground(PaletaColores.TEXT_DARK);
        txtEmail.setBorder(BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1));
        txtEmail.setPreferredSize(new Dimension(280, 35));
        inputPanel.add(txtEmail, gbc);

        contentPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0)); // Espaciado horizontal
        buttonPanel.setOpaque(false);

        btnRecoverPassword = new JButton("Recuperar Contraseña");
        btnRecoverPassword.setBackground(PaletaColores.PRIMARY_BROWN);
        btnRecoverPassword.setForeground(PaletaColores.CREAM_WHITE);
        btnRecoverPassword.setFont(new Font("Arial", Font.BOLD, 16));
        btnRecoverPassword.setFocusPainted(false);
        btnRecoverPassword.setBorder(new RoundedBorder(25, PaletaColores.PRIMARY_BROWN, 2)); // Con RoundedBorder
        btnRecoverPassword.setPreferredSize(new Dimension(220, 45));
        buttonPanel.add(btnRecoverPassword);

        btnBackToLogin = new JButton("Volver a Iniciar Sesión");
        btnBackToLogin.setBackground(PaletaColores.CREAM_WHITE); // Fondo suave
        btnBackToLogin.setForeground(PaletaColores.PRIMARY_BROWN); // Texto oscuro
        btnBackToLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnBackToLogin.setFocusPainted(false);
        btnBackToLogin.setBorder(new RoundedBorder(25, PaletaColores.PRIMARY_BROWN, 2));
        btnBackToLogin.setPreferredSize(new Dimension(220, 45));
        buttonPanel.add(btnBackToLogin);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        backgroundPanel.add(contentPanel); // Añadir el panel de contenido al panel de fondo
        add(backgroundPanel);

        // Action Listeners
        btnRecoverPassword.addActionListener(e -> attemptPasswordRecovery());
        btnBackToLogin.addActionListener(e -> {
            dispose(); // Cierra esta ventana
            new LoginForm().setVisible(true); // Vuelve a abrir el LoginForm
        });

        // Asegurarse de que el LoginForm se muestre de nuevo si esta ventana se cierra por la X
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                new LoginForm().setVisible(true); // Abre el LoginForm al cerrar
            }
        });
    }

    private void attemptPasswordRecovery() {
        String email = txtEmail.getText().trim();

        if (email.isEmpty()) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this, "Por favor, ingrese su dirección de email.", "Email Vacío", JOptionPane.WARNING_MESSAGE);
            resetJOptionPaneColors();
            return;
        }

        try {
            Usuario user = userDAO.findByEmail(email); // Busca el usuario solo por email
            if (user != null) {
                // *** SIMULACIÓN DE RECUPERACIÓN ***
                setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                JOptionPane.showMessageDialog(this,
                        "Si el email está registrado, se le ha enviado un correo con instrucciones para restablecer su contraseña.",
                        "Instrucciones Enviadas", JOptionPane.INFORMATION_MESSAGE);
                resetJOptionPaneColors();
                // En un sistema real aquí se enviaría un email con un enlace seguro o un código.
                // Por seguridad, no decimos si el email existe o no.
                dispose(); // Cierra esta ventana después de "enviar"
                new LoginForm().setVisible(true); // Vuelve al login
            } else {
                // Por motivos de seguridad, para evitar enumeración de usuarios,
                // el mensaje no debe indicar si el email existe o no.
                setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                JOptionPane.showMessageDialog(this,
                        "Si el email está registrado, se le ha enviado un correo con instrucciones para restablecer su contraseña.",
                        "Instrucciones Enviadas", JOptionPane.INFORMATION_MESSAGE);
                resetJOptionPaneColors();
                txtEmail.setText(""); // Limpia el campo
            }
        } catch (SQLException ex) {
            setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
            JOptionPane.showMessageDialog(this,
                    "Error de base de datos al intentar recuperar la contraseña: " + ex.getMessage(),
                    "Error SQL", JOptionPane.ERROR_MESSAGE);
            resetJOptionPaneColors();
            ex.printStackTrace();
        }
    }

    // Métodos para estilizar JOptionPane (copiados de LoginForm)
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

    // Método main para probar la ventana de recuperación directamente
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ForgotPasswordForm().setVisible(true);
        });
    }
}
