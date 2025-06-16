package org.PracticaEsfe;

import org.PracticaEsfe.Presentacion.Autorform;
import org.PracticaEsfe.Presentacion.LibroForm;
import org.PracticaEsfe.Presentacion.UserForm;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public Main() {
        setTitle("Menú Principal");
        setSize(500, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel para los botones, con FlowLayout (horizontal)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton btnAutores = new JButton("Autores");
        JButton btnLibros = new JButton("Libros");
        JButton btnUsuarios = new JButton("Usuarios");

        // Acciones para abrir formularios
        btnAutores.addActionListener(e -> new Autorform().setVisible(true));
        btnLibros.addActionListener(e -> new LibroForm().setVisible(true));
        btnUsuarios.addActionListener(e -> new UserForm().setVisible(true));

        panelBotones.add(btnAutores);
        panelBotones.add(btnLibros);
        panelBotones.add(btnUsuarios);

        // Agregamos el panel a la parte superior
        setLayout(new BorderLayout());
        add(panelBotones, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}


//Aaron Juarez, Hola buenos Dias participe en el proyecto.
//jazmin lue, hola buenas noches participe en el proyecto

// Gerardo Barrientos, participe en el poyecto hoy tambien