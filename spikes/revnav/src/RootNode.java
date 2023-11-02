import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
The root node of the program. This is shown first when the program is launched.
*/
public class RootNode implements IReversable {
    /**
    A handle to the {@link IReversableManager} within which this node is.
    */
    private IReversableManager reversableManager;
    /**
    The contents of the node.
    */
    private HBox pane;

    /**
    Construct a new root node from its manager.

    @param reversableManager The root node's manager.
    */
    public RootNode(IReversableManager reversableManager) {
        this.reversableManager = reversableManager;
        Button incSharedButton = new Button("Increment Counter");
        incSharedButton.setOnAction(this::incrementShared);
        Button sayHiButton = new Button("Hi!");
        sayHiButton.setOnAction(this::sayHi);;
        Button showCount = new Button("Display Counter");
        showCount.setOnAction(this::showCount);
        pane = new HBox();
        pane.getChildren().addAll(incSharedButton, sayHiButton, showCount);
    }

    @Override
    /**
    The method which is triggered when the node is shown.
    */
    public void onShow() {}

    @Override
    /**
    The method which is triggered when the node is hidden.
    */
    public void onHide() {}

    @Override
    /**
    The method which is triggered when the node to be destroyed.
    */
    public void destroy() {}

    @Override
    /**
    Gets the title of the node.

    @return The title of the node.
    */
    public String getTitle() {
        return "Root Pane";
    }

    /**
    Gets the underlying node.

    @param The underlying node.
    */
    public Pane getNode() {
        return pane;
    }

    /**
    Process the incrementShared event.
    */
    private void incrementShared(ActionEvent event) {
        SharedData data = reversableManager.getSharedData();
        data.setValue(data.getValue() + 1);
    }

    /**
    Process the sayHi event.
    */
    private void sayHi(ActionEvent event) {
        Text text = new Text("Hello, my name is Spike!");
        Button button = new Button("Display Counter");
        button.setOnAction(this::showCount);
        reversableManager.pushNewNode(new TextAndButton(text, button));
    }

    /**
    Process the showCount event.
    */
    private void showCount(ActionEvent event) {
        Text text = new Text(String.format("The counter is currently %d.", reversableManager.getSharedData().getValue()));
        Button button = new Button("Hi!");
        button.setOnAction(this::sayHi);
        reversableManager.pushNewNode(new TextAndButton(text, button));
    }
}
