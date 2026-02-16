package jhn.ui;

import javax.swing.*;

import jhn.API.Weather;
import jhn.run.WeatherApp;

import java.time.LocalDate;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class DisplayStats implements MouseListener {

    private JPanel statsPanel;
    private JLabel background;

    public DisplayStats(LocalDate date, Weather weather, JFrame parentFrame, int hourSelected) {

        statsPanel = new JPanel(new GridBagLayout());
        statsPanel.setBackground(Color.BLACK);
        statsPanel.setVisible(true);
        parentFrame.add(statsPanel);

        background = new JLabel(new ImageIcon(WeatherApp.getBackgroundHandler().getBackgroundPath()));
        background.setBounds(0, 0, 1920, 1080);
        background.setLayout(new GridBagLayout());
        background.setOpaque(true);
        statsPanel.add(background);

        

        // keys correspond to the JSON keys used for toggling each stat in ConfigureStats
        String keys[] = {
                "humidity",
                "dewPoint",
                "apparentTemp",
                "precipitation",
                "rain",
                "showers",
                "snowfall",
                "PressureMSL",
                "surfacePressure",
                "cloudCover",
                "windSpeed",
                "soilTemp"
        };

        // Build a list of stat strings, only when the stat is enabled and available
        List<String> statsToShow = new ArrayList<>();
        for (int i = 0; i < keys.length; i++) {
            boolean enabled = WeatherApp.json.getBoolean(keys[i]);
            if (!enabled) continue;

            String value = getStatValue(weather, keys[i], date, hourSelected);
            if (value == null) continue;            // not available
            if (value.equals("N/A")) continue;    // explicit N/A

            statsToShow.add(value);
        }

        // Place stats into a 3x4 grid (3 columns, up to 4 rows)
        for (int k = 0; k < statsToShow.size() && k < 12; k++) {
            int row = k / 3; // 0..3
            int col = k % 3; // 0..2
            labelCreator(new JLabel(), statsToShow.get(k), row, col, 500, 200, false);
        }

        // Determine a safe cell for "Go Back": prefer start of last row (3,0) when free,
        // otherwise place at next available cell after stats; fall back to (3,2).
        int goBackRow, goBackCol;
        if (statsToShow.size() <= 9) {
            goBackRow = 3;
            goBackCol = 0;
        } else if (statsToShow.size() < 12) {
            goBackRow = statsToShow.size() / 3;
            goBackCol = statsToShow.size() % 3;
        } else {
            goBackRow = 3;
            goBackCol = 2;
        }

        labelCreator(new JLabel(), "Go Back", goBackRow, goBackCol, 500, 200, true);

        background.revalidate();
        background.repaint();
    }

    private String getStatValue(Weather weather, String key, LocalDate date, int hour) {
        switch (key) {
            case "humidity":
                return weather.getHumidity(date, hour);
            case "dewPoint":
                return weather.getDewPoint(date, hour, WeatherApp.json.getBoolean("celcius"));
            case "apparentTemp":
                return weather.getApparentTemp(date, hour, WeatherApp.json.getBoolean("celcius"));
            case "precipitation":
                return weather.getPrecipitation(date, hour);
            case "rain":
                return weather.getRain(date, hour);
            case "showers":
                return weather.getShowers(date, hour);
            case "snowfall":
                return weather.getSnowfall(date, hour);
            case "PressureMSL":
                return weather.getPressureMsl(date, hour);
            case "surfacePressure":
                return weather.getSurfacePressure(date, hour);
            case "cloudCover":
                return weather.getCloudCover(date, hour);
            case "windSpeed":
                return weather.getWindSpeed(date, hour);
            case "soilTemp":
                return weather.getSoilTemp(date, hour, WeatherApp.json.getBoolean("celcius"));
            default:
                return null;
        }
    }

    public void labelCreator(JLabel label, String text, int gridy, int gridx, int width, int height, boolean mouseListener) {

        label = new JLabel(text, SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(width, height));
        label.setForeground(Color.BLACK);
        label.setFont(new Font("Monospaced", Font.TYPE1_FONT, 38));
        // make labels clickable only when requested
        if (mouseListener) {
            label.addMouseListener(this);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.insets = new Insets(10, 10, 10, 10); // spacing between cells
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; // distribute extra horizontal space evenly
        gbc.weighty = 1.0; // distribute extra vertical space evenly
        gbc.anchor = GridBagConstraints.CENTER;

        background.add(label, gbc);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JLabel source = (JLabel) e.getSource();
        String text = source.getText();
        switch (text) {
            case "Go Back":
                statsPanel.setVisible(false);
                WeatherApp.getMenu().setPanel();
                System.out.println("Going back to main menu");
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
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

}
