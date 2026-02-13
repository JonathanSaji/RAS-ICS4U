package jhn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConfigureStats extends JPanel implements MouseListener {

    ActionListener listener;
    JsonHandler json;
    JFrame parentFrame;
    boolean showStats[] = new boolean[12];

    public ConfigureStats(JFrame parentFrame, JsonHandler json) {
        this.parentFrame = parentFrame;
        this.json = json;
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        setVisible(true);
        parentFrame.add(this);
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

        componentCreator(0, 0, new JLabel("Go Back", SwingConstants.CENTER), true,null);

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

            int buttonRow = baseRow;    // e.g., 2,4,6,...

            float location = json.getBoolean(statNames[i]) ? 1f : 0f;
            boolean selected = location == 1f;

            componentCreator(col, buttonRow, new AnimationToggle(location, selected), false,statNames[i]);
        }

       /// repaint();
    };

    public void componentCreator(int gridx, int gridy, Component component, boolean mouseListener,String key) {

        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            label.setForeground(Color.WHITE);
            label.setFont((new Font("Monospaced", Font.BOLD, 48)));
            if (mouseListener) {
                label.setBackground(Color.DARK_GRAY);
                label.addMouseListener(this);
            }

        } else if (component instanceof JButton && key != null) {
            JButton button = (JButton) component;
            button.setHorizontalAlignment(SwingConstants.CENTER);
            button.setFont(new Font("Monospaced", Font.BOLD, 40));
            button.setText(key);
            button.setForeground(Color.darkGray);
            button.addActionListener(listener);
        }
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.insets = new Insets(50, 50, 50, 50);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.0000001; // Adjust as needed for spacing
        gbc.weighty = 0.0000001; // Adjust as needed for spacing
        add(component, gbc);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        JLabel source = (JLabel) e.getSource();
        String text = source.getText();
        switch (text) {
            case "Go Back":
                setVisible(false);
                new settings(parentFrame);
                System.out.println("Going back to settings");
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getComponent() instanceof JLabel) {
            JLabel label = (JLabel) e.getComponent();
                label.setForeground(Color.YELLOW);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getComponent() instanceof JLabel) {
            JLabel label = (JLabel) e.getComponent();
                label.setForeground(Color.WHITE);
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {


    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

}
