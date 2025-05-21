import javax.swing.JFrame;

/**
 * Main application class for the Pac Man game. Sets up the JFrame and
 * initialises the game.
 */
public class App {

	/**
	 * The main method initialises the game window.
	 */
	public static void main(String[] args) {
		// setup the JFrame for the game window
		JFrame frame = new JFrame("Pac Man");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Game pacmanGame = new Game();
		frame.add(pacmanGame);

		frame.pack();
		frame.setLocationRelativeTo(null);
		pacmanGame.requestFocus();
		frame.setVisible(true);
	}

}
