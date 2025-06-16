package org.PracticaEsfe;

import org.PracticaEsfe.Presentacion.Autorform;
import org.PracticaEsfe.Presentacion.LibroForm;
import org.PracticaEsfe.Presentacion.LoginForm;
import org.PracticaEsfe.Utilidades.PaletaColores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Main extends JFrame {

    private ImageIcon backgroundImage;

    public Main() {
        setTitle("Menú Principal de la Biblioteca");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            backgroundImage = new ImageIcon(getClass().getResource("/images/biblioteca.png"));
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen de fondo local 'biblioteca.png' para Main: " + e.getMessage());
            backgroundImage = new ImageIcon("https://placehold.co/700x500/8B4513/FFFFFF?text=Fondo no disponible");
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 25));
        buttonPanel.setBackground(new Color(0,0,0,0));

        JButton btnAutores = new JButton("Gestión de Autores");
        JButton btnLibros = new JButton("Gestión de Libros");

        styleButton(btnAutores);
        styleButton(btnLibros);

        btnAutores.addActionListener(e -> {
            dispose(); // Cierra la ventana actual de Main
            new Autorform().setVisible(true); // Abre la ventana de Autorform
        });
        btnLibros.addActionListener(e -> {
            dispose(); // Cierra la ventana actual de Main
            new LibroForm().setVisible(true); // Abre la ventana de LibroForm
        });

        buttonPanel.add(btnAutores);
        buttonPanel.add(btnLibros); // Asegúrate de que estos botones también tienen listeners si quieres que hagan algo

        mainContentPanel.add(buttonPanel, BorderLayout.CENTER);

        backgroundPanel.add(mainContentPanel);
        add(backgroundPanel);
    }

    private void styleButton(JButton button) {
        button.setBackground(PaletaColores.SOFT_BROWN); // ¡CAMBIO AQUÍ! Usa el nuevo color suave
        button.setForeground(PaletaColores.CREAM_WHITE);
        button.setFont(new Font("Serif", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(15, 30, 15, 30));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}