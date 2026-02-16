package jhn.configure;

import javax.swing.*;

import jhn.run.WeatherApp;
import jhn.ui.uiComponents.AnimationToggle;

import java.awt.*;
import java.awt.event.*;

public class settings extends JFrame implements MouseListener {

    JPanel settingsPanel;
    JLabel background;
    ActionListener listener;
    JFrame parentFrame;
    boolean celciusFarhenheit = WeatherApp.json.getBoolean("celcius");
    boolean musicOn = WeatherApp.json.getBoolean("music");

    public settings(JFrame parentFrame) {

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Settings");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.parentFrame = parentFrame;
        settingsPanel = new JPanel();
        settingsPanel.setLayout(null);
        settingsPanel.setBackground(Color.WHITE);
        parentFrame.add(settingsPanel);

        // background = new JLabel(new
        // ImageIcon("weatherapp1\\src\\main\\java\\jhn\\resources\\scenery.gif"));
        background = new JLabel(new ImageIcon(WeatherApp.getBackgroundHandler().getBackgroundPath()));
        background.setOpaque(true);
        background.setBounds(0, 0, 1920, 1080);
        background.setLayout(new GridBagLayout());
        settingsPanel.add(background);

        settingsPanel.repaint();
        settingsPanel.revalidate();

        /*
         * two options
         * 0f,false
         * 1f,true
         */

        JButton celciusToFarenheit, musicButton;
        JLabel celciusOrFarhenheit = new JLabel("", SwingConstants.CENTER),
                musicLabel = new JLabel("", SwingConstants.CENTER);

        if (celciusFarhenheit) {
            celciusToFarenheit = new AnimationToggle(1f, true);
            celciusOrFarhenheit.setText("Celcius");
        } else {
            celciusToFarenheit = new AnimationToggle(0f, false);
            celciusOrFarhenheit.setText("Fahrenheit");
        }

        if (musicOn) {
            musicButton = new AnimationToggle(1f, true);
            musicLabel.setText("Music On");
        } else {
            musicButton = new AnimationToggle(0f, false);
            musicLabel.setText("Music Off");
        }

        // One ActionListener for all buttons
        listener = e -> {
            Object source = e.getSource();
            if (source == celciusToFarenheit) {
                celciusFarhenheit = !celciusFarhenheit;
                celciusOrFarhenheit.setText(celciusFarhenheit ? "Celcius" : "Farhenheit");
                WeatherApp.json.setValue("celcius", celciusFarhenheit);
            } else if (source == musicButton) {
                musicOn = !musicOn;
                musicLabel.setText(musicOn ? "Music On" : "Music Off");
                WeatherApp.json.setValue("music", musicOn);

                if (WeatherApp.json.getBoolean("music")) {
                    WeatherApp.getMusicHandler().play();
                } else {
                    WeatherApp.getMusicHandler().stop();
                }
            }

            background.repaint();
        };

        componentCreator(0, 0, celciusToFarenheit, false);
        componentCreator(4, 0, celciusOrFarhenheit, false);

        componentCreator(0, 1, musicButton, false);
        componentCreator(4, 1, musicLabel, false);

        componentCreator(4, 3, new JLabel("Go Back", SwingConstants.CENTER), true);
        componentCreator(4, 4, new JLabel("Configure Stats", SwingConstants.CENTER), true);
        
        background.repaint();
    }

    public void componentCreator(int gridx, int gridy, Component component, boolean mouseListener) {

        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            label.setForeground(Color.BLACK);
            label.setFont((new Font("Monospaced", Font.PLAIN, 48)));
            if (mouseListener) {
                label.setBackground(Color.BLACK);
                label.addMouseListener(this);
            }

        } else if (component instanceof JButton) {
            JButton button = (JButton) component;
            button.addActionListener(listener);
            button.setForeground(Color.BLACK);

        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.insets = new Insets(100, 0, 0, 100);
        gbc.fill = GridBagConstraints.BOTH;
        background.add(component, gbc);
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
                    new ConfigureStats(parentFrame, WeatherApp.getJsonHandler());
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
            label.setForeground(Color.BLACK);

        }
    }

}
