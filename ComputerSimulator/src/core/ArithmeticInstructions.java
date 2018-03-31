package core;
import gui.controllers.EngineerConsoleController;

//This class contains all arithmetic instructions includes add, subtract, multiply and divide.
//These instructions are parts of ISA.
public class ArithmeticInstructions extends ISA{
	
	//******************************************************************************************************************************************************************************************
	//*****************************************************************************************
	//* 		Multiply Register by Register													*
	//*		rx, rx+1 <- c(rx) * c(ry)														*
	//*		rx must be 0 or 2																*
	//*		ry must be 0 or 2																*
	//* 		rx contains the high order bits, rx+1 contains the low order bits of the result	*
	//*		Set OVERFLOW flag, if overflow													*
	//*****************************************************************************************
	public static void MLT() {
		Register rx = null, ry = null , rxPlusOne = null;
		
		//Get RX and RY, determine which register will be used in this instruction.
		switch(RX) {
			case"00":
	            rx = cpu.getR0();
	            rxPlusOne = cpu.getR1();
	            break;
	        case "10":
	        		rx = cpu.getR2();
	        		rxPlusOne = cpu.getR3();
	            break;
		}
		switch(RY) {
			case"00":
	            ry = cpu.getR0();
	            break;
	        case "10":
	        		ry = cpu.getR2();
	            break;
		}
		
		//Get PR and MLR ready.
		PR pr = CPU.getInstance().getPR();
		Register mlr = CPU.getInstance().getMLR();
		//Initialize registers which will be used in MLT
		//******Cycle++
		mlr.setContent(rx.getContent());
		CPU.cyclePlusOne();
		EngineerConsoleController.setStepInformation("MLR <= " + rx.getName(), false);
		Halt.halt();
		//PR is a 32bits register.
		//the first 16 bits store the products and the second 16 bits stores the multiplier (Ry).
		//******Cycle++
		pr.setContent("0000000000000000" + ry.getContent());
		CPU.cyclePlusOne();
		EngineerConsoleController.setStepInformation("PR <= " + ry.getName() + ". Add 16 zeroes in the higher 16 bits.", false);
		Halt.halt();
		for( int i = 0 ; i < rx.getSize(); i++) {
			//if the last bit of ry == 1, add multiplicand and product in ALU and save result in the high order of PR.
			if(pr.getLowOrderBits().charAt(15) == '1') {
				//add and save
				//Cycle++
				CPU.cyclePlusOne();
				pr.saveProduct(CPU.getInstance().getALU().add(mlr.getContent(), pr.getHighOrderBits()));
				//right shift pr. ready for next circle.
				//Cycle++
				CPU.cyclePlusOne();
				pr.rightShift();
			}else {
				//if the last bit of ry == 0. Just right shift PR.
				//Cycle++
				CPU.cyclePlusOne();
				pr.rightShift();
			}
			EngineerConsoleController.setStepInformation("Multiple c(PR) whith c(MLR). Result is stored in PR", false);
			Halt.halt();
			
		}
		//when multiply finish, save high order bits in rx, and low order bits in rx+1;
		//******Cycle++
		rx.setContent(pr.getHighOrderBits());
		CPU.cyclePlusOne();
		EngineerConsoleController.setStepInformation(rx.getName() + " <= PR high order bits", false);
		Halt.halt();
		//******Cycle++
		rxPlusOne.setContent(pr.getLowOrderBits());
		CPU.cyclePlusOne();
		EngineerConsoleController.setStepInformation(rxPlusOne.getName() + " <= PR low order bits", false);
		Halt.halt();
	}
	
