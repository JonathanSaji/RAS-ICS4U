
import javax.swing.*;

import java.awt.event.*;

import java.awt.*;

public class Menu extends JFrame implements MouseListener{

    private JPanel panel;
    public Menu() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Menu Example");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.BLACK);
        this.add(panel);

        JLabel title = new JLabel();
        labelCreator(title, "Weather App", 500, 100, 
        Color.WHITE, new Font("Monospaced", Font.BOLD, 48),false,0);

        JLabel todayLabel = new JLabel();
        labelCreator(todayLabel, "Today's Weather", 300, 75,
        Color.WHITE, new Font("Monospaced", Font.PLAIN, 24),true,1);

        JLabel tommorrowLabel = new JLabel();
        labelCreator(tommorrowLabel, "Tommorrow's Weather", 300, 75,
        Color.WHITE, new Font("Monospaced", Font.PLAIN, 24),true,2);

        JLabel PickDateLabel = new JLabel();
        labelCreator(PickDateLabel, "Pick a Date", 300, 75,
        Color.WHITE, new Font("Monospaced", Font.PLAIN, 24),true,3);

        JLabel exitLabel = new JLabel();
        labelCreator(exitLabel, "Exit", 300, 75,
        Color.WHITE, new Font("Monospaced", Font.PLAIN, 24),true,4);
        
        
    }

    @Override
	public void mouseClicked(MouseEvent e) {
		// Invoked when the mouse button has been clicked (pressed and released) on a component
        if (e.getComponent() instanceof JLabel) {
        JLabel label = (JLabel) e.getComponent();
        switch(label.getText()) {
            case "Today's Weather":
                // Open Today's Weather Window
                System.out.println("Opening Today's Weather Window...");
                break;
            case "Tommorrow's Weather":
                // Open Tommorrow's Weather Window
                System.out.println("Opening Tommorrow's Weather Window...");
                break;
            case "Pick a Date":
                // Open Pick a Date Window
                System.out.println("Opening Pick a Date Window...");
                break;
            case "Exit":
                // Exit Application
                System.out.println("Exiting Application...");
                System.exit(0);
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

    public void labelCreator(JLabel label, String text,  int width, int height,Color textColor, Font font, boolean addMouseListener,int row) {
        label = new JLabel(text, SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(width, height));
        label.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        label.setForeground(textColor);
        label.setFont(font);
        label.setBackground(Color.DARK_GRAY);
        if(addMouseListener) {
            label.addMouseListener(this);
        }
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;   // Always in the same column to stay centered
        gbc.gridy = row; // Increments to move down
        gbc.insets = new Insets(10, 0, 10, 0); // Adds 10 pixels of space above and below
        
        panel.add(label, gbc);
        }

}

/*
Sources:
https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html

*/