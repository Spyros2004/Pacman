import java.awt.Image;
import java.util.HashSet;

/**
 * Represents the game board including the map layout, walls, pellets, ghosts,
 * and the player (Pacman). Loads the map from a tile map string and manages
 * entity initialization.
 */
public class GameBoard {
	private final int tileSize = 32; // Size of each tile (square) in pixels
	private final int rows = 21; // Number of tile rows
	private final int columns = 19; // Number of tile columns
	private final int boardWidth = columns * tileSize; // Total board width in pixels
	private final int boardHeight = rows * tileSize; // Total board height in pixels

	private int updateSpeed = 50; // Delay in ms between game updates (lower is faster)

	// Map layout encoded as a string array, where each char represents a tile type:
	// 'X' = wall, '.' = power pellet, ' ' = pellet, 'P' = Pacman start,
	// 'b','o','p','r' = ghosts, 'O' = empty tile
	private String[] tileMap = { "XXXXXXXXXXXXXXXXXXX", "X.       X       .X", "X XX XXX X XXX XX X",
			"X                 X", "X XX X XXXXX X XX X", "X    X       X    X", "XXXX XXXX XXXX XXXX",
			"OOOX X       X XOOO", "XXXX X XXrXX X XXXX", "O       bpo       O", "XXXX X XXXXX X XXXX",
			"OOOX X       X XOOO", "XXXX X XXXXX X XXXX", "X        X        X", "X XX XXX X XXX XX X",
			"X  X     P     X  X", "XX X X XXXXX X X XX", "X    X   X   X    X", "X XXXXXX X XXXXXX X",
			"X.               .X", "XXXXXXXXXXXXXXXXXXX" };

	// Sets to hold different types of entities for easy management and collision
	// detection
	private final HashSet<Tile> walls = new HashSet<>();
	private final HashSet<Pellet> pellets = new HashSet<>();
	private final HashSet<Ghost> ghosts = new HashSet<>();
	private final HashSet<Integer> initPositionsX = new HashSet<>(); // Starting X positions for reset
	private final HashSet<Integer> initPositionsY = new HashSet<>(); // Starting Y positions for reset
	private Player pacman; // The player character

	// Images for game entities
	private final Image wallImage;
	private final Image blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage;
	private final Image pacmanUpImage, pacmanDownImage, pacmanLeftImage, pacmanRightImage;
	private final Image scaredGhostImage;

	/**
	 * Constructor sets images for all entities and loads the initial map layout.
	 */
	public GameBoard(Image wallImage, Image blueGhost, Image orangeGhost, Image pinkGhost, Image redGhost, Image up,
			Image down, Image left, Image right, Image scaredGhost) {

		this.wallImage = wallImage;
		this.blueGhostImage = blueGhost;
		this.orangeGhostImage = orangeGhost;
		this.pinkGhostImage = pinkGhost;
		this.redGhostImage = redGhost;
		this.pacmanUpImage = up;
		this.pacmanDownImage = down;
		this.pacmanLeftImage = left;
		this.pacmanRightImage = right;
		this.scaredGhostImage = scaredGhost;

		loadMap(); // Parse tileMap string and instantiate entities accordingly
	}

