package machine;

public class ChannelSystem {

	private int sa; // source address
	private int da; // destination address
	private int io; // input = 1/output = 0 type
	private int dv; // device: hard-drive (dv=0) / external device(dv=1)printer(io=0) keyboard(io=1)
	
	private String savedInput;
	private Printer printer;
	private HardDrive hdd;
	
	public ChannelSystem (HardDrive hdd, Printer printer, Keyboard keyboard) {
		this.printer = printer;
		this.hdd = hdd;
		
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
