import javafx.scene.Node;

/**
The interface for any reversable nodes.
*/
public interface IReversable {
    /**
    The method which is called when the node is to be shown.
    */
    public void onShow();

    /**
    The method which is called after the node is hidden.
    */
    public void onHide();

    /**
    Get the underlying JavaFX node.

    @return The underlying JavaFX node.
    */
    public Node getNode();

    /**
    Get the title of the node.

    @return The title.
    */
    public String getTitle();

    /**
    The method which is called when the node is to be destroyed.
    */
    public void destroy();
}
