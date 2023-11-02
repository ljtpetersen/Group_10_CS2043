/**
This class contains the data which is shared between reversable nodes.
*/
public class SharedData {
    /**
    The shared value.
    */
    private int value;

    /**
    Construct a new shared data instance.
    */
    public SharedData() {
        value = 0;
    }

    /**
    Get the value.

    @return The value.
    */
    public int getValue() {
        return value;
    }

    /**
    Set the value.

    @param newValue The new value.
    */
    public void setValue(int newValue) {
        value = newValue;
    }
}
