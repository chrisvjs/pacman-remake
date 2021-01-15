import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.Arrays;

import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

@SuppressWarnings("serial")

public class Board extends JPanel implements KeyListener, ActionListener { // Adds JFrame, Keylistener(for keyboard)
																			// ActionListener (to read input from
																			// keyboard)
	private Timer gameTimer = new Timer(250, this); // Timer with 300 ms refresh rate

	private Timer animateTimer = new Timer(1, this); // Animation Timer with 1 ms refresh rate

	private Timer scaredTimer = new Timer(10000, this); // Scared timer for the ghost with 6000 ms refresh rate

	private static final ImageIcon WALL = new ImageIcon("images/StdWall.bmp"); // Constant for wall image

	private static final ImageIcon FOOD = new ImageIcon("images/StdFood.bmp"); // Constant for food image

	private static final ImageIcon BLANK = new ImageIcon("images/Black.bmp"); // Constant for blank image

	private static final ImageIcon DOOR = new ImageIcon("images/Black.bmp"); // Constant for door image

	private static final ImageIcon SKULL = new ImageIcon("images/Skull.bmp"); // Constant for skull image

	private static final ImageIcon GHOSTHOUSE = new ImageIcon("images/Black2.bmp"); // Constant for ghost house door
																					// image
	private static final ImageIcon BLUEGHOST = new ImageIcon("images/BlueGhost.bmp");// Constant for blue ghosts

	private static final ImageIcon POWERUP = new ImageIcon("images/powerUp.png"); // Constant for PowerUp

	private static final ImageIcon CHERRY = new ImageIcon("images/Cherry.bmp"); // Constant for cherry image

	private char[][] maze = new char[25][27]; // 2-D array of characters for rows and columns

	private JLabel[][] cell = new JLabel[25][27]; // 2-D array of JLabels for rows and columns

	private PacMan pacMan; // PacMan instance

	private Ghost[] ghost = new Ghost[3]; // Array for number of Ghosts

	private int pellet = 0; // Amount of pellets

	public static int score = 0; // Keeping track of score

	public static int highScore; // High Score variable

	public static int lives = 3; // Number of lives

	public static int ghostEatScore = 0; // Score for eating power ups

	public static int cherryEatScore = 0; // Score for eating cherries

	public static int levelUpBonus = 1000;

	private int pStep; // Animation steps

	private int xSpawn; // X coordinate of pacMan's spawn

	private int ySpawn; // Y coordinate of pacMan's spawn

	private int[] ghostX = new int[3];

	private int[] ghostY = new int[3];

	private boolean isScared = false; // Stores if Ghosts are running away or not

	// Constructor
	public Board() {

		// Sets grid and background to black
		setLayout(new GridLayout(25, 27));
		setBackground(Color.BLACK);
		setBounds(0, 300, 600, 600);

		// New PacMan Object
		pacMan = new PacMan();

		// 3 Ghost Objects
		ghost[0] = new Ghost(0);
		ghost[1] = new Ghost(1);
		ghost[2] = new Ghost(2);

		// Call loadboard method
		loadBoard();

	}