	//MLT without single step halt.
	public static void MLTNHLT() {
		Register rx = null, ry = null , rxPlusOne = null;
		
		//Get RX and RY, determine which register will be used in this instruction.
		switch(RX) {
			case"00":
	            rx = cpu.getR0();
	            rxPlusOne = cpu.getR1();
	            break;
	        case "10":
	        		rx = cpu.getR2();
	        		rxPlusOne = cpu.getR3();
	            break;
		}
		switch(RY) {
			case"00":
	            ry = cpu.getR0();
	            break;
	        case "10":
	        		ry = cpu.getR2();
	            break;
		}
		
		//Get PR and MLR ready.
		PR pr = CPU.getInstance().getPR();
		Register mlr = CPU.getInstance().getMLR();
		//Initialize registers which will be used in MLT
		//******Cycle++
		mlr.setContent(rx.getContent());
		CPU.cyclePlusOne();
		//PR is a 32bits register.
		//the first 16 bits store the products and the second 16 bits stores the multiplier (Ry).
		//******Cycle++
		pr.setContent("0000000000000000" + ry.getContent());
		CPU.cyclePlusOne();
		for( int i = 0 ; i < rx.getSize(); i++) {
			//if the last bit of ry == 1, add multiplicand and product in ALU and save result in the high order of PR.
			if(pr.getLowOrderBits().charAt(15) == '1') {
				//add and save
				//Cycle++
				CPU.cyclePlusOne();
				pr.saveProduct(CPU.getInstance().getALU().add(mlr.getContent(), pr.getHighOrderBits()));
				//right shift pr. ready for next circle.
				//Cycle++
				CPU.cyclePlusOne();
				pr.rightShift();
			}else {
				//if the last bit of ry == 0. Just right shift PR.
				//Cycle++
				CPU.cyclePlusOne();
				pr.rightShift();
			}		
		}
		//when multiply finish, save high order bits in rx, and low order bits in rx+1;
		//******Cycle++
		rx.setContent(pr.getHighOrderBits());
		CPU.cyclePlusOne();
		//******Cycle++
		rxPlusOne.setContent(pr.getLowOrderBits());
		CPU.cyclePlusOne();
	}
	
	//******************************************************************************************************************************************************************************************	
	//*************************************************************************
	//*		Divide Register by Register										*
	//*		rx, rx+1 <- c(rx)/ c(ry)											*
	//*		rx must be 0 or 2												*
	//*		rx contains the quotient; rx+1 contains the remainder				*
	//*		ry must be 0 or 2												*
	//*		If c(ry) = 0, set cc(3) to 1 (set DIVZERO flag)					*
	//*************************************************************************
	public static void DVD() {
		Register rx = null, ry = null, rxPlusOne = null;
		//Get RX and RY from "ISA", determine which register will be used in this instruction.
		switch(RX) {
			case"00":
	            rx = cpu.getR0();
	            rxPlusOne = cpu.getR1();
	            break;
	        case"10":
	        		rx = cpu.getR2();
	        		rxPlusOne = cpu.getR3();
	            break;
		}
		switch(RY) {
			case"00":
	            ry = cpu.getR0();
	            break;
	        case"10":
	        		ry = cpu.getR2();
	            break;
		}
		//if c(ry) = 0 , set cc(3) to 1
		if(ry.getContent().equals("0000000000000000")) {
			cpu.getCC().setContent("DZ");
			CPU.cyclePlusOne();
			EngineerConsoleController.setStepInformation("Divided by Zero, CC(3) <- 1", false);
			Halt.halt();
		}
		//Get the instance of quotient register and remainder register.
		DR dr = cpu.getDR();
		QR qr = cpu.getQR();
		Register rr = cpu.getRR();
		
		//steps of divide
		//1. Initialize
		dr.setContent(ry.getContent());
		CPU.cyclePlusOne();
		EngineerConsoleController.setStepInformation("DR <= " + ry.getName(), false);
		Halt.halt();
		
		rr.setContent(rx.getContent());
		qr.setContent("0000000000000000");
		CPU.cyclePlusOne();
		EngineerConsoleController.setStepInformation("RR <= " + rx.getName() + ". Set qr to 0", false);
		Halt.halt();

		//2.calculate
		for(int i = 0 ; i < 17; i++) {
			//2a. subtract divider from remainder register. 
			cpu.getALU().minus(rr.getContent(), dr.getContent());
			//2b. write the result back to remainder register 
			rr.setContent(cpu.getALU().get32BitsContent());
			CPU.cyclePlusOne();
			//2c. check the highest bit
			//if the highest bit in rr is 1, that is the number in rr is negative. Which means the previous minus operation should not be done.
			//we need to revert the content of RR. And left shift the qr, set the lowest bit to "0";
			if(rr.getContent().charAt(0) == '1') {
				cpu.getALU().add(rr.getContent(), dr.getContent());
				rr.setContent(cpu.getALU().get32BitsContent());
				qr.leftShiftWithZero();
				CPU.cyclePlusOne();
			}//else the subtract operation is necessary. we need to left shift the qr and set the lowest bit to "1";
			else {
				qr.leftShiftWithOne();
				CPU.cyclePlusOne();
			}
			//3. right shift dr, this could be done in the same clock cycle with step 2c
			dr.rightShift();
			//end of one divide calculation cycle.
		}
		EngineerConsoleController.setStepInformation("Divide c(RR) by c(dr), quotient is stored in QR and Remainder is stored in RR", false);
		Halt.halt();
		//4. store result
		//move content of qr to rx and lower 16 bits of content of rr to rx+1
		rx.setContent(qr.getContent());
		CPU.cyclePlusOne();
		EngineerConsoleController.setStepInformation(rx.getName() + "<= QR", false);
		Halt.halt();
		rxPlusOne.setContent(rr.getContent().substring(16));
		CPU.cyclePlusOne();
		EngineerConsoleController.setStepInformation(rxPlusOne.getName() + "<= RR low order 16 bits", false);
		Halt.halt();
	}

