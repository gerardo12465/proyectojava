package org.PracticaEsfe.Presentacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import org.PracticaEsfe.Dominio.Usuario;
import org.PracticaEsfe.Persistence.UserDAO;

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

    public UserForm() {
        super("Registro de Usuario");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setSize(450, 380);
        setLocationRelativeTo(null);

        Color primaryBrown = new Color(101, 67, 33);
        Color lightBrown = new Color(188, 152, 126);
        Color creamWhite = new Color(245, 245, 220);
        Color accentGold = new Color(212, 175, 55);
        Color textDark = new Color(50, 50, 50);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(primaryBrown);
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        titleLabel = new JLabel("Registro de Nuevo Usuario", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(creamWhite);
        titleLabel.setBorder(new EmptyBorder(10, 0, 15, 0));

        JPanel inputAndButtonPanel = new JPanel(new GridLayout(5, 2, 15, 15));
        inputAndButtonPanel.setBackground(lightBrown);
        inputAndButtonPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primaryBrown, 5),
                new EmptyBorder(15, 15, 15, 15)
        ));

        nameLabel = new JLabel("Nombre:");
        nameLabel.setForeground(textDark);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputAndButtonPanel.add(nameLabel);
        nameField = new JTextField(20);
        nameField.setBackground(creamWhite);
        nameField.setForeground(textDark);
        nameField.setBorder(BorderFactory.createLineBorder(primaryBrown, 1));
        inputAndButtonPanel.add(nameField);

        emailLabel = new JLabel("Email:");
        emailLabel.setForeground(textDark);
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputAndButtonPanel.add(emailLabel);
        emailField = new JTextField(20);
        emailField.setBackground(creamWhite);
        emailField.setForeground(textDark);
        emailField.setBorder(BorderFactory.createLineBorder(primaryBrown, 1));
        inputAndButtonPanel.add(emailField);

        passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setForeground(textDark);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputAndButtonPanel.add(passwordLabel);
        passwordField = new JPasswordField(20);
        passwordField.setBackground(creamWhite);
        passwordField.setForeground(textDark);
        passwordField.setBorder(BorderFactory.createLineBorder(primaryBrown, 1));
        inputAndButtonPanel.add(passwordField);

        saveButton = new JButton("Guardar");
        saveButton.setBackground(primaryBrown);
        saveButton.setForeground(creamWhite);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setFocusPainted(false);
        saveButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        inputAndButtonPanel.add(saveButton);

        cancelButton = new JButton("Cancelar");
        cancelButton.setBackground(primaryBrown);
        cancelButton.setForeground(creamWhite);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        inputAndButtonPanel.add(cancelButton);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(inputAndButtonPanel, BorderLayout.CENTER);


        add(mainPanel);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(mainPanel,
                            "Todos los campos son obligatorios.",
                            "Error de Validación",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                UserDAO userDAO = new UserDAO();

                try {
                    Usuario existingUser = userDAO.findByEmail(email);
                    if (existingUser != null) {
                        JOptionPane.showMessageDialog(mainPanel,
                                "Ya existe un usuario con este email. Por favor, use otro email.",
                                "Error de Registro",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    Usuario newUser = new Usuario(0, name, email, password);
                    Usuario createdUser = userDAO.create(newUser);

                    if (createdUser != null && createdUser.getId() > 0) {
                        JOptionPane.showMessageDialog(mainPanel,
                                "Usuario '" + createdUser.getNombre() + "' registrado con éxito. Ahora puede iniciar sesión.",
                                "Registro Exitoso",
                                JOptionPane.INFORMATION_MESSAGE);

                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(mainPanel,
                                "Error al registrar el usuario. No se pudo obtener el ID.",
                                "Error de Registro",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(mainPanel,
                            "Error de base de datos al registrar el usuario: " + ex.getMessage(),
                            "Error de Base de Datos",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
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
}

