package cs2043group10;

public interface IReversableManager {
	public void pushNewNode(IReversable newNode);
	public IDatabase getDatabaseManager();
	public void goBackwards();
	public void goForwards();
	public void popNode();
	public void replaceTop(IReversable newTop);
	public boolean isAtTop(IReversable node);
}
