package machine;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class Keyboard extends JTextField{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9094749025906717616L;
	private KeyboardInterface ki;
	
	public Keyboard (int column) {
		super(column);
		
		Keyboard k = this;
		
		this.addCaretListener (new CaretListener(){
			
			@Override
			public void caretUpdate(CaretEvent e) {
		        
				if (k.getText().length() >= 5) {
					if (ki != null) {
						ki.deliverData(k.getText().substring(0, 5));
					}
					SwingUtilities.invokeLater(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							k.setText("");
						}
						
					});
				}
				
		    }
			
		});
	}
	
	public void addKeyboardInterface (KeyboardInterface p) {
		this.ki = p;
	}
	
}
