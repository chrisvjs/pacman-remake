import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
// This class makes the Menu Bar at the top of the Frame which allows you to go to the Main Menu, and Exit the game
// https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html
// ^^Used this link to help understand the JMenu but nothing directly copied
// https://stackoverflow.com/questions/37470884/how-to-dispose-jframe
// ^^Used this link to learn how to dispose the Frame after a new window comes up (Somewhat copied but modified)
public class MenuBar extends JMenuBar implements ActionListener {

	// Creates the variable for the menu and menu items
	JMenu fileMenu = new JMenu("Main Menu");
	JMenu exitGame = new JMenu("Exit Game");
	JMenu restartGame = new JMenu("Restart Game");
	JMenuItem mainMenuItem = new JMenuItem("Main Menu");
	JMenuItem exitMenuItem = new JMenuItem("Exit");
	public static JMenuItem restartMenuItem = new JMenuItem("Restart");

	// This method adds the action listener to the menu items and then the menu
	// items to the menu then adds all of that to the frame
	public MenuBar() {

		mainMenuItem.addActionListener(this);
		exitMenuItem.addActionListener(this);
		restartMenuItem.addActionListener(this);
		fileMenu.add(mainMenuItem);
		restartGame.add(restartMenuItem);
		exitGame.add(exitMenuItem);
		add(fileMenu);
		add(restartGame);
		add(exitGame);
	}

	// This method takes the action of the mouse and then if it is on the menu item,
	// then calls the certain methods
	public void actionPerformed(ActionEvent event) {

		if (event.getSource() == mainMenuItem) {

			new MainMenu();
			// This gets rid of the previous window once one is made
			SwingUtilities.getWindowAncestor(this).dispose();

		}
		// If they click restart
		if (event.getSource() == restartMenuItem) {

			Board.score = 0;
			Board.lives = 3;
			// Make a new game
			new MainMenu();

			// Update the labels
			PacManGUI.score.setText("Score: 0");
			PacManGUI.lives.setText("Lives: 3");

			SwingUtilities.getWindowAncestor(this).dispose();

		}

		// If they want to exit then this terminates all windows on the click
		else if (event.getSource() == exitMenuItem)
			System.exit(0);
	}

}
