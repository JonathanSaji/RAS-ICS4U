package jhn.configure;

import javax.swing.*;

import jhn.API.Weather;
import jhn.handlers.BackgroundHandler;
import jhn.handlers.JsonHandler;
import jhn.run.WeatherApp;
import jhn.ui.Menu;
import jhn.ui.uiComponents.AnimationToggle;

import java.awt.*;
import java.awt.event.*;

public class ConfigureStats extends JPanel implements MouseListener {

    ActionListener listener;
    JsonHandler json;
    JFrame parentFrame;
    JPanel statsPanel;
    JLabel latLabel, longLabel, background;
    boolean showStats[] = new boolean[12];

    double latBefore = WeatherApp.getLat();
    double longBefore = WeatherApp.getLong();

    public ConfigureStats(JFrame parentFrame, JsonHandler json) {
        this.parentFrame = parentFrame;
        this.json = json;
        setLayout(null);
        setBackground(Color.WHITE);
        setVisible(true);
        parentFrame.add(this);

        statsPanel = new JPanel();
        statsPanel.setBackground(Color.BLACK);
        statsPanel.setVisible(true);
        statsPanel.setBounds(0, 100, 1920, 980);
        add(statsPanel);

        background = new JLabel(new ImageIcon(WeatherApp.getBackgroundHandler().getBackgroundPath()));
        background.setBounds(0, 0, 1920, 1080);
        background.setLayout(new GridBagLayout());
        background.setOpaque(true);
        statsPanel.add(background);

        parentFrame.revalidate();
        parentFrame.repaint();

        String statNames[] = {
                "apparentTemp",
                "humidity",
                "dewPoint",
                "precipitation",
                "rain",
                "showers",
                "PressureMSL",
                "surfacePressure",
                "cloudCover",
                "windSpeed",
                "soilTemp",
                "soilMoisture"
        };

        for (int i = 0; i <= 11; i++) {
            showStats[i] = WeatherApp.json.getBoolean(statNames[i]);
            System.out.println(statNames[i] + ": " + showStats[i]);
        }

        latLabel = new JLabel("Lat: " + (int)WeatherApp.getLat(), SwingConstants.CENTER);
        longLabel = new JLabel(" Long: " + (int)WeatherApp.getLong(), SwingConstants.CENTER);

        componentCreator(0, 0, new JLabel("Go Back", SwingConstants.CENTER), true, "nol");
        componentCreator(1, 0, new JLabel("Current", SwingConstants.CENTER), true, "nol");
        componentCreator(2, 0, new JLabel("- LAT", SwingConstants.CENTER), true, "nol");
        componentCreator(3, 0, new JLabel("+ LAT", SwingConstants.CENTER), true, "nol");
        componentCreator(4, 0, new JLabel("- LONG", SwingConstants.CENTER), true, "nol");
        componentCreator(5, 0, new JLabel("+ LONG", SwingConstants.CENTER), true, "nol");
        componentCreator(6, 0, latLabel, true, "nol");
        componentCreator(7, 0, longLabel, true, "nol");

        // prepare listener before creating buttons so they get it
        listener = e -> {
            Object source = e.getSource();
            if (source instanceof JButton) {
                JButton btn = (JButton) source;
                String btnText = btn.getText();
                json.setValue(btnText, !json.getBoolean(btnText));
            }

        };

        for (int i = 0; i < 12; i++) {
            int baseRow = (i / 3) + 1; // 3 stats per row group
            int col = i % 3;

            int buttonRow = baseRow; // e.g., 2,4,6,...

            boolean selected = showStats[i];
            float location = selected ? 1f : 0f;

            componentCreator(col, buttonRow, new AnimationToggle(location, selected), false, statNames[i]);
        }

    };

    public void componentCreator(int gridx, int gridy, Component component, boolean mouseListener, String key) {

        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            label.setFont((new Font("Monospaced", Font.BOLD, 24)));
            if (mouseListener) {
                label.addMouseListener(this);
            }
            if(key.equals("nol")) {
                label.setBounds(gridx * 250, 0, 150, 100);
                label.setForeground(Color.BLACK);
                add(label);
            }


        } else if (component instanceof JButton && key != null) {
            JButton button = (JButton) component;
            button.setHorizontalAlignment(SwingConstants.CENTER);
            button.setFont(new Font("Monospaced", Font.BOLD, 40));
            button.setText(key);
            button.setForeground(Color.darkGray);
            button.addActionListener(listener);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = gridx;
            gbc.gridy = gridy;
            gbc.insets = new Insets(50, 100, 100, 50);
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1;
            gbc.weighty = 1;
            background.add(button, gbc);
        }


        repaint();
        revalidate();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        JLabel source = (JLabel) e.getSource();
        String text = source.getText();
        switch (text) {
            case "Go Back":
                if (WeatherApp.getLat() != latBefore || WeatherApp.getLong() != longBefore) {
                    Weather weather = new Weather(WeatherApp.getLat(), WeatherApp.getLong());
                    WeatherApp.setBackgroundHandler(new BackgroundHandler(weather));
                    Menu menu = new Menu(weather);
                    WeatherApp.setMenu(menu);
                    parentFrame.dispose();

                } else {
                    setVisible(false);
                    new settings(parentFrame);
                    System.out.println("Going back to settings");
                }
            case "- LAT":
                WeatherApp.setLat(WeatherApp.getLat() - 5);
                latLabel.setText("Lat: " + (int)WeatherApp.getLat());
                break;
            case "+ LAT":
                WeatherApp.setLat(WeatherApp.getLat() + 5);
                latLabel.setText("Lat: " + (int)WeatherApp.getLat());
                break;
            case "- LONG":
                WeatherApp.setLong(WeatherApp.getLong() - 5);
                longLabel.setText("Long: " + (int)WeatherApp.getLong());
                break;
            case "+ LONG":
                WeatherApp.setLong(WeatherApp.getLong() + 5);
                longLabel.setText("Long: " + (int)WeatherApp.getLong());
                break;
            case "Current":
                WeatherApp.setLat(WeatherApp.getCurrentLat());
                WeatherApp.setLong(WeatherApp.getCurrentLong());
                latLabel.setText("Lat: " + (int)WeatherApp.getLat());
                longLabel.setText("Long: " + (int)WeatherApp.getLong());
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getComponent() instanceof JLabel) {
            JLabel label = (JLabel) e.getComponent();
            label.setForeground(Color.RED);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getComponent() instanceof JLabel) {
            JLabel label = (JLabel) e.getComponent();
            label.setForeground(Color.BLACK);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

}
