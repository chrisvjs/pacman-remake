/*
 * Name: Christopher Singh

 * 
 * This is a basic PacMan game where you must eat all the pellets to progess to the next level and once you beat that level, you win the game
 * You eat pellets, and cherries to make the score go up, and if you eat a power up, you are able to eat ghosts for 10 seconds which gives you additional score
 * The Ghosts will chase you and will increase in difficulty when you go to the next level but when you eat the Power Pellet they will run away.
 * 
 * Features: Score Tracker, Ghost Chase AI, 3 Lives, High Score for Current Session, Cherry Bonus score, Sounds, Barrier so PacMan can't go inside the ghost house,
 * 2 Levels, Power pellets so the ghosts run away and turn blue, and helping the ghosts get outside of the house.
 */

public class PacManGame {

	public static void main(String[] args) {

		// Call GUI method and plays song
		new MainMenu();
		Board.soundTrack("menumusic.wav");

	}

}
