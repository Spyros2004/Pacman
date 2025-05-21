import java.awt.event.KeyEvent;
import java.awt.Rectangle;

/**
 * Controls the game logic, including player movement, ghost behavior, collision
 * detection, scoring, lives, and game state management.
 */
public class GameController {
	private int score; // Current player's score
	private int lives; // Number of lives remaining
	private boolean gameOver; // Flag to indicate if the game has ended

	private final GameBoard board; // Reference to the game board and its entities
	private final DirectionQueue directionQueue; // Tracks player input directions

	private static int highScore = 0; // Static high score across all games

	private SoundManager soundManager; // Handles playing game sounds

	/**
	 * Initializes the controller with a game board and sound manager. Starts the
	 * player with 3 lives and zero score.
	 */
	public GameController(GameBoard board, SoundManager soundManager) {
		this.board = board;
		this.directionQueue = new DirectionQueue();
		this.score = 0;
		this.lives = 3;
		this.gameOver = false;
		this.soundManager = soundManager;
	}

	/**
	 * Main update method called on each game loop tick. - Stops if game is over. -
	 * Processes player movement, pellet consumption, ghost movement. - Checks for
	 * collisions between player and ghosts. - Resets and speeds up the game if all
	 * pellets are eaten.
	 */
	public void update() {
		if (gameOver) {
			return; // Skip update logic if the game has ended
		}

		handlePlayerMovement();
		handlePelletConsumption();
		handleGhostMovement();
		checkCollisions();

		// When all pellets are consumed, reset and increase difficulty
		if (allPelletsConsumed()) {
			increaseGameSpeed();
			board.resetPellets();
			board.resetEntities();
		}
	}

	/**
	 * Checks if all pellets on the board are consumed. If so, plays a success sound
	 * and returns true.
	 */
	private boolean allPelletsConsumed() {
		for (Pellet pellet : board.getPellets()) {
			if (!pellet.isConsumed()) {
				return false;
			}
		}
		soundManager.playSuccessSound();
		return true;
	}

	/**
	 * Instructs the board to increase the game speed (lower delay between moves).
	 */
	private void increaseGameSpeed() {
		board.increaseGameSpeed();
	}

	/**
	 * Processes player movement based on input directions. Uses direction queue to
	 * get the latest direction key pressed. Updates player's direction and attempts
	 * to move within board boundaries, avoiding walls.
	 */
	private void handlePlayerMovement() {
		if (gameOver)
			return;

		char held = directionQueue.getCurrent(); // Current direction from input
		Player pacman = board.getPacman();

		if (held != ' ') {
			pacman.updateDirection(held, board.getWalls()); // Change direction if valid
		}
		pacman.move(board.getWalls(), board.getBoardWidth(), board.getBoardHeight());
	}

	/**
	 * Checks if Pacman intersects with any pellets. Consumes the pellet, triggers
	 * frightened mode if power pellet, increments score, and plays pellet sound.
	 */
	private void handlePelletConsumption() {
		if (gameOver)
			return;

		Player pacman = board.getPacman();
		Rectangle pacBounds = pacman.getBounds();

		for (Pellet pellet : board.getPellets()) {
			if (!pellet.isConsumed() && pacBounds.intersects(pellet.getBounds())) {
				pellet.consume();

				// If power pellet, set all ghosts to frightened state for 6 seconds
				if (pellet instanceof PowerPellet) {
					for (Ghost ghost : board.getGhosts()) {
						ghost.setFrightened(true, 6000);
					}
				}
				increaseScore(10); // Increase score for eating pellet
				soundManager.playPelletSound();
			}
		}
	}

	/**
	 * Moves all ghosts on the board using random movement algorithm, skipping
	 * ghosts that are currently respawning.
	 */
	private void handleGhostMovement() {
		if (gameOver)
			return;

		for (Ghost ghost : board.getGhosts()) {
			if (ghost.isRespawning())
				continue;

			ghost.randomMovement(board.getWalls()); // Choose next move direction randomly
			ghost.move(board.getWalls(), board.getBoardWidth(), board.getBoardHeight()); // Move ghost
		}
	}

	/**
	 * Checks for collisions between Pacman and ghosts. If Pacman collides with a
	 * frightened ghost, ghost is eaten and score increases. If Pacman collides with
	 * a normal ghost, player loses a life.
	 */
	private void checkCollisions() {
		if (gameOver)
			return;

		Player pacman = board.getPacman();

		for (Ghost ghost : board.getGhosts()) {
			if (pacman.getBounds().intersects(ghost.getBounds())) {
				if (ghost.isFrightened()) {
					ghost.eatGhost(3000); // Ghost is eaten and goes to respawn mode
					increaseScore(200);
					soundManager.playSuccessSound();
				} else {
					// Pacman hit by a normal ghost - lose a life and reset or end game
					loseLife();
					return; // Stop checking after losing life to avoid multiple deductions
				}
			}
		}
	}

	/**
	 * Handles key press input by mapping key codes to directions and adding them to
	 * the direction queue.
	 */
	public void onKeyPress(int keyCode) {
		char dir = mapKeyToDirection(keyCode);
		if (dir != ' ') {
			directionQueue.press(dir);
		}
	}

	/**
	 * Handles key release input by removing direction from the direction queue.
	 */
	public void onKeyRelease(int keyCode) {
		char dir = mapKeyToDirection(keyCode);
		if (dir != ' ') {
			directionQueue.release(dir);
		}
	}

	/**
	 * Maps key codes (arrows and WASD) to directional characters used internally:
	 * 'U' = Up, 'D' = Down, 'L' = Left, 'R' = Right, ' ' = no direction.
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

	/**
	 * Increases the player's score by a given amount.
	 */
	private void increaseScore(int amount) {
		score += amount;
	}

	/**
	 * Decrements player's lives. If no lives left, sets game over and updates high
	 * score. If lives remain, resets entities and plays appropriate sounds.
	 */
	private void loseLife() {
		if (lives > 0) {
			lives--;

			if (lives == 0) {
				gameOver = true;
				soundManager.playGameOverSound();

				// Update static high score if current score exceeds it
				if (score > highScore) {
					highScore = score;
				}
			} else {
				board.resetEntities(); // Reset player and ghosts positions for new life
			}

			if (!gameOver)
				soundManager.playLifeLostSound();
		}
	}

	/**
	 * Gets the current player's score.
	 * 
	 * @return the current score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Gets the remaining number of lives the player has.
	 * 
	 * @return the number of lives left
	 */
	public int getLives() {
		return lives;
	}

	/**
	 * Checks whether the game is over.
	 * 
	 * @return true if the game is over, false otherwise
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	/**
	 * Gets the highest score achieved in the game (static).
	 * 
	 * @return the highest recorded score
	 */
	public static int getHighScore() {
		return highScore;
	}
}
