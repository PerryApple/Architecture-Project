package core;

import java.util.HashMap;

public class ReOrderBuffer {
	public static class Instruction{
		public String address;
		public String instruction;
		public Instruction(String address, String instruction) {
			this.address = address;
			this.instruction = instruction;
		}
	}
	
	private static HashMap<Integer, Instruction> reOrderBuffer = new HashMap<Integer, Instruction>();
	public static int fetchPointer = 0;
	public static int commitPointer = 0;

	public static void fetchInstruction(String address, String instruction) {
		fetchPointer = fetchPointer == 10 ? 1 : fetchPointer+1;
		reOrderBuffer.put(fetchPointer, new Instruction(address,instruction));
	}
	
	public static String getInstruction(int index) {
		if(reOrderBuffer.containsKey(index)) {
			return reOrderBuffer.get(index).instruction;
		}else {
			return "0000000000000000";
		}
	}
	
	public static String getAddress (int index) {
		if(reOrderBuffer.containsKey(index)) {
			return reOrderBuffer.get(index).address;
		}else {
			return "000000000000";
		}
	}
	
	public static String commit() {
		commitPointer = commitPointer == 10 ? 1 : commitPointer+1; 
		String committedInstruction = reOrderBuffer.get(commitPointer).instruction;
		return committedInstruction;
	}
	
	public static void clear() {
		reOrderBuffer.clear();
		fetchPointer = 0;
		commitPointer = 0;
	}
	
	public static HashMap getContent() {
		return  reOrderBuffer;
	}
}