	/**
	 * Parses the tileMap array to create game objects: - Walls at 'X' - Regular
	 * pellets at ' ' - Power pellets at '.' - Player at 'P' - Various ghosts at
	 * their respective characters ('b','o','p','r') Tracks initial positions for
	 * resets.
	 */
	private void loadMap() {
		for (int i = 0; i < rows; i++) {
			String row = tileMap[i];
			for (int j = 0; j < columns; j++) {
				char tile = row.charAt(j);
				int x = j * tileSize;
				int y = i * tileSize;

				switch (tile) {
				case 'X': // Wall tile
					walls.add(new Tile(x, y, tileSize, tileSize, wallImage));
					break;
				case ' ': // Regular pellet (small dot)
					pellets.add(new Pellet(x + 14, y + 14, 4)); // Center pellet inside tile
					break;
				case '.': // Power pellet (larger dot)
					int pelletSize = 4;
					int powerSize = pelletSize * 4;
					pellets.add(new PowerPellet(x + 14 - powerSize / 2, y + 14 - powerSize / 2, powerSize));
					break;
				case 'P': // Player start position
					pacman = new Player(x, y, tileSize, pacmanUpImage, pacmanDownImage, pacmanLeftImage,
							pacmanRightImage);
					initPositionsX.add(x);
					initPositionsY.add(y);
					break;
				case 'b': // Blue ghost
					ghosts.add(new Ghost(x, y, tileSize, blueGhostImage, blueGhostImage, blueGhostImage, blueGhostImage,
							scaredGhostImage));
					initPositionsX.add(x);
					initPositionsY.add(y);
					break;
				case 'o': // Orange ghost
					ghosts.add(new Ghost(x, y, tileSize, orangeGhostImage, orangeGhostImage, orangeGhostImage,
							orangeGhostImage, scaredGhostImage));
					initPositionsX.add(x);
					initPositionsY.add(y);
					break;
				case 'p': // Pink ghost
					ghosts.add(new Ghost(x, y, tileSize, pinkGhostImage, pinkGhostImage, pinkGhostImage, pinkGhostImage,
							scaredGhostImage));
					initPositionsX.add(x);
					initPositionsY.add(y);
					break;
				case 'r': // Red ghost
					ghosts.add(new Ghost(x, y, tileSize, redGhostImage, redGhostImage, redGhostImage, redGhostImage,
							scaredGhostImage));
					initPositionsX.add(x);
					initPositionsY.add(y);
					break;
				case 'O': // Empty tile, no action needed
					break;
				default:
					System.err.println("Invalid character in tile map: " + tile);
					System.exit(1);
				}
			}
		}
	}

	/**
	 * Resets Pacman's and ghosts' positions to their starting coordinates, and
	 * clears frightened mode on ghosts.
	 */
	public void resetEntities() {
		pacman.resetPosition();

		for (Ghost ghost : ghosts) {
			ghost.resetPosition();
			ghost.setFrightened(false, 0);
		}
	}

	/**
	 * Gets the set of wall tiles on the game board.
	 * 
	 * @return a HashSet containing all wall tiles
	 */
	public HashSet<Tile> getWalls() {
		return walls;
	}

	/**
	 * Gets the set of pellets available on the game board.
	 * 
	 * @return a HashSet containing all pellets
	 */
	public HashSet<Pellet> getPellets() {
		return pellets;
	}

	/**
	 * Gets the set of ghost entities currently in the game.
	 * 
	 * @return a HashSet containing all ghosts
	 */
	public HashSet<Ghost> getGhosts() {
		return ghosts;
	}

	/**
	 * Gets the player (Pacman) entity.
	 * 
	 * @return the Player object representing Pacman
	 */
	public Player getPacman() {
		return pacman;
	}

	/**
	 * Gets the width of the game board in pixels.
	 * 
	 * @return the width of the board
	 */
	public int getBoardWidth() {
		return boardWidth;
	}

	/**
	 * Gets the height of the game board in pixels.
	 * 
	 * @return the height of the board
	 */
	public int getBoardHeight() {
		return boardHeight;
	}

	/**
	 * Speeds up the game by decreasing update delay, with a lower bound.
	 */
	public void increaseGameSpeed() {
		updateSpeed = Math.max(5, updateSpeed - 5);
	}

	/**
	 * Returns the current delay between game updates.
	 */
	public int getGameSpeed() {
		return updateSpeed;
	}

	/**
	 * Restores all pellets to unconsumed state
	 */
	public void resetPellets() {
		for (Pellet pellet : pellets) {
			pellet.restore();
		}
	}
}
