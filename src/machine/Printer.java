package machine;

import javax.swing.JTextArea;

public class Printer extends JTextArea{

	public Printer (int row, int column) {
		super(row,column);
		setEditable(false);
	}
	
	public void printData (String data) {
		append(data);
	}
	
}
