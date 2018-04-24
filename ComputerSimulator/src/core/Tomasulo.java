package core;

import gui.controllers.EngineerConsoleController;
import gui.controllers.UserInterfaceController;
import gui.controllers.UserPart4MainController;

public class Tomasulo {
	
	private static boolean flush = false;
	public static boolean terminate = false;
	
	public static void proceedTomasulo() {
		TomasuloThreadControllor.halt();
		//proceed start from here
		//In order to simulate the speculative execution, fetch 10 instructions first into the ROB
		//Using branch prediction
		while(!terminate) {
			flush = false;
			int i = 1;
			while(i < 11) {
				
				//fetch instructions index by PC
				//Set instruction address in MAR
				CPU.getInstance().getMAR().setContent(CPU.getInstance().getPC().getContent());
				CPU.cyclePlusOne();
				//uses the address in the MAR to fetch a word from cache. This fetch occurs in one cycle.
	            //The word fetched from cache is placed in the Memory Buffer Register (MBR).
	            //if it is a miss, extract from memory and store it in cache
	            Cache.getInstance().cacheToMBR(CPU.getInstance().getMAR().getContent());
	            //Store instruction in ROB
	            ReOrderBuffer.fetchInstruction(CPU.getInstance().getMAR().getContent(), CPU.getInstance().getMBR().getContent());

				//Branch prediction, get next instruction address
				String nextPC = DirectionPredictor.getInstance().predict(CPU.getInstance().getPC().getContent());
	            
	            //PC++
				CPU.getInstance().getPC().setContent(nextPC);
				
	            i++;
			}
			
			
			//decode those 10 instructions into ReservationStation and renaming the registers
			i = 1;
			while(i < 11) {
				//Instruction decode start here
	            //The contents of the Reorderbuffer are moved to the Instruction Register (IR) sequentially.
	            //IR = ROB[i]
				CPU.getInstance().getIR().setContent(ReOrderBuffer.getInstruction(i));
				//Decode the instruction in IR
	            //In 1 cycle  process the instruction and use it to set several flags:
				CPU.getInstance().getTomasuloDecoder().setInstruction(ReOrderBuffer.getAddress(i), CPU.getInstance().getIR().getContent());
				CPU.getInstance().getTomasuloDecoder().decode();
				ReservationStation.rsPushInstruction(i,ReOrderBuffer.getAddress(i));
				//Save Register's new name in Register File
				RegisterFile.reNameRegister(ReservationStation.getInstruction(i).des, i);
				i++;
			}
			
			//Execute the instructions which operands are ready.
			String curInstruction = ReOrderBuffer.commit();
			while(curInstruction != "0000000000000000"){
				//TomasuloThreadControllor.halt();
				//Go through all the instructions in Reservation Station
				i = 1;
				for(; i < 11 ; i++) {
					ReservationStation.Instruction instruction = ReservationStation.getInstruction(i);
					//Check Register file to find the replaceable variables;
					if(instruction.Qi != 0) {
						if(RegisterFile.getTempResult(instruction.Qi)!="") {
							instruction.Vi = RegisterFile.getTempResult(instruction.Qi);
							instruction.Qi = 0;
						}
					}
					if(instruction.Qj != 0) {
						if(RegisterFile.getTempResult(instruction.Qj)!="") {
							instruction.Vj = RegisterFile.getTempResult(instruction.Qj);
							instruction.Qj = 0;
						}
					}
					
					if(instruction.Qi == 0 && instruction.Qj == 0 && !instruction.ex) {
						//if qi and qj == 0, which means all the operand of that instruction is ready, execute it.
						//if the instruction is IN, do not execute until it is committing.
						if(!instruction.opCode.equals("IN") || ReOrderBuffer.commitPointer == i) {
							String result = execute(instruction);
							//save the result in Register file tempResult table.
							RegisterFile.refreshResult(i, result);
							//set execute flag to true
							ReservationStation.getInstruction(i).ex = true;
						}
					}	
				}
				
				//commit instruction, write result back to register or memory
				//This block of code will only run if the committing instruction is executed successfully.
				if(ReservationStation.getInstruction(ReOrderBuffer.commitPointer).ex) {
					//If this is an branch instruction
					String opCode = ReservationStation.getInstruction(ReOrderBuffer.commitPointer).opCode;
					boolean result = RegisterFile.getTempResult(ReOrderBuffer.commitPointer).equals("1") ? true : false;
					switch(opCode) {
					case "JZ":
						returnBranchResult(result, ReservationStation.getInstruction(ReOrderBuffer.commitPointer));
						break;
					case "JNE":
						returnBranchResult(result, ReservationStation.getInstruction(ReOrderBuffer.commitPointer));
						break;
					case "JMA":
						returnBranchResult(result, ReservationStation.getInstruction(ReOrderBuffer.commitPointer));
						break;
					case "JGE":
						returnBranchResult(result, ReservationStation.getInstruction(ReOrderBuffer.commitPointer));
						break;
					}
					
					//if flushed, terminate the while loop and restart.
					if(flush) {
						break;
					}
					
					writeBackData(ReservationStation.getInstruction(ReOrderBuffer.commitPointer).des,RegisterFile.getTempResult(ReOrderBuffer.commitPointer));
					//clear corresponding data in two register file table
					
					//if the temp name of the destination register of this committed instruction is the index of this instruction. set register name to 0; 
					if(RegisterFile.getRegisterName(ReservationStation.getInstruction(ReOrderBuffer.commitPointer).des) == ReOrderBuffer.commitPointer) {
						RegisterFile.reNameRegister(ReservationStation.getInstruction(ReOrderBuffer.commitPointer).des, 0);
					}
					
					//clean the tempResult table
					if(ReservationStation.getInstruction(ReOrderBuffer.commitPointer).des != "") {
						RegisterFile.refreshResult(ReOrderBuffer.commitPointer, "");
					}
					
					//fetch a new instruction into reorder buffer:
					//fetch instructions index by PC
					//Set instruction address in MAR
					CPU.getInstance().getMAR().setContent(CPU.getInstance().getPC().getContent());
					CPU.cyclePlusOne();
					//uses the address in the MAR to fetch a word from cache. This fetch occurs in one cycle.
		            //The word fetched from cache is placed in the Memory Buffer Register (MBR).
		            //if it is a miss, extract from memory and store it in cache
					Cache.getInstance().cacheToMBR(CPU.getInstance().getMAR().getContent());
		            //Store instruction in ROB
					ReOrderBuffer.fetchInstruction(CPU.getInstance().getMAR().getContent(), CPU.getInstance().getMBR().getContent());
		            
		            //Branch prediction, get next instruction address
					String nextPC = DirectionPredictor.getInstance().predict(CPU.getInstance().getPC().getContent());
		            
		            //PC++
					CPU.getInstance().getPC().setContent(nextPC);
		            
		            //Decode the new Instruction into Reservation Station
		            //The contents of the Reorderbuffer are moved to the Instruction Register (IR) sequentially.
		            //IR = ROB[i]
					CPU.getInstance().getIR().setContent(ReOrderBuffer.getInstruction(ReOrderBuffer.fetchPointer));
					//Decode the instruction in IR
		            //In 1 cycle  process the instruction and use it to set several flags:
					CPU.getInstance().getTomasuloDecoder().setInstruction(ReOrderBuffer.getAddress(ReOrderBuffer.fetchPointer), CPU.getInstance().getIR().getContent());
					CPU.getInstance().getTomasuloDecoder().decode();
					ReservationStation.rsPushInstruction(ReOrderBuffer.fetchPointer,ReOrderBuffer.getAddress(ReOrderBuffer.fetchPointer));
					//Save Register's new name in Register File
					RegisterFile.reNameRegister(ReservationStation.getInstruction(ReOrderBuffer.fetchPointer).des, ReOrderBuffer.fetchPointer);
					
					
					//commit a new instruction
		            curInstruction = ReOrderBuffer.commit();
				}
			}
			if(curInstruction.equals("0000000000000000")) {
				terminate = true;
			}
		}
		
		UserPart4MainController.setStepInformation("Execute done, press next to see result");
		TomasuloThreadControllor.halt();
	}
	
