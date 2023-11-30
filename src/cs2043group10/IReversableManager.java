package cs2043group10;

/**
 * This interface represents a manager for the reversable nodes. It managers the backwards,
 * forwards, refresh, logout, and the node history.
 * 
 * @author James Petersen
 */
public interface IReversableManager {
	/**
	 * Push a new node to the front of the manager.
	 * @param newNode The node to push.
	 */
	public void pushNewNode(IReversable newNode);
	/**
	 * Get the database manager associated with the reversable manager.
	 * @return The database manager.
	 */
	public IDatabase getDatabaseManager();
	/**
	 * Go backwards in the node history.
	 */
	public void goBackwards();
	/**
	 * Go forwards in the node history.
	 */
	public void goForwards();
	/**
	 * Remove the topmost node from the stack, and set the one below it to be active.
	 */
	public void popNode();
	/**
	 * Replace the top node with the passed node.
	 * @param newTop The new node.
	 */
	public void replaceTop(IReversable newTop);
	/**
	 * Check if the passed node is at the top of the stack.
	 * @param node The node to check.
	 * @return Whether it is at the top of the stack.
	 */
	public boolean isAtTop(IReversable node);
	/**
	 * Trigger the refresh event on the current node.
	 */
	public void refresh();
}
