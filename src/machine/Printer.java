package machine;

import javax.swing.JTextArea;

public class Printer extends JTextArea{

	/**
	 * 
	 */
	private static final long serialVersionUID = -493726414499097754L;

	public Printer (int row, int column) {
		super(row,column);
		setEditable(false);
	}
	
	public void printData (String data) {
		append(data);
	}
	
}
