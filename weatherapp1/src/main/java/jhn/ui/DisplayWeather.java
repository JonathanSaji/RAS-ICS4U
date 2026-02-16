package jhn.ui;

import javax.swing.*;

import jhn.API.Weather;
import jhn.location.CurrentTime;
import jhn.run.WeatherApp;

import java.awt.event.*;
import java.time.LocalDate;
import java.awt.*;

public class DisplayWeather extends JFrame implements MouseListener {

    public JPanel todayPanel;
    public LocalDate date;
    public Weather weather;
    String labelTexts[] = new String[24];
    int hourSelected;
    public JLabel backgroundLabel;

    public DisplayWeather(JFrame parentFrame, Weather weather, LocalDate date) {

        this.date = date;
        this.weather = weather;
        todayPanel = new JPanel();
        todayPanel.setBackground(Color.BLACK);
        todayPanel.setVisible(true);
        parentFrame.add(todayPanel);

        backgroundLabel = new JLabel(new ImageIcon(WeatherApp.getBackgroundHandler().getBackgroundPath()));
        backgroundLabel.setBounds(0, 0, 1920, 1080);
        backgroundLabel.setLayout(new GridBagLayout());
        backgroundLabel.setOpaque(true);
        todayPanel.add(backgroundLabel);

        for (int i = 0; i <= 23; i++) {
            if (i == 0) {
                labelTexts[i] = "12 AM";
            } else if (i == 12) {
                labelTexts[i] = "12 PM";
            } else if (i < 12) {
                labelTexts[i] = i + " AM";
            } else {
                labelTexts[i] = (i - 12) + " PM";
            }
        }

        // Loop through 4 columns (i) and 6 rows (j)
        for (int i = 1; i <= 4; i++) {
            int offset = (i - 1) * 6; // Cleaner logic for index
            for (int j = 0; j < 6; j++) {
                int currentIndex = j + offset;
                System.out.println(date);
                labelCreator(new JLabel(),
                        labelTexts[currentIndex] + " "
                                + weather.getTemperature(date, currentIndex,
                                        WeatherApp.getJsonHandler().getBoolean("celcius")),
                        true, j, i - 1, 500, 200, currentIndex, true);

            }
        }
        labelCreator(new JLabel(), "Menu", true, -1, -1, 500, 200, 25, false);

    }

    public void labelCreator(JLabel label, String text, boolean addMouseListener, int gridy, int gridx, int width,
            int height, int index, boolean useIndex) {

        label = new JLabel(text, SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(width, height));

        if (useIndex) {
            if (labelTexts[index].equals(new CurrentTime().getHour()) && date.equals(LocalDate.now())) {
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
            }
        }

        label.setForeground(Color.BLACK);
        label.setFont(new Font("Monospaced", Font.TYPE1_FONT, 60));

        if (addMouseListener) {
            label.addMouseListener(this);
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.insets = new Insets(20, 40, 40, 20); // Spacing between "cards"
        gbc.fill = GridBagConstraints.BOTH;

        backgroundLabel.add(label, gbc);

    }

    // --- Mouse Listener Events ---

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getComponent() instanceof JLabel) {
            JLabel label = (JLabel) e.getComponent();
            label.setOpaque(true);
            label.setBackground(new Color(50, 50, 50)); // Dark highlight
            label.setForeground(Color.YELLOW);
            label.repaint();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getComponent() instanceof JLabel) {
            JLabel label = (JLabel) e.getComponent();
            label.setOpaque(false);
            label.setForeground(Color.BLACK);
            label.repaint();
        }
    }

    // Unchanged Boilerplate
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getComponent() instanceof JLabel) {
            JLabel label = (JLabel) e.getComponent();
            switch (label.getText()) {
                case "Menu":
                    todayPanel.setVisible(false);
                    WeatherApp.getMenu().setPanel();
            }

            if (label.getText().contains("AM") || label.getText().contains("PM")) {
                String text = label.getText();
                int hour = text.substring(0, text.indexOf(' ')).equals("12") ? 0
                        : Integer.parseInt(text.substring(0, text.indexOf(' ')));
                if (text.contains("PM")) {
                    hour += 12;
                }
                hour = hour == 24 ? 0 : hour;
                System.out.println(hour);
                todayPanel.setVisible(false);
                new DisplayStats(date, weather, WeatherApp.getMenu().getFrame(), hour);
            }
        }

    }

}
