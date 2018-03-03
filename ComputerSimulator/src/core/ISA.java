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
	
	public static void execute(String op, String r, String x, String i, String addr) {
		//Store R, X, I and address bits for later use.
		R = r;
		X = x;
		I = i;
		address = addr;
		// Identify the operation code and execute in specific subclass
		switch (op){
			//Load and Store Instruction.
			case "LDR": LoadAndStore.LDR();break;
			case "STR": LoadAndStore.STR();break;
			case "LDA": LoadAndStore.LDA();break;
			case "LDX": LoadAndStore.LDX();break;
			case "STX": LoadAndStore.STX();break;
			
			//Arithmetic with address field
			case "AMR": ArithmeticInstructions.AMR();break;
			case "SMR": ArithmeticInstructions.SMR();break;
			
		}

	}
	
	public static void execute(String op, String rx, String ry) {

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
	}
	
	public static void execute(String op, String r, Integer c, String LR, String AL) {
		R = r;
		count = c;
		LorR = LR;
		AorL = AL;
		
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
	}
	
}
