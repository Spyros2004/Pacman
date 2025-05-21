import java.awt.event.KeyEvent;
import java.util.HashSet;

/**
 * Controls the player's movement based on keyboard input and game board
 * constraints.
 */
public class CharacterController {
	private final Player player;
	private HashSet<Tile> walls; // Set of wall tiles for collision detection
	private final int boardWidth; // Width of the game board
	private final int boardHeight; // Height of the game board
	private final DirectionQueue directionQueue = new DirectionQueue(); // Tracks pressed directions

	/**
	 * Constructs a CharacterController to handle player movement.
	 * 
	 * @param player      the player character to control
	 * @param walls       set of wall tiles for collision checks
	 * @param boardWidth  width of the game board
	 * @param boardHeight height of the game board
	 */
	public CharacterController(Player player, HashSet<Tile> walls, int boardWidth, int boardHeight) {
		this.player = player;
		this.walls = walls;
		this.boardWidth = boardWidth;
		this.boardHeight = boardHeight;
	}

	/**
	 * Updates the player's direction and moves the player accordingly.
	 */
	public void update() {
		char held = directionQueue.getCurrent(); // Current active direction key
		if (held != ' ' && held != player.getDirection())
			player.updateDirection(held, walls); // Change direction if different and valid

		player.move(walls, boardWidth, boardHeight); // Move player with collision checks
	}

	/**
	 * Handles key press events by adding the direction to the queue.
	 * 
	 * @param keyCode the key code of the pressed key
	 */
	public void onKeyPress(int keyCode) {
		char dir = mapKeyToDirection(keyCode);
		if (dir != ' ')
			directionQueue.press(dir);
	}

	/**
	 * Handles key release events by removing the direction from the queue.
	 * 
	 * @param keyCode the key code of the released key
	 */
	public void onKeyRelease(int keyCode) {
		char dir = mapKeyToDirection(keyCode);
		if (dir != ' ')
			directionQueue.release(dir);
	}

	/**
	 * Maps keyboard key codes to direction characters.
	 * 
	 * @param keyCode the key code to map
	 * @return a character representing the direction ('U', 'D', 'L', 'R') or space
	 *         if unmapped
	 */
	private char mapKeyToDirection(int keyCode) {
		return switch (keyCode) {
		case KeyEvent.VK_UP, KeyEvent.VK_W -> 'U';
		case KeyEvent.VK_DOWN, KeyEvent.VK_S -> 'D';
		case KeyEvent.VK_LEFT, KeyEvent.VK_A -> 'L';
		case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> 'R';
		default -> ' ';
		};
	}
}
