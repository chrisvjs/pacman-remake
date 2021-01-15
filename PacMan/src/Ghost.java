import javax.swing.ImageIcon;

@SuppressWarnings("serial")

// This class sets the icons of the ghosts and gets the last move of the ghost
public class Ghost extends Mover {
	
	// Variable for last move of the ghost
	private int lastMove = 0;
	
	// Getters and setters
	public int getLastMove() {
		return lastMove;
	}

	public void setLastMove(int lastMove) {
		this.lastMove = lastMove;
	}

	// Sets the image constants for the ghosts
	public static final ImageIcon[] IMAGE = {
			
			new ImageIcon("images/Ghost1.bmp"),
			new ImageIcon("images/Ghost2.bmp"),
			new ImageIcon("images/Ghost3.bmp"),
			
	};
	
	public Ghost(int gNum) {
		
		this.setIcon(IMAGE[gNum]);
	}

}
