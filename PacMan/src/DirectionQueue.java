import java.util.LinkedList;

/**
 * Manages a queue of active direction inputs, tracking the order of pressed
 * keys.
 */
public class DirectionQueue {
	private LinkedList<Character> queue = new LinkedList<>();

	/**
	 * Adds a direction to the queue or moves it to the end if already present,
	 * representing the most recent key press.
	 * 
	 * @param dir the direction character to add ('U', 'D', 'L', 'R')
	 */
	public void press(char dir) {
		queue.remove((Character) dir); // Remove if already in queue to avoid duplicates
		queue.addLast(dir); // Add to the end to mark as most recent
	}

	/**
	 * Removes a direction from the queue when its key is released.
	 * 
	 * @param dir the direction character to remove
	 */
	public void release(char dir) {
		queue.remove((Character) dir);
	}

	/**
	 * Gets the current active direction, which is the most recently pressed key.
	 * 
	 * @return the last pressed direction character, or space if none
	 */
	public char getCurrent() {
		if (queue.isEmpty())
			return ' ';
		return queue.getLast();
	}

	/**
	 * Checks if there are no active directions in the queue.
	 * 
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty() {
		return queue.isEmpty();
	}
}
