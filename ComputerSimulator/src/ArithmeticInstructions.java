import java.util.LinkedList;
import java.util.Queue;

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
		
		//Get RX and RY, determin which register will be used in this instruction.
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
		mlr.setContent(rx.getContent());
		//PR is a 32bits register.
		//the first 16 bits store the products and the second 16 bits stores the multiplier (Ry).
		//******Cycle++
		pr.setContent("0000000000000000" + ry.getContent());
		
		//******Cycle+16!!
		for( int i = 0 ; i < rx.getSize(); i++) {
			//if the last bit of ry == 1, add multiplicand and product in ALU and save result in the high order of PR.
			if(pr.getLowOrderBits().charAt(15) == '1') {
				//add and save
				pr.saveProduct(CPU.getInstance().getALU().add(mlr.getContent(), pr.getHighOrderBits()));
				//right shift pr. ready for next circle.
				pr.rightShift();
			}else {
				//if the last bit of ry == 0. Just right shift PR.
				pr.rightShift();
			}
		}
		//when multiply finish, save high order bits in rx, and low order bits in rx+1;
		//******Cycle++
		rx.setContent(pr.getHighOrderBits());
		//******Cycle++
		rxPlusOne.setContent(pr.getLowOrderBits());
	}
	
	//Test function
	public static void main(String[] s) {
		String s1 = "0000000000000001";
		System.out.println(s1.charAt(15));
		
	}
}
