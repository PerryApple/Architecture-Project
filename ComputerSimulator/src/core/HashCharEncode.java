package core;
import java.util.HashMap;

public class HashCharEncode {
	public static HashMap<String, Integer> complexCodeMap = new HashMap<String, Integer>();
	public static void saveInMemory(String address, String[] s) {
		for(int i = 0 ; i < s.length ; i++) {
			//convert all word to lower case
			s[i] = s[i].toLowerCase();
			//split by " "
			String[] singleWords = s[i].split(" ");
			for(int j = 0 ; j < singleWords.length ; j++) {
				//delete all characters which is not a-z.
				singleWords[j] = singleWords[j].replaceAll("[^a-z\\s]","");
				//get the word's code and store it in memory.
				if(complexCodeMap.containsKey(singleWords[j])) {
					String wordCode = Integer.toBinaryString(complexCodeMap.get(singleWords[j]));
					Cache.getInstance().writeBack(address, CPU.alignment(wordCode));
					//Address++
					address = CPU.getInstance().getALU().add(address, CPU.alignment("1"));
				}
			}
			//add a "enter" character to the end of a sentence. code of "enter" character is "0000000000001010"
			Cache.getInstance().writeBack(address, CPU.alignment("1010"));
			//Address++
			address = CPU.getInstance().getALU().add(address, CPU.alignment("1"));
		}
		//add an "END" flag at the end of this paragraph.
		Cache.getInstance().writeBack(address, CPU.alignment("1011"));
	}

	public static void encode(String[] s) {
		//enCoding will begin at code 32.
		int code = 32;
		for(int i = 0 ; i < s.length ; i++) {
			//convert all word to lower case
			s[i] = s[i].toLowerCase();
			//split by " "
			String[] singleWords = s[i].split(" ");
			for(int j = 0 ; j < singleWords.length; j++) {
				//delete all characters which is not a-z.
				singleWords[j] = singleWords[j].replaceAll("[^a-z\\s]","");
				if(!complexCodeMap.containsKey(singleWords[j])) {
					complexCodeMap.put(singleWords[j], Integer.valueOf(code));
					code += (int)(Math.random()*8 + 1);
				}
			}
		}
	}
	
	public static String getCode(String s) {
		//delete all characters which is not a-z.
		s = s.toLowerCase();
		s = s.replaceAll("[^a-z\\s]","");
		if(complexCodeMap.containsKey(s)) {
			return Integer.toBinaryString(complexCodeMap.get(s));
		}else {
			return CPU.alignment("1");
		}
	}
}
