package machine;

public class OperativeMemory extends MemoryListable {

	public OperativeMemory(int blockCount, int blockSize) {
		super(blockCount,blockSize);
	}
	
	@Override
	public String getTitle() {
		return "Operative Memory";
	}
	
}
