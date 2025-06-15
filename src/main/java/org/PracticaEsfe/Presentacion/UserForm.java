package org.PracticaEsfe.Presentacion;

import javax.swing.*;
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

    // Eliminamos la referencia a LoginForm para evitar circularidad
    // private LoginForm parentLoginForm;

    // Modificamos el constructor para que no reciba el LoginForm
    public UserForm() {
        super("Registro de Usuario");
        // this.parentLoginForm = parentLoginForm; // Ya no es necesario

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Solo cierra esta ventana

        setSize(400, 300);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(6, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        titleLabel = new JLabel("Registro de Nuevo Usuario");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 18));


        nameLabel = new JLabel("Nombre:");
        nameField = new JTextField(20);

        emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);

        passwordLabel = new JLabel("Contraseña:");
        passwordField = new JPasswordField(20);

        saveButton = new JButton("Guardar");
        cancelButton = new JButton("Cancelar");

        mainPanel.add(titleLabel);
        mainPanel.add(new JLabel(""));

        mainPanel.add(nameLabel);
        mainPanel.add(nameField);

        mainPanel.add(emailLabel);
        mainPanel.add(emailField);

        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);

        mainPanel.add(saveButton);
        mainPanel.add(cancelButton);

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

                        dispose(); // Cierra esta ventana (UserForm)
                        // No intentamos hacer visible el parentLoginForm aquí. Lo manejaremos desde LoginForm.
                        // if (parentLoginForm != null) {
                        //     parentLoginForm.setVisible(true);
                        // }

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
                dispose(); // Cierra esta ventana (UserForm)
                // No intentamos hacer visible el parentLoginForm aquí. Lo manejaremos desde LoginForm.
                // if (parentLoginForm != null) {
                //     parentLoginForm.setVisible(true);
                // }
            }
        });

        add(mainPanel);
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
