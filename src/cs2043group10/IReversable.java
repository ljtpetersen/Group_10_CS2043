package cs2043group10;

import javafx.scene.Node;

public interface IReversable {
	public void onShow();
	public void onHide();
	public Node getNode();
	public String getTitle();
	public void destroy();
}
