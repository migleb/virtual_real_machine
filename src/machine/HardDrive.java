package machine;

public class HardDrive extends MemoryListable{
	
	public HardDrive (int blockCount, int blockSize) {
		super(blockCount, blockSize);
	}
	
	@Override
	public String getTitle() {
		return "Hard Drive";
	}
	
}