	public void loadBoard() {

		// rows
		int r = 0;

		// User input
		Scanner input;

		// Try Statement to replace all the letters with the corresponding image
		try {

			// Uses the maze text file for the maze
			input = new Scanner(new File("images/maze.txt"));

			while (input.hasNext()) {

				// Converts the string to character array
				maze[r] = input.nextLine().toCharArray();

				// For Loop: Creates the rows and columns and replaces the letters with the
				// images
				for (int c = 0; c < maze[r].length; c++) {

					// Each cell is a new JLabel
					cell[r][c] = new JLabel();

					// When there is a "W" on the maze file, replace it with a wall
					if (maze[r][c] == 'W')

						cell[r][c].setIcon(WALL);

					// When there is a "F" or "V" replace with the food and add to the total amount
					// of pellets
					else if (maze[r][c] == 'F' || maze[r][c] == 'V') {

						cell[r][c].setIcon(FOOD);
						pellet++;
					}

					// Sets C to the powerUp Icon
					else if (maze[r][c] == 'C')
						cell[r][c].setIcon(POWERUP);

					// Sets A to the cherry Icon
					else if (maze[r][c] == 'A')
						cell[r][c].setIcon(CHERRY);

					// When there is a P replace with PacMan and set his cell column and row and
					// direction
					else if (maze[r][c] == 'P') {

						cell[r][c].setIcon(pacMan.getIcon());
						pacMan.setRow(r);
						pacMan.setColumn(c);
						pacMan.setDirection(0);

						xSpawn = r;
						ySpawn = c;

					}

					// When there is "L" replace with blank so that the ghosts can exit but PacMan
					// can't enter
					else if (maze[r][c] == 'L')
						cell[r][c].setIcon(GHOSTHOUSE);

					// Replace "0, 1, 2" with the ghosts and set their cell row and columns
					else if (maze[r][c] == '0' || maze[r][c] == '1' || maze[r][c] == '2') {

						int gNum = (int) (maze[r][c]) - 48;

						cell[r][c].setIcon(ghost[gNum].getIcon());
						ghost[gNum].setRow(r);
						ghost[gNum].setColumn(c);

						ghostX[gNum] = r;
						ghostY[gNum] = c;

					}

					// Replace "D" with black image which acts as a teleporter
					else if (maze[r][c] == 'D') {

						cell[r][c].setIcon(DOOR);

					}

					// Add the cells
					add(cell[r][c]);
				}

				// Row++ and play the PacMan sound
				r++;
				soundTrack("GAMEBEGINNING.wav");

			}

			// Input closes
			input.close();

			// If there is a file error, then display message
		} catch (FileNotFoundException e) {

			System.out.println("File not found");

		}

	}

	// Takes the keyboard entries to start the game and controls PacMans movement
	public void keyPressed(KeyEvent key) {

		// Start the game if it isn't running and PacMan is not dead
		if (gameTimer.isRunning() == false && pacMan.isDead() == false)
			gameTimer.start();

		// If PacMan is alive and the score does not equal the pellets, then use the
		// keyboard entries for movement
		if (pacMan.isDead() == false && score != pellet) {

			// Takes the ASCII for the arrow keys and subtracts 37 to get direction
			int direction = key.getKeyCode() - 37;

			// Doesn't let pacMan go through walls or the ghost house
			if (direction == 0 && maze[pacMan.getRow()][pacMan.getColumn() - 1] != 'W')
				pacMan.setDirection(0);
			else if (direction == 1 && maze[pacMan.getRow() - 1][pacMan.getColumn()] != 'W')
				pacMan.setDirection(1);
			else if (direction == 2 && maze[pacMan.getRow()][pacMan.getColumn() + 1] != 'W')
				pacMan.setDirection(2);
			else if (direction == 3 && maze[pacMan.getRow() + 1][pacMan.getColumn()] != 'W'
					&& maze[pacMan.getRow() + 1][pacMan.getColumn()] != 'L')
				pacMan.setDirection(3);

		}
	}

	// Not used but required to run
	public void keyReleased(KeyEvent key) {

	}

	// Not used but require to run
	public void keyTyped(KeyEvent key) {

	}

