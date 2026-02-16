package jhn.ui.uiComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class AnimationToggle extends JButton {
    private boolean selected;
    private float location; // 0 is left (off), 1 is right (on)
    private Timer timer;
    private Color onColor = new Color(0, 255, 0);
    private Color offColor = new Color(255, 255, 255);

    public AnimationToggle(float locat, boolean select) {

        this.selected = select;
        this.location = locat;

        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(200, 50));

        // Animation Timer (runs at 60 FPS)
        timer = new Timer(10, e -> {
            if (selected) {
                location += 0.1f;
                if (location >= 1f) {
                    location = 1f;
                    timer.stop();
                }
            } else {
                location -= 0.1f;
                if (location <= 0f) {
                    location = 0f;
                    timer.stop();
                }
            }
            repaint();
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                selected = !selected;
                timer.start();
                //debugging
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Interpolate background color
        Color currentColor = lerpColor(offColor, onColor, location);
        g2.setColor(currentColor);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));

        // Draw the knob
        int margin = 2;
        int knobSize = getHeight() - (margin * 2);
        float endPosition = getWidth() - knobSize - margin;
        float currentKnobX = margin + (endPosition - margin) * location;

        g2.setColor(Color.BLACK);
        g2.fillOval((int) currentKnobX, margin, knobSize, knobSize);
        
        super.paintComponent(g);
    }

    // Smoothly transition between offColor and onColor
    private Color lerpColor(Color c1, Color c2, float t) {
        int r = (int) (c1.getRed() + (c2.getRed() - c1.getRed()) * t);
        int g = (int) (c1.getGreen() + (c2.getGreen() - c1.getGreen()) * t);
        int b = (int) (c1.getBlue() + (c2.getBlue() - c1.getBlue()) * t);
        return new Color(r, g, b);
    }
   
}