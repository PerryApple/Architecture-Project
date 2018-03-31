package core;
import gui.controllers.EngineerConsoleController;

public class ShiftAndRotate extends ISA {
	
	//********************************************************************************************************************************
	//opCode 031, SRC r, count, L/R, A/L:
	//**********************************************************************************************************************
	//*		Shift Register by Count																						*
	//*		c(r) is shifted left (L/R =1) or right (L/R = 0) either logically (A/L = 1) or arithmetically (A/L = 0)		*
	//*		XX, XXX are ignored																							*
	//*		Count = 0…15																									*
	//*		If Count = 0, no shift occurs																				*
	//**********************************************************************************************************************
	public static void SRC() {
		//This instruction will be done in Shift and Rotate Register (SRR).
		//1. determine which Register will be used in this instruction
		Register r = null;
		switch (R){
	        case"00":
	            r = cpu.getR0();
	            break;
	        case "01":
	        		r = cpu.getR1();
	            break;
	        case "10":
	        		r = cpu.getR2();
	            break;
	        case "11":
	        		r = cpu.getR3();
	    }
		
		//2.Move the content of r to SRR
		cpu.getSRR().setContent(r.getContent());
		CPU.cyclePlusOne();
		EngineerConsoleController.setStepInformation("SRR <= " + r.getName() , false);
		Halt.halt();
		//3.Do shift
		//if L/R = 0 , right shift
		if(LorR.equals("0")) {
			//if A/L = 0, arithmetic shift
			if(AorL.equals("0")) {
				EngineerConsoleController.setStepInformation("Arithmetic shift the content of SRR " + count + " bits right", false);
				while(count > 0) {
					cpu.getSRR().arithmeticRightShift();
					count--;
				}
				CPU.cyclePlusOne();
				Halt.halt();
			}
			//if A/L = 1, logical shift
			else {
				EngineerConsoleController.setStepInformation("Logical shift the content of SRR " + count + " bits right", false);
				while(count > 0) {
					cpu.getSRR().logicalRightShift();
					count--;
				}
				CPU.cyclePlusOne();
				Halt.halt();
			}
		}
		//if L/R = 1 , left shift
		else {
			//if A/L = 0, arithmetic shift
			if(AorL.equals("0")) {
				EngineerConsoleController.setStepInformation("Arithmetic shift the content of SRR " + count + " bits left", false);
				while(count > 0) {
					cpu.getSRR().arithmeticLeftShift();
					count--;
				}
				CPU.cyclePlusOne();
				Halt.halt();
			}
			//if A/L = 1, logical shift
			else {
				EngineerConsoleController.setStepInformation("Logical shift the content of SRR " + count + " bits left", false);
				while(count > 0) {
					cpu.getSRR().logicalLeftShift();
					count--;
				}
				CPU.cyclePlusOne();
				Halt.halt();
			}
		}
		//4. Write the shifted data back to r
		r.setContent(cpu.getSRR().getContent());
		CPU.cyclePlusOne();
		EngineerConsoleController.setStepInformation(r.getName() + " <= SRR", false);
		Halt.halt();
	}
	
