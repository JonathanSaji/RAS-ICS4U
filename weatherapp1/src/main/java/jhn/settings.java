package jhn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class settings extends JFrame implements MouseListener {

    JPanel settingsPanel;
    ActionListener listener;
    JFrame parentFrame;
    boolean celciusFarhenheit = WeatherApp.json.getBoolean("celcius");

    public settings(JFrame parentFrame) {

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Menu Example");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.parentFrame = parentFrame;
        settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setBackground(Color.BLACK);
        parentFrame.add(settingsPanel);

        /*
         * two options
         * 0f,false
         * 1f,true
         */

        JButton celciusToFarenheit;
        JLabel celciusOrFarhenheit = new JLabel("", SwingConstants.CENTER);
        if (celciusFarhenheit) {
            celciusToFarenheit = new AnimationToggle(1f, true);
            celciusOrFarhenheit.setText("Celcius");
        } else {
            celciusToFarenheit = new AnimationToggle(0f, false);
            celciusOrFarhenheit.setText("Fahrenheit");
        }

        // One ActionListener for all buttons
        listener = e -> {
            Object source = e.getSource();
            if (source == celciusToFarenheit) {
                celciusFarhenheit = !celciusFarhenheit;
                celciusOrFarhenheit.setText(celciusFarhenheit ? "Celcius" : "Farhenheit");
                WeatherApp.json.setValue("celcius", celciusFarhenheit);

            }

            settingsPanel.repaint();
        };

        componentCreator(0, 0, celciusToFarenheit, false);
        componentCreator(4, 0, celciusOrFarhenheit, false);
        componentCreator(4, 1, new JLabel("Go Back", SwingConstants.CENTER), true);
        componentCreator(4, 3, new JLabel("Configure Stats", SwingConstants.CENTER), true);
    }

    public void componentCreator(int gridx, int gridy, Component component, boolean mouseListener) {

        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            label.setForeground(Color.white);
            label.setFont((new Font("Monospaced", Font.PLAIN, 48)));
            if (mouseListener) {
                label.setBackground(Color.DARK_GRAY);
                ;
                label.addMouseListener(this);
            }

        } else if (component instanceof JButton) {
            ((JButton) component).addActionListener(listener);    

        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.insets = new Insets(100, 0, 0, 100);
        gbc.fill = GridBagConstraints.BOTH;
        settingsPanel.add(component, gbc);
    }

    /*
     * Mouse Listener methods
     */

    @Override
    public void mouseClicked(MouseEvent e) {
        // Invoked when the mouse button has been clicked (pressed and released) on a
        // component
        if (e.getComponent() instanceof JLabel) {
            JLabel label = (JLabel) e.getComponent();
            switch (label.getText()) {
                case "Go Back":
                    settingsPanel.setVisible(false);
                    WeatherApp.getMenu().setPanel();
                    break;
                case "Configure Stats":
                    settingsPanel.setVisible(false);
                    new ConfigureStats(parentFrame,WeatherApp.getJsonHandler());
                    break;
                default:

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
            label.setForeground(Color.YELLOW);
        }

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Invoked when the mouse exits a component
        if (e.getComponent() instanceof JLabel) {
            JLabel label = (JLabel) e.getComponent();
            label.setForeground(Color.WHITE);

        }
    }

}