	// Allows the objects to move and updates on the maze and screen
	public void performMove(Mover mover) {

		// If the column is equal to 1 then create the teleporter
		if (mover.getColumn() == 1) {

			mover.setColumn(25);
			cell[12][1].setIcon(DOOR);

		}

		// Create the other side of the teleporter
		else if (mover.getColumn() == 25) {

			mover.setColumn(1);
			cell[12][25].setIcon(DOOR);

		}

		// If there is no wall infront of the player or ghost, then the player or ghost
		// moves
		if (maze[mover.getNextRow()][mover.getNextColumn()] != 'W') {

			// Start the animation timer if mover is equal to PacMan
			if (mover == pacMan) {

				animateTimer.start();
				PacManGUI.lives.setText("Lives: " + lives);

			}

			// Otherwise, if there is food in a cell, and the mover moves to it,
			// replace it with a blank
			else {

				if (maze[mover.getRow()][mover.getColumn()] == 'F' || maze[mover.getRow()][mover.getColumn()] == 'V')
					cell[mover.getRow()][mover.getColumn()].setIcon(FOOD);

				else if (maze[mover.getRow()][mover.getColumn()] == 'C')
					cell[mover.getRow()][mover.getColumn()].setIcon(POWERUP);

				else if (maze[mover.getRow()][mover.getColumn()] == 'A')
					cell[mover.getRow()][mover.getColumn()].setIcon(CHERRY);

				else
					cell[mover.getRow()][mover.getColumn()].setIcon(BLANK);

				mover.move();

				// If pacMan and the ghost collide while the ghosts are scared
				// Then the ghosts return to their position and add 10 to the score and plays
				// the sound
				if (collided() && isScared) {

					for (int g = 0; g < 3; g++) {

						// If any of the ghosts are in the same cell as PacMan, then a collision happens
						// Also add a score of ten for ghosts eaten and play the ghost eaten sound
						if (ghost[g].getRow() == pacMan.getRow() && ghost[g].getColumn() == pacMan.getColumn()) {

							ghost[g].setRow(ghostX[g]);
							ghost[g].setColumn(ghostY[g]);
							ghostEatScore += 10;
							soundTrack("GHOSTEATEN.wav");

						}

					}

				}
				// If the ghost and PacMan collide, and you lost a life and pacMan respawns at
				// the starting point
				else if (collided() && lives > 0) {

					// Lose one life and set the dead spot as blank and sets text
					lives--;
					cell[pacMan.getRow()][pacMan.getColumn()].setIcon(BLANK);

					// Puts the pacMan image on the spawn point
					cell[xSpawn][ySpawn].setIcon(pacMan.getIcon());
					pacMan.setRow(xSpawn);
					pacMan.setColumn(ySpawn);
					pacMan.setDirection(0);

				}

				// If pacMan and a ghost collides and there are no lives left, pacMan dies and
				// says the high score and plays the killed sound
				else if (collided() && lives == 0) {

					// PacMan is dead
					death();

					// Play the sound of death
					soundTrack("killed.wav");

					// Sets highscore equal to the score plus the ghostEat
					highScore = score + ghostEatScore + cherryEatScore;

					// Automatically sets the highest as the high score for the first game
					int highest = highScore;

					// Make highest the new high score if it it bigger than the last
					if (highScore > highest)
						highest = highScore;

					// Updates the label and sets the score
					PacManGUI.highScore.setText("High Score for Current Sessions: " + highest);

				} else if (score == pellet)
					stopGame();

				// Sets the ghosts to blue when they are scared
				if (isScared && mover != pacMan)
					cell[mover.getRow()][mover.getColumn()].setIcon(BLUEGHOST);

				// Otherwise get the regular ghost icon
				else
					cell[mover.getRow()][mover.getColumn()].setIcon(mover.getIcon());

			}

		}

	}

	// Collision Method
	public boolean collided() {

		// For Loop: To check all 3 of the ghosts for collisions
		for (int g = 0; g < 3; g++) {

			// If any of the ghosts are in the same cell as PacMan, then a collision happens
			if (ghost[g].getRow() == pacMan.getRow() && ghost[g].getColumn() == pacMan.getColumn())
				return true;
		}

		// Otherwise false
		return false;
	}

	// Death Method
	private void death() {

		// If collision happens then set Dead true
		pacMan.setDead(true);

		// Stop the game
		stopGame();

		// Replace PacMan with a skull after death
		cell[pacMan.getRow()][pacMan.getColumn()].setIcon(SKULL);

	}

	// Stop the game once death happens
	private void stopGame() {

		// Either the game ends by win or PacMan dies then
		if (pacMan.isDead()) {

			// Stops the timers which stops the games
			animateTimer.stop();
			gameTimer.stop();

		}

		// If the score matches up with the pellet then make level 2 appear
		// And stop the timers for Level 1
		if (score == pellet) {

			// Stop the timers for the Level 1 Window
			animateTimer.stop();
			gameTimer.stop();

			// Create the new window for Level 2
			new PacManGUIMedium();

			// Add a bonus for advancing to the next level
			score = score + levelUpBonus;

			SwingUtilities.getWindowAncestor(this).dispose();

		}

	}