	private static String execute(ReservationStation.Instruction instruction) {
		String opCode = instruction.opCode;
		String result = "opCode Error";
		switch(opCode){
			case "HLT":
				result = "";
				break;
			case "LDR":
				result = LoadAndStore.LDRTom(instruction.Vi);
				break;
			case "STR":
				result = LoadAndStore.STRTom(instruction.Vi);
				break;
			case "LDA":
				result = LoadAndStore.LDATom(instruction.Vi);
				break;
			case "LDX":
				result = LoadAndStore.LDXTom(instruction.Vi);
				break;
			case "STX":
				result = LoadAndStore.STXTom(instruction.Vi);
				break;
			case "JZ":
				result = TransferInstruction.JZTom(instruction.Vi);
				break;
			case "JNE":
				result = TransferInstruction.JNETom(instruction.Vi);
				break;
			case "JMA":
				result = TransferInstruction.JMATom();
				break;
			case "JGE":
				result = TransferInstruction.JGETom(instruction.Vi);
				break;
			case "AMR":
				result = ArithmeticInstructions.AMRTom(instruction.Vi, instruction.Vj);
				break;
			case "SMR":
				result = ArithmeticInstructions.SMRTom(instruction.Vi, instruction.Vj);
				break;
			case "AIR":
				result = ArithmeticInstructions.AIRTom(instruction.Vi, instruction.Vj);
				break;
			case "SIR":
				result = ArithmeticInstructions.SIRTom(instruction.Vi, instruction.Vj);
				break;
			case "IN":
				result = IOinstructions.INTom(instruction.Vi);
				break;
			case "OUT":
				result = IOinstructions.OUTTom(instruction.Vi);
				break;
			case "NOT":
				result = LogicalInstruction.NOTTom(instruction.Vi);
		}
		return result;
	}
	
	//Write back data
	private static void writeBackData(String destination, String data) {
		
		CPU.cyclePlusOne();
		//Instruction has no destination
		if(destination == "") {
			//do nothing
		}
		
		// TO General Purpose Register 
		else if(destination.length() == 2) {
			switch(destination) {
				case "00":
					CPU.getInstance().getR0().setContent(data);
					break;
				case "01":
					CPU.getInstance().getR1().setContent(data);
					break;
				case "10":
					CPU.getInstance().getR2().setContent(data);
					break;
				case "11":
					CPU.getInstance().getR3().setContent(data);
					break;
			}
		}
		
		//To Index Register
		else if (destination.length() == 3) {
			switch(destination) {
				case "x01":
					CPU.getInstance().getX1().setContent(data);
					break;
				case "x10":
					CPU.getInstance().getX2().setContent(data);
					break;
				case "x11":
					CPU.getInstance().getX3().setContent(data);
					break;
			}
		}
		
		//To IO Memory
		else if (destination.length() == 5) {
			IOmemory.getInstance().setContent(destination,data);
		}
		
		//To Cache and Memroy
		else if (destination.length() == 12) {
			Cache.getInstance().writeBack(destination,data);
		}
		
		else {
			EngineerConsoleController.setStepInformation("Commit Write Bake Error",false);
	        UserInterfaceController.setStepInformation("Commit Write Bake Error");
		}
	}
	
	private static void flushInstructions() {
		//reset PC
		CPU.getInstance().getPC().setContent(ReOrderBuffer.getAddress(ReOrderBuffer.commitPointer));
		//set flush flag to restart the fetch of instruction.
		flush = true;
		//clear ReOrder Buffer, Register File, Reservation Station
		ReOrderBuffer.clear();
		RegisterFile.clear();
		ReservationStation.clear();
	}
	
	private static void returnBranchResult(boolean branchResult, ReservationStation.Instruction instruction) {
		//See whether the branch prediction is correct or not
		boolean prediction = DirectionPredictor.getInstance().get(instruction.address);	
		try {
			//if correct, keep running
			if(prediction == branchResult) {
				//use the result of this jump instruction to modify the Direction Predictor.
				//Branch taken
				if(branchResult) {
					DirectionPredictor.getInstance().addOrUpdate(instruction.address, true);
				}
				//Branch not taken
				else if (!branchResult) {
					DirectionPredictor.getInstance().addOrUpdate(instruction.address, false);
				}else {
					System.out.print("returnBranchResult Error");
				}
			}
			//if not correct
			else {
				//use the result of this jump instruction to modify the Direction Predictor.
				//Branch taken
				if(branchResult) {
					DirectionPredictor.getInstance().addOrUpdate(instruction.address, true);
				}
				//Branch not taken
				else if (!branchResult) {
					DirectionPredictor.getInstance().addOrUpdate(instruction.address, false);
				}
				//Otherwise
				else {
					System.out.print("returnBranchResult Error");
				}
				//Flush all wrong instructions
				flushInstructions();
			}
		}catch(Exception e) {
			System.out.print(e);
		}
		
		
	}
}
