package jhn.ui;

import javax.swing.*;

import jhn.API.Weather;
import jhn.run.WeatherApp;
import jhn.ui.uiComponents.WeatherIcons;

import java.awt.*;
import java.time.LocalDate;

public class TimeDivider {

    JPanel background;
    JLabel morningJLabel, afternoonJLabel;
    WeatherIcons weatherIcons;

    public TimeDivider(JFrame parentFrame, LocalDate date,Weather weather) {
        weatherIcons = new WeatherIcons(weather);
        // Use a JPanel with custom paintComponent for the background gif
        background = new JPanel(null) {
            private final ImageIcon icon = new ImageIcon(
                    "weatherapp1\\src\\main\\java\\jhn\\resources\\springBackground.gif");

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (icon.getImage() != null) {
                    g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        background.setBounds(0, 0, 1920, 1080);

        parentFrame.add(background);

        // Create morning and afternoon panels
        morningJLabel = new JLabel();
        afternoonJLabel = new JLabel();

        timeLabelCreator(morningJLabel, true);
        timeLabelCreator(afternoonJLabel, false);

        statsLabel(new JLabel(), true,  "Morning", 0,false,null);
        statsLabel(new JLabel(), true, "Temp: " + weather.getLOWVal(date, weather.getTemperatures(),0,12) + " -> " + weather.getHIGHVal(date, weather.getTemperatures(), 0, 12), 2,false,null);
        statsLabel(new JLabel(), true,  "Humidity: " + weather.getAverageVal(date, weather.getHumidity(), 0, 12) + "%", 3,false,null);
        statsLabel(new JLabel(), true,  "Wind Speed: " + weather.getAverageVal(date, weather.getWindSpeed(), 0, 12) + " km/h", 4,false,null);
        statsLabel(new JLabel(), true, null, 5, true, weatherIcons.getIconForCloud(date, 0, 12));

        statsLabel(new JLabel(), false,  "Afternoon", 0,false,null);
        statsLabel(new JLabel(), false, "Temp: " + weather.getLOWVal(date, weather.getTemperatures(),12,24) + " -> " + weather.getHIGHVal(date, weather.getTemperatures(), 12, 24), 2,false,null);
        statsLabel(new JLabel(), false,  "Humidity: " + weather.getAverageVal(date, weather.getHumidity(), 12, 24) + "%", 3,false,null);
        statsLabel(new JLabel(), false,  "Wind Speed: " + weather.getAverageVal(date, weather.getWindSpeed(), 12, 24) + " km/h", 4,false,null);        
        statsLabel(new JLabel(), false, null, 5, true, weatherIcons.getIconForCloud(date, 12, 24));

        JLabel menu = new JLabel("MENU",SwingConstants.CENTER);
        menu.setBounds(WeatherApp.getMiddleX(200),WeatherApp.getMiddleY(100),200,100);
        menu.setFont(new Font("Monospaced", Font.BOLD, 38));
        menu.setForeground(Color.BLACK);
        background.add(menu);

        parentFrame.revalidate();
        parentFrame.repaint();
    }

    public void timeLabelCreator(JLabel label, boolean left) {
        //label.addMouseListener(this);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.setBackground(Color.WHITE);
        label.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 5));
        label.setOpaque(true);
        label.setLayout(new GridBagLayout());

        if (left) {
            label.setBounds(WeatherApp.getMiddleX(500) - 500, 200, 500, 700);
        } else {
            label.setBounds(WeatherApp.getMiddleX(500) + 500, 200, 500, 700);
        }

        background.add(label);
    }

    public void statsLabel(JLabel label, boolean morning, String text, int gridy,boolean icon,String imageFilePath) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTH;

        if(icon){
            label.setIcon(new ImageIcon(imageFilePath));
        }
        else{
            label = new JLabel(text);
        }
        label.setForeground(Color.BLACK);
        label.setFont(new Font("Monospaced", Font.BOLD, 38));
        if(morning){
            morningJLabel.add(label, gbc);
        }
        else{
            afternoonJLabel.add(label,gbc);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();

            new TimeDivider(frame,LocalDate.now(), new Weather(45, -75));

            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(null);
            frame.setVisible(true);
        });
    }
}