	//DVD without single step halt
	public static void DVDNHLT() {
		Register rx = null, ry = null, rxPlusOne = null;
		//Get RX and RY from "ISA", determine which register will be used in this instruction.
		switch(RX) {
			case"00":
	            rx = cpu.getR0();
	            rxPlusOne = cpu.getR1();
	            break;
	        case"10":
	        		rx = cpu.getR2();
	        		rxPlusOne = cpu.getR3();
	            break;
		}
		switch(RY) {
			case"00":
	            ry = cpu.getR0();
	            break;
	        case"10":
	        		ry = cpu.getR2();
	            break;
		}
		//if c(ry) = 0 , set cc(3) to 1
		if(ry.getContent().equals("0000000000000000")) {
			cpu.getCC().setContent("DZ");
			CPU.cyclePlusOne();
		}
		//Get the instance of quotient register and remainder register.
		DR dr = cpu.getDR();
		QR qr = cpu.getQR();
		Register rr = cpu.getRR();
		
		//steps of divide
		//1. Initialize
		dr.setContent(ry.getContent());
		CPU.cyclePlusOne();
		
		rr.setContent(rx.getContent());
		qr.setContent("0000000000000000");
		CPU.cyclePlusOne();

		//2.calculate
		for(int i = 0 ; i < 17; i++) {
			//2a. subtract divider from remainder register. 
			cpu.getALU().minus(rr.getContent(), dr.getContent());
			//2b. write the result back to remainder register 
			rr.setContent(cpu.getALU().get32BitsContent());
			CPU.cyclePlusOne();
			//2c. check the highest bit
			//if the highest bit in rr is 1, that is the number in rr is negative. Which means the previous minus operation should not be done.
			//we need to revert the content of RR. And left shift the qr, set the lowest bit to "0";
			if(rr.getContent().charAt(0) == '1') {
				cpu.getALU().add(rr.getContent(), dr.getContent());
				rr.setContent(cpu.getALU().get32BitsContent());
				qr.leftShiftWithZero();
				CPU.cyclePlusOne();
			}//else the subtract operation is necessary. we need to left shift the qr and set the lowest bit to "1";
			else {
				qr.leftShiftWithOne();
				CPU.cyclePlusOne();
			}
			//3. right shift dr, this could be done in the same clock cycle with step 2c
			dr.rightShift();
			//end of one divide calculation cycle.
		}
		//4. store result
		//move content of qr to rx and lower 16 bits of content of rr to rx+1
		rx.setContent(qr.getContent());
		CPU.cyclePlusOne();
		rxPlusOne.setContent(rr.getContent().substring(16));
		CPU.cyclePlusOne();
	}
//******************************************************************************************************************************************************************************************		
//	Add Memory To Register, r = 0..3
//	r<- c(r) + c(EA)
	public static void AMR(){
		Register r = null;
		//get the Register according to R
		switch (R){
			case "00": r = cpu.getR0(); break;
			case "01": r = cpu.getR1();break;
			case "10": r = cpu.getR2();break;
			case "11": r = cpu.getR3();break;
		}
		//set MAR register
		cpu.getMAR().setContent(cpu.getIAR().getContent());
		CPU.cyclePlusOne();
		EngineerConsoleController.setStepInformation("Execute:MAR<=IAR",false);
		Halt.halt();
		//get the content in memory using address in MAR, and load it to MBR.
		Cache.getInstance().cacheToMBR(cpu.getMAR().getContent());
		//Execute the operation move data to IRR
		cpu.getIRR().setContent(cpu.getMBR().getContent());
		CPU.cyclePlusOne();
		EngineerConsoleController.setStepInformation("Execute:IRR<=MBR",false);
		Halt.halt();
		//move c(r) to Y
		cpu.getY().setContent(r.getContent());
		CPU.cyclePlusOne();
		EngineerConsoleController.setStepInformation("Execute:Y<=c(r)",false);
		Halt.halt();

		// r<- c(r)+c(EA)
		r.setContent(ALU.getInstance().add(cpu.getY().getContent(),cpu.getIRR().getContent()));
		CPU.cyclePlusOne();
		EngineerConsoleController.setStepInformation("Execute:r<=c(r)+c(EA)",false);
		Halt.halt();
	}
	
