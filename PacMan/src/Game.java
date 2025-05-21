import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main game panel responsible for rendering the game, handling user input,
 * managing the game loop, and displaying UI elements like score and lives.
 */
public class Game extends JPanel implements ActionListener, KeyListener {
	private static final long serialVersionUID = 1L;

	private final Timer gameLoop; // Timer triggering regular game updates (frame rate control)
	private final GameBoard board; // Holds game objects and board state
	private final GameController controller; // Processes game logic and input handling
	private Image heartImage; // Icon representing player lives
	private JButton restartButton; // Button to restart the game after game over

	private int currentSpeed; // Current delay (ms) between game updates
	private int speedLevel = 1; // Tracks speed increments to display

	private SoundManager soundManager; // Controls game audio playback

	/**
	 * Constructor initializes the game, loads assets, sets up game components, and
	 * starts background music with a delayed game loop start.
	 */
	public Game() {
		heartImage = load("/res/assets/heart.png");

		// Initialize the game board with various images for walls, ghosts, and Pacman
		// sprites
		board = new GameBoard(load("/res/assets/wall.png"), load("/res/assets/blueGhost.png"),
				load("/res/assets/orangeGhost.png"), load("/res/assets/pinkGhost.png"),
				load("/res/assets/redGhost.png"), load("/res/assets/pacmanUp.png"), load("/res/assets/pacmanDown.png"),
				load("/res/assets/pacmanLeft.png"), load("/res/assets/pacmanRight.png"),
				load("/res/assets/scaredGhost.png"));

		soundManager = new SoundManager();
		controller = new GameController(board, soundManager);

		currentSpeed = board.getGameSpeed();

		soundManager.playStartMusic(); // Play intro music before game starts

		// Setup panel size and input handling
		setPreferredSize(new Dimension(board.getBoardWidth(), board.getBoardHeight()));
		setBackground(Color.BLACK);
		addKeyListener(this);
		setFocusable(true);

		// Game loop timer controls update frequency (speed)
		gameLoop = new Timer(currentSpeed, this);

		// Start the game loop after a 4-second delay to let music play
		int delay = 4000; // milliseconds
		new Timer(delay, e -> {
			gameLoop.start();
			((Timer) e.getSource()).stop(); // Stop this one-shot timer after starting game loop
		}).start();
	}

	/**
	 * Loads an image from the resource path.
	 *
	 * @param path the path to the image resource
	 * @return the loaded Image object
	 */
	private Image load(String path) {
		return new ImageIcon(getClass().getResource(path)).getImage();
	}

	/**
	 * Renders all game elements: Pacman, ghosts, walls, pellets, UI (score, speed,
	 * lives), and game over messages if applicable.
	 *
	 * @param g the Graphics context to paint on
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Draw main character
		board.getPacman().draw(g);

		// Draw all ghosts in their current states and positions
		for (Ghost ghost : board.getGhosts()) {
			ghost.draw(g);
		}

		// Draw walls to form the maze
		for (Tile wall : board.getWalls()) {
			wall.draw(g);
		}

		// Draw pellets only if not yet consumed
		for (Pellet pellet : board.getPellets()) {
			if (!pellet.isConsumed()) {
				pellet.draw(g);
			}
		}

		// Draw score in white, top-left corner
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 18));
		g.drawString("Score: " + controller.getScore(), 10, 20);

		// Draw current speed level in yellow, bottom-left corner
		g.setColor(Color.YELLOW);
		g.drawString(String.format("Speed: %d", speedLevel), 10, board.getBoardHeight() - 10);

		// Draw player lives as heart icons in the top-right corner
		int heartSize = 20;
		for (int i = 0; i < controller.getLives(); i++) {
			g.drawImage(heartImage, getWidth() - (heartSize * (i + 1)) - 10, 10, heartSize, heartSize, null);
		}

		// If the game is over, display centered "Game Over" and high score messages
		if (controller.isGameOver()) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 36));
			String message = "Game Over";
			FontMetrics fm = g.getFontMetrics();
			int x = (getWidth() - fm.stringWidth(message)) / 2;
			int y = getHeight() / 2 - 40;
			g.drawString(message, x, y);

			// Draw high score slightly below the game over text
			String highScoreMessage = "High Score: " + GameController.getHighScore();
			g.setFont(new Font("Arial", Font.PLAIN, 24));
			fm = g.getFontMetrics();
			x = (getWidth() - fm.stringWidth(highScoreMessage)) / 2;
			y = getHeight() / 2 - 10;
			g.drawString(highScoreMessage, x, y);
		}
	}

	/**
	 * Called by the game loop timer on each tick to update game state. It checks
	 * for game over condition, updates speed dynamically, and requests screen
	 * repaint.
	 *
	 * @param e the ActionEvent triggered by the Timer
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!controller.isGameOver()) {
			controller.update(); // Update game logic

			int newSpeed = board.getGameSpeed();
			// If speed changed, adjust timer delay to speed up the game
			if (newSpeed != currentSpeed) {
				currentSpeed = newSpeed;
				gameLoop.stop();
				gameLoop.setDelay(currentSpeed);
				gameLoop.start();
				speedLevel++; // Increment visible speed level
			}
		} else {
			gameLoop.stop(); // Stop game updates when over
			showRestartUI(); // Show restart button
		}
		repaint(); // Refresh screen
	}

	/**
	 * Shows a restart button centered on the panel after game over. The button
	 * triggers restarting the game when clicked.
	 */
	private void showRestartUI() {
		if (restartButton == null) {
			restartButton = new JButton("Restart");
			restartButton.setFont(new Font("Arial", Font.BOLD, 20));
			restartButton.setFocusable(false);
			restartButton.setBounds(getWidth() / 2 - 75, getHeight() / 2 + 20, 150, 40);
			restartButton.addActionListener(e -> restartGame());

			setLayout(null); // Use absolute positioning
			add(restartButton);
			repaint();
		}
	}

	/**
	 * Restarts the game by creating a new Game instance and replacing the current
	 * game panel in the JFrame.
	 */
	private void restartGame() {
		remove(restartButton);

		Game newGame = new Game();
		JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
		frame.getContentPane().removeAll();
		frame.getContentPane().add(newGame);
		frame.revalidate();
		frame.pack();
		newGame.requestFocusInWindow();
	}

	/**
	 * Handles key press events by forwarding the key code to the controller.
	 *
	 * @param e the KeyEvent triggered by pressing a key
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		controller.onKeyPress(e.getKeyCode());
	}

	/**
	 * Handles key release events by forwarding the key code to the controller.
	 *
	 * @param e the KeyEvent triggered by releasing a key
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		controller.onKeyRelease(e.getKeyCode());
	}

	/**
	 * Handles key typed events. Not used.
	 *
	 * @param e the KeyEvent triggered by typing a key
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		// not used
	}
}
