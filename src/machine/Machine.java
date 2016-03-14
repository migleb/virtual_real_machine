package machine;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


public class Machine extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5096454467219086729L;
	private Processor cpu;
	private OperativeMemory ram;
	private ChannelSystem chs;
	private Printer printer;
	private Keyboard keyboard;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Machine();
	}
	
	public Machine() {
		HardDrive hdd = new HardDrive(10,5);
		ram = new OperativeMemory(100, 10);
		printer = new Printer(5, 30);
		keyboard = new Keyboard(30);
		getContentPane().setLayout(new GridLayout(1, 3));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Machine");
		setSize(1200,700);
		setResizable(false);
		
		JPanel leftPanel = new JPanel();
		
		leftPanel.setLayout(new BoxLayout(leftPanel,BoxLayout.PAGE_AXIS));
		
		leftPanel.add(initializeRegisters());
		leftPanel.add(initializeChannelSystem());
		
		getContentPane().add(leftPanel);
		getContentPane().add(initializeTable(ram));
		getContentPane().add(initializeTable(hdd));
		
		setVisible(true);
	}
	
	private JPanel initializeRegisters() {
		JPanel registersPanel = new JPanel();
		registersPanel.setBorder(BorderFactory.createTitledBorder("Processor"));
		
		Map<ProcessorRegister, JTextField> registersMap = new HashMap<ProcessorRegister, JTextField>();
		/*
		for (ProcessorRegister reg : ProcessorRegister.values()) {
			JPanel registerPanel = new JPanel();
			JLabel regLabel = new JLabel(reg.name().toUpperCase());
			final JTextField regField = new JTextField(String.format("%5d", cpu.getValue(reg)));
			registersMap.put(reg, regField);
			regField.setEditable(false);
			registerPanel.add(regLabel);
			registerPanel.add(regField);
			registersPanel.add(registerPanel);
		}
		*/
		return registersPanel;
	}
	
	private JPanel initializeChannelSystem() {
		JPanel channelSystemPanel = new JPanel();
		channelSystemPanel.setBorder(BorderFactory.createTitledBorder("Channel System"));
		
		
		channelSystemPanel.add(keyboard);
		channelSystemPanel.add(printer);
		
		return channelSystemPanel;
	}
	
	private JScrollPane initializeTable (MemoryListable memory) {
		String[] columns = {"Address","Content"};
		final DefaultTableModel table = new MemoryTable(columns, memory);
		final JTable dataTable = new JTable(table);
		JScrollPane scrollPane = new JScrollPane(dataTable);
		scrollPane.setBorder(BorderFactory.createTitledBorder(memory.getTitle()));
		
		return scrollPane;
	}

}
