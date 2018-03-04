package core;

import java.util.HashMap;
//I/O mapped I/O it has separated i/o memory space
public class IOmemory {
    //use HashMap to represent IOmemory,key represents address,value represents data
    private HashMap<String ,String> IOmemorySpace=new HashMap<String, String>();
    //size of the IOmemory
    private final int size = 32;
    //Single instance of IOMemory
    private static final IOmemory instance = new IOmemory();

    //constructor
    private IOmemory(){
        String address="00000";
        for(int i=0;i<size;i++){
            IOmemorySpace.put(address,"0000000000000000");
            address=Memory.getInstance().addressAddone(address);
        }
    }
    //Method to get instance
    public static IOmemory getInstance() {
        return instance;
    }

    //get the content from  memory
    public String getContent(String address){
        if(address.length()==5){
            return IOmemorySpace.get(address);
        }else {
            return "Error";
        }
    }

    //store value to the address
    public void setContent(String address,String value){
       if(address.length()==5){
           IOmemorySpace.put(address,value);
        }else {
            System.err.println("Illegal address length!!");
        }
    }

    public void clearIOmemory(){
        String address="00000";
        for(int i=0;i<size;i++){
            IOmemorySpace.put(address,"0000000000000000");
            address=Memory.getInstance().addressAddone(address);
        }
    }
}
