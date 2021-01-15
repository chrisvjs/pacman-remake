import javax.swing.ImageIcon;

@SuppressWarnings("serial")

// This method sets the images for PacMan
public class PacMan extends Mover {
	
	// Sets the image constants for PacMan and all the direction he can be aswell as
	// if his mouth is open or not
	public static final ImageIcon[][] IMAGE = {

			{ new ImageIcon("images/PacLeftClosed.bmp"), new ImageIcon("images/PacLeftOpen.bmp") },
			{ new ImageIcon("images/PacUpClosed.bmp"), new ImageIcon("images/PacUpOpen.bmp") },
			{ new ImageIcon("images/PacRightClosed.bmp"), new ImageIcon("images/PacRightOpen.bmp") },
			{ new ImageIcon("images/PacDownClosed.bmp"), new ImageIcon("images/PacDownOpen.bmp") },

	};

	public PacMan() {

		this.setIcon(IMAGE[0][0]);

	}

}
