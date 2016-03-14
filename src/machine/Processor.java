package machine;

public class Processor {

	int mode; // Machine mode
	int ptr; // Pages table register
	int gr; // General register
	int pc; // Program counter
	int ih; // Interrupt handler
	int sp; // Stack pointer
	int cf; // Carry flag
	int pi; // Programming interrupt
	int si; // Supervisor interrupt
	int ti; // Timer interrupt
	int mr; // I/O address
	
	OperativeMemory ram;
	ChannelSystem chn;
	
	public Processor (OperativeMemory ram, ChannelSystem chn) {
		this.ram = ram;
		this.chn = chn;
		
	}
}
