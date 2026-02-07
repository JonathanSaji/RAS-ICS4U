package jhn;

import javax.swing.*;
import java.awt.*;

public class settings extends JFrame{

    JPanel settingsPanel;
    public settings(JFrame parentFrame){
        settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setBackground(Color.BLACK);
        parentFrame.add(settingsPanel);

        JLabel label = new JLabel("Settings");
        label.setBounds((1920 - 500)/2, (1080 -500)/2, 500,500);
        settingsPanel.add(label);
    }
    

//    // Main method to test the component
//     public static void main(String[] args) {
//         JFrame frame = new JFrame("Settings");
//         frame.setLayout(new FlowLayout());
//         new settings(frame);
//         frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         frame.setLocationRelativeTo(null);
//         frame.setVisible(true);
//     }
}
