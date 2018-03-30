package core;

public class ISA {
	protected static CPU cpu = CPU.getInstance();
	protected static String R;
	protected static String X;
	protected static String I;
	protected static String RX;
	protected static String RY;
	protected static String LorR;
	protected static String AorL;
	protected static Integer count;
	protected static String address;
    protected static Integer CC;
    protected static String Immed;
    protected static String DevID;
    protected static String trapCode;
	public static void execute(String op, String r, String x, String i, String addr) {
		//Store R, X, I and address bits for later use.
		R = r;
		X = x;
		I = i;
		address = addr;
		// Identify the operation code and execute in specific subclass
		if(cpu.getControler().singleStep) {
			switch (op){
				//Load and Store Instruction.
				case "LDR": LoadAndStore.LDR();break;
				case "STR": LoadAndStore.STR();break;
				case "LDA": LoadAndStore.LDA();break;
				case "LDX": LoadAndStore.LDX();break;
				case "STX": LoadAndStore.STX();break;
				//Transfer Instructions
				case "JZ": TransferInstruction.JZ(); break;
				case "JNE": TransferInstruction.JNE(); break;
				case "JMA": TransferInstruction.JMA(); break;
				case "JSR": TransferInstruction.JSR(); break;
				case "SOB": TransferInstruction.SOB(); break;
				case "JGE": TransferInstruction.JGE(); break;
				//Arithmetic with address field
				case "AMR": ArithmeticInstructions.AMR();break;
				case "SMR": ArithmeticInstructions.SMR();break;	
			}
		}else {
			switch (op){
				//Load and Store Instruction.
				case "LDR": LoadAndStore.LDRNHLT();break;
				case "STR": LoadAndStore.STRNHLT();break;
				case "LDA": LoadAndStore.LDANHLT();break;
				case "LDX": LoadAndStore.LDXNHLT();break;
				case "STX": LoadAndStore.STXNHLT();break;
				//Transfer Instructions
				case "JZ": TransferInstruction.JZNHLT(); break;
				case "JNE": TransferInstruction.JNENHLT(); break;
				case "JMA": TransferInstruction.JMANHLT(); break;
				case "JSR": TransferInstruction.JSRNHLT(); break;
				case "SOB": TransferInstruction.SOBNHLT(); break;
				case "JGE": TransferInstruction.JGENHLT(); break;
				//Arithmetic with address field
				case "AMR": ArithmeticInstructions.AMRNHLT();break;
				case "SMR": ArithmeticInstructions.SMRNHLT();break;	
			}
		}
	}

	public static void execute(String op, String rx, String ry) {
		if(cpu.getControler().singleStep) {
			switch (op){
				//Arithmetic with Immed field
				case "AIR": R = rx; Immed = ry; ArithmeticInstructions.AIR();break;
				case "SIR": R = rx; Immed = ry; ArithmeticInstructions.SIR();break;
				//Arithmetic Instructions with out address field
				case "MLT": 	RX = rx; RY = ry; ArithmeticInstructions.MLT();break;
				case "DVD": RX = rx; RY = ry; ArithmeticInstructions.DVD();break;
				//Logical Instructions
				case "TRR": RX = rx; RY = ry; LogicalInstruction.TRR();break;
				case "AND": RX = rx; RY = ry; LogicalInstruction.AND();break;
				case "ORR": RX = rx; RY = ry; LogicalInstruction.ORR();break;
				case "NOT": R = rx; LogicalInstruction.NOT();break;
				//Transfer Instruction RFS
				case "RFS": Immed = rx; TransferInstruction.RFS(); break;
				//IO Instruction
				case "IN": R = rx; DevID = ry; IOinstructions.IN(); break;
				case "OUT": R = rx; DevID = ry; IOinstructions.OUT(); break;
			}
		}else {
			switch (op){
				//Arithmetic with Immed field
				case "AIR": R = rx; Immed = ry; ArithmeticInstructions.AIRNHLT();break;
				case "SIR": R = rx; Immed = ry; ArithmeticInstructions.SIRNHLT();break;
				//Arithmetic Instructions with out address field
				case "MLT": 	RX = rx; RY = ry; ArithmeticInstructions.MLTNHLT();break;
				case "DVD": RX = rx; RY = ry; ArithmeticInstructions.DVDNHLT();break;
				//Logical Instructions
				case "TRR": RX = rx; RY = ry; LogicalInstruction.TRRNHLT();break;
				case "AND": RX = rx; RY = ry; LogicalInstruction.ANDNHLT();break;
				case "ORR": RX = rx; RY = ry; LogicalInstruction.ORRNHLT();break;
				case "NOT": R = rx; LogicalInstruction.NOTNHLT();break;
				//Transfer Instruction RFS
				case "RFS": Immed = rx; TransferInstruction.RFSNHLT(); break;
				//IO Instruction
				case "IN": R = rx; DevID = ry; IOinstructions.INNHLT(); break;
				case "OUT": R = rx; DevID = ry; IOinstructions.OUTNHLT(); break;
			}
		}
	}
	
	public static void execute(String op, String r, Integer c, String LR, String AL) {
		R = r;
		count = c;
		LorR = LR;
		AorL = AL;
		if(cpu.getControler().singleStep) {
			switch(op) {
				//Shift and Rotate Instruction
				case "SRC":
					ShiftAndRotate.SRC();
					break;
				case "RRC":
					ShiftAndRotate.RRC();
					break;
				//JCC
				case "JCC":
					CC = c;
					TransferInstruction.JCC();
					break;
			}	
		}else {
			switch(op) {
				//Shift and Rotate Instruction
				case "SRC":
					ShiftAndRotate.SRCNHLT();
					break;
				case "RRC":
					ShiftAndRotate.RRCNHLT();
					break;
				//JCC
				case "JCC":
					CC = c;
					TransferInstruction.JCCNHLT();
					break;
			}	
		}
	}
	
	public static void execute(String opCode, String code) {
		trapCode = code;

		//Illegal TRAP code
		if(Integer.valueOf(code,2) > 15){
			Fault f = new Fault(1,"Illegal TRAP code");
			FaultHandler.getInstance().faultHandleNHLT(f);
		}else{
			if(cpu.getControler().singleStep) {
				Trap.trap();
			}else {
				Trap.trapNHLT();
			}
		}
	}

}
