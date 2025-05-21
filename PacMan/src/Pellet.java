import java.awt.*;

public class Pellet extends Entity implements Consumable {
	private boolean consumed = false; // Tracks if the pellet has been eaten

	/**
	 * Constructor to create a Pellet at the given position with specified size.
	 * 
	 * @param x    The x-coordinate of the pellet.
	 * @param y    The y-coordinate of the pellet.
	 * @param size The width and height of the pellet.
	 */
	public Pellet(int x, int y, int size) {
		super(x, y, size, size);
	}

	/**
	 * Draws the pellet on the screen only if it has not been consumed.
	 * 
	 * @param g The Graphics object used for drawing.
	 */
	public void draw(Graphics g) {
		if (!consumed) {
			g.setColor(Color.WHITE); // Pellets are white squares
			g.fillRect(getX(), getY(), getWidth(), getHeight());
		}
	}

	/**
	 * Checks if the pellet has been consumed (eaten) by the player.
	 * 
	 * @return true if consumed, false otherwise.
	 */
	@Override
	public boolean isConsumed() {
		return consumed;
	}

	/**
	 * Marks the pellet as consumed, so it will no longer be drawn or interact.
	 */
	@Override
	public void consume() {
		consumed = true;
	}

	/**
	 * Restores the pellet to its initial state (not consumed).
	 */
	public void restore() {
		consumed = false;
	}
}
