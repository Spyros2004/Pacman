import java.awt.*;

/**
 * Abstract base class representing a drawable entity in the game world. Stores
 * position and size information.
 */
public abstract class Entity {
	private int x, y; // Position coordinates
	private int width, height; // Dimensions of the entity

	/**
	 * Constructs an Entity with specified position and size.
	 * 
	 * @param x      the x-coordinate of the entity
	 * @param y      the y-coordinate of the entity
	 * @param width  the width of the entity
	 * @param height the height of the entity
	 */
	public Entity(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * Returns the bounding rectangle of the entity for collision detection.
	 * 
	 * @return a Rectangle representing the entity's bounds
	 */
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	/**
	 * Abstract method to draw the entity using the provided Graphics context.
	 * 
	 * @param g the Graphics object used for drawing
	 */
	public abstract void draw(Graphics g);

	/**
	 * Returns the current x-coordinate of the entity.
	 *
	 * @return x position in pixels
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the current y-coordinate of the entity.
	 *
	 * @return y position in pixels
	 */
	public int getY() {
		return y;
	}

	/**
	 * Returns the width of the entity.
	 *
	 * @return width in pixels
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the entity.
	 *
	 * @return height in pixels
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the entity's position.
	 * 
	 * @param x the new x-coordinate
	 * @param y the new y-coordinate
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
