import java.awt.*;

public class PowerPellet extends Pellet {

	/**
	 * Constructs a PowerPellet at the given position and size.
	 * 
	 * @param x    The x-coordinate of the pellet.
	 * @param y    The y-coordinate of the pellet.
	 * @param size The diameter (width and height) of the pellet.
	 */
	public PowerPellet(int x, int y, int size) {
		super(x, y, size);
	}

	/**
	 * Draws the power pellet as a white circle if it has not been consumed.
	 * 
	 * @param g The Graphics context to draw on.
	 */
	@Override
	public void draw(Graphics g) {
		if (!isConsumed()) {
			g.setColor(Color.WHITE);
			// Draw a filled oval (circle) representing the power pellet
			g.fillOval(getX(), getY(), getWidth(), getHeight());
		}
	}
}
