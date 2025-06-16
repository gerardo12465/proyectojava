package org.PracticaEsfe;

import org.PracticaEsfe.Presentacion.Autorform;
import org.PracticaEsfe.Presentacion.LibroForm;
import org.PracticaEsfe.Presentacion.PrestamoForm;
import org.PracticaEsfe.Presentacion.LoginForm;
import org.PracticaEsfe.Utilidades.PaletaColores;
import org.PracticaEsfe.Utilidades.RoundedBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Main extends JFrame {

    private ImageIcon backgroundImage;
    private JPanel cardPanel; // Panel que contendrá las diferentes "tarjetas" (formularios)
    private CardLayout cardLayout; // Gestor de layout para alternar entre paneles

    public Main() {
        setTitle("Menú Principal de la Biblioteca");
        setSize(1000, 750); // Mantiene el tamaño del marco principal para acomodar los formularios
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Carga la imagen de fondo una sola vez
        try {
            backgroundImage = new ImageIcon(getClass().getResource("/images/biblioteca.png"));
            if (backgroundImage.getImageLoadStatus() == MediaTracker.ERRORED) {
                System.err.println("Error al cargar la imagen de fondo local 'biblioteca.png' para Main: " + getClass().getResource("/images/biblioteca.png"));
                // Fallback image if local fails
                backgroundImage = new ImageIcon("https://placehold.co/1000x750/8B4513/FFFFFF?text=Fondo+no+disponible");
            }
        } catch (Exception e) {
            System.err.println("Excepción al cargar la imagen de fondo local 'biblioteca.png': " + e.getMessage());
            // Fallback image if exception occurs
            backgroundImage = new ImageIcon("https://placehold.co/1000x750/8B4513/FFFFFF?text=Fondo+no+disponible");
        }

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null && backgroundImage.getImage() != null) {
                    // Dibuja la imagen escalada para ajustarse al panel
                    g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(PaletaColores.PRIMARY_BROWN);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout()); // Usar BorderLayout para backgroundPanel

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false); // Hacer el cardPanel transparente para que se vea el backgroundPanel

        // Configuración del Panel del Menú Principal (contenido original de Main)
        JPanel mainMenuPanel = createMainMenuPanel();

        // --- Wrapped mainMenuPanel to center it and apply its own rounded border ---
        JPanel centeredMainMenuWrapper = new JPanel(new GridBagLayout());
        centeredMainMenuWrapper.setOpaque(false); // Make wrapper transparent
        centeredMainMenuWrapper.add(mainMenuPanel); // Add the main menu panel to the wrapper
        // --- END Wrapper ---

        cardPanel.add(centeredMainMenuWrapper, "MainMenu"); // Add the wrapper as a card

        // Inicializar y añadir otros formularios como tarjetas
        // Pasar 'this' (el marco Main) a los constructores de los paneles de formulario
        Autorform autorFormPanel = new Autorform(this);
        LibroForm libroFormPanel = new LibroForm(this);
        // UserForm userFormPanel = new UserForm(this); // Eliminado (as per your original code)
        PrestamoForm prestamoFormPanel = new PrestamoForm(this);

        cardPanel.add(autorFormPanel, "AutorForm");
        cardPanel.add(libroFormPanel, "LibroForm");
        // cardPanel.add(userFormPanel, "UserForm"); // Eliminado
        cardPanel.add(prestamoFormPanel, "PrestamoForm");

        backgroundPanel.add(cardPanel, BorderLayout.CENTER);
        add(backgroundPanel);

        // Mostrar el menú principal inicialmente
        cardLayout.show(cardPanel, "MainMenu");
    }

    // Método que crea y devuelve el panel del menú principal
    private JPanel createMainMenuPanel() {
        JPanel mainContentPanel = new JPanel(new BorderLayout(20, 20));
        mainContentPanel.setBackground(PaletaColores.SEMI_TRANSPARENT_PRIMARY_BROWN);
        mainContentPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        new RoundedBorder(30, PaletaColores.PRIMARY_BROWN, 2), // Adjust corner radius as needed
                        new EmptyBorder(40, 40, 40, 40) // Original padding
                )
        );

        JLabel menuTitle = new JLabel("Menú de Administración", SwingConstants.CENTER);
        menuTitle.setFont(new Font("Serif", Font.BOLD, 32));
        menuTitle.setForeground(PaletaColores.CREAM_WHITE);
        menuTitle.setBorder(new EmptyBorder(0, 0, 30, 0));
        mainContentPanel.add(menuTitle, BorderLayout.NORTH);

        // Ajustamos el GridLayout a 1 fila, 3 columnas para los 3 botones restantes
        JPanel buttonGridPanel = new JPanel(new GridLayout(1, 3, 25, 25)); // Cambiado a 1 fila, 3 columnas
        buttonGridPanel.setOpaque(false);

        // Botones
        JButton btnAutores = new JButton("Gestión de Autores");
        JButton btnLibros = new JButton("Gestión de Libros");
        JButton btnPrestamos = new JButton("Gestión de Préstamos");

        // Estilizar y añadir acciones a los botones (tamaño más pequeño)
        styleButton(btnAutores);
        btnAutores.addActionListener(e -> showPanel("AutorForm")); // Cambia a la tarjeta del formulario de autores
        buttonGridPanel.add(btnAutores);

        styleButton(btnLibros);
        btnLibros.addActionListener(e -> showPanel("LibroForm")); // Cambia a la tarjeta del formulario de libros
        buttonGridPanel.add(btnLibros);

        styleButton(btnPrestamos);
        btnPrestamos.addActionListener(e -> showPanel("PrestamoForm")); // Cambia a la tarjeta del formulario de préstamos
        buttonGridPanel.add(btnPrestamos);

        mainContentPanel.add(buttonGridPanel, BorderLayout.CENTER);
        return mainContentPanel;
    }

    // Método para aplicar estilos a los botones (tamaño más pequeño)
    private void styleButton(JButton button) {
        // --- CAMBIO AQUÍ: Usamos PaletaColores.LIGHT_BROWN para el fondo del botón ---
        button.setBackground(PaletaColores.LIGHT_BROWN);
        // --- También podrías cambiar el color del borde si deseas que coincida con el fondo ---
        button.setBorder(new RoundedBorder(25, PaletaColores.LIGHT_BROWN, 2)); // Borde del mismo color
        // --- O mantener el borde original si lo prefieres: PaletaColores.PRIMARY_BROWN ---
        // button.setBorder(new RoundedBorder(25, PaletaColores.PRIMARY_BROWN, 2));


        button.setForeground(PaletaColores.TEXT_DARK); // Mantener el texto oscuro o ajustarlo si no contrasta bien
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Fuente ligeramente más pequeña
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 50)); // Reducido el tamaño de los botones
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Muestra el panel (tarjeta) especificado en el CardLayout.
     * Este método será llamado desde los formularios (Autorform, LibroForm, etc.)
     * para regresar al menú principal.
     * @param panelName El nombre de la tarjeta a mostrar (ej. "MainMenu", "AutorForm").
     */
    public void showPanel(String panelName) {
        cardLayout.show(cardPanel, panelName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}