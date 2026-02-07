package jhn;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class TodayWeather extends JFrame implements MouseListener {

    public JPanel timePanel, todayPanel;
    private JScrollPane scrollPane; // Added for navigation

    TodayWeather(JFrame parentFrame, Weather weather) {

        todayPanel = new JPanel();
        todayPanel.setLayout(null);
        todayPanel.setSize(1920, 1080);
        todayPanel.setBackground(Color.BLACK);
        parentFrame.add(todayPanel);
        
        // 1. Initialize timePanel with GridBagLayout
        timePanel = new JPanel(new GridBagLayout());
        timePanel.setBackground(Color.BLACK);

        // 2. Wrap timePanel in a JScrollPane
        scrollPane = new JScrollPane(timePanel);
        // Position the scrollpane in the center of the screen
        int scrollW = 1200;
        int scrollH = 800;
        scrollPane.setBounds(getMiddleX(scrollW), 100, scrollW, scrollH);
        scrollPane.setBorder(null); // Keep it clean
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Make scrolling smooth
        
        todayPanel.add(scrollPane);

        String labelTexts[] = new String[24];

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

        // 3. Loop through 4 columns (i) and 6 rows (j)
        for (int i = 1; i <= 4; i++) {
            int offset = (i - 1) * 6; // Cleaner logic for index
            for (int j = 0; j < 6; j++) {
                int currentIndex = j + offset;
                // Reduced height to 100 so it actually fits reasonably
                labelCreator(new JLabel(), labelTexts[currentIndex] + " " + weather.getTemp(currentIndex), true, j, i - 1, 250, 100);
            }
        }
    }

    public void labelCreator(JLabel label, String text, boolean addMouseListener, int gridy, int gridx, int width, int height) {
        label = new JLabel(text, SwingConstants.CENTER);
        // Use setPreferredSize so GridBagLayout knows how big to make the cells
        label.setPreferredSize(new Dimension(width, height));
        label.setMinimumSize(new Dimension(width, height));
        
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Gray looks cleaner on black
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Monospaced", Font.BOLD, 22));
        
        if (addMouseListener) {
            label.addMouseListener(this);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx; 
        gbc.gridy = gridy; 
        gbc.insets = new Insets(10, 10, 10, 10); // Spacing between "cards"
        gbc.fill = GridBagConstraints.BOTH;

        timePanel.add(label, gbc);
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
            label.setForeground(Color.WHITE);
            label.repaint();
        }
    }

    // Unchanged Boilerplate
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}

    public int getMiddleX(int sizeDiff){ return (1920 - sizeDiff) / 2; }
    public int getMiddleY(int sizeDiff){ return (1080 - sizeDiff) / 2; }
}