	// Movement for the Ghosts in a random pattern
	private void moveGhosts() {

		// Direction variable
		int dir = 0;

		// Goes through all of the ghosts
		for (int x = 0; x < 3; x++) {

			// This is for difficulty - higher Math.random multiplied by the game is harder
			// because the ghosts actions are less random (1/10 chance of random movement)
			if ((int) ((Math.random() * 10) + 1) == 1) {

				// Randomize the ghosts movement
				do {
					dir = (int) (Math.random() * 4);
				} while (Math.abs(ghost[x].getDirection() - dir) == 2);

				// Set the direction for the ghost
				ghost[x].setDirection(dir);

			}

			// If anything is "V" on the maze, then it is a intersection and uses checkMove
			// method so the ghosts can follow PacMan but if PacMan eats a powerup and the
			// ghosts are scared, then call the method to make them run away
			else if (maze[ghost[x].getRow()][ghost[x].getColumn()] == 'V') {

				// If they are scared then make them go the farthest from pacMan
				if (isScared) {
					ghost[x].setDirection(checkMoveFarthest(x));

				}

				// Otherwise, make them go the closest
				else
					ghost[x].setDirection(checkMoveClosest(x));

			}
			// Otherwise check if there is no intersection
			else
				ghost[x].setDirection(checkNoIntersection(x));

			// Set the last move of the ghost and their direction
			ghost[x].setLastMove(ghost[x].getDirection());

			// Calls performMove for the ghosts so that they move and it updates
			performMove(ghost[x]);

		}
	}

	// Determines the source of the action as either the game timer or the animation
	// timer and then performs the coreesponding actions
	public void actionPerformed(ActionEvent e) {

		// Adds the timer to the game
		if (e.getSource() == gameTimer) {

			// Calls these methods to move pacMan and Ghosts
			performMove(pacMan);
			moveGhosts();

		}

		// Otherwise, start the animateTimer
		if (e.getSource() == animateTimer) {

			// Animates PacMan
			animatePacMan();

			// pStep goes up by 1
			pStep++;

			// Reset pStep
			if (pStep == 3)
				pStep = 0;

		}

		// If the timer is over, then stop the timer and set the ghosts scared to false
		if (e.getSource() == scaredTimer) {

			scaredTimer.stop();

			isScared = false;
		}

	}

	// This method animates pacMan
	private void animatePacMan() {

		// When pStep = 0, get the direction of PacMan and place the corresponding image
		if (pStep == 0) {

			cell[pacMan.getRow()][pacMan.getColumn()].setIcon(PacMan.IMAGE[pacMan.getDirection()][1]);

			animateTimer.setDelay(100);

		}

		// If pStep == 1, then set the icon on the cell blank
		else if (pStep == 1)

			cell[pacMan.getRow()][pacMan.getColumn()].setIcon(BLANK);

		// If pStep = 2, then pacMan moves and if the maze.txt is a "F' then add to the
		// scoer and send it to the scoreboard GUI and play the eating sound and then
		// set it to empty
		else if (pStep == 2) {

			pacMan.move();

			if (maze[pacMan.getRow()][pacMan.getColumn()] == 'F' || maze[pacMan.getRow()][pacMan.getColumn()] == 'V') {

				// Adds score and sends score to scoreboard GUI and plays food eat sound
				score++;
				PacManGUI.score.setText("Score: " + (score + ghostEatScore + cherryEatScore));
				soundTrack("pacchomp.wav");

				maze[pacMan.getRow()][pacMan.getColumn()] = 'E';

			}

			// If pacMan goes over a cherry then...
			else if (maze[pacMan.getRow()][pacMan.getColumn()] == 'A') {

				// Add score of 5
				cherryEatScore += 5;

				// Play chomp sound
				soundTrack("pacchomp.wav");

				// Set the 'A' to Empty
				maze[pacMan.getRow()][pacMan.getColumn()] = 'E';
			}

			// If PacMan goes on the powerUp then the ghosts are set to scared
			// and everytime they step on one the timer restarts then replaces the C with E
			// for BLANK
			else if (maze[pacMan.getRow()][pacMan.getColumn()] == 'C') {

				isScared = true;

				scaredTimer.restart();

				maze[pacMan.getRow()][pacMan.getColumn()] = 'E';
			}

			// Stop the amimation timer
			animateTimer.stop();

			// Check to see if PacMan is dead and if he is replace his icon with a skull
			if (pacMan.isDead())
				cell[pacMan.getRow()][pacMan.getColumn()].setIcon(SKULL);

			// Otherwise, get his position and direction and set to corresponding images
			else
				cell[pacMan.getRow()][pacMan.getColumn()].setIcon(PacMan.IMAGE[pacMan.getDirection()][0]);

		}

	}

