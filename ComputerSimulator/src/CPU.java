// This class include all registers, ALU, and Memory
public class CPU {
	private Register pc = PC.getInstance();
	private Register mar = MAR.getInstance();
	private Register mbr = MBR.getInstance();
	private Register ir = IR.getInstance();
	private Register y = Y.getInstance();
	private Register z = Z.getInstance();
	private Register iar = new IAR("IAR");
	private Register irr = new IRR("IRR");
	private Register x1 = new X("X1");
	private Register x2 = new X("X2");
	private Register x3 = new X("X3");
	private Register r0 = new GPR("R0");
	private Register r1 = new GPR("R1");
	private Register r2 = new GPR("R2");
	private Register r3 = new GPR("R3");
	private Register cc = CC.getInstance();
	private Register mfr = MFR.getInstance();
	private Decoder decoder = Decoder.getInstance();
	private ALU alu = ALU.getInstance();
	private Memory memory = Memory.getInstance();
	private Controler controler = Controler.getInstance();
	//cycle counter
	private static int cycle = 0;

	//Instance of CPU
	private static final CPU instance = new CPU();
	//constructor
	private CPU() {
		
	}
	//Method to get instance of CPU
	public static CPU getInstance() {
		return instance;
	}
	//Getters
	public Register getPC() {
		return pc;
	}
	public Register getMAR() {
		return mar;
	}
	public Register getMBR() {
		return mbr;
	}
	public Register getIR() {
		return ir;
	}
	public Register getY() {
		return y;
	}
	public Register getZ() {
		return z;
	}
	public Register getIAR() {
		return iar;
	}
	public Register getIRR() {
		return irr;
	}
	public Register getX1() {
		return x1;
	}
	public Register getX2() {
		return x2;
	}
	public Register getX3() {
		return x3;
	}
	public Register getR0() {
		return r0;
	}
	public Register getR1() {
		return r1;
	}
	public Register getR2() {
		return r2;
	}
	public Register getR3() {
		return r3;
	}
	public Register getCC() {
		return cc;
	}
	public Register getMFR() {
		return mfr;
	} 
	public Decoder getDecoder() {
		return decoder;
	}
	public ALU getALU() {
		return alu;
	}
	public Memory getMemory() {
		return memory;
	}
	public Controler getControler() {
		return controler;
	}
	public static int getCycle() {
		return cycle;
	}
	
	//Clean all register
	public void clearAll() {
		pc.setContent("000000000000");
		mar.setContent("0000000000000000");
		mbr.setContent("0000000000000000");
		ir.setContent("0000000000000000");
		y.setContent("0000000000000000");
		z.setContent("0000000000000000");
		x1.setContent("0000000000000000");
		x2.setContent("0000000000000000");
		x3.setContent("0000000000000000");
		r0.setContent("0000000000000000");
		r1.setContent("0000000000000000");
		r2.setContent("0000000000000000");
		r3.setContent("0000000000000000");
		irr.setContent("0000000000000000");
		iar.setContent("0000000000000000");
                cc.setContent("0000");
                mfr.setContent("0000");
		cycle = 0;
		Monitor.setStepInformation("",false);
	}
	
	//Cycle plus 1
	public static void cyclePlusOne() {
		cycle++;
	}
	
	//Alignment
	public static String alignment(String content) {
		int supplement = 16 - content.length();
		if(supplement >= 0) {
			while(supplement > 0) {
				content = "0" + content;
				supplement--;
			}
		}
		return content;
	}

	
}