	//AMR without single step halt
	public static void AMRNHLT(){
		Register r = null;
		//get the Register according to R
		switch (R){
			case "00": r = cpu.getR0(); break;
			case "01": r = cpu.getR1();break;
			case "10": r = cpu.getR2();break;
			case "11": r = cpu.getR3();break;
		}
		//set MAR register
		cpu.getMAR().setContent(cpu.getIAR().getContent());
		CPU.cyclePlusOne();
		//get the content in memory using address in MAR, and load it to MBR.
		Cache.getInstance().cacheToMBRNHLT(cpu.getMAR().getContent());
		//Execute the operation move data to IRR
		cpu.getIRR().setContent(cpu.getMBR().getContent());
		CPU.cyclePlusOne();
		//move c(r) to Y
		cpu.getY().setContent(r.getContent());
		CPU.cyclePlusOne();
		// r<- c(r)+c(EA)
		r.setContent(ALU.getInstance().add(cpu.getY().getContent(),cpu.getIRR().getContent()));
		CPU.cyclePlusOne();
	}
	
//******************************************************************************************************************************************************************************************		
//	Add Immediate to Register, r = 0..3
//	r <- c(r) + Immed
//	Note:
//	 1. if Immed = 0, does nothing
//   2. if c(r) = 0, loads r with Immed
//	IX and I are ignored in this instruction

	public static void AIR(){
		Register r = null;
		//get the Register according to R
		switch (R){
			case "00": r = cpu.getR0(); break;
			case "01": r = cpu.getR1();break;
			case "10": r = cpu.getR2();break;
			case "11": r = cpu.getR3();break;
		}
		if(!address.equals("00000")){
			cpu.getY().setContent(CPU.alignmentImmed(Immed));
			CPU.cyclePlusOne();
			EngineerConsoleController.setStepInformation("Y <= immed",false);
			Halt.halt();
			if(r.getContent().equals("0000000000000000")){
				r.setContent(cpu.getY().getContent());
				CPU.cyclePlusOne();
				EngineerConsoleController.setStepInformation(r.getName() + " <= immed",false);
				Halt.halt();
			}else{
				// r<-c(r)+ c(Y)
				r.setContent(ALU.getInstance().add(r.getContent(),cpu.getY().getContent()));
				CPU.cyclePlusOne();
				EngineerConsoleController.setStepInformation(r.getName() + " <= c(r) + c(Y) (Immed)",false);
				Halt.halt();
			}
		}
	}
	
	//AIR without single step halt
	public static void AIRNHLT(){
		Register r = null;
		//get the Register according to R
		switch (R){
			case "00": r = cpu.getR0(); break;
			case "01": r = cpu.getR1();break;
			case "10": r = cpu.getR2();break;
			case "11": r = cpu.getR3();break;
		}
		if(!address.equals("00000")){
			cpu.getY().setContent(CPU.alignmentImmed(Immed));
			CPU.cyclePlusOne();
			if(r.getContent().equals("0000000000000000")){
				r.setContent(cpu.getY().getContent());
				CPU.cyclePlusOne();
			}else{
				// r<-c(r)+ c(Y)
				r.setContent(ALU.getInstance().add(r.getContent(),cpu.getY().getContent()));
				CPU.cyclePlusOne();
			}
		}
	}
	
//******************************************************************************************************************************************************************************************	
//	Subtract  Immediate  from Register, r = 0..3
//	r <- c(r) - Immed
//	Note:
//	1. if Immed = 0, does nothing
//  2. if c(r) = 0, loads r1 with –(Immed)
//	IX and I are ignored in this instruction

