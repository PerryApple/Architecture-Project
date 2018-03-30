package core;

import java.util.ArrayList;
import java.util.List;

//Encode the english character to binary and save in memory
public class CharEncoder {

	//The storage of the characters start at "address"
	public static void saveInMemory(String address, String[] s) {
		//Convert all characters to lower case
		for(int k = 0; k < s.length; k++) {
			s[k] = s[k].toLowerCase();
			List<String> encoded = encode(s[k]);
			//Store in memory
			for(int i = 0; i < encoded.size() ; i++) {
				Memory.getInstance().setContent(address, encoded.get(i));
				address = CPU.getInstance().getALU().add(address, CPU.alignment("1"));
			}
		}
	}
	private static List encode(String s) {
		List<String> encoded = new ArrayList<String>();
		for(int i = 0; i < s.length(); i++) {
			//Convert each character to binary String based on ASCII coding.
			encoded.add(CPU.alignment(Integer.toBinaryString(Integer.valueOf((int)s.charAt(i)))));
		}
		//Add a 
		encoded.add("0000000000001010");
		return encoded;
	}
}
