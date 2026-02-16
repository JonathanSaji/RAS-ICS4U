package jhn.ui.uiComponents;

import javax.swing.*;

import jhn.API.Weather;
import jhn.run.WeatherApp;
import jhn.ui.DisplayWeather;


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;

public class CustomCalendarPopup extends JPanel {

    private LocalDate selectedDate;
    private JLabel monthLabel;
    private JPanel calendarPanel;
    private YearMonth currentMonth;
    private JFrame parentFrame;
    private JPanel parentPanel;
    private Weather weather;

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    public CustomCalendarPopup(JFrame parentFrame, Weather weather,JPanel parentPanel) {
        this.parentFrame = parentFrame;
        this.currentMonth = YearMonth.now();
        this.weather = weather;
        this.parentPanel = parentPanel;
        
        createAndShowPopup();
    }

    private void createAndShowPopup() {

        setLayout(new BorderLayout());
        setSize(WIDTH, HEIGHT);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JPanel topPanel = new JPanel();
        JLabel prevButton = new JLabel("<");
        JLabel nextButton = new JLabel(">");
        monthLabel = new JLabel();
        monthLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        prevButton.setFont(new Font("Monospaced", Font.BOLD, 20));
        nextButton.setFont(new Font("Monospaced", Font.BOLD, 20));
        prevButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nextButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        updateMonthLabel();

        prevButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentMonth = currentMonth.minusMonths(1);
                updateCalendar();
            }
        });

        nextButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentMonth = currentMonth.plusMonths(1);
                updateCalendar();
            }
        });

        parentPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                closePopup();
            }
        });

        topPanel.add(prevButton);
        topPanel.add(monthLabel);
        topPanel.add(nextButton);

        calendarPanel = new JPanel(new GridLayout(0, 7));
        updateCalendar();

        add(topPanel, BorderLayout.NORTH);
        add(calendarPanel, BorderLayout.CENTER);

        // 🔥 Add to layered pane (proper popup behavior)
        JLayeredPane layeredPane = parentFrame.getLayeredPane();
        layeredPane.add(this, JLayeredPane.POPUP_LAYER);

        // 🔥 Center popup in frame
        int x = (parentFrame.getWidth() - WIDTH) / 2;
        int y = (parentFrame.getHeight() - HEIGHT) / 2;

        setBounds(x, y, WIDTH, HEIGHT);

        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private void updateMonthLabel() {
        monthLabel.setText(currentMonth.getMonth() + " " + currentMonth.getYear());
    }

    private void updateCalendar() {

        calendarPanel.removeAll();
        updateMonthLabel();

        String[] days = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

        for (String day : days) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
            calendarPanel.add(dayLabel);
        }

        LocalDate firstDay = currentMonth.atDay(1);
        int start = firstDay.getDayOfWeek().getValue() % 7;

        for (int i = 0; i < start; i++) {
            calendarPanel.add(new JLabel(""));
        }

        for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {

            final int d = day;
            JLabel label = new JLabel(String.valueOf(day), SwingConstants.CENTER);
            label.setFont(new Font("Monospaced", Font.BOLD, 20));
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            if(weather.isThereTemp(currentMonth.atDay(day), 0)){
                label.setForeground(Color.BLACK);
            }
            else{
                label.setForeground(Color.LIGHT_GRAY);
            }

            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectedDate = currentMonth.atDay(d);

                    if (weather.isThereTemp(selectedDate, 0)) {
                        WeatherApp.getMenu().closePanel();
                        new DisplayWeather(parentFrame, weather, selectedDate);
                        closePopup();
                    }
                }
            });

            calendarPanel.add(label);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    public void closePopup() {
        JLayeredPane layeredPane = parentFrame.getLayeredPane();
        layeredPane.remove(this);
        layeredPane.repaint();
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }
}
