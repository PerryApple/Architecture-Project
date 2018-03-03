package core;
import gui.controllers.CenterPaneController;
//This class contains all arithmetic instructions includes add, subtract, multiply and divide.
//These instructions are parts of ISA.
public class ArithmeticInstructions extends ISA{
	
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
		//Initialize registers using in MLT
		//******Cycle++
		cpu.cyclePlusOne();
		mlr.setContent(rx.getContent());
		//PR is a 32bits register.
		//the first 16 bits store the products and the second 16 bits stores the multiplier (Ry).
		//******Cycle++
		cpu.cyclePlusOne();
		pr.setContent("0000000000000000" + ry.getContent());
		for( int i = 0 ; i < rx.getSize(); i++) {
			//if the last bit of ry == 1, add multiplicand and product in ALU and save result in the high order of PR.
			if(pr.getLowOrderBits().charAt(15) == '1') {
				//add and save
				//Cycle++
				cpu.cyclePlusOne();
				pr.saveProduct(CPU.getInstance().getALU().add(mlr.getContent(), pr.getHighOrderBits()));
				//right shift pr. ready for next circle.
				//Cycle++
				cpu.cyclePlusOne();
				pr.rightShift();
			}else {
				//if the last bit of ry == 0. Just right shift PR.
				//Cycle++
				cpu.cyclePlusOne();
				pr.rightShift();
			}
		}
		//when multiply finish, save high order bits in rx, and low order bits in rx+1;
		//******Cycle++
		cpu.cyclePlusOne();
		rx.setContent(pr.getHighOrderBits());
		//******Cycle++
		cpu.cyclePlusOne();
		rxPlusOne.setContent(pr.getLowOrderBits());
	}
	
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
		}
		//Get the instance of quotient register and remainder register.
		DR dr = cpu.getDR();
		QR qr = cpu.getQR();
		Register rr = cpu.getRR();
		
		//steps of divide
		//1. Initialize
		dr.setContent(ry.getContent());
		//cycle++
		cpu.cyclePlusOne();
		rr.setContent(rx.getContent());
		//cycle++
		cpu.cyclePlusOne();
		qr.setContent("0000000000000000");
		//2.calculate
		for(int i = 0 ; i < 17; i++) {
			//2a. subtract divider from remainder register. 
			cpu.getALU().minus(rr.getContent(), dr.getContent());
			//2b. write the result back to remainder register 
			rr.setContent(cpu.getALU().get32BitsContent());
			cpu.cyclePlusOne();
			//2c. check the highest bit
			//if the highest bit in rr is 1, that is the number in rr is negative. Which means the previous minus operation should not be done.
			//we need to revert the content of RR. And left shift the qr, set the lowest bit to "0";
			if(rr.getContent().charAt(0) == '1') {
				cpu.getALU().add(rr.getContent(), dr.getContent());
				rr.setContent(cpu.getALU().get32BitsContent());
				qr.leftShiftWithZero();
				cpu.cyclePlusOne();
			}//else the subtract operation is necessary. we need to left shift the qr and set the lowest bit to "1";
			else {
				qr.leftShiftWithOne();
				cpu.cyclePlusOne();
			}
			//3. right shift dr, this could be done in the same clock cycle with step 2c
			dr.rightShift();
			//end of one divide calculation cycle.
		}
		//4. store result
		//move content of qr to rx and lower 16 bits of content of rr to rx+1
		cpu.cyclePlusOne();
		rx.setContent(qr.getContent());
		cpu.cyclePlusOne();
		rxPlusOne.setContent(rr.getContent().substring(16));
	}

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
		CenterPaneController.setStepInformation("Execute:MAR<=IAR",false);
		Halt.halt();
		cpu.cyclePlusOne();
		//get the content in memory using address in MAR, and load it to MBR.
		Cache.getInstance().cacheToMBR(cpu.getMAR().getContent());
		//Execute the operation move data to IRR
		cpu.getIRR().setContent(cpu.getMBR().getContent());
		CenterPaneController.setStepInformation("Execute:IRR<=MBR",false);
		Halt.halt();
		cpu.cyclePlusOne();
		//move c(r) to Y
		cpu.getY().setContent(r.getContent());
		CenterPaneController.setStepInformation("Execute:Y<=c(r)",false);
		Halt.halt();
		cpu.cyclePlusOne();
		// r<- c(r)+c(EA)
		r.setContent(ALU.getInstance().add(cpu.getY().getContent(),cpu.getIRR().getContent()));
		CenterPaneController.setStepInformation("Execute:r<=c(r)+c(EA)",false);
		Halt.halt();
	}
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
			cpu.getIAR().setContent(cpu.alignment(address));
			CenterPaneController.setStepInformation("Execute:IAR<-immed",false);
			Halt.halt();
			if(r.getContent().equals("0000000000000000")){
				r.setContent(cpu.getIAR().getContent());
				CenterPaneController.setStepInformation("Execute:r<-immed",false);
				Halt.halt();
			}else{
				// Y<-c(r)
				cpu.getY().setContent(r.getContent());
				CenterPaneController.setStepInformation("Execute:Y<-c(r)",false);
				Halt.halt();
				// r<-c(r)+ immed
				r.setContent(ALU.getInstance().add(cpu.getY().getContent(),cpu.getIAR().getContent()));
				CenterPaneController.setStepInformation("Execute:r<-c(r)+immed",false);
				Halt.halt();
			}
		}
	}
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
			cpu.getIAR().setContent(cpu.alignment(address));
			CenterPaneController.setStepInformation("Execute:IAR<-immed",false);
			Halt.halt();
			if(r.getContent().equals("0000000000000000")){
				r.setContent(ALU.getInstance().minus("000000000000000",cpu.getIAR().getContent()));
				CenterPaneController.setStepInformation("Execute:r<-(-immed)",false);
				Halt.halt();
			}else{
				// Y<-c(r)
				cpu.getY().setContent(r.getContent());
				CenterPaneController.setStepInformation("Execute:Y<-c(r)",false);
				Halt.halt();
				// r<-c(r)+ immed
				r.setContent(ALU.getInstance().minus(cpu.getY().getContent(),cpu.getIAR().getContent()));
				CenterPaneController.setStepInformation("Execute:r<-c(r)-immed",false);
				Halt.halt();
			}
		}
	}

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
		CenterPaneController.setStepInformation("Execute:MAR<=IAR",false);
		Halt.halt();
		cpu.cyclePlusOne();
		//get the content in memory using address in MAR, and load it to MBR.
		Cache.getInstance().cacheToMBR(cpu.getMAR().getContent());
		//Execute the operation move data to IRR
		cpu.getIRR().setContent(cpu.getMBR().getContent());
		CenterPaneController.setStepInformation("Execute:IRR<=MBR",false);
		Halt.halt();
		cpu.cyclePlusOne();
		//move c(r) to Y
		cpu.getY().setContent(r.getContent());
		CenterPaneController.setStepInformation("Execute:Y<=c(r)",false);
		Halt.halt();
		cpu.cyclePlusOne();
		// r<- c(r)-c(EA)
		r.setContent(ALU.getInstance().minus(cpu.getY().getContent(),cpu.getIRR().getContent()));
		CenterPaneController.setStepInformation("Execute:r<=c(r)+c(EA)",false);
		Halt.halt();
	}


	//Test function
//	public static void main(String[] s) {
//		cpu.getR0().setContent("1000000000001111");
//		cpu.getR2().setContent("0000000000000011");
//		RX = "00";
//		RY = "10";
//		MLT();
//		System.out.println(cpu.getR0().getContent());
//		System.out.println(cpu.getR1().getContent());
////		System.out.println(cpu.getCC().getContent());
////		cpu.getCC().setContent("DIVZERO");
////		System.out.println(cpu.getCC().getContent());
//	}
}
