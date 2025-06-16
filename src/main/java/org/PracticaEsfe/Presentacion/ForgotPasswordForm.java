package org.PracticaEsfe.Presentacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.PracticaEsfe.Utilidades.PaletaColores;
import org.PracticaEsfe.Utilidades.RoundedBorder;

public class ForgotPasswordForm extends JFrame {

    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel emailLabel;
    private JTextField emailField;
    private JButton resetButton;
    private JButton cancelButton;

    private ImageIcon backgroundImage;
    private LoginForm parentLoginForm; // Referencia al LoginForm padre

    // Constructor sin argumentos (para pruebas o si no se necesita el parentLoginForm)
    public ForgotPasswordForm() {
        this(null); // Llama al constructor principal con null
    }

    // Constructor principal que acepta el LoginForm padre
    public ForgotPasswordForm(LoginForm parentLoginForm) {
        super("Recuperar Contraseña");
        this.parentLoginForm = parentLoginForm; // Guarda la referencia

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500); // Mismo tamaño
        setLocationRelativeTo(null);

        // Intenta cargar la imagen de fondo, similar a LoginForm y UserForm
        try {
            backgroundImage = new ImageIcon(getClass().getResource("/images/biblioteca.png"));
            if (backgroundImage.getImageLoadStatus() == MediaTracker.ERRORED) {
                System.err.println("Error al cargar la imagen de fondo: " + getClass().getResource("/images/biblioteca.png"));
                backgroundImage = new ImageIcon("https://placehold.co/700x500/8B4513/FFFFFF?text=Fondo+no+disponible");
            }
        } catch (Exception e) {
            System.err.println("Excepción al cargar la imagen de fondo local 'biblioteca.png': " + e.getMessage());
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

        titleLabel = new JLabel("Recuperar Contraseña", SwingConstants.CENTER);
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

        // Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        emailLabel = new JLabel("Email:");
        emailLabel.setForeground(labelColor);
        emailLabel.setFont(labelFont);
        inputFieldsPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        emailField = new JTextField(20);
        emailField.setBackground(PaletaColores.CREAM_WHITE);
        emailField.setForeground(PaletaColores.TEXT_DARK);
        emailField.setBorder(BorderFactory.createLineBorder(PaletaColores.PRIMARY_BROWN, 1));
        emailField.setPreferredSize(fieldSize);
        inputFieldsPanel.add(emailField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        resetButton = new JButton("Restablecer");
        resetButton.setBackground(PaletaColores.PRIMARY_BROWN);
        resetButton.setForeground(PaletaColores.CREAM_WHITE);
        resetButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetButton.setFocusPainted(false);
        resetButton.setBorder(new RoundedBorder(25, PaletaColores.PRIMARY_BROWN, 2));
        resetButton.setPreferredSize(new Dimension(150, 50));
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonPanel.add(resetButton);

        cancelButton = new JButton("Cancelar");
        cancelButton.setBackground(PaletaColores.PRIMARY_BROWN);
        cancelButton.setForeground(PaletaColores.CREAM_WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 16));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(new RoundedBorder(25, PaletaColores.PRIMARY_BROWN, 2));
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

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                if (email.isEmpty()) {
                    setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                    JOptionPane.showMessageDialog(mainPanel, "Por favor, ingrese su email.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
                    resetJOptionPaneColors();
                    return;
                }
                // Aquí iría la lógica para enviar el correo de restablecimiento
                setJOptionPaneColors(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN, PaletaColores.CREAM_WHITE);
                JOptionPane.showMessageDialog(mainPanel, "Se ha enviado un enlace para restablecer la contraseña a " + email, "Correo Enviado", JOptionPane.INFORMATION_MESSAGE);
                resetJOptionPaneColors();
                dispose();
                if (parentLoginForm != null) {
                    parentLoginForm.setVisible(true);
                    parentLoginForm.clearFields();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                if (parentLoginForm != null) {
                    parentLoginForm.setVisible(true);
                    parentLoginForm.clearFields();
                }
            }
        });

        // Añadir WindowListener para manejar el cierre de la ventana ForgotPasswordForm
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (parentLoginForm != null) {
                    parentLoginForm.setVisible(true);
                    parentLoginForm.clearFields();
                }
            }
        });
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
            new ForgotPasswordForm().setVisible(true);
        });
    }
}