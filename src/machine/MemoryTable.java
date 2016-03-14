package machine;

import javax.swing.table.DefaultTableModel;

public class MemoryTable extends DefaultTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1526885160213490445L;
	
	public MemoryTable (String[] columns, MemoryListable memory) {
		super(columns,0);
		
		for (int i = 0; i < memory.getBlockCount(); i++) {
			for (int j = 0; j < memory.getBlockSize(); j++) {
				this.addRow(new Object[]{
						i * memory.getBlockSize() + j, 
						memory.getMemory(i, j) });
			}
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {                
		return false;
	}
}
