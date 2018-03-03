package core;

public class CacheLine {
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
