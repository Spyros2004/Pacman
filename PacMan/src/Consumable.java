/**
 * Represents an item or object that can be consumed.
 */
public interface Consumable {

	/**
	 * Checks if the item has been consumed.
	 * 
	 * @return true if consumed, false otherwise
	 */
	boolean isConsumed();

	/**
	 * Marks the item as consumed.
	 */
	void consume();

	/**
	 * Restores the item to its original state.
	 */
	void restore();
}
