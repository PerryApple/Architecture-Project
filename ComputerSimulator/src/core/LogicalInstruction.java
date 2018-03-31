package core;
import gui.controllers.EngineerConsoleController;

public class LogicalInstruction extends ISA {
	//******************************************************************************************************************************
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
		EngineerConsoleController.setStepInformation("Y <= " + rx.getName() , false);
		Halt.halt();
		//compare content of rx (now in Y) and content of ry
		cpu.getALU().compareTwo(cpu.getY().getContent(), ry.getContent());
		CPU.cyclePlusOne();
		EngineerConsoleController.setStepInformation("CC(4) <= ALU Compare( c(Y), c(" + ry.getName() + ") )." , false);
		Halt.halt();
	}
	
	//TRR without single step halt
	public static void TRRNHLT() {
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
	
	//******************************************************************************************************************************
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
		EngineerConsoleController.setStepInformation("Y <= " + rx.getName() , false);
		CPU.cyclePlusOne();
		Halt.halt();
		//Do "AND" operation in ALU, two operands are content of Y and content of RY;
		//The result will be temporarily stored in Register Z.
		cpu.getALU().and(cpu.getY().getContent(), ry.getContent());
		EngineerConsoleController.setStepInformation("Z <= ALU AND( c(Y), c(" + ry.getName() + ") )." , false);
		Halt.halt();
		//Store the result in RX
		rx.setContent(cpu.getZ().getContent());
		EngineerConsoleController.setStepInformation(rx.getName() + " <= Z." , false);
		CPU.cyclePlusOne();
		Halt.halt();
	}
	
	//AND without single step halt
	public static void ANDNHLT() {
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
		CPU.cyclePlusOne();
		//Store the result in RX
		rx.setContent(cpu.getZ().getContent());
		CPU.cyclePlusOne();
	}
	
	//******************************************************************************************************************************
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
		EngineerConsoleController.setStepInformation("Y <= " + rx.getName(), false);
		CPU.cyclePlusOne();
		Halt.halt();
		//Do "OR" operation in ALU, two operands are content of Y and content of RY;
		//The result will be temporarily stored in Register Z.
		cpu.getALU().or(cpu.getY().getContent(), ry.getContent());
		EngineerConsoleController.setStepInformation("Z <= ALU OR( c(Y), c(" + ry.getName() + ") )." , false);
		Halt.halt();
		//Store the result in RX
		rx.setContent(cpu.getZ().getContent());
		EngineerConsoleController.setStepInformation(rx.getName() + " <= Z." , false);
		CPU.cyclePlusOne();
		Halt.halt();
	}
	
	//ORR without single step halt
	public static void ORRNHLT() {
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
		CPU.cyclePlusOne();
		//Store the result in RX
		rx.setContent(cpu.getZ().getContent());
		CPU.cyclePlusOne();
	}
	//******************************************************************************************************************************
	//Instruction 025, NOT rx:
	//Logical Not of Register To Register
	//c(rx) <- NOT c(rx)
	public static void NOT() {
		//This operation will be done by ALU.
		//Firstly, get rx, determine which register will be used in this instruction
		Register rx = null;
		switch(R) {
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
		EngineerConsoleController.setStepInformation("Z <= ALU NOT( c(" + rx.getName() + ") )." , false);
		CPU.cyclePlusOne();
		Halt.halt();
		//Store the result in RX
		rx.setContent(cpu.getZ().getContent());
		EngineerConsoleController.setStepInformation(rx.getName() + " <= Z." , false);
		CPU.cyclePlusOne();
		Halt.halt();
	}
	
	//NOT without single step halt
	public static void NOTNHLT() {
		//This operation will be done by ALU.
		//Firstly, get rx, determine which register will be used in this instruction
		Register rx = null;
		switch(R) {
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
		CPU.cyclePlusOne();
		//Store the result in RX
		rx.setContent(cpu.getZ().getContent());
		CPU.cyclePlusOne();
	}
}
