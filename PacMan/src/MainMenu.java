import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
// This class sets up the Main Menu frame and adds things like buttons and labels to make it interactive for the user
// https://www.javatpoint.com/java-jbutton - Used this link to help but nothing directly copied from it
public class MainMenu extends JFrame implements ActionListener {

	// Creates the Panel, all the Labels and Buttons
	JPanel mainMenu = new JPanel();
	JButton button = new JButton("Play Game");
	JButton button3 = new JButton("Quit Game");
	JLabel imgLabel = new JLabel(new ImageIcon("images/PacManBackground (2).jpg"));
	JLabel imgTitle = new JLabel(new ImageIcon("images/PacManTitle (2).jpg"));

	// This method sets up the frame
	public MainMenu() {

		setSize(800, 800);
		setVisible(true);
		setTitle("Christopher's Pac-Man Game");
		setLayout(null);
		PanelSetup();

	}

	// This method adds all the labels to the background and changes the font
	// As well as setting the bounds and adding the action listener to the button
	private void PanelSetup() {

		// Setting bounds and background to black and then adding those to the frame
		imgLabel.setBounds(0, 0, 800, 800);
		imgTitle.setBounds(160, 50, 480, 200);
		mainMenu.setLayout(null);
		mainMenu.setBackground(Color.BLACK);
		add(mainMenu);
		add(imgLabel);

		// Setting the bounds for the buttons
		button.setBounds(200, 250, 400, 50);
		button3.setBounds(200, 450, 400, 50);

		// Adding the action listener to the buttons
		button.addActionListener(this);
		button3.addActionListener(this);

		// Setting the fonts for the buttons
		button.setFont(new Font("Comic Sans", Font.BOLD, 20));
		button3.setFont(new Font("Comic Sans", Font.BOLD, 20));

		// Adding the buttons and the title to the background label
		imgLabel.add(button);
		imgLabel.add(button3);
		imgLabel.add(imgTitle);

	}

	@Override
	// This method does a certain action depending on what button is clicked
	public void actionPerformed(ActionEvent event) {

		// If the click is on the button then do certain tasks
		if ((event.getSource() == button))
			new PacManGUI();
		setVisible(false);

		setVisible(false);
		if ((event.getSource() == button3))
			System.exit(0);

	}

}
