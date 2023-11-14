package cs2043group10;

import javafx.scene.Node;

public interface IReversable {
	public void beforeShow();
	public void afterShow();
	public void beforeHide();
	public void afterHide();
	public Node getNode();
	public String getTitle();
	public void destroy();
	public void refresh();
}
