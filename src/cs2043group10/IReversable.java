package cs2043group10;

import javafx.scene.Node;

/**
 * This interface represents a view that is shown by the reversable manager.
 * 
 * @author James Petersen
 */
public interface IReversable {
	/**
	 * This method is called before the reversable node is shown.
	 */
	public void beforeShow();
	/**
	 * This method is called after the reversable node is shown.
	 */
	public void afterShow();
	/**
	 * This method is called before the reversable node is hidden.
	 */
	public void beforeHide();
	/**
	 * This method is called after the reversable node is hidden.
	 */
	public void afterHide();
	/**
	 * This method returns the JavaFX node that this reversable node holds.
	 * @return The node.
	 */
	public Node getNode();
	/**
	 * This method returns the title of the node.
	 * @return The title of the node.
	 */
	public String getTitle();
	/**
	 * This method is called before the reversable node is removed from the manager.
	 */
	public void destroy();
	/**
	 * This method is called when the node is currently active and the refresh button is pressed.s
	 */
	public void refresh();
}
