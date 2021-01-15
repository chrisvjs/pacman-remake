import javax.swing.JLabel;

@SuppressWarnings("serial")

// This class takes the rows, columns, difference of rows, difference of columns and uses that 
// to set and get direction which is either left, right, up, or down
public abstract class Mover extends JLabel {

	// Variables for rows and columns
	private int row;
	private int column;

	// Variables for difference of rows and difference of columns
	private int dRow;
	private int dColumn;

	// Variable for if pacMan is dead or not
	private boolean isDead;

	// Getters and Setters
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getdRow() {
		return dRow;
	}

	public void setdRow(int dRow) {
		this.dRow = dRow;
	}

	public int getdColumn() {
		return dColumn;
	}

	public void setdColumn(int dColumn) {
		this.dColumn = dColumn;
	}

	public int getNextRow() {
		return row + dRow;
	}

	public int getNextColumn() {
		return column + dColumn;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	// Adds the difference of the rows and columns
	public void move() {

		row += dRow;
		column += dColumn;

	}

	// This method sets the direction depending on if the player goes in any
	// direction
	public void setDirection(int direction) {

		dRow = 0;
		dColumn = 0;

		// Directions
		if (direction == 0)
			dColumn = -1; // Left
		else if (direction == 1)
			dRow = -1; // Up
		else if (direction == 2)
			dColumn = 1; // Right
		else if (direction == 3)
			dRow = 1; // Down

	}

	// This method gets the direction depending on where the player moves
	public int getDirection() {

		if (dRow == 0 && dColumn == -1)
			return 0; // Return 0 if you go left
		else if (dRow == -1 && dColumn == 0)
			return 1; // Return 1 if you go up
		else if (dRow == 0 && dColumn == 1)
			return 2; // Return 2 if you go down
		else
			return 3; // Return 3 if you go right

	}

}
