package core;

import java.util.HashMap;

public class ReservationStation {
	public static class Instruction{
		public String address;
		public String opCode;
		//first operand
		public Integer Qi;
		public String Vi;
		//second operand
		public Integer Qj;
		public String Vj;
		//destination
		public String des;
		//flags
		//executed?
		public boolean ex = false;

		public Instruction(String address, String opCode, Integer qi, String vi, Integer qj, String vj, String des) {
			this.address = address;
			this.opCode = opCode;
			this.Qi = qi;
			this.Vi = vi;
			this.Qj = qj;
			this.Vj = vj;
			this.des = des;
		}
	}
	private static HashMap<Integer, Instruction> reservationStation = new HashMap<Integer, Instruction>();
	private static Instruction instruction = new Instruction("address", "opCode", 0, "vi", 0, "vj", "des");
	
	public static void newInstruction(String address, String opCode, Integer qi, String vi, Integer qj, String vj, String des) {
		instruction.address = address;
		instruction.opCode = opCode;
		instruction.Qi = qi;
		instruction.Vi = vi;
		instruction.Qj = qj;
		instruction.Vj = vj;
		instruction.des = des;
	}
	
	//
	public static void rsPushInstruction(Integer index, String address) {
		Instruction newInstruction = new Instruction(address, instruction.opCode, instruction.Qi, instruction.Vi, instruction.Qj, instruction.Vj, instruction.des);
		reservationStation.put(index, newInstruction);
		if(newInstruction.des != "") {
			RegisterFile.reNameRegister(newInstruction.des, index);
		}
	}
	
	public static Instruction getInstruction(int index) {
		return reservationStation.get(index);
	}
	
	public static void clear() {
		reservationStation.clear();
	}
	
	public static HashMap getContent() {
		return reservationStation;
	}
}
