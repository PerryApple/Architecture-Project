import java.util.LinkedList;
import java.util.Queue;

//fully associated cache
public class Cache {
    //number of lines
    private static final int numOfLine = 16;
    //Cache Line
    private  Queue<CacheLine> cacheLines;
    //get instance
    private static final Cache instance = new Cache();

    private Cache(){
        cacheLines = new LinkedList<>();
        for(int i=0;i<numOfLine;i++){
            cacheLines.add(new CacheLine());
        }
    }
    //According to address ,return data
    public String getCacheLine(String address){
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
        for(int i=0;i<4;i++){
            String offset = Integer.toBinaryString(i);
            if(offset.length()<2) offset = "0"+offset;
            String addedAddress = tag + offset;
            String data = Memory.getInstance().getContent(addedAddress);
            //make res equals to data if i equals to dataoffset
            if(dataoffset==i) res = data;
            //set blocks
            cacheLine.setBlock(i,data);
        }
        //get first-in data out
        cacheLines.remove();
        //put cacheline into queue
        cacheLines.add(cacheLine);
        return res;
    }
    //write back data
    public void writeBack(String address,String data){
        String tag = address.substring(0,10);
        int offset = Integer.valueOf(address.substring(10,12),2);
        boolean done = false;
        for(CacheLine cacheLine : cacheLines){
            if(cacheLine.getTag().equals(tag)){
               cacheLine.setBlock(offset,data);
               done = true;
            }
        }
        //it doesn't exist in cache , so write it in cache
        if(!done){
            CacheLine cacheLine = new CacheLine();
            cacheLine.setValid(1);
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

    public static Cache getInstance(){ return instance; }

}
class CacheLine{
    //valid bit,if valid equals to one that means this line has data else if equals to zero then no data
    private String valid;
    //blocks every line has n blocks
    private String[] blocks;
    private String tag;
    private static final int n =4;
    public CacheLine(){
        valid = "0";
        tag = "0000000000";
        blocks = new String[n];
        for(int i=0;i<4;i++){
            blocks[i]="0000000000000000";
        }
    }
    //set valid bit
    public void setValid(int x){
        valid = String.valueOf(x);
    }
    //set tag bits
    public void setTag(String tag){
        if(tag.length()==10){
            this.tag = tag;
        }else{
            System.out.println("tag length error");
        }
    }
    //set blocks
    public void setBlock(int i,String data){
        blocks[i] = data;
    }
    //get tag bits
    public String getTag(){
        return tag;
    }
    //get valid bit
    public String getValid(){
        return valid;
    }
    //get block at offset i
    public String getBlock(int i){
        return blocks[i];
    }
}
