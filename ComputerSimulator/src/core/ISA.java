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
	
	public static void execute(String op, String r, String x, String i, String addr) {
		//Store R, X, I and address bits for later use.
		R = r;
		X = x;
		I = i;
		address = addr;
		// Identify the operation code and execute in specific subclass
		//Subclass Load and Store.
		//LDR
		 if(op.equals("LDR")) {
			 LoadAndStore.LDR();
		 }
		 //STR
	     else if(op.equals("STR")){
	        LoadAndStore.STR();
	     }
		 //LDA
	     else if(op.equals("LDA")){
	        LoadAndStore.LDA();
	     }
		 //LDX
	     else if(op.equals("LDX")){
	        LoadAndStore.LDX();
	     }
		 //STX
	     else if(op.equals("STX")){
	        LoadAndStore.STX();
	     }
	}
	
	public static void execute(String op, String rx, String ry) {
		RX = rx;
		RY = ry;
		
		//Arithmetic Instructions with out address field
		//MLT
		if(op.equals("MLT")) {
			ArithmeticInstructions.MLT();
		}
		//DVD
		else if(op.equals("DVD")) {
			ArithmeticInstructions.DVD();
		}
		
		//Logical Instructions
		//TRR
		else if(op.equals("TRR")) {
			LogicalInstruction.TRR();
		}
		//AND
		else if(op.equals("AND")) {
			LogicalInstruction.AND();
		}
		//ORR
		else if(op.equals("ORR")) {
			LogicalInstruction.ORR();
		}
		//NOT
		else if(op.equals("NOT")) {
			LogicalInstruction.NOT();
		}
	}
	
	public static void execute(String op, String r, Integer c, String LR, String AL) {
		R = r;
		count = c;
		LorR = LR;
		AorL = AL;
		//SRC
		if(op.equals("SRC")) {
			ShiftAndRotate.SRC();
		}
		//RRC
		else if(op.equals("RRC")) {
			ShiftAndRotate.RRC();
		}
	}
	
}
