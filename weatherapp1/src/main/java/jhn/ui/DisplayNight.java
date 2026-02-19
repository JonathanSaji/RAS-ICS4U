package jhn.ui;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jhn.Time;
import jhn.API.Weather;
import jhn.handlers.JsonHandler;
import jhn.run.WeatherApp;

public class DisplayNight implements MouseListener {

    Weather weather;
    JPanel background;
    JLabel[] panels = new JLabel[6]; // Fix 1: was 12, only 6 panels needed for 18:00–24:00
    JsonHandler json = WeatherApp.json;
    JPanel timeDividerPanel;
    int count = 0;

    public DisplayNight(JFrame parentFrame, Weather weather, LocalDate date, JPanel timeDividerPanel) {
        this.weather = weather;
        this.timeDividerPanel = timeDividerPanel;

        background = new JPanel(new GridBagLayout()) {
            private final ImageIcon icon = new ImageIcon(
                    WeatherApp.backgroundHandler.getBackgroundPath());

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (icon.getImage() != null) {
                    g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        background.addMouseListener(this);
        parentFrame.add(background, BorderLayout.CENTER);

        // Create 6 hour panels in a single row
        for (int j = 0; j < 6; j++) {
            PanelLabelCreator(new JLabel(), j, 0, true);
        }

        // Add hour + weather labels inside each panel (18:00 – 23:00)
        for (int i = 18; i < 24; i++) {
            int hour12 = i % 12;
            if (hour12 == 0)
                hour12 = 12;
            String amPm = i < 12 ? "AM" : "PM";
            String hourLabel = hour12 + " " + amPm;

            // Fix 2: pass all weather values as arguments — was incorrectly placed outside the call
            addHourData(
                    panels[i - 18], // Fix 3: was i - 12, causing ArrayIndexOutOfBoundsException
                    hourLabel,
                    weather.getTemperature(date, i, json.getBoolean("celcius")),
                    weather.getApparentTemp(date, i, json.getBoolean("celcius")),
                    weather.getWindSpeed(date, i),
                    weather.getHumidity(date, i),
                    weather.getCloudCover(date, i),
                    weather.getPrecipitation(date, i),
                    weather.getRain(date, i),
                    weather.getShowers(date, i),
                    weather.getDewPoint(date, i, json.getBoolean("celcius")),
                    weather.getPressureMsl(date, i),
                    weather.getSurfacePressure(date, i),
                    weather.getSoilTemp(date, i, json.getBoolean("celcius")));
        }
    }

    public void PanelLabelCreator(JLabel label, int col, int row, boolean isPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.insets = new Insets(50, 10, 10, 50);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        if (isPanel) {
            panels[count++] = label;
            label.setOpaque(true);
            label.setBackground(new Color(176, 196, 222));
            label.setLayout(new GridBagLayout());
        }
        background.add(label, gbc);
    }

    // Fix 4: added missing pressure, surfacePressure, soilTemp, soilMoisture parameters
    public void addHourData(JLabel panel, String hour, String temp, String apparentTemp,
            String windSpeed, String humidity, String cloudCover,
            String precipitation, String rain, String showers, String dewPoint,
            String pressure, String surfacePressure, String soilTemp) {

        panel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(3, 2, 2, 3);
        int row = 0;

        // Always shown
        addLabel(panel, createDataLabel(hour), gbc, row++);

        LocalDateTime date = LocalDateTime.now();
        Time time = new Time(hour, date);
        boolean timeMatch = time.getMatchTime();
        System.out.println(timeMatch);
        if (timeMatch) {
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        }

        addLabel(panel, createDataLabel(temp), gbc, row++);

        // Toggleable
        if (WeatherApp.json.getBoolean("apparentTemp"))
            addLabel(panel, createDataLabel(apparentTemp), gbc, row++);
        if (WeatherApp.json.getBoolean("windSpeed"))
            addLabel(panel, createDataLabel(windSpeed), gbc, row++);
        if (WeatherApp.json.getBoolean("humidity"))
            addLabel(panel, createDataLabel(humidity), gbc, row++);
        if (WeatherApp.json.getBoolean("cloudCover"))
            addLabel(panel, createDataLabel(cloudCover), gbc, row++);
        if (WeatherApp.json.getBoolean("precipitation"))
            addLabel(panel, createDataLabel(precipitation), gbc, row++);
        if (WeatherApp.json.getBoolean("rain"))
            addLabel(panel, createDataLabel(rain), gbc, row++);
        if (WeatherApp.json.getBoolean("showers"))
            addLabel(panel, createDataLabel(showers), gbc, row++);
        if (WeatherApp.json.getBoolean("dewPoint"))
            addLabel(panel, createDataLabel(dewPoint), gbc, row++);
        if (WeatherApp.json.getBoolean("PressureMSL"))
            addLabel(panel, createDataLabel(pressure), gbc, row++);
        if (WeatherApp.json.getBoolean("surfacePressure"))
            addLabel(panel, createDataLabel(surfacePressure), gbc, row++);
        if (WeatherApp.json.getBoolean("soilTemp"))
            addLabel(panel, createDataLabel(soilTemp), gbc, row++);

        panel.revalidate();
        panel.repaint();
    }

    private void addLabel(JLabel panel, JLabel label, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        label.setFont(new Font("Monospaced", Font.BOLD, 25));
        panel.add(label, gbc);
    }

    private JLabel createDataLabel(String text) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setForeground(Color.BLACK);
        return label;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Fix 6: was instanceof JPanel which never matched hour panels (they are JLabels)
        // Now correctly checks for the background panel specifically
        if (e.getComponent() == background) {
            background.setVisible(false);
            timeDividerPanel.setVisible(true);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
}