package machine;

public abstract class MemoryListable {
	
	protected int blockCount;
	protected int blockSize;
	protected String[] memory;
	
	public MemoryListable (int blockCount, int blockSize) {
		this.blockCount = blockCount;
		this.blockSize = blockSize;
		this.memory = new String[blockCount * blockSize];
		
		for (int i = 0; i < blockCount; i++) {
			for (int j = 0; j < blockSize; j++) {
				occupyMemory(i,j,"0");
			}
		}
	}
	
	public void occupyMemory (int block, int idx, String value) {
		this.memory[block * this.blockSize + idx] = value;
	}
	
	public String getMemory (int block, int idx) {
		return memory[block * this.blockSize + idx];
	}
	
	public int getBlockCount() {
		return blockCount;
	}

	public int getBlockSize() {
		return blockSize;
	}
	
	//public abstract String getTitle ();
	
}
