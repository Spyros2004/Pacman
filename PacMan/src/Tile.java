import java.awt.*;

public class Tile extends Entity {
	private final Image image; // The image representing this tile

	/**
	 * Constructs a Tile at the specified position and size with the given image.
	 *
	 * @param x      The x-coordinate of the tile
	 * @param y      The y-coordinate of the tile
	 * @param width  The width of the tile
	 * @param height The height of the tile
	 * @param image  The image used to render the tile
	 */
	public Tile(int x, int y, int width, int height, Image image) {
		super(x, y, width, height);
		this.image = image;
	}

	/**
	 * Draws the tile using its image at the tile's position and size.
	 *
	 * @param g The Graphics context to draw on
	 */
	public void draw(Graphics g) {
		// Draw the tile's image stretched to the tile's bounding rectangle
		g.drawImage(image, getX(), getY(), getWidth(), getHeight(), null);
	}
}
