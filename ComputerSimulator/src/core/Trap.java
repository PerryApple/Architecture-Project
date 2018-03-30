package core;

public class Trap extends ISA {
	//Instruction 036 Trap
	public static void trap(){
		if(CPU.getInstance().getControler().trap) {
			//Move the content of memory location 2 to register PC
			CPU.getInstance().getMAR().setContent(CPU.alignment("10"));
			CPU.cyclePlusOne();
			Halt.halt();
			Cache.getInstance().cacheToMBR(CPU.getInstance().getMAR().getContent());
			CPU.cyclePlusOne();
			Halt.halt();
			CPU.getInstance().getPC().setContent(CPU.getInstance().getMBR().getContent());
			CPU.cyclePlusOne();
			Halt.halt();
			//reset trap flag
			CPU.getInstance().getControler().trap = false;
		}else {
			//Store the content of PC into memory location 2
			//move PC to MBR
			CPU.getInstance().getMBR().setContent(CPU.getInstance().getPC().getContent());
			CPU.cyclePlusOne();
			Halt.halt();
			//set MAR to 2
			CPU.getInstance().getMAR().setContent(CPU.alignment("10"));
			CPU.cyclePlusOne();
			Halt.halt();
			//Store content of MBR into memory via cache, address is indicated by MAR .
			Cache.getInstance().writeBack(CPU.getInstance().getMAR().getContent(),CPU.getInstance().getMBR().getContent());
			CPU.cyclePlusOne();
			Halt.halt();
			
			//Find the target routine according to the content of memory location 0;
			//Move memory[0] to MBR
			Cache.getInstance().cacheToMBR((CPU.alignment("0")));
			CPU.cyclePlusOne();
			Halt.halt();
			//get routine address according to the trap code. Result will be stored in register Z
			CPU.getInstance().getALU().add(CPU.getInstance().getMBR().getContent(), CPU.alignment(trapCode));
			//move the result to MAR
			CPU.getInstance().getMAR().setContent(CPU.getInstance().getZ().getContent());
			CPU.cyclePlusOne();
			Halt.halt();
			
			//find the address of target instructions in trap table, and Store the address into MBR
			Cache.getInstance().cacheToMBR(CPU.getInstance().getMAR().getContent());
			CPU.cyclePlusOne();
			Halt.halt();
			//store the instruction's address to PC		
			CPU.getInstance().getPC().setContent(CPU.getInstance().getMBR().getContent());
			CPU.cyclePlusOne();
			Halt.halt();
			//set jump flag, avoid unnecessary PC++
			CPU.getInstance().getControler().jump = true;
			//set trap flag true
			CPU.getInstance().getControler().trap = true;
		}
	}
	
	
	//Trap without single step halt
	public static void trapNHLT() {
		if(CPU.getInstance().getControler().trap) {
			//Move the content of memory location 2 to register PC
			CPU.getInstance().getMAR().setContent(CPU.alignment("10"));
			Cache.getInstance().cacheToMBR(CPU.getInstance().getMAR().getContent());
			CPU.getInstance().getPC().setContent(CPU.getInstance().getMBR().getContent());
			//reset trap flag
			CPU.getInstance().getControler().trap = false;
		}else {
			//Store the content of PC into memory location 2
			//move PC to MBR
			CPU.getInstance().getMBR().setContent(CPU.getInstance().getPC().getContent());
			//set MAR to 2
			CPU.getInstance().getMAR().setContent(CPU.alignment("10"));
			//Store content of MBR into memory via cache, address is indicated by MAR .
			Cache.getInstance().writeBack(CPU.getInstance().getMAR().getContent(),CPU.getInstance().getMBR().getContent());
			
			//Find the target routine according to the content of memory location 0;
			//Move memory[0] to MBR
			Cache.getInstance().cacheToMBR(CPU.alignment("0"));
			//get routine address according to the trap code. Result will be stored in register Z
			CPU.getInstance().getALU().add(CPU.getInstance().getMBR().getContent(), CPU.alignment(trapCode));
			//move the result to MAR
			CPU.getInstance().getMAR().setContent(CPU.getInstance().getZ().getContent());

			//find the address of target instructions in trap table, and Store the address into MBR
			Cache.getInstance().cacheToMBR(CPU.getInstance().getMAR().getContent());
			//store the instruction's address to PC		
			CPU.getInstance().getPC().setContent(CPU.getInstance().getMBR().getContent());
			//set jump flag, avoid unnecessary PC++
			CPU.getInstance().getControler().jump = true;
			//set trap flag true
			CPU.getInstance().getControler().trap = true;
		}
	}
}
