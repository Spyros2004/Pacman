import java.util.HashSet;
import java.util.Random;
import java.awt.*;

/**
 * Represents a Ghost entity in the game. Ghosts move autonomously, can become
 * frightened (vulnerable), and can respawn after being eaten.
 */
public class Ghost extends MovableEntity {

	private Random random; // Random generator for movement decisions
	private boolean frightened = false; // Whether ghost is currently frightened
	private long frightenedEndTime = 0; // Timestamp when frightened state ends
	private long respawnEndTime; // Timestamp when respawn state ends
	private boolean respawning; // Whether ghost is currently respawning

	private Image scared; // Image to display when frightened

	/**
	 * Constructs a Ghost at given position with given size and images.
	 * 
	 * @param x          initial x-coordinate
	 * @param y          initial y-coordinate
	 * @param size       width and height (square)
	 * @param upImage    image when facing up
	 * @param downImage  image when facing down
	 * @param leftImage  image when facing left
	 * @param rightImage image when facing right
	 * @param scared     image when frightened
	 */
	public Ghost(int x, int y, int size, Image upImage, Image downImage, Image leftImage, Image rightImage,
			Image scared) {
		super(x, y, size, size, 'R', upImage, downImage, leftImage, rightImage);
		random = new Random();
		this.scared = scared;
	}

	/**
	 * Updates the ghost's direction if possible. Checks collisions with walls
	 * before confirming direction change. If new direction leads to collision,
	 * reverts to previous direction.
	 */
	@Override
	public void updateDirection(char newDir, HashSet<Tile> walls) {
		char prevDir = getDirection();
		setDirection(newDir);
		updateVelocity();

		int newX = getX() + getVelocityX();
		int newY = getY() + getVelocityY();
		Rectangle nextInstance = new Rectangle(newX, newY, getWidth(), getHeight());

		// Check if next position collides with any wall
		for (Tile wall : walls) {
			if (nextInstance.intersects(wall.getBounds())) {
				// Collision detected, revert direction and velocity
				setDirection(prevDir);
				updateVelocity();
				return;
			}
		}

		// No collision, update sprite to match new direction
		updateSprite();
	}

	/**
	 * Chooses a movement direction randomly at times: - when ghost hits a wall - or
	 * 10% chance each update to change direction. Movement is restricted to
	 * perpendicular directions: If moving horizontally, next direction can only be
	 * vertical, and vice versa.
	 */
	public void randomMovement(HashSet<Tile> walls) {
		int newX = getX() + getVelocityX();
		int newY = getY() + getVelocityY();
		Rectangle nextInstance = new Rectangle(newX, newY, getWidth(), getHeight());

		boolean canMove = true;
		for (Tile wall : walls) {
			if (nextInstance.intersects(wall.getBounds())) {
				canMove = false;
				break;
			}
		}

		// Change direction if collision ahead or randomly 10% of the time
		if (!canMove || random.nextDouble() < 0.1) {
			char currentDir = getDirection();
			char[] possibleDirections;

			// Restrict movement direction to perpendicular axis
			if (currentDir == 'L' || currentDir == 'R') {
				possibleDirections = new char[] { 'U', 'D' };
			} else { // currentDir == 'U' || currentDir == 'D'
				possibleDirections = new char[] { 'L', 'R' };
			}

			// Randomly pick one of the two possible directions
			char nextDir = possibleDirections[random.nextInt(2)];
			updateDirection(nextDir, walls);
		}
	}

	/**
	 * Sets the frightened state for the ghost for a given duration. Only applies if
	 * ghost is not currently respawning.
	 * 
	 * @param frightened     true to frighten, false to clear frightened
	 * @param durationMillis duration in milliseconds for frightened state
	 */
	public void setFrightened(boolean frightened, long durationMillis) {
		if (!respawning) {
			this.frightened = frightened;
			this.frightenedEndTime = System.currentTimeMillis() + durationMillis;
		}
	}

	/**
	 * Checks whether ghost is currently frightened. Automatically clears frightened
	 * state if duration expired.
	 * 
	 * @return true if frightened, false otherwise
	 */
	public boolean isFrightened() {
		if (frightened && System.currentTimeMillis() > frightenedEndTime)
			frightened = false;
		return frightened;
	}

	/**
	 * Called when ghost is eaten by Pacman. Moves ghost off-screen, clears
	 * frightened state, and starts respawn timer.
	 * 
	 * @param durationMillis time in milliseconds to respawn
	 */
	public void eatGhost(long durationMillis) {
		setPosition(1000, 1000); // Move ghost far off the visible board
		setFrightened(false, 0); // Clear frightened state
		respawnEndTime = System.currentTimeMillis() + durationMillis;
		respawning = true;
	}

	/**
	 * Checks if ghost is currently respawning. If respawn time has passed, ghost
	 * returns to starting position.
	 * 
	 * @return true if still respawning, false if active
	 */
	public boolean isRespawning() {
		if (respawning && System.currentTimeMillis() > respawnEndTime) {
			respawning = false;
			resetPosition(); // Place ghost back at starting position
		}
		return respawning;
	}

	/**
	 * Draws the ghost on the screen. Displays frightened (scared) sprite if
	 * frightened, otherwise shows normal sprite according to direction.
	 */
	public void draw(Graphics g) {
		if (isFrightened()) {
			setCurrentSprite(scared);
		} else {
			updateSprite();
		}
		super.draw(g);
	}
}
