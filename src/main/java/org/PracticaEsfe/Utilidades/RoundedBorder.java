package org.PracticaEsfe.Utilidades;

import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedBorder implements Border {
    private int radius;
    private Color color;
    private int thickness;

    public RoundedBorder(int radius, Color color, int thickness) {
        this.radius = radius;
        this.color = color;
        this.thickness = thickness;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        // Ajusta los insets para que el contenido no se solape con el borde redondeado
        return new Insets(radius / 2 + 1, radius / 2 + 1, radius / 2 + 2, radius / 2 + 1);
    }

    @Override
    public boolean isBorderOpaque() {
        return true; // El borde es opaco
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibuja el borde con el color y grosor especificados
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));
        // Ajusta las coordenadas y el tamaño para dibujar el borde correctamente
        g2.draw(new RoundRectangle2D.Double(x + thickness / 2, y + thickness / 2,
                width - thickness, height - thickness,
                radius, radius));

        g2.dispose();
    }
}