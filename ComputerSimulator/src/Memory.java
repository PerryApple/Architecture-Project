import java.util.HashMap;

public class Memory {
    //use HashMap to represent memory,key represents address,value represents data
    private HashMap<String ,String> memorySpace=new HashMap<String, String>();
    //size of the memory
    private final int size = 2048;
    //Single instance of Memory
    private static final Memory instance = new Memory();

    //constructor
    private Memory(){
        String address="000000000000";
        for(int i=0;i<size;i++){
            memorySpace.put(address,"0000000000000000");
            address=addressAddone(address);
        }
    }
    //Method to get instance
    public static Memory getInstance() {
    		return instance;
    }

    //get the content from  memory
    public String getContent(String address){
    		if(address.length() == 16) {
    			address = address.substring(4, 16);
    			return memorySpace.get(address);
    		}else if (address.length() == 12) {
    			return memorySpace.get(address);
    		}else {
    			return "Error";
    		}
    }

    //store value to the address
    public void setContent(String address,String value){
    		if(address.length() == 16) {
    			address = address.substring(4, 16);
    			memorySpace.put(address,value);
    		}else if (address.length() == 12) {
    			memorySpace.put(address,value);
    		}else {
    			System.err.println("Illegal address length!!");
    		}
        
    }

    //add one to the address to help build the HashMap
    public String addressAddone(String s1){
        int carry=1;
        for(int i=s1.length()-1;i>=0;i--){
            if(s1.charAt(i)=='0'&&carry==1){
                StringBuilder temp=new StringBuilder();
                temp.append(s1.substring(0,i));
                temp.append("1");
                if(i<s1.length()-1)temp.append(s1.substring(i+1,s1.length()));
                carry=0;
                s1=temp.toString();
            }else if(s1.charAt(i)=='1'&&carry==1){
                StringBuilder temp=new StringBuilder();
                temp.append(s1.substring(0,i));
                temp.append("0");
                if(i<s1.length()-1)temp.append(s1.substring(i+1,s1.length()));
                carry=1;
                s1=temp.toString();
            }

            if(carry==0) break;
        }
        return s1;
    }
    
    //clear all memory unit
    public void clear() {
    	String address="000000000000";
        for(int i=0;i<size;i++){
            memorySpace.put(address,"0000000000000000");
            address=addressAddone(address);
        }
    }

//    public static void main(String[] args){
//        Memory memory = Memory.getInstance();
//
//    }
}
