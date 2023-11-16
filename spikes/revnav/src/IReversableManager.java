import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
The interface which the reversable manager implements.
*/
public interface IReversableManager {
    /**
    Change the current node to the passed node.

    @param newNode The node to change to.
    */
    public void pushNewNode(IReversable newNode);
    /**
    Set the event handler to be triggered when the home event is triggered.

    @param newEvent The new event handler.
    */
    public void setHomeEvent(EventHandler<ActionEvent> newEvent);

    /**
    Get the shared data of the manager.

    @return The shared data.
    */
    public SharedData getSharedData();
}
