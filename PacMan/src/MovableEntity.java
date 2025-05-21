import java.awt.*;
import java.util.HashSet;

/**
 * Abstract class for entities that can move on the game board. Implements
 * movement logic, velocity updates, collision detection with walls, direction
 * handling, and sprite updating.
 */
public abstract class MovableEntity extends Entity implements Movable {
	// Current velocity in x and y directions (pixels per update)
	private int velocityX = 0, velocityY = 0;

	// Current movement direction ('U' = up, 'D' = down, 'L' = left, 'R' = right)
	private char direction;

	// Sprites representing the entity facing each direction
	private final Image upImage, downImage, leftImage, rightImage;

	// The sprite currently used for rendering
	private Image currentSprite;

	// Starting position coordinates for resetting the entity
	private final int startX, startY;

	/**
	 * Constructor for a movable entity.
	 * 
	 * @param x          Initial x-coordinate on the board
	 * @param y          Initial y-coordinate on the board
	 * @param width      Width of the entity
	 * @param height     Height of the entity
	 * @param direction  Initial facing direction ('U', 'D', 'L', 'R')
	 * @param upImage    Sprite facing upwards
	 * @param downImage  Sprite facing downwards
	 * @param leftImage  Sprite facing left
	 * @param rightImage Sprite facing right
	 */
	public MovableEntity(int x, int y, int width, int height, char direction, Image upImage, Image downImage,
			Image leftImage, Image rightImage) {
		super(x, y, width, height);
		this.direction = direction;
		this.upImage = upImage;
		this.downImage = downImage;
		this.leftImage = leftImage;
		this.rightImage = rightImage;
		this.currentSprite = rightImage; // Default sprite facing right
		this.startX = x;
		this.startY = y;
	}

	/**
	 * Abstract method to update movement direction with collision checks.
	 * 
	 * @param newDir New direction to move in ('U', 'D', 'L', 'R')
	 * @param walls  Set of wall tiles to check collisions against
	 */
	public abstract void updateDirection(char newDir, HashSet<Tile> walls);

	/**
	 * Updates velocity components based on the current direction. Velocity
	 * magnitude is proportional to entity's width.
	 */
	public void updateVelocity() {
		switch (direction) {
		case 'U':
			setVelocity(0, -getWidth() / 4); // Move upward: negative y velocity
			break;
		case 'D':
			setVelocity(0, getWidth() / 4); // Move downward: positive y velocity
			break;
		case 'L':
			setVelocity(-getWidth() / 4, 0); // Move left: negative x velocity
			break;
		case 'R':
			setVelocity(getWidth() / 4, 0); // Move right: positive x velocity
			break;
		}
	}

	/**
	 * Updates the sprite image based on the current direction.
	 */
	public void updateSprite() {
		switch (direction) {
		case 'U':
			currentSprite = upImage;
			break;
		case 'D':
			currentSprite = downImage;
			break;
		case 'L':
			currentSprite = leftImage;
			break;
		case 'R':
			currentSprite = rightImage;
			break;
		}
	}

	/**
	 * Moves the entity by its current velocity, checking for wall collisions and
	 * enforcing board wrapping when moving off the edges.
	 * 
	 * @param walls       Set of wall tiles for collision detection
	 * @param boardWidth  Width of the board in pixels
	 * @param boardHeight Height of the board in pixels
	 */
	public void move(HashSet<Tile> walls, int boardWidth, int boardHeight) {
		int newX = getX() + velocityX; // Calculate new X position based on velocity
		int newY = getY() + velocityY; // Calculate new Y position based on velocity

		// Check collision with walls before moving
		Rectangle newBounds = new Rectangle(newX, newY, getWidth(), getHeight());
		for (Tile wall : walls) {
			if (newBounds.intersects(wall.getBounds())) {
				return; // Collision detected: abort movement
			}
		}

		// Update position if no collision detected
		setPosition(newX, newY);

		// Screen wrap-around logic for X coordinate
		if (getX() < 0)
			setPosition(boardWidth - getWidth(), getY()); // Wrap from left edge to right
		else if (getX() >= boardWidth)
			setPosition(0, getY()); // Wrap from right edge to left

		// Screen wrap-around logic for Y coordinate
		if (getY() < 0)
			setPosition(getX(), boardHeight - getHeight()); // Wrap from top edge to bottom
		else if (getY() >= boardHeight)
			setPosition(getX(), 0); // Wrap from bottom edge to top
	}

	/**
	 * Sets velocity components.
	 * 
	 * @param vx Velocity in x direction
	 * @param vy Velocity in y direction
	 */
	protected void setVelocity(int vx, int vy) {
		this.velocityX = vx;
		this.velocityY = vy;
	}

	/**
	 * Gets the current direction of movement.
	 * 
	 * @return direction character ('U', 'D', 'L', 'R')
	 */
	public char getDirection() {
		return direction;
	}

	/**
	 * Sets the current direction of movement.
	 * 
	 * @param direction new direction character
	 */
	public void setDirection(char direction) {
		this.direction = direction;
	}

	/**
	 * Gets the current velocity in the X direction (pixels per update).
	 * 
	 * @return velocity along the X-axis
	 */
	public int getVelocityX() {
		return velocityX;
	}

	/**
	 * Gets the current velocity in the Y direction (pixels per update).
	 * 
	 * @return velocity along the Y-axis
	 */
	public int getVelocityY() {
		return velocityY;
	}

	/**
	 * Gets the current sprite image used for rendering.
	 * 
	 * @return current Image representing the entity's direction
	 */
	public Image getCurrentSprite() {
		return currentSprite;
	}

	/**
	 * Changes the current sprite for rendering.
	 * 
	 * @param newImage New sprite image to display
	 */
	public void setCurrentSprite(Image newImage) {
		this.currentSprite = newImage;
	}

	/**
	 * Resets the entity to its starting position and default direction.
	 */
	public void resetPosition() {
		setPosition(startX, startY); // Move back to start position
		setVelocity(0, 0); // Stop movement
		direction = 'R'; // Default direction facing right
		updateSprite(); // Update sprite accordingly
	}

	/**
	 * Draws the entity's current sprite on the provided graphics context.
	 * 
	 * @param g Graphics context to draw on
	 */
	public void draw(Graphics g) {
		g.drawImage(getCurrentSprite(), getX(), getY(), getWidth(), getHeight(), null);
	}
}
