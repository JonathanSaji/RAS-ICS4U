package jhn;

import javax.swing.*;

import java.awt.event.*;

import java.awt.*;

public class Menu extends JFrame implements MouseListener {

    private JPanel structuredPanel, panel;
    private JLabel settings;

    public Menu() {

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Menu Example");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        

        // has no layout has to be components have to be bound
        panel = new JPanel(null);
        panel.setBackground(Color.BLACK);
        this.add(panel);

        settings = new JLabel("Settings");
        settings.setIcon(new ImageIcon("weatherapp1\\src\\main\\java\\jhn\\settingsLogo.png"));
        settings.setForeground(Color.WHITE);
        settings.setBounds(0, 0, 100, 100);
        settings.addMouseListener(this);
        settings.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(settings);

        // has a layout
        structuredPanel = new JPanel(new GridBagLayout());
        structuredPanel.setBackground(Color.BLACK);
        structuredPanel.setBounds((1920 - 500) / 2, (1080 - 500) / 2, 500, 500);

        panel.add(structuredPanel);

        JLabel title = new JLabel();
        labelCreator(title, "Weather App", 500, 100,
                Color.WHITE, new Font("Monospaced", Font.BOLD, 48), false, 0);

        JLabel todayLabel = new JLabel();
        labelCreator(todayLabel, "Today's Weather", 300, 75,
                Color.WHITE, new Font("Monospaced", Font.PLAIN, 24), true, 1);

        JLabel tommorrowLabel = new JLabel();
        labelCreator(tommorrowLabel, "Tommorrow's Weather", 300, 75,
                Color.WHITE, new Font("Monospaced", Font.PLAIN, 24), true, 2);

        JLabel PickDateLabel = new JLabel();
        labelCreator(PickDateLabel, "Pick a Date", 300, 75,
                Color.WHITE, new Font("Monospaced", Font.PLAIN, 24), true, 3);

        JLabel exitLabel = new JLabel();
        labelCreator(exitLabel, "Exit", 300, 75,
                Color.WHITE, new Font("Monospaced", Font.PLAIN, 24), true, 4);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Invoked when the mouse button has been clicked (pressed and released) on a
        // component
        if (e.getComponent() instanceof JLabel) {
            JLabel label = (JLabel) e.getComponent();
            switch (label.getText()) {
                case "Today's Weather":
                    // Open Today's Weather Window
                    structuredPanel.setVisible(false);
                    panel.setVisible(false);
                    new TodayWeather(this, new Weather(52.52, 13.41));
                    break;
                case "Tommorrow's Weather":
                    // Open Tommorrow's Weather Window
                    System.out.println("Opening Tommorrow's Weather Window...");
                    break;
                case "Pick a Date":
                    // Open Pick a Date Window
                    System.out.println("Opening Pick a Date Window...");
                    break;
                case "Exit":
                    // Exit Application
                    System.out.println("Exiting Application...");
                    System.exit(0);
                    break;
                case "Settings":
                        structuredPanel.setVisible(false);
                        panel.setVisible(false);
                        new settings(this);
                        break;
                default:
                    break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Invoked when a mouse button has been pressed on a component

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Invoked when a mouse button has been released on a component
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Invoked when the mouse enters a component

        if (e.getComponent() instanceof JLabel) {
            JLabel label = (JLabel) e.getComponent();
            if (label != settings) {
                label.setOpaque(true);
                label.setForeground(Color.YELLOW);
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Invoked when the mouse exits a component
        if (e.getComponent() instanceof JLabel) {
            JLabel label = (JLabel) e.getComponent();
            if (label != settings) {
                label.setOpaque(false);
                label.setForeground(Color.WHITE);
            }

        }

    }

    public void labelCreator(JLabel label, String text, int width, int height, Color textColor, Font font,
            boolean addMouseListener, int row) {
        label = new JLabel(text, SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(width, height));
        label.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        label.setForeground(textColor);
        label.setFont(font);
        label.setBackground(Color.DARK_GRAY);
        if (addMouseListener) {
            label.addMouseListener(this);
            label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Always in the same column to stay centered
        gbc.gridy = row; // Increments to move down
        gbc.insets = new Insets(10, 0, 10, 0); // Adds 10 pixels of space above and below

        structuredPanel.add(label, gbc);

    }

}

/*
 * Sources:
 * https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html
 * 
 */