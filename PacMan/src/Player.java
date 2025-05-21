import java.awt.*;
import java.util.HashSet;

public class Player extends MovableEntity {

	/**
	 * Constructs the player (Pac-Man) with given position, size, and directional
	 * sprites.
	 * 
	 * @param x          Initial x-coordinate.
	 * @param y          Initial y-coordinate.
	 * @param size       Width and height of the player sprite.
	 * @param upImage    Image for facing up.
	 * @param downImage  Image for facing down.
	 * @param leftImage  Image for facing left.
	 * @param rightImage Image for facing right.
	 */
	public Player(int x, int y, int size, Image upImage, Image downImage, Image leftImage, Image rightImage) {
		super(x, y, size, size, 'R', upImage, downImage, leftImage, rightImage);
	}

	/**
	 * Attempts to update the player's direction to newDir if it does not collide
	 * with walls. If the movement would cause a collision, reverts to the previous
	 * direction.
	 * 
	 * @param newDir The desired new direction ('U', 'D', 'L', 'R').
	 * @param walls  The set of wall tiles for collision detection.
	 */
	@Override
	public void updateDirection(char newDir, HashSet<Tile> walls) {
		char prevDir = getDirection(); // Save current direction to revert if needed
		setDirection(newDir);
		updateVelocity();

		// Calculate player's next position based on velocity
		int newX = getX() + getVelocityX();
		int newY = getY() + getVelocityY();
		Rectangle nextInstance = new Rectangle(newX, newY, getWidth(), getHeight());

		// Check if next position collides with any wall tile
		for (Tile wall : walls) {
			if (nextInstance.intersects(wall.getBounds())) {
				// Collision detected, revert direction and velocity
				setDirection(prevDir);
				updateVelocity();
				return;
			}
		}

		updateSprite(); // Update sprite to match the new direction if move allowed
	}
}
