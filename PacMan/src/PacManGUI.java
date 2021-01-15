import java.awt.Color;

import javax.swing.*;

@SuppressWarnings("serial")

// This class makes the frame and panels aswell as the label for lives, high score, and score
// It also adds the Board and Menu bar
public class PacManGUI extends JFrame {

	// Adds Frames and Panels and Labels
	private Board board = new Board();

	public static MenuBar menubar = new MenuBar();

	public static JPanel scoreboardPanel = new JPanel(null);

	public static JLabel score = new JLabel("Score: 0");

	public static JLabel highScore = new JLabel("High Score for current session: 0");

	public static JLabel lives = new JLabel("Lives: 0");
	
	// Makes the frame, sets size, name
	public PacManGUI() {

		// Makes the scoreboard panel at the top
		scoreboardPanel();

		// Set size, name, and closes on exit
		setSize(800, 800);
		setTitle("PacMan");
		setJMenuBar(menubar);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Adds Key listener to the baord and add the board and the scoreboardPanel
		// Adds the board aswell
		addKeyListener(board);
		add(scoreboardPanel);
		board.setBounds(0, 10, 800, 750);
		add(board);

		// Sets visible to true
		setVisible(true);

	}

	// Makes a scoreboard panel at the top
	public void scoreboardPanel() {

		// Setting to bounds of the panel
		scoreboardPanel.setBounds(0, 0, 800, 25);

		// Sets the boundaries for the score label
		score.setBounds(10, 0, 200, 25);

		// Sets the boundaries for the lives label
		lives.setBounds(350, 0, 200, 25);

		// Sets the boundaries for the lives label
		highScore.setBounds(550, 0, 300, 25);

		// Makes the text white
		score.setForeground(Color.WHITE);
		lives.setForeground(Color.WHITE);
		highScore.setForeground(Color.WHITE);

		// Adds score label to scoreboard panel
		scoreboardPanel.add(score);

		// Adds the lives label to scoreboard panel
		scoreboardPanel.add(lives);

		// Adds the highScore label to scoreboard panel
		scoreboardPanel.add(highScore);

		// Sets panel background to black
		scoreboardPanel.setBackground(Color.BLACK);

	}


}