	// Checks the distance between the ghost and pacMan
	// (GHOST AI)
	public double checkPacDis(int row, int column) {

		// Num is the distance
		double num;

		// Calculate the row differences
		int rowDiff = pacMan.getRow() - row;
		int columnDiff = pacMan.getColumn() - column;

		// Use pythagorean theorem to find the distance between pacMan and the ghosts
		num = Math.sqrt(Math.pow(rowDiff, 2) + Math.pow(columnDiff, 2));

		return num;
	}

	// Takes the current position of the ghosts and then
	// calculates the distance if they were to move in a certain direction
	// (GHOST AI)
	public int checkMoveClosest(int ghostNum) {

		// Set the distances (high impossible number so that if the if statements don't
		// work, it wouldn't give 0 and then 0 would be the lowest distance)
		double upDistance = 9999;
		double downDistance = 9999;
		double leftDistance = 9999;
		double rightDistance = 9999;

		// Checks to see if moving in a certain direction if there would be a wall, and
		// if there isn't then it would calculate the distance by calling checkPacDis
		// and getting that double value
		if (maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() - 1] != 'W')
			leftDistance = checkPacDis(ghost[ghostNum].getRow(), ghost[ghostNum].getColumn() - 1);

		if (maze[ghost[ghostNum].getRow() - 1][ghost[ghostNum].getColumn()] != 'W')
			upDistance = checkPacDis(ghost[ghostNum].getRow() - 1, ghost[ghostNum].getColumn());

