package core;

import java.util.LinkedList;
import java.util.Queue;

import gui.controllers.CenterPaneController;

//fully associated cache
public class Cache {
    //number of lines
    private static final int numOfLine = 16;
    //Cache Line
    private  Queue<CacheLine> cacheLines;
    //get instance
    private static final Cache instance = new Cache();

    public static Cache getInstance(){ return instance; }

    private Cache(){
        cacheLines = new LinkedList<>();
        for(int i=0;i<numOfLine;i++){
            cacheLines.add(new CacheLine());
        }
    }
    //According to address ,return data
    public String getdata(String address){
        if(address.length()==16) address = address.substring(4,16);
        String tag = address.substring(0,10);
        int offset = Integer.valueOf(address.substring(10,12),2);
        for(CacheLine cacheLine : cacheLines){
            if(cacheLine.getValid().equals("1")&&cacheLine.getTag().equals(tag)){
                return cacheLine.getBlock(offset);
            }
        }
        return "miss";
    }
    //if miss get data from memory
    public String getIfMiss(String address){
        // the begin address's last two bits are zeros
        if(address.length()==16) address = address.substring(4,16);
        String res ="";
        String tag = address.substring(0,10);
        int dataoffset = Integer.valueOf(address.substring(10,12),2);
        CacheLine cacheLine = new CacheLine();
        cacheLine.setValid(1);
        cacheLine.setTag(tag);
        for(int i=0;i<4;i++){
            String offset = Integer.toBinaryString(i);
            if(offset.length()<2) {
            		offset = "0"+offset;
            }
            String addedAddress = tag + offset;
            String data = Memory.getInstance().getContent(addedAddress);
            //make res equals to data if i equals to dataoffset
            if(dataoffset==i) {
            		res = data;
            }
            //set blocks
            cacheLine.setBlock(i,data);
        }
        //get first-in data out
        cacheLines.remove();
        //put cacheline into queue
        cacheLines.add(cacheLine);
        return getdata(address);
    }

    public  void cacheToMBR(String address){
        String data = getdata(address);
        if(data.equals("miss")){
            data = Cache.getInstance().getIfMiss(address);
            CPU.getInstance().getMBR().setContent(data);
            CenterPaneController.setStepInformation("Execute:Cache miss, MBR<=Cache<=Memory[MAR]",true);
			CPU.cyclePlusOne();
            Halt.halt();
        }else{
            //hit , and store the data in MBR
            CPU.getInstance().getMBR().setContent(data);
            CenterPaneController.setStepInformation("Execute:Cache hit, MBR<=Cache",true);
			CPU.cyclePlusOne();
            Halt.halt();
        }
    }
    
    //Cache to MBR without halt
    public  void cacheToMBRNHLT(String address){
        String data = getdata(address);
        if(data.equals("miss")){
            data = Cache.getInstance().getIfMiss(address);
            CPU.getInstance().getMBR().setContent(data);
			CPU.cyclePlusOne();
        }else{
            //hit , and store the data in MBR
            CPU.getInstance().getMBR().setContent(data);
			CPU.cyclePlusOne();
        }
    }


    //write back data
    public void writeBack(String address,String data){
     	if(address.length()==16) address = address.substring(4,16);
        String tag = address.substring(0,10);
        int offset = Integer.valueOf(address.substring(10,12),2);
        boolean done = false;
        for(CacheLine cacheLine : cacheLines){
            // if it exists in cache, just update it in the cache and memory
            if(cacheLine.getTag().equals(tag)){
               cacheLine.setBlock(offset,data);
               Memory.getInstance().setContent(address, data);
               done = true;
            }
        }
        //it doesn't exist in cache , so write it to cache and memory
        if(!done){
            CacheLine cacheLine = new CacheLine();
            cacheLine.setValid(1);
            cacheLine.setTag(tag);
            for(int i=0;i<4;i++){
                String addedOffset = Integer.toBinaryString(i);
                if(addedOffset.length()<2) addedOffset = "0"+addedOffset;
                String addedAddress = tag + addedOffset;
                String writeData = "";
                if(offset!=i){
                    writeData = Memory.getInstance().getContent(addedAddress);
                }else{
                    writeData = data;
                }
                //set blocks
                cacheLine.setBlock(i,data);
            }
            //get first-in data out
            cacheLines.remove();
            //put cacheline into queue
            cacheLines.add(cacheLine);
            //write back to memory
            Memory.getInstance().setContent(address,data);
        }
    }

    public void clearCache(){
        for(int i=0;i<numOfLine;i++){
            cacheLines.remove();
        }
        for(int i=0;i<numOfLine;i++){
            cacheLines.add(new CacheLine());
        }
    }

    public Queue<CacheLine> getCacheLines() {
        return cacheLines;
    }
}