package core;

import gui.controllers.EngineerConsoleController;

//Addressing function, provide the way to calculate the effective address.
public class Addressing {

	private static String stepInformation;
	
	private static CPU cpu = CPU.getInstance();
	
	//calculate effective address
    public static void getEffectiveAddress(String x, String i, String address){
		//In one cycle, move the first operand address to the Internal Address Register IAR
		cpu.getIAR().setContent(CPU.alignment(address));
		stepInformation=("Locate and fetch operand data:IAR<=address");
		sendStepInformation();
		CPU.cyclePlusOne();
		Halt.halt();
		
    		//if I field = 0, NO indirect addressing
        if(i.equals("0")){
        		//If  IX = 00, EA = contents of the Address field.
        	
            if(x.equalsIgnoreCase("00")){
            		//else just indexing
            }else{
            	
            		//if IX = 1..3, c(Xj) + contents of the Address field, where j = c(IX)
            		// that is, the IX field has an index register number 
            		//the contents of which are added to the contents of the address field
				stepInformation=("Locate and fetch operand data");
				sendStepInformation();
                if(x.equalsIgnoreCase("01")) {
                		//	If the operand is indexed, in 1 cycle add the contents of the specified index register to the IAR
                		// Index and put indexed address into IAR
                		indexed(cpu.getX1());
                		
                }else if(x.equalsIgnoreCase("10")) {
                		// Index and put indexed address into IAR
            			indexed(cpu.getX2());
	            		
                }else if(x.equalsIgnoreCase("11")) {
                		// Index and put indexed address into IAR
            			indexed(cpu.getX3());
	            		
            		}else {
            			cpu.getIAR().setContent("IAR Error");
            		}
            }
        }
        
        //if I field  = 1 
        else if(i.equals("1")){
			stepInformation=("Locate and fetch operand data");
			sendStepInformation();
        		//if IX = 00, c(Address). Indirect addressing, but NO indexing

			if(x.equals("00")){
            		//In 1 cycle, move the contents of the IAR to the MAR
            		cpu.getMAR().setContent(cpu.getIAR().getContent());
            		CPU.cyclePlusOne();	
            		Halt.halt();
            		//In one cycle fetch the contents of the word in memory specified by the MAR into the MBR
            		Cache.getInstance().cacheToMBR(cpu.getMAR().getContent());
    				CPU.cyclePlusOne();
                Halt.halt();
        			//IAR = MBR
                cpu.getIAR().setContent(cpu.getMBR().getContent());
				CPU.cyclePlusOne();
                Halt.halt();
                
            }else{
            		//if IX = 1..3, c(c(Xj) + Address), where j = c(IX). Both indirect addressing and indexing
                if(x.equalsIgnoreCase("01")) {
	                	//If the operand is indexed, in 1 cycle add the contents of the specified index register to the IAR
                		// Index and put indexed address into IAR
            			indexed(cpu.getX1());
            			
                }else if(x.equalsIgnoreCase("10")) {
                		// Index and put indexed address into IAR
            			indexed(cpu.getX2());
            			
                }else if (x.equalsIgnoreCase("11")) {
                		// Index and put indexed address into IAR
            			indexed(cpu.getX3());
            			
                }else {
                		cpu.getIAR().setContent("IAR Error");
                }
                
				//move IAR to MAR
				cpu.getMAR().setContent(cpu.getIAR().getContent());
				stepInformation=("MAR <= IAR");
				sendStepInformation();
				CPU.cyclePlusOne();
				Halt.halt();
				//Get content in Memory using address in MAR, and move it to MBR.
				stepInformation=("MBR <= Cache(Memory)[MAR]");
				sendStepInformation();
				Cache.getInstance().cacheToMBR(cpu.getMAR().getContent());
				CPU.cyclePlusOne();
				Halt.halt();
				//Move the effective address from MBR to IAR
				cpu.getIAR().setContent(cpu.getMBR().getContent());
				stepInformation=("IAR <= MBR");
				sendStepInformation();
				CPU.cyclePlusOne();
				Halt.halt();
            }
        }
    }
    
