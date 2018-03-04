package core;

public class Decoder {
	private String opClass;
	private String opCode;
	private String instruction;
	//data using in load and store instruction (LSI)
	private String X;
	private String R;//also using in SRI
	private String I;
	private String address;
	//Transfer Instruction:
	private Integer cc;
	private String Immed;
	//Data using in arithmetic instruction (AI) and Logical instructions (LI)
	private String RX;
	private String RY;
	//Data using in Shift and Rotate instructions (SRI)
	private String LorR;
	private String AorL;
	private Integer count;
	//Data using in IO Instructions(IOI)
	private String DevID;
	//Single instance for class Decoder
	private static final Decoder instance = new Decoder();
	//private constructor
	private Decoder() {

	}
	// Method to get instance
	public static Decoder getInstance() {
		return instance;
	}

	//get the instruction
	public void setInstruction(String instruction){
		if(instruction.length() == 16) {
			this.instruction=instruction;
		}else {
			//remain for failure process
			this.instruction = "0000000000000000";
		}

	}

	//decode instruction
	public void decode(){

		String op=instruction.substring(0,6);
		switch (op){

			//Load and Store Instructions
			case "000001":
				opCode="LDR";
				decodeLoadAndStore();
				break;
			case "000010":
				opCode="STR";
				decodeLoadAndStore();
				break;
			case "000011":
				opCode="LDA";
				decodeLoadAndStore();
				break;
			case "100001":
				opCode="LDX";
				decodeLoadAndStore();
				break;
			case "100010":
				opCode = "STX";
				decodeLoadAndStore();
				break;

			//Transfer Instructions (TI):
			case "001000":
				opCode = "JZ";
				transferInstruction();
				break;
			case "001001":
				opCode = "JNE";
				transferInstruction();
				break;
			case "001010":
				opCode = "JCC";
				transferInstruction();
				break;
			case "001011":
				opCode = "JMA";
				transferInstruction();
				break;
			case "001100":
				opCode = "JSR";
				transferInstruction();
				break;
			case "001101":
				opCode = "RFS";
				transferInstruction();
				break;
			case "001110":
				opCode = "SOB";
				transferInstruction();
				break;
			case "001111":
				opCode = "JGE";
				transferInstruction();
				break;

			//Arithmetic Instructions
			//with Address field
			case "000100":
				opCode = "MAR";
				arithmeticInstructionwithAddress();
				break;
			case "000101":
				opCode = "SMR";
				arithmeticInstructionwithAddress();
				break;
			//with Immed field
			case "000110":
				opCode = "AIR";
				arithmeticInstructionImmed();
				break;
			case "000111":
				opCode = "SIR";
				arithmeticInstructionImmed();
				break;
			//multiple and divide
			case "010000":
				opCode = "MLT";
				arithmetic();
				break;
			case "010001":
				opCode = "DVD";
				arithmetic();
				break;

			//Logical Instructions
			case "010010":
				opCode = "TRR";
				logical();
				break;
			case "010011":
				opCode = "AND";
				logical();
				break;
			case "010100":
				opCode = "ORR";
				logical();
				break;
			case "010101":
				opCode = "NOT";
				logical();
				break;

			//Shift and Rotate Instruction
			case "011001":
				opCode = "SRC";
				shiftAndRotate();
				break;
			case "011010":
				opCode = "RRC";
				shiftAndRotate();
				break;

			//IO Instruction
			case "110001":
				opCode = "IN";
				ioInstruction();
				break;
			case "110010":
				opCode = "OUT";
				ioInstruction();
				break;
		}

	}

	//search in ISA to identify the instruction.
	public void identify(){

		switch(opClass) {
			case "LSI":
				ISA.execute(opCode, R, X, I, address);
			case "TI":
				switch(opCode) {
					case "JCC":
						ISA.execute(opCode, X, cc,  I, address);
					case "RFS":
						ISA.execute(opCode, Immed, "0");
					default:
						ISA.execute(opCode, R, X, I, address);
				}
			case "AIA":
				ISA.execute(opCode, R, X, I, address);
			case "AII":
				ISA.execute(opCode, R, Immed);
			case "AI":
				ISA.execute(opCode, RX, RY);
			case "LI":
				ISA.execute(opCode, RX, RY);
			case "SRI":
				ISA.execute(opCode, R, count, LorR, AorL);
			case "IOI":
				ISA.execute(opCode, R, DevID);

		}
	}

	//clear decoder
	public void clear() {
		instruction = "0000000000000000";
		decode();
	}

	//Decode instructions which belong to Load and Store
	private void decodeLoadAndStore() {
		//LSI stands for Load and Store Instructions
		opClass = "LSI";
		R=instruction.substring(6,8);
		X=instruction.substring(8,10);
		I=instruction.substring(10,11);
		address=instruction.substring(11,16);
		Addressing.getEffectiveAddress(X,I,address);
	}

	private void arithmetic() {
		//AI stands for Arithmetic Instructions
		opClass = "AI";
		RX = instruction.substring(6, 8);
		RY = instruction.substring(8, 10);
	}

	private void logical() {
		//LI stands for Logical Instructions
		opClass = "LI";
		RX = instruction.substring(6, 8);
		RY = instruction.substring(8, 10);
	}

	private void shiftAndRotate() {
		//SRI stands for Shift and Rotate Instructions
		opClass = "SRI";
		R = instruction.substring(6,8);
		LorR = instruction.substring(8, 9);
		AorL = instruction.substring(9, 10);
		count = Integer.valueOf(instruction.substring(12, 15),2);
	}

	private void transferInstruction() {
		//TI stands for Transfer Instructions
		opClass = "TI";
		R=instruction.substring(6,8);
		X=instruction.substring(8,10);
		I=instruction.substring(10,11);
		address=instruction.substring(11,16);
		Addressing.getEffectiveAddress(X,I,address);
		if(opCode.equals("JCC")) {
			this.cc = Integer.valueOf(R, 2);
		}
		if(opCode.equals("RFS")) {
			this.Immed = address;
		}
	}

	private void arithmeticInstructionwithAddress() {
		//AIA stands for Arithmetic Instruction with Address field. Include AMR, SMR
		opClass = "AIA";
		R=instruction.substring(6,8);
		X=instruction.substring(8,10);
		I=instruction.substring(10,11);
		address=instruction.substring(11,16);
	}

	private void arithmeticInstructionImmed() {
		//AIA stands for Arithmetic Instruction with Immed field. Include AIR, SIR
		opClass = "AII";
		R=instruction.substring(6,8);
		Immed=instruction.substring(11,16);
	}

	private void ioInstruction() {
		//IOI stands for IO Instruction. Include IN, OUT
		opClass = "IOI";
		R = instruction.substring(6,8);
		DevID = instruction.substring(11,16);
	}
}
