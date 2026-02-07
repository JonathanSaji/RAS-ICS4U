package jhn;

import javax.swing.*;

import java.awt.event.*;

import java.awt.*;

public class TodayWeather extends JFrame implements MouseListener {

    public JPanel todayPanel;

    TodayWeather(JFrame parentFrame, Weather weather) {

        todayPanel = new JPanel(new GridBagLayout());
        todayPanel.setBackground(Color.BLACK);
        parentFrame.add(todayPanel);

        String labelTexts[] = new String[24];

        for (int i = 0; i <= 23; i++) {
            if (i == 0) {
                labelTexts[i] = "12 AM";
            } else if (i > 0 && i <= 12) {
                if (i == 12) {
                    labelTexts[i] = i + " PM";
                } else {
                    labelTexts[i] = i + " AM";
                }
            } else {
                labelTexts[i] = (i - 12) + " PM";
            }
            System.out.println(labelTexts[i]);
        }

        for (int i = 1; i <= 4; i++) {
            int index = 0;
            for (int j = 0; j < 6; j++) {

                switch (i) {
                    case 2:
                        index = 6;
                        break;
                    case 3:
                        index = 12;
                        break;
                    case 4:
                        index = 18;
                        break;
                }
                labelCreator(new JLabel(), labelTexts[j + index] + " " + weather.getTemp(j + index), true, j, i - 1,300,75);
            }
        }
        //System.out.println(weather.getLongLat());

        labelCreator(new JLabel(), weather.getLongLat(),false,7,-1,400,175);


    }

    public void labelCreator(JLabel label, String text, boolean addMouseListener, int gridy, int gridx, int width, int height) {

        label = new JLabel(text, SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(width, height));
        label.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Monospaced", Font.PLAIN, 24));
        label.setBackground(Color.DARK_GRAY);
        if (addMouseListener) {
            label.addMouseListener(this);
        }
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx; // Always in the same column to stay centered
        gbc.gridy = gridy; // Increments to move down
        gbc.insets = new Insets(10, 0, 10, 0); // Adds 10 pixels of space above and below

        todayPanel.add(label, gbc);

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
            label.setOpaque(true);
            label.setForeground(Color.YELLOW);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Invoked when the mouse exits a component
        if (e.getComponent() instanceof JLabel) {
            JLabel label = (JLabel) e.getComponent();
            label.setOpaque(false);
            label.setForeground(Color.WHITE);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
}