		if (maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() + 1] != 'W')
			rightDistance = checkPacDis(ghost[ghostNum].getRow(), ghost[ghostNum].getColumn() + 1);

		if (maze[ghost[ghostNum].getRow() + 1][ghost[ghostNum].getColumn()] != 'W')
			downDistance = checkPacDis(ghost[ghostNum].getRow() + 1, ghost[ghostNum].getColumn());

		// Create an array with all the numbers
		double[] lowest = { upDistance, leftDistance, downDistance, rightDistance };

		// Sort them by ascending order
		Arrays.sort(lowest);

		// Take the lowest and send the ghost in that direction
		if (leftDistance == lowest[0])
			return 0;
		else if (upDistance == lowest[0])
			return 1;
		else if (rightDistance == lowest[0])
			return 2;
		else if (downDistance == lowest[0])
			return 3;
		else
			return 0; // This return 0 is meaningless it never happens but it needs to be there

	}

	// Takes the current position of the ghosts and then
	// calculates the distance if they were to move in a certain direction
	// (GHOST AI)
	public int checkMoveFarthest(int ghostNum) {

		// Set the distances (low impossible number so that if the if statements don't
		// work, it wouldn't give 0 and then 0 would be the highest distance)
		double upDistance = -1;
		double downDistance = -1;
		double leftDistance = -1;
		double rightDistance = -1;

		// Checks to see if moving in a certain direction if there would be a wall, and
		// if there isn't then it would calculate the distance by calling checkPacDis
		// and getting that double value
		if (maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() - 1] != 'W')
			leftDistance = checkPacDis(ghost[ghostNum].getRow(), ghost[ghostNum].getColumn() - 1);

		if (maze[ghost[ghostNum].getRow() - 1][ghost[ghostNum].getColumn()] != 'W')
			upDistance = checkPacDis(ghost[ghostNum].getRow() - 1, ghost[ghostNum].getColumn());

		if (maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() + 1] != 'W')
			rightDistance = checkPacDis(ghost[ghostNum].getRow(), ghost[ghostNum].getColumn() + 1);

		if (maze[ghost[ghostNum].getRow() + 1][ghost[ghostNum].getColumn()] != 'W')
			downDistance = checkPacDis(ghost[ghostNum].getRow() + 1, ghost[ghostNum].getColumn());

		// Create an array with all the numbers
		double[] highest = { upDistance, leftDistance, downDistance, rightDistance };

		// Sort them by ascending order
		Arrays.sort(highest);

		// Take the highest and send the ghost in that direction
		if (leftDistance == highest[3])
			return 0;
		else if (upDistance == highest[3])
			return 1;
		else if (rightDistance == highest[3])
			return 2;
		else if (downDistance == highest[3])
			return 3;
		else
			return 0; // This return 0 is meaningless it never happens but it needs to be there

	}

	// Run when ghost is no in an intersection and returns the place to move when
	// they aren't in an intersection (has atmost 2 ways to go)
	// (GHOST AI)
	public int checkNoIntersection(int ghostNum) {

		// Get the direction of their last move
		int direction = ghost[ghostNum].getLastMove();

		// If the direction they are going in is not a wall
		// then set that as the direction. Otherwise, set any direction other than
		// backwards if there is a wall
		if (direction == 0 && maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() - 1] != 'W')
			return direction;
		else if (maze[ghost[ghostNum].getRow() - 1][ghost[ghostNum].getColumn()] != 'W'
				&& 1 != getOpposite(ghost[ghostNum].getLastMove()))
			return 1;
		else if (maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() + 1] != 'W'
				&& 2 != getOpposite(ghost[ghostNum].getLastMove()))
			return 2;
		else if (maze[ghost[ghostNum].getRow() + 1][ghost[ghostNum].getColumn()] != 'W'
				&& 3 != getOpposite(ghost[ghostNum].getLastMove()))
			return 3;

		if (direction == 1 && maze[ghost[ghostNum].getRow() - 1][ghost[ghostNum].getColumn()] != 'W')
			return direction;
		else if (maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() - 1] != 'W'
				&& 0 != getOpposite(ghost[ghostNum].getLastMove()))
			return 0;
		else if (maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() + 1] != 'W'
				&& 2 != getOpposite(ghost[ghostNum].getLastMove()))
			return 2;
		else if (maze[ghost[ghostNum].getRow() + 1][ghost[ghostNum].getColumn()] != 'W'
				&& 3 != getOpposite(ghost[ghostNum].getLastMove()))
			return 3;

		if (direction == 2 && maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() + 1] != 'W')
			return direction;
		else if (maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() - 1] != 'W'
				&& 0 != getOpposite(ghost[ghostNum].getLastMove()))
			return 0;
		else if (maze[ghost[ghostNum].getRow() - 1][ghost[ghostNum].getColumn()] != 'W'
				&& 1 != getOpposite(ghost[ghostNum].getLastMove()))
			return 1;
		else if (maze[ghost[ghostNum].getRow() + 1][ghost[ghostNum].getColumn()] != 'W'
				&& 3 != getOpposite(ghost[ghostNum].getLastMove()))
			return 3;

		if (direction == 3 && maze[ghost[ghostNum].getRow() + 1][ghost[ghostNum].getColumn()] != 'W')
			return direction;
		else if (maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() - 1] != 'W'
				&& 0 != getOpposite(ghost[ghostNum].getLastMove()))
			return 0;
		else if (maze[ghost[ghostNum].getRow() - 1][ghost[ghostNum].getColumn()] != 'W'
				&& 1 != getOpposite(ghost[ghostNum].getLastMove()))
			return 1;
		else if (maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() + 1] != 'W'
				&& 2 != getOpposite(ghost[ghostNum].getLastMove()))
			return 2;
		else
			return 999;

	}

	// Returns the opposite direction
	// (GHOST AI)
	public int getOpposite(int direction) {

		// If they go one way return the opposite
		if (direction == 0)
			return 2;
		else if (direction == 2)
			return 0;
		else if (direction == 1)
			return 3;
		else if (direction == 3)
			return 1;
		else
			return 0;

	}

	/*
	 * Used https://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java
	 * to help figure out how to put audio but I didn't completely copy it This
	 * method adds the music and put the file name in the parameter when calling the
	 * method to play that specific file
	 */
	public static void soundTrack(String filename) {

		Clip clip;

		try {
			// Open an audio input stream
			File soundFile = new File("sounds/" + filename);
			AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundFile);
			// Get a sound clip resource.
			clip = AudioSystem.getClip();
			// Open audio clip and load samples from the audio input stream.
			clip.open(audioInput);
			clip.start();
		} catch (Exception e) { // Returns errors when file errors occur.
			e.printStackTrace();
		}
	}

}
