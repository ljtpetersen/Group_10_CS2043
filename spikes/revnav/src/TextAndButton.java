import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
A reverable node which only contains a button and some text.
*/
public class TextAndButton implements IReversable {
    /**
    The underlying JavaFX node.
    */
    private HBox pane;

    /**
    Construct a new text and button instance.

    @param text The text of the instance.
    @param button The button of the instance.
    */
    public TextAndButton(Text text, Button button) {
        pane = new HBox();
        pane.getChildren().addAll(text, button);
    }

    @Override
    public void onShow() {}

    @Override
    public void onHide() {}

    @Override
    public Node getNode() {
        return pane;
    }

    @Override
    public String getTitle() {
        return "Text and Button";
    }

    @Override
    public void destroy() {}
}
