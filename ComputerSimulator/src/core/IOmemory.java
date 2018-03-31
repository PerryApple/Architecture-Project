package core;

import java.util.HashMap;
import gui.controllers.*;

import gui.controllers.UserInterfaceController;
//I/O mapped I/O it has separated i/o memory space
public class IOmemory {
    //use HashMap to represent IOmemory,key represents address,value represents data
    private HashMap<String ,Device> IOmemorySpace=new HashMap<String, Device>();
    //size of the IOmemory
    private final int size = 32;
    //Single instance of IOMemory
    private static final IOmemory instance = new IOmemory();

    //constructor
    private IOmemory(){
        String address="00000";
        for(int i=0;i<size;i++){
            Device m = new Device();
            IOmemorySpace.put(address,m);
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
            return IOmemorySpace.get(address).getValue();
        }else {
            return "Error";
        }
    }

    //store value to the address
    public void setContent(String address,String value){
       if(address.length()==5){
           IOmemorySpace.get(address).setValue(value);
        }else {
            System.err.println("Illegal address length!!");
        }
    }

    //get devID status
    public String getStatus(String address){
        if(address.length()==5){
          return IOmemorySpace.get(address).getStatus();
        }else {
            return "Error";
        }
    }
    //set devID status
    public void setStatus(String address,String status){
        if(address.length()==5){
            IOmemorySpace.get(address).setStatus(status);
        }else {
            System.err.println("Illegal address length!!");
        }
    }

    public void clearIOmemory(){
        String address="00000";
        for(int i=0;i<size;i++){
            IOmemorySpace.get(address).setValue("0000000000000000");
            IOmemorySpace.get(address).setStatus("0");
            address=Memory.getInstance().addressAddone(address);
        }
    }
}

class Device{
    private String value;
    private String status;
    public Device(){
        value = "0000000000000000";
        status = "0";
    }
    //check the length of value and set value
    public void setValue(String value){
        if(value.length()<16){
           value = CPU.alignment(value);
        }else if(value.length()>16){
            value = value.substring(0,17);
        }
        this.value = value;
    }

    //get value
    public String getValue(){
        if(value.length()<16){
            value = CPU.alignment(value);
        }
        return value;
    }

    //set status
    public void setStatus(String s){
        if(s.length()!=1){
            System.out.println("status code length error");
        }else {
            status = s;
        }
    }

    //get status
    public String getStatus(){
        return status;
    }


}
