package core;

import java.util.concurrent.TransferQueue;

public class ISA {
    protected static CPU cpu = CPU.getInstance();
	protected static String R;
	protected static String X;
	protected static String I;
	protected static String RX;
	protected static String RY;
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
		switch (op){
			case "LDR": LoadAndStore.LDR();break;
			case "STR": LoadAndStore.STR();break;
			case "LDA": LoadAndStore.LDA();break;
			case "LDX": LoadAndStore.LDX();break;
			case "STX": LoadAndStore.STX();break;
			case "AMR": ArithmeticInstructions.AMR();break;
			case "SMR": ArithmeticInstructions.SMR();break;
			case "AIR": ArithmeticInstructions.AIR();break;
			case "SIR":ArithmeticInstructions.SIR();break;
		}

	}
	
	public static void execute(String op, String rx, String ry) {
		RX = rx;
		RY = ry;
		
		//Arithmetic Instructions with out address field
		switch (op){
			case "MLT": ArithmeticInstructions.MLT();break;
			case "DVD": ArithmeticInstructions.DVD();break;
			//Logical Instructions
			case "TRR": LogicalInstruction.TRR();break;
			case "AND": LogicalInstruction.AND();break;
			case "ORR": LogicalInstruction.ORR();break;
			case "NOT": LogicalInstruction.NOT();break;
		}

	}

}
