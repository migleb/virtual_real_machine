package machine;

public class ChannelSystem extends PropertyChange {

	private int sa; // source address
	private int da; // destination address
	private int io; // input = 1/output = 0 type
	private int dv; // device: hard-drive (dv=0) / external device(dv=1)printer(io=0) keyboard(io=1)
	
	private String savedInput;
	private Printer printer;
	private HardDrive hdd;
	private ChannelSystemInput protocol;
	
	public ChannelSystem (HardDrive hdd, Printer printer, Keyboard keyboard) {
		this.printer = printer;
		this.hdd = hdd;
		
		keyboard.addKeyboardInterface(new KeyboardInterface() {
			
			@Override
			public void deliverData(String data) {
				setSavedInput(data);
				protocol.notifyAboutInput();
			}
			
		});
	}

	public void dataOut (String data) {
		this.printer.printData(data);
	}
	
	public int getValue(ChannelSystemRegister reg) {
		switch (reg) {
		case SA: return sa;
		case DA: return da;
		case IO: return io;
		case DV: return dv;
		}
		return 0;
	}

	public int getSa() {
		return sa;
	}

	public void setSa(int sa) {
		if (this.sa != sa) {
			this.sa = sa;
		}
	}

	public int getDa() {
		return da;
	}

	public void setDa(int da) {
		if (this.da != da) {
			this.da = da;
		}
	}

	public int getIo() {
		return io;
	}

	public void setIo(int io) {
		if (this.io != io) {
			this.io = io;
		}
	}
	
	public int getDv() {
		return dv;
	}

	public void setDv(int dv) {
		if (this.dv != dv) {
			this.dv = dv;
		}
	}

	public String getSavedInput() {
		return savedInput;
	}
	
	public void setProtocol(ChannelSystemInput protocol) {
		this.protocol = protocol;
	}

	public void setSavedInput(String savedInput) {
		this.savedInput = savedInput;
	}
	
	public void writeToExternalDrive(int block, int idx, String value) {
		hdd.occupyMemory(block, idx, value);
	}
	
	public String readExternalDrive(int block, int idx) {
		return hdd.getMemory(block, idx);
	}
	
}