    //get Effective Address without halt
    public static void getEffectiveAddressNHLT(String x, String i, String address){
		//In one cycle, move the first operand address to the Internal Address Register IAR
		cpu.getIAR().setContent(CPU.alignment(address));
		CPU.cyclePlusOne();
		
    		//if I field = 0, NO indirect addressing
        if(i.equals("0")){
        		//If  IX = 00, EA = contents of the Address field.
        	
            if(x.equalsIgnoreCase("00")){
            		//else just indexing
            }else{
            	
            		//if IX = 1..3, c(Xj) + contents of the Address field, where j = c(IX)
            		// that is, the IX field has an index register number 
            		//the contents of which are added to the contents of the address field
                if(x.equalsIgnoreCase("01")) {
                		//	If the operand is indexed, in 1 cycle add the contents of the specified index register to the IAR
                		// Index and put indexed address into IAR
                		indexedNHLT(cpu.getX1());
                		
                }else if(x.equalsIgnoreCase("10")) {
                		// Index and put indexed address into IAR
            			indexedNHLT(cpu.getX2());
	            		
                }else if(x.equalsIgnoreCase("11")) {
                		// Index and put indexed address into IAR
            			indexedNHLT(cpu.getX3());
	            		
            		}else {
            			cpu.getIAR().setContent("IAR Error");
            		}
            }
        }
        
        //if I field  = 1 
        else if(i.equals("1")){
        		//if IX = 00, c(Address). Indirect addressing, but NO indexing

			if(x.equals("00")){
            		//In 1 cycle, move the contents of the IAR to the MAR
            		cpu.getMAR().setContent(cpu.getIAR().getContent());
            		CPU.cyclePlusOne();	
            		//In one cycle fetch the contents of the word in memory specified by the MAR into the MBR
            		Cache.getInstance().cacheToMBRNHLT(cpu.getMAR().getContent());
    				CPU.cyclePlusOne();
        			//IAR = MBR
                cpu.getIAR().setContent(cpu.getMBR().getContent());
				CPU.cyclePlusOne();
                
            }else{
            		//if IX = 1..3, c(c(Xj) + Address), where j = c(IX). Both indirect addressing and indexing
                if(x.equalsIgnoreCase("01")) {
	                	//If the operand is indexed, in 1 cycle add the contents of the specified index register to the IAR
                		// Index and put indexed address into IAR
            			indexedNHLT(cpu.getX1());
            			
                }else if(x.equalsIgnoreCase("10")) {
                		// Index and put indexed address into IAR
            			indexedNHLT(cpu.getX2());
            			
                }else if (x.equalsIgnoreCase("11")) {
                		// Index and put indexed address into IAR
            			indexedNHLT(cpu.getX3());
            			
                }else {
                		cpu.getIAR().setContent("IAR Error");
                }
                
				//move IAR to MAR
				cpu.getMAR().setContent(cpu.getIAR().getContent());
				CPU.cyclePlusOne();
				//Get content in Memory using address in MAR, and move it to MBR.
				Cache.getInstance().cacheToMBRNHLT(cpu.getMAR().getContent());
				CPU.cyclePlusOne();
				//Move the effective address from MBR to IAR
				cpu.getIAR().setContent(cpu.getMBR().getContent());
				stepInformation=("IAR <= MBR");
				sendStepInformation();
				CPU.cyclePlusOne();
            }
        }
    }
  
 //********************************************************************************************************************************
 // Index using specified index register and put indexed address into IAR
    private static void indexed(Register x) {
    		// If the operand is indexed, in 1 cycle add the contents of the specified index register to the IAR
		// Temporarily move content in IAR to Y and prepare to add will content in specified index register.
		cpu.getY().setContent(cpu.getIAR().getContent());
		stepInformation=("ALU calculate: Y <= IAR");
		sendStepInformation();
		//@@@@@@Question@@@@@
		CPU.cyclePlusOne();
		Halt.halt();
		//add content in specific index register with content in IAR to get effective address
		cpu.getALU().add(cpu.getY().getContent(), x.getContent());
		stepInformation=("ALU calculate: Y + c(" + x.getName() + ").");
		sendStepInformation();
		CPU.cyclePlusOne();
		Halt.halt();
		cpu.getIAR().setContent(cpu.getZ().getContent());
		stepInformation=("ALU calculate: IAR <= Z (ALU result)");
		sendStepInformation();
		CPU.cyclePlusOne();
		Halt.halt();
    }
    //Index without halt
    private static void indexedNHLT(Register x) {
		// If the operand is indexed, in 1 cycle add the contents of the specified index register to the IAR
		// Temporarily move content in IAR to Y and prepare to add will content in specified index register.
		cpu.getY().setContent(cpu.getIAR().getContent());
		//@@@@@@Question@@@@@
		CPU.cyclePlusOne();
		//add content in specific index register with content in IAR to get effective address
		cpu.getALU().add(cpu.getY().getContent(), x.getContent());
		CPU.cyclePlusOne();
		cpu.getIAR().setContent(cpu.getZ().getContent());
		CPU.cyclePlusOne();
    }
    
//***************************************************************************************************************************
    public static void sendStepInformation(){
    	EngineerConsoleController.setStepInformation(stepInformation,false);
	}
    
}
