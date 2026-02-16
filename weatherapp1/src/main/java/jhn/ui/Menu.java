package jhn.ui;

import javax.swing.*;

import jhn.API.Weather;
import jhn.configure.settings;
import jhn.run.WeatherApp;
import jhn.ui.uiComponents.CustomCalendarPopup;

import java.awt.event.*;

import java.awt.*;

import java.time.LocalDate;


public class Menu extends JFrame implements MouseListener {

    private JPanel structuredPanel, panel;
    private JLabel settings;
    private Weather weather;
    private JFrame frame;
    private CustomCalendarPopup popupCalendar;
    JLabel background;

    public Menu(Weather weather) {

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Menu Example");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setUndecorated(true);
        this.setResizable(false);

        if(!WeatherApp.getJsonHandler().getBoolean("music")) {
            WeatherApp.getMusicHandler().stop();
        }
        else {
            WeatherApp.getMusicHandler().play();
        }

         // Get screen device
         GraphicsDevice device = GraphicsEnvironment
         .getLocalGraphicsEnvironment()
         .getDefaultScreenDevice();

         device.setFullScreenWindow(this);

        setFrame(this);


        // intialize weather
        this.weather = weather;

        // has no layout has to be components have to be bound
        panel = new JPanel(null);
        panel.setBackground(Color.BLACK);
        this.add(panel);

        // has a layout
        structuredPanel = new JPanel();
        structuredPanel.setBackground(Color.BLACK);
        structuredPanel.setBounds(WeatherApp.getMiddleX(1900), WeatherApp.getMiddleY(1080), 1900, 1080);

        panel.add(structuredPanel);

        background = new JLabel(new ImageIcon(WeatherApp.getBackgroundHandler().getBackgroundPath()));
        background.setLayout(new GridBagLayout());
        background.setBounds(0, 0, 1900, 1080);
        structuredPanel.add(background);

        int amp = 4;
        JLabel title = new JLabel();
        labelCreator(title, "Weather App", 500 * amp, 100 * amp,
                Color.BLACK, new Font("Monospaced", Font.BOLD, 48*amp), false, 0);

        JLabel todayLabel = new JLabel();
        labelCreator(todayLabel, "Today's Weather", 300 * amp, 75 * amp,
                Color.BLACK, new Font("Monospaced", Font.PLAIN, 24* amp), true, 1);

        JLabel tommorrowLabel = new JLabel();
        labelCreator(tommorrowLabel, "Tommorrow's Weather", 300 * amp, 75 * amp,
                Color.BLACK, new Font("Monospaced", Font.PLAIN, 24* amp), true, 2);

        JLabel PickDateLabel = new JLabel();
        labelCreator(PickDateLabel, "Pick a Date", 300 * amp, 75 * amp,
                Color.BLACK, new Font("Monospaced", Font.PLAIN, 24* amp), true, 3);

        JLabel exitLabel = new JLabel();
        labelCreator(exitLabel, "Exit", 300 * amp, 75 * amp,
                Color.BLACK, new Font("Monospaced", Font.PLAIN, 24* amp), true, 4);

        settings = new JLabel(" ");
        settings.setIcon(new ImageIcon("weatherapp1\\src\\main\\java\\jhn\\resources\\settingsLogo.png"));

        labelCreator(settings, null, 0, 0, null, null, true, 5);



        repaint();
    }

    public void labelCreator(JLabel label, String text, int width, int height, Color textColor, Font font,
        boolean addMouseListener, int row) {
        if(text != null){
            label = new JLabel(text, SwingConstants.CENTER);
        }
        label.setPreferredSize(new Dimension(width*5, height*5));

        label.setForeground(textColor);
        label.setFont(font);
        label.setBackground(Color.DARK_GRAY);
        if (addMouseListener) {
            label.addMouseListener(this);
            label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Always in the same column to stay centered
        gbc.gridy = row; // Increments to move down
        gbc.insets = new Insets(10, 0, 10, 0); // Adds 10 pixels of space above and below

        background.add(label, gbc);

    }









    @Override
    public void mouseClicked(MouseEvent e) {
        // Invoked when the mouse button has been clicked (pressed and released) on a
        // component
        if (e.getComponent() instanceof JLabel) {
            JLabel label = (JLabel) e.getComponent();
            switch (label.getText()) {
                case "Today's Weather":
                    // Open Today's Weather Window
                    structuredPanel.setVisible(false);
                    panel.setVisible(false);
                    if(popupCalendar != null){
                        popupCalendar.closePopup();
                    }
                    new DisplayWeather(this, weather, LocalDate.now());
                    break;
                case "Tommorrow's Weather":
                    // Open Tommorrow's Weather Window
                    structuredPanel.setVisible(false);
                    panel.setVisible(false);
                    if(popupCalendar != null){
                        popupCalendar.closePopup();
                    }
                    new DisplayWeather(this, weather, LocalDate.now().plusDays(1));
                    break;
                case "Pick a Date":
                    // Open Pick a Date Window
                    if(popupCalendar != null){
                        popupCalendar.closePopup();
                    }
                    popupCalendar = new CustomCalendarPopup(this, weather,structuredPanel);
                    break;
                case "Exit":
                    // Exit Application
                    System.out.println("Exiting Application...");
                    System.exit(0);
                    break;
                case " ":
                    structuredPanel.setVisible(false);
                    panel.setVisible(false);
                    if(popupCalendar != null){
                        popupCalendar.closePopup();
                    }
                    new settings(this);
                    break;
                default:
                    break;
            }
        }
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
            if (label != settings) {
                label.setForeground(Color.YELLOW);
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Invoked when the mouse exits a component
        if (e.getComponent() instanceof JLabel) {
            JLabel label = (JLabel) e.getComponent();
            if (label != settings) {
                label.setForeground(Color.BLACK);
            }

        }

    }

    public void setPanel() {
        panel.setVisible(true);
        structuredPanel.setVisible(true);
    }

    public void closePanel(){
        panel.setVisible(false);
        structuredPanel.setVisible(false);
    }

    public void setFrame(JFrame frame){
         this.frame = frame;
    }

    public JFrame getFrame(){
         return frame;
    }

}

/*
 * Sources:
 * https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html
 * 
 */