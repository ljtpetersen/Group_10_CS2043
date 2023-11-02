import java.util.Vector;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
The main class contains the entry point of the program.
It also manages which node is currently active,
and it keeps track of the history of different nodes.
*/
public class Main extends Application implements IReversableManager {
    /**
    This is the container for the history of the nodes.
    */
    private Vector<IReversable> paneStack;
    /**
    This is the index of the currently active node in the paneStack object.
    */
    private int currentNodeIndex;
    /**
    This is the pane which contains the history controls and the currently active
    node.
    */
    private GridPane gridPane;
    /**
    This is the button which the user presses when they want to go backwards.
    */
    private Button backButton;
    /**
    This is the button which the user presses when they want to go forwards.
    */
    private Button forwardsButton;
    /**
    This is the button which the user presses when they want to trigger the home event.
    */
    private Button homeButton;
    /**
    This is the text representing the title of the currently active node.
    */
    private Text currentNodeTitle;
    /**
    This is the shared data which is accessible to holders of the {@link IReversableManager} instance.
    */
    private SharedData sharedData;
    /**
    This is the action event handler for the home event.
    */
    private EventHandler<ActionEvent> homeEventHandler;

    @Override
    /**
    The entry-point of the application.

    This will initialize the variables within the class, create the root node and grid pane,
    orgranize the grid pane's layout, and show the root node to the user.

    @param primaryStage The primary stage of the program.
    */
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Reversable Navigation Spike Test");

        paneStack = new Vector<IReversable>();
        sharedData = new SharedData();
        currentNodeIndex = 0;
        homeEventHandler = this::resetProgram;

        backButton = new Button("Back");
        backButton.setOnAction(this::historyButtonPress);
        backButton.setDisable(true);
        forwardsButton = new Button("Forwards");
        forwardsButton.setOnAction(this::historyButtonPress);
        forwardsButton.setDisable(true);
        homeButton = new Button("Home");
        homeButton.setOnAction(homeEventHandler);
        Button resetButton = new Button("Reset");
        resetButton.setOnAction(this::resetProgram);

        RootNode root = new RootNode(this);
        paneStack.add(root);
        currentNodeTitle = new Text(root.getTitle());

        gridPane = new GridPane();
        HBox topPane = new HBox(8);
        topPane.getChildren().addAll(homeButton, backButton, forwardsButton, currentNodeTitle, resetButton);
        gridPane.add(topPane, 0, 0);
        root.onShow();
        gridPane.add(root.getNode(), 0, 1);

        Scene scene = new Scene(gridPane, 500, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    /**
    Change the currently active node to the new node.

    @param newNode The node to change to.
    */
    public void pushNewNode(IReversable newNode) {
        for (int i = ++currentNodeIndex; i < paneStack.size(); ++i)
            paneStack.get(i).destroy();
        paneStack.setSize(currentNodeIndex + 1);
        paneStack.set(currentNodeIndex, newNode);
        backButton.setDisable(false);
        forwardsButton.setDisable(true);
        currentNodeTitle.setText(newNode.getTitle());
        gridPane.getChildren().remove(1);
        paneStack.get(currentNodeIndex - 1).onHide();
        newNode.onShow();
        gridPane.add(newNode.getNode(), 0, 1);
    }

    /**
    This is the event handler for the forwards and back buttons. It will
    either go forwards or backwards in history.

    @param event The event which triggered the method.
    */
    private void historyButtonPress(ActionEvent event) {
        if (event.getSource() == backButton) {
            // assume that currentPaneIndex > 0.
            gridPane.getChildren().remove(1);
            paneStack.get(currentNodeIndex--).onHide();
            IReversable newNode = paneStack.get(currentNodeIndex);
            newNode.onShow();
            gridPane.add(newNode.getNode(), 0, 1);
            if (currentNodeIndex == 0) {
                backButton.setDisable(true);
            }
            forwardsButton.setDisable(false);
            currentNodeTitle.setText(newNode.getTitle());
        } else {
            gridPane.getChildren().remove(1);
            paneStack.get(currentNodeIndex++).onHide();
            IReversable newNode = paneStack.get(currentNodeIndex);
            newNode.onShow();
            gridPane.add(newNode.getNode(), 0, 1);
            if (currentNodeIndex == paneStack.size() - 1) {
                forwardsButton.setDisable(true);
            }
            backButton.setDisable(false);
            currentNodeTitle.setText(newNode.getTitle());
        }
    }

    @Override
    /**
    This method will get the shared data.

    @return The shared data.
    */
    public SharedData getSharedData() {
        return sharedData;
    }

    @Override
    /**
    This method will set the home event handler.

    @param newEvent The new event handler.
    */
    public void setHomeEvent(EventHandler<ActionEvent> newEvent) {
        homeEventHandler = newEvent;
    }

    /**
    This method will reset the program.

    @param event THe event which triggered the reset.
    */
    private void resetProgram(ActionEvent event) {
        gridPane.getChildren().remove(1);
        paneStack.get(currentNodeIndex).onHide();
        for (int i = paneStack.size() - 1; i >= 0; --i) {
            paneStack.get(i).destroy();
        }
        paneStack.clear();
        currentNodeIndex = 0;
        forwardsButton.setDisable(true);
        backButton.setDisable(true);
        sharedData = new SharedData();
        homeEventHandler = this::resetProgram;
        RootNode root = new RootNode(this);
        paneStack.add(root);
        currentNodeTitle.setText(root.getTitle());
        gridPane.add(root.getNode(), 0, 1);
    }
}
