package core;

import java.util.HashMap;

public class RegisterFile {
	private static HashMap<String, Integer> registerFile = new HashMap<String, Integer> (){
		{
			put("00",0);
			put("01",0);
			put("10",0);
			put("11",0);
			put("x01",0);
			put("x10",0);
			put("x11",0);
		}
	};
	
	private static HashMap<Integer,String> tempResult = new HashMap<> () {
		{
			for(int i = 1; i < 11 ; i++) {
				put(i,"");
			}
		}
	};
	
	public static void reNameRegister(String registerName, Integer newName) {
		registerFile.put(registerName, newName);
	}
	public static Integer getRegisterName(String register) {
		if(registerFile.containsKey(register)) {
			return registerFile.get(register);
		}else {
			return 0;
		}
	}
	public static boolean containsName(String register) {
		return registerFile.containsKey(register);
	}
	
	public static String getTempResult(Integer index) {
		if(tempResult.containsKey(index)) {
			return tempResult.get(index);
		}else {
			return "";
		}
	}
	public static void refreshResult(Integer index, String newValue) {
		tempResult.put(index, newValue);
	}
	
	public static void clear() {
		//clear tempResult
		for(int i = 1 ; i < 11 ; i++) {
			tempResult.put(i, "");
		}
		
		//clear registerFile
		registerFile.clear();
		registerFile.put("00",0);
		registerFile.put("01",0);
		registerFile.put("10",0);
		registerFile.put("11",0);
		registerFile.put("x01",0);
		registerFile.put("x10",0);
		registerFile.put("x11",0);
	}
	
	public static HashMap getRegisterFileContent(){
		return registerFile;
	}
	public static HashMap getTempResultContent(){
		return tempResult;
	}
}
