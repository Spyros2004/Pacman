import java.util.HashSet;

/**
 * Interface for any entity that can move within the game board. Provides
 * methods to update movement direction and to move while considering collisions
 * with walls and board boundaries.
 */
public interface Movable {

	/**
	 * Updates the entity's movement direction. The implementation should verify if
	 * the new direction is valid, e.g., does not collide with walls.
	 * 
	 * @param direction the new direction character ('U', 'D', 'L', 'R')
	 * @param walls     set of wall tiles to check for collisions
	 */
	void updateDirection(char direction, HashSet<Tile> walls);

	/**
	 * Moves the entity based on its current velocity/direction. Must handle
	 * collision detection against walls and respect board boundaries.
	 * 
	 * @param walls       set of wall tiles to check for collisions
	 * @param boardWidth  total width of the game board in pixels
	 * @param boardHeight total height of the game board in pixels
	 */
	void move(HashSet<Tile> walls, int boardWidth, int boardHeight);
}
