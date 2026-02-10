package jhn;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class TodayWeather extends JFrame implements MouseListener {

    public JPanel todayPanel;

    String labelTexts[] = new String[24];

    TodayWeather(JFrame parentFrame, Weather weather) {

        todayPanel = new JPanel(new GridBagLayout());
        todayPanel.setBackground(Color.BLACK);
        todayPanel.setVisible(true);
        parentFrame.add(todayPanel);
        


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
                // Reduced height to 100 so it actually fits reasonably
                labelCreator(new JLabel(), labelTexts[currentIndex] + " " + weather.getTemp(currentIndex,WeatherApp.json.getBoolean("celcius")), true, j, i - 1, 500, 200,currentIndex,true);
            }
        }
        labelCreator(new JLabel(), "Menu", true, -1, -1, 500, 200, 25,false);

    }

    public void labelCreator(JLabel label, String text, boolean addMouseListener, int gridy, int gridx, int width, int height,int index,boolean useIndex) {

        label = new JLabel(text, SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(width,height));
    if(useIndex){
        if(labelTexts[index].equals(new CurrentTime().getHour())){
            label.setBorder(BorderFactory.createLineBorder(Color.YELLOW,10)); 
        }
        else{
            label.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5)); 
        }
    }
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Monospaced", Font.BOLD, 48));
        
        if (addMouseListener) {
            label.addMouseListener(this);
        }

        

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx; 
        gbc.gridy = gridy; 
        gbc.insets = new Insets(10, 0, 0, 10); // Spacing between "cards"
        gbc.fill = GridBagConstraints.BOTH;

        todayPanel.add(label, gbc);
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
    @Override public void mouseClicked(MouseEvent e) {
        if (e.getComponent() instanceof JLabel) {
            JLabel label = (JLabel) e.getComponent();
            switch (label.getText()) {
                case "Menu":
                    todayPanel.setVisible(false);
                    WeatherApp.getMenu().setPanel();
                default:
                    break;
            }
        }







    }







}

