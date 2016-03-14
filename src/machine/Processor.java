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

	// Register value setters
	private void setMode (int mode) {
		if (this.mode != mode) {
			this.mode = mode;
		}
	}
	private void setPtr (int ptr) {
		if (this.ptr != ptr) {
			this.ptr = ptr;
		}
	}
	private void setGr (int gr) {
		if (this.gr != gr){
			this.gr = gr;
		}
	}
	private void setPc (int pc) {
		if (this.pc != pc){
			this.pc = pc;
		}
	}
	private void setIh (int ih) {
		if (this.ih != ih){
			this.ih = ih;
		}
	}
	private void setSp (int sp) {
		if (this.sp != sp){
			this.sp = sp;
		}
	}
	private void setCf (int cf) {
		if (this.cf != cf){
			this.cf = cf;
		}
	}
	private void setPi (int pi) {
		if (this.pi != pi){
			this.pi = pi;
		}
	}
	private void setSi (int si) {
		if (this.si != si){
			this.si = si;
		}
	}
	private void setTi (int ti) {
		if (this.ti != ti){
			this.ti = ti;
		}
	}
	private void setMr (int mr) {
		if (this.mr != mr){
			this.mr = mr;
		}
	}
	private void incPc () {
		setPc(this.pc+1);
	}

	// Register value getter
	public int getValue (ProcessorRegister reg){
		switch (reg) {
			case CF: return cf;
			case GR: return gr;
			case IH: return ih;
			case MODE: return mode;
			case PC: return pc;
			case PI: return pi;
			case SP: return sp;
			case PTR: return ptr;
			case SI: return si;
			case TI: return ti;
			case MR: return mr;
		}
		return 0;
	}

	// for interrupt handling
	private void test () {
		if ((si + pi > 0) || (ti == 0)) {
			setMode(0);
			push(pc);
			push(ptr);
			push(cf);
			push(gr);
			setPc(ih);
		}
	}

	// work with stack
	private void push (int data) {
		ram.occupyMemory(this.sp/10, this.sp%10, String.valueOf(data));
		setSp (this.sp + 1);
	}
	private int pop () {
		setSp(this.sp - 1);
		String data =  ram.getMemory(this.sp/10, this.sp%10);
		return Integer.parseInt(data);
	}

	// address functions
	private int physicalTrack (int vTrack) {
		String track = ram.getMemory(this.ptr, vTrack);
		return Integer.parseInt(track);
	}
	private int buildAdress (String address) throws InvalidMemoryException {
		if (this.mode == 0) {
			return Integer.parseInt(address);
		} else {
			int blockNumber = Integer.parseInt(address);
			if (blockNumber >= ram.getBlockCount()) {
				throw new InvalidMemoryException();
			}
			int block = Math.floorDiv(blockNumber, 10);
			if (block >= ram.getBlockSize()) {
				throw new InvalidMemoryException();
			}
			int word = blockNumber%10;
			int realBlock = Integer.parseInt(ram.getMemory(ptr, block));
			if (realBlock == 0) {
				throw new InvalidMemoryException();
			}
			return (realBlock*10 + word);
		}
	}

	// do something
	public void step () {
		String command = getValueInAddress(pc);
		doCommand(command);
	}

	// for command interpretation
	private String getValueInAddress (int address) {
		int block = address/10;
		int word = address%10;
		return ram.getMemory(block, word);
	}
	private void doCommand (String cmd) {
		incPc();
		int cmdTime = 1;			// how much time of processor used
		try {
			// just supervisor mode commands
			if (mode == 0) {
				// 3 letter length commands
				if (cmd.length() >= 4) {
					switch (cmd.substring(0, 3)) {
						case "SSI" : {
							setSi(Integer.parseInt(cmd.substring(3,4)));
							return;
						}
						case "SPI" : {
							setPi(Integer.parseInt(cmd.substring(3,4)));
							return;
						}
						case "STI" : {
							setTi(Integer.parseInt(cmd.substring(3,5)));
							return;
						}
						case "SPT" : {
							setSi(Integer.parseInt(cmd.substring(3,5)));
							return;
						}
					}
				}
				// 2 letter length commands
				switch (cmd.substring(0, 2)) {
					case "SP" : {
						setSp (buildAdress(cmd.substring(2, 5)));
						return;
					}
					case "IH" : {
						setIh (buildAdress(cmd.substring(2, 5)));
						return;
					}
					case "LD" : {
						String register = cmd.substring(2, 4);
						switch (register) {
							case "SI" : {
								setGr(this.si);
								break;
							}
							case "PI" : {
								setGr(this.pi);
								break;
							}
							case "TI" : {
								setGr(this.ti);
								break;
							}
							case "MR" : {
								setGr(this.mr);
								break;
							}
						}
						return;
					}
					case "VM" : {
						int start = this.gr % 1000;
						setPc(start);
						setMode(1);
						return;
					}
					case "SA" : {
						this.chn.setSa(gr);
						return;
					}
					case "DA" : {
						this.chn.setDa(gr);
						return;
					}
					case "IO" : {
						this.chn.setIo(Integer.parseInt(cmd.substring(2,3)));
						return;
					}
					case "DV" : {
						this.chn.setDv(Integer.parseInt(cmd.substring(2,3)));
						return;
					}
				}
				// 4 letter length commands
				switch (cmd.substring(0, 4)) {
					case "SMOD" : {
						setMode(Integer.parseInt(cmd.substring(4, 5)));
						return;
					}
					case "XCHG" : {
						int block = Math.floorDiv(chn.getSa(), 10);
						int word = chn.getSa() % 10;

						// exchange memory with hard drive
						if (chn.getDv() == 0) {
							int daBlock = Math.floorDiv(chn.getDa(), 10);
							int daWord = chn.getDa() % 10;
							// save to hard drive from ram
							if (chn.getIo() == 0) {
								chn.writeToExternalDrive(daBlock, daWord, ram.getMemory(block, word));
							// save to ram from hard drive
							} else if (chn.getIo() == 1){
								ram.occupyMemory(block, word, chn.readExternalDrive(daBlock, daWord));
							}
						// exchange memory with other devices
						} else {
							if (chn.getDv() == 1) {
								// print data
								if (chn.getIo() == 0) {
									//
									//
								// read data
								} else if (chn.getIo() == 1){
									//
									//
								}
							}
						}
						return;
					}
				}
				// 5 letter length commands
				if (cmd.substring(0, 5).equalsIgnoreCase("RESTR")) {
					setGr(pop());
					setCf(pop());
					setPtr(pop());
					setPc(pop());
					setMode(1);
					return;
				}
			}

			// all commands (2 letter length) (virtual and real machines)
			switch (cmd.substring(0, 2)) {
				case "MG" : {
					int address = buildAdress(cmd.substring(2, 5));
					int value = Integer.parseInt(getValueInAddress(address));
					setGr(value);
					break;
				}
				case "MM" : {
					int address = buildAdress(cmd.substring(2, 5));
					ram.occupyMemory(address/10, address%10, String.valueOf(this.gr));
					break;
				}
				case "GV" : {
					setGr(Integer.parseInt(cmd.substring(2,5)));
					break;
				}
				case "AD" : {
					int address = buildAdress(cmd.substring(2, 5));
					int value = Integer.parseInt(getValueInAddress(address));
					setGr(this.gr + value);
					break;
				}
				case "CP" : {
					int address = buildAdress(cmd.substring(2, 5));
					int value = Integer.parseInt(getValueInAddress(address));
					if (this.gr ==  value) {
						setCf (0);
					} else {
						if (this.gr > value) {
							setCf(1);
						} else {
							setCf(2);
						}
					}
					break;
				}
				case "JE" : {
					if (this.cf == 0) {
						int address = buildAdress(cmd.substring(2, 5));
						setPc(address);
					}
					break;
				}
				case "JL" : {
					if (this.cf == 2) {
						int address = buildAdress(cmd.substring(2, 5));
						setPc(address);
					}
					break;
				}
				case "JG" : {
					if (this.cf == 1) {
						int address = buildAdress(cmd.substring(2, 5));
						setPc(address);
					}
					break;
				}
				case "GO" : {
					int address = buildAdress(cmd.substring(2, 5));
					setPc(address);
					break;
				}
				case "CL" : {
					int address = buildAdress(cmd.substring(2, 5));
					push (pc);
					setPc(address);
					break;
				}
				case "RT" : {
					setPc(pop());
					break;
				}
				case "SC" : {
					cmdTime = 3;			// reading takes more time
					setMr(buildAdress(String.valueOf(gr)));
					setSi (3);
					break;
				}
				case "PT" : {
					cmdTime = 3;			// writing takes more time
					setMr(buildAdress(String.valueOf(gr)));
					setSi (2);
					break;
				}
				case "FM" : {
					if (this.mode == 1) {
						int block = physicalTrack(Integer.valueOf(cmd.substring(2,3)));
						setMr(block);
						setPi(4);
					}
					break;
				}
				case "RM" : {
					if (this.mode == 1) {
						int block = physicalTrack(Integer.valueOf(cmd.substring(2,3)));
						setMr(block);
						setPi(3);
					}
					break;
				}
				case "HT" : {
					if (mode == 1) {
						setSi(1);
						break;
					}
					break;
				}
				default: {
					throw new Exception("Unknown command");
				}

			}

		} catch (InvalidMemoryException e) {
			setPi(1);
		} catch (Exception e) {
			System.out.println(((mode == 0)? "Supervisor" : "User") + ": Invalid command");
			if (mode == 1) {
				setPi(2);
			}
		}

		if (mode == 1) {
			setTi(Math.max(ti - cmdTime, 0));
			test();
		}
	}

}
