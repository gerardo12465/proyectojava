package org.PracticaEsfe;

import org.PracticaEsfe.Presentacion.Autorform;
import org.PracticaEsfe.Presentacion.LibroForm;
import org.PracticaEsfe.Presentacion.UserForm;
import org.PracticaEsfe.Presentacion.PrestamoForm;
import org.PracticaEsfe.Presentacion.LoginForm;
import org.PracticaEsfe.Utilidades.PaletaColores;
import org.PracticaEsfe.Utilidades.RoundedBorder; // Aún lo importamos, pero el borde será del mismo color que el fondo

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Main extends JFrame {

    private ImageIcon backgroundImage;

    public Main() {
        setTitle("Menú Principal de la Biblioteca");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Carga la imagen de fondo una sola vez
        try {
            backgroundImage = new ImageIcon(getClass().getResource("/images/biblioteca.png"));
            if (backgroundImage.getImageLoadStatus() == MediaTracker.ERRORED) {
                System.err.println("Error al cargar la imagen de fondo local 'biblioteca.png' para Main: " + getClass().getResource("/images/biblioteca.png"));
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

        JPanel mainContentPanel = new JPanel(new BorderLayout(20, 20));
        mainContentPanel.setBackground(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN);
        mainContentPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel menuTitle = new JLabel("Menú de Administración", SwingConstants.CENTER);
        menuTitle.setFont(new Font("Serif", Font.BOLD, 32));
        menuTitle.setForeground(PaletaColores.CREAM_WHITE);
        menuTitle.setBorder(new EmptyBorder(0, 0, 30, 0));
        mainContentPanel.add(menuTitle, BorderLayout.NORTH);

        JPanel buttonGridPanel = new JPanel(new GridLayout(2, 2, 25, 25));
        buttonGridPanel.setOpaque(false);

        // Botones
        JButton btnAutores = new JButton("Gestión de Autores");
        JButton btnLibros = new JButton("Gestión de Libros");
        JButton btnUsuarios = new JButton("Gestión de Usuarios");
        JButton btnPrestamos = new JButton("Gestión de Préstamos");

        // Estilizar y añadir acciones a los botones
        styleButton(btnAutores);
        btnAutores.addActionListener(e -> {
            dispose();
            new Autorform().setVisible(true);
        });
        buttonGridPanel.add(btnAutores);

        styleButton(btnLibros);
        btnLibros.addActionListener(e -> {
            dispose();
            new LibroForm().setVisible(true);
        });
        buttonGridPanel.add(btnLibros);

        styleButton(btnUsuarios);
        btnUsuarios.addActionListener(e -> {
            dispose();
            new UserForm().setVisible(true);
        });
        buttonGridPanel.add(btnUsuarios);

        styleButton(btnPrestamos);
        btnPrestamos.addActionListener(e -> {
            dispose();
            new PrestamoForm().setVisible(true);
        });
        buttonGridPanel.add(btnPrestamos);

        mainContentPanel.add(buttonGridPanel, BorderLayout.CENTER);

        backgroundPanel.add(mainContentPanel);
        add(backgroundPanel);

        // Notas de los participantes
        // Aaron Juarez, Hola buenos Dias participe en el proyecto.
        // jazmin lue, hola buenas noches participe en el proyecto
        // Gerardo Barrientos, participe en el poyecto hoy tambien
    }

    // Método para aplicar estilos a los botones
    private void styleButton(JButton button) {
        button.setBackground(PaletaColores.PRIMARY_BROWN);
        button.setForeground(PaletaColores.TEXT_DARK); // CAMBIO: Color del texto a TEXT_DARK
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setFocusPainted(false);
        // CAMBIO: El borde ahora usa PRIMARY_BROWN, el mismo color que el fondo, haciéndolo invisible
        // Si quieres un borde completamente invisible, puedes usar BorderFactory.createEmptyBorder()
        button.setBorder(new RoundedBorder(25, PaletaColores.PRIMARY_BROWN, 2));
        button.setPreferredSize(new Dimension(200, 60));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}