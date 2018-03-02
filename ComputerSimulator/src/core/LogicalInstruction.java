package core;
public class LogicalInstruction extends ISA {
	
	//Instruction 022 TRR rx, ry
	//Test the Equality of Register and Register
	//If c(rx) = c(ry), set cc(4) <- 1; else, cc(4) <- 0
	public static void TRR() {
		//This instruction will be implemented by XOR logical operation by ALU.
		//get registers rx and ry, determine which register will be used in this instruction.
		Register rx = null, ry = null;
		switch(RX) {
			case"00":
	            rx = cpu.getR0();
	            break;
			case"01":
	            rx = cpu.getR1();
	            break;
	        case "10":
	        		rx = cpu.getR2();
	            break;
	        case "11":
	        		rx = cpu.getR3();
	            break;
		}
		switch(RY) {
			case"00":
	            ry = cpu.getR0();
	            break;
			case"01":
	            ry = cpu.getR1();
	            break;
	        case "10":
	        		ry = cpu.getR2();
	            break;
	        case "11":
	        		ry = cpu.getR3();
	            break;
		}
		//move content of rx to register Y, get ready to compare.
		cpu.getY().setContent(rx.getContent());
		CPU.cyclePlusOne();
		//compare content of rx (now in Y) and content of ry
		cpu.getALU().compareTwo(cpu.getY().getContent(), ry.getContent());
		CPU.cyclePlusOne();
	}
	
	//Instruction 023, AND rx, ry:
	//Logical And of Register and Register
	//c(rx) <- c(rx) AND c(ry)
	public static void AND() {
		//This operation will be done by ALU.
		//First, get rx and ry, determine which register will be used in this instruction
		Register rx = null, ry = null;
		switch(RX) {
			case"00":
	            rx = cpu.getR0();
	            break;
			case"01":
	            rx = cpu.getR1();
	            break;
	        case "10":
	        		rx = cpu.getR2();
	            break;
	        case "11":
	        		rx = cpu.getR3();
	            break;
		}
		switch(RY) {
			case"00":
	            ry = cpu.getR0();
	            break;
			case"01":
	            ry = cpu.getR1();
	            break;
	        case "10":
	        		ry = cpu.getR2();
	            break;
	        case "11":
	        		ry = cpu.getR3();
	            break;
		}
		//Move the content of rx to Register Y, get ready to do AND
		cpu.getY().setContent(rx.getContent());
		CPU.cyclePlusOne();
		//Do "AND" operation in ALU, two operands are content of Y and content of RY;
		//The result will be temporarily stored in Register Z.
		cpu.getALU().and(cpu.getY().getContent(), ry.getContent());
		//Store the result in RX
		rx.setContent(cpu.getZ().getContent());
		CPU.cyclePlusOne();
	}
	
	//Instruction 024, ORR rx, ry:
	//Logical Or of Register and Register
	//c(rx) <- c(rx) OR c(ry)
	public static void ORR() {
		//This operation will be done by ALU.
		//Firstly, get rx and ry, determine which register will be used in this instruction
		Register rx = null, ry = null;
		switch(RX) {
			case"00":
	            rx = cpu.getR0();
	            break;
			case"01":
	            rx = cpu.getR1();
	            break;
	        case "10":
	        		rx = cpu.getR2();
	            break;
	        case "11":
	        		rx = cpu.getR3();
	            break;
		}
		switch(RY) {
			case"00":
	            ry = cpu.getR0();
	            break;
			case"01":
	            ry = cpu.getR1();
	            break;
	        case "10":
	        		ry = cpu.getR2();
	            break;
	        case "11":
	        		ry = cpu.getR3();
	            break;
		}
		//Move the content of rx to Register Y, get ready to do OR
		cpu.getY().setContent(rx.getContent());
		CPU.cyclePlusOne();
		//Do "OR" operation in ALU, two operands are content of Y and content of RY;
		//The result will be temporarily stored in Register Z.
		cpu.getALU().or(cpu.getY().getContent(), ry.getContent());
		//Store the result in RX
		rx.setContent(cpu.getZ().getContent());
		CPU.cyclePlusOne();
	}
	
	//Instruction 025, NOT rx:
	//Logical Not of Register To Register
	//c(rx) <- NOT c(rx)
	public static void NOT() {
		//This operation will be done by ALU.
		//Firstly, get rx and ry, determine which register will be used in this instruction
		Register rx = null;
		switch(RX) {
			case"00":
	            rx = cpu.getR0();
	            break;
			case"01":
	            rx = cpu.getR1();
	            break;
	        case "10":
	        		rx = cpu.getR2();
	            break;
	        case "11":
	        		rx = cpu.getR3();
	            break;
		}
		//Move the content of rx to ALU, get ready for operate NOT instruction.
		//Do "NOT" operation in ALU
		//The result will be temporarily stored in Register Z.
		cpu.getALU().not(rx.getContent());
		//Store the result in RX
		rx.setContent(cpu.getZ().getContent());
		CPU.cyclePlusOne();
	}
}