	//SRC without single step halt
	public static void SRCNHLT() {
		//This instruction will be done in Shift and Rotate Register (SRR).
		//1. determine which Register will be used in this instruction
		Register r = null;
		switch (R){
	        case"00":
	            r = cpu.getR0();
	            break;
	        case "01":
	        		r = cpu.getR1();
	            break;
	        case "10":
	        		r = cpu.getR2();
	            break;
	        case "11":
	        		r = cpu.getR3();
	    }
		
		//2.Move the content of r to SRR
		cpu.getSRR().setContent(r.getContent());
		CPU.cyclePlusOne();
		//3.Do shift
		//if L/R = 0 , right shift
		if(LorR.equals("0")) {
			//if A/L = 0, arithmetic shift
			if(AorL.equals("0")) {
				while(count > 0) {
					cpu.getSRR().arithmeticRightShift();
					count--;
				}
				CPU.cyclePlusOne();
			}
			//if A/L = 1, logical shift
			else {
				while(count > 0) {
					cpu.getSRR().logicalRightShift();
					count--;
				}
				CPU.cyclePlusOne();
			}
		}
		//if L/R = 1 , left shift
		else {
			//if A/L = 0, arithmetic shift
			if(AorL.equals("0")) {
				while(count > 0) {
					cpu.getSRR().arithmeticLeftShift();
					count--;
				}
				CPU.cyclePlusOne();
			}
			//if A/L = 1, logical shift
			else {
				while(count > 0) {
					cpu.getSRR().logicalLeftShift();
					count--;
				}
				CPU.cyclePlusOne();
			}
		}
		//4. Write the shifted data back to r
		r.setContent(cpu.getSRR().getContent());
		CPU.cyclePlusOne();
	}
	
	
	//******************************************************************************************************************************
	//opCode 032, RRC r, count, L/R, A/L:
	//*************************************************************************************************
	//*		Rotate Register by Count																	*
	//*		c(r) is rotated left (L/R = 1) or right (L/R =0) either logically (A/L =1)				*
	//*		XX, XXX is ignored																		*
	//*		Count = 0…15																				*
	//*		If Count = 0, no rotate occurs															*
	//*************************************************************************************************
	public static void RRC() {
		//This instruction will be done in Shift and Rotate Register (SRR).
		//1. determine which Register will be used in this instruction
		Register r = null;
		switch (R){
	        case"00":
	            r = cpu.getR0();
	            break;
	        case "01":
	        		r = cpu.getR1();
	            break;
	        case "10":
	        		r = cpu.getR2();
	            break;
	        case "11":
	        		r = cpu.getR3();
	    }
		//2.Move the content of r to SRR
				cpu.getSRR().setContent(r.getContent());
				CPU.cyclePlusOne();
				//3.Do Rotate
				//if L/R = 0 , right rotate
				if(LorR.equals("0")) {
					if(AorL.equals("0")) {
						EngineerConsoleController.setStepInformation("Right Rotate the content of SRR " + count + " bits", false);
						while(count > 0) {
							cpu.getSRR().rightRotate();;
							count--;
						}
						CPU.cyclePlusOne();
						Halt.halt();
						//4. Write the shifted data back to r
						r.setContent(cpu.getSRR().getContent());
						CPU.cyclePlusOne();
						EngineerConsoleController.setStepInformation(r.getName() + " <= SRR", false);
						Halt.halt();
					}
					//else Error
					else {
						EngineerConsoleController.setStepInformation("A/L Should be 0!", false);
						r.setContent("Error!");
					}
				}
				//if L/R = 1 , left rotate
				else {
					if(AorL.equals("0")) {
						EngineerConsoleController.setStepInformation("Left Rotate the content of SRR " + count + " bits", false);
						while(count > 0) {
							cpu.getSRR().leftRotate();
							count--;
						}
						CPU.cyclePlusOne();
						Halt.halt();
						//4. Write the shifted data back to r
						r.setContent(cpu.getSRR().getContent());
						CPU.cyclePlusOne();
						EngineerConsoleController.setStepInformation(r.getName() + " <= SRR", false);
						Halt.halt();
					}
					//else error!
					else {
						EngineerConsoleController.setStepInformation("A/L Should be 0!", false);
						r.setContent("Error");
						Halt.halt();
					}
				}
	}
	
	//RRC without single step halt
	public static void RRCNHLT() {
		//This instruction will be done in Shift and Rotate Register (SRR).
		//1. determine which Register will be used in this instruction
		Register r = null;
		switch (R){
	        case"00":
	            r = cpu.getR0();
	            break;
	        case "01":
	        		r = cpu.getR1();
	            break;
	        case "10":
	        		r = cpu.getR2();
	            break;
	        case "11":
	        		r = cpu.getR3();
	    }
		//2.Move the content of r to SRR
				cpu.getSRR().setContent(r.getContent());
				CPU.cyclePlusOne();
				//3.Do Rotate
				//if L/R = 0 , right rotate
				if(LorR.equals("0")) {
					if(AorL.equals("0")) {
						while(count > 0) {
							cpu.getSRR().rightRotate();;
							count--;
						}
						CPU.cyclePlusOne();
						//4. Write the shifted data back to r
						r.setContent(cpu.getSRR().getContent());
						CPU.cyclePlusOne();
					}
					//else Error
					else {
						r.setContent("Error!");
					}
				}
				//if L/R = 1 , left rotate
				else {
					if(AorL.equals("0")) {
						while(count > 0) {
							cpu.getSRR().leftRotate();
							count--;
						}
						CPU.cyclePlusOne();
						//4. Write the shifted data back to r
						r.setContent(cpu.getSRR().getContent());
						CPU.cyclePlusOne();
					}
					//else error!
					else {
						r.setContent("Error");
					}
				}
	}
}
