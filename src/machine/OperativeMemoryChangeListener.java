package machine;

public interface OperativeMemoryChangeListener {

	public void memoryChanged(int block, int idx, String value);
	public void memoryExecuted(int block, int idx);
}