	public static void SIR(){
		Register r = null;
		//get the Register according to R
		switch (R){
			case "00": r = cpu.getR0(); break;
			case "01": r = cpu.getR1();break;
			case "10": r = cpu.getR2();break;
			case "11": r = cpu.getR3();break;
		}
		if(!address.equals("00000")){
			cpu.getY().setContent(CPU.alignmentImmed(Immed));
			CPU.cyclePlusOne();
			EngineerConsoleController.setStepInformation("Y <= immed",false);
			Halt.halt();
			if(r.getContent().equals("0000000000000000")){
				r.setContent(ALU.getInstance().minus("0000000000000000",cpu.getY().getContent()));
				CPU.cyclePlusOne();
				EngineerConsoleController.setStepInformation(r.getName() + " <= (-immed)",false);
				Halt.halt();
			}else{
				// r<-c(r)+ c(Y)immed
				r.setContent(ALU.getInstance().minus(r.getContent(),cpu.getY().getContent()));
				CPU.cyclePlusOne();
				EngineerConsoleController.setStepInformation(r.getName() + " <= c(r) - c(Y) (Immed)",false);
				Halt.halt();
			}
		}
	}

	//SIR without single step halt
	public static void SIRNHLT(){
		Register r = null;
		//get the Register according to R
		switch (R){
			case "00": r = cpu.getR0(); break;
			case "01": r = cpu.getR1();break;
			case "10": r = cpu.getR2();break;
			case "11": r = cpu.getR3();break;
		}
		if(!address.equals("00000")){
			cpu.getY().setContent(CPU.alignmentImmed(Immed));
			CPU.cyclePlusOne();
			if(r.getContent().equals("0000000000000000")){
				r.setContent(ALU.getInstance().minus("0000000000000000",cpu.getY().getContent()));
				CPU.cyclePlusOne();
			}else{
				// r<-c(r)+ c(Y)immed
				r.setContent(ALU.getInstance().minus(r.getContent(),cpu.getY().getContent()));
				CPU.cyclePlusOne();
			}
		}
	}
	
//******************************************************************************************************************************		
//	Subtract Memory From Register, r = 0..3
//	r<- c(r) – c(EA)
	public static void SMR(){
		Register r = null;
		//get the Register according to R
		switch (R){
			case "00": r = cpu.getR0(); break;
			case "01": r = cpu.getR1();break;
			case "10": r = cpu.getR2();break;
			case "11": r = cpu.getR3();break;
		}
		//set MAR register
		cpu.getMAR().setContent(cpu.getIAR().getContent());
		CPU.cyclePlusOne();
		EngineerConsoleController.setStepInformation("Execute: MAR <= IAR",false);
		Halt.halt();

		//get the content in memory using address in MAR, and load it to MBR.
		Cache.getInstance().cacheToMBR(cpu.getMAR().getContent());
		//Execute the operation move data to IRR
		cpu.getIRR().setContent(cpu.getMBR().getContent());
		CPU.cyclePlusOne();
		EngineerConsoleController.setStepInformation("Execute: IRR <= MBR",false);
		Halt.halt();

		//move c(r) to Y
		cpu.getY().setContent(r.getContent());
		CPU.cyclePlusOne();
		EngineerConsoleController.setStepInformation("Execute:Y <= c(r)",false);
		Halt.halt();

		// r<- c(r)-c(EA)
		r.setContent(ALU.getInstance().minus(cpu.getY().getContent(),cpu.getIRR().getContent()));
		CPU.cyclePlusOne();
		EngineerConsoleController.setStepInformation("Execute: r <= c(r) - c(EA)",false);
		Halt.halt();
	}
	
	//SMR with out single step halt
	public static void SMRNHLT(){
		Register r = null;
		//get the Register according to R
		switch (R){
			case "00": r = cpu.getR0(); break;
			case "01": r = cpu.getR1();break;
			case "10": r = cpu.getR2();break;
			case "11": r = cpu.getR3();break;
		}
		//set MAR register
		cpu.getMAR().setContent(cpu.getIAR().getContent());
		CPU.cyclePlusOne();

		//get the content in memory using address in MAR, and load it to MBR.
		Cache.getInstance().cacheToMBRNHLT(cpu.getMAR().getContent());
		CPU.cyclePlusOne();
		//Execute the operation move data to IRR
		cpu.getIRR().setContent(cpu.getMBR().getContent());
		CPU.cyclePlusOne();

		//move c(r) to Y
		cpu.getY().setContent(r.getContent());
		CPU.cyclePlusOne();

		// r<- c(r)-c(EA)
		r.setContent(ALU.getInstance().minus(cpu.getY().getContent(),cpu.getIRR().getContent()));
		CPU.cyclePlusOne();
	}
}
