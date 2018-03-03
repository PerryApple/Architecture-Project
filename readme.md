# Simulated Computer - Architecture Project

## 1. Structure

### Register:

Register Interface(Register.java),

then those register class implement the Register Interface:

1. Program Counter Register(12 bits) contains the address of the nextinstruction to be executed(PC.java)
2. Condition Code(4bits) set when arithmetic/logical operations are executed(CC.java)
3. Divider Register to store the divider of the DVD instruction(DR)
4. General Program Register(GPR.java)
5. Internal Address Register(IAR.java)
6. Instruction Register(IR.java)
7. Internal Result Register which is used to temporarily store calculation results(IRR.java)
8. Memory Address Register(MAR.java)
9. Memory Buffer Register(MBR.java)
10. Reserved Register MFR, Only one instance ofthis class exist in computer(MFR.java)
11. Multiplicand Register MLR(MLR.java)
12. Register which stores the product in instruction "MLT"(PR.java)
13. Quotient Register(QR.java)
14. Index Register(X.java)
15. Temporary Register to store operands forcalculation in ALU(Y.java)
16. Z Register to store the result calculated in ALU(Z.java)

### ISA

ISA implementd in ISA.java is used to get the make decision which instuction is executed.

Implementation of arithmetic instructions includes add, substract, multiply and divide(ArithmeticInstructions.java),

Implementation of load and store instruction(LoadAndStore.java),

Implementation of logical instruction(LogicalInstruction.java),



### Controler

Ued to control the process in the system which is implemented in controler.java

### CPU

It includes all registers, ALU, Memory, cycle and Cache implemented in CPU.java

### Decoder

Implemented in Decoder.java, and aim to decode the instruction sent by I/O

### Memory



### Cache

Fully associated cache implemented in Cache.java

```
public class Cache {
    //According to address ,return data
    public String getdata(String address){
        ...
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

    public  void cacheToMBR(String address){
        String data = getdata(address);
        if(data.equals("miss")){
            data = Cache.getInstance().getIfMiss(address);
            CPU.getInstance().getMBR().setContent(data);
            CenterPaneController.setStepInformation("Execute:Cache miss,MBR<=Cache<=Memory[MAR]",true);
            Halt.halt();
            CPU.getInstance().cyclePlusOne(); //??????????????? add how many ???????????????
        }else{
            //hit , and store the data in MBR
            CPU.getInstance().getMBR().setContent(data);
            CenterPaneController.setStepInformation("Execute:Cache hit, MBR<=Cache",true);
            Halt.halt();
            CPU.getInstance().cyclePlusOne();//??????????????? add how many ????????????????
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
```

### Addressing

Addressing function implemented in the Addressing.java, provide the way to calculate the effective address.

Core Functions:

```
public class Addressing {
	...
    public static void getEffectiveAddress(String x, String i, String address){
    	...
    	// direct addressing
    	if (I=="0"){
            // no indexing
            if X=="00", EA = Address
            // indexing
            if X==1..3, EA = c(X) + A
    	}
    	// indirect addressing
    	else if (I=="1"){
            // no indexing
            if X=="00", EA = c(Address)
            // indexing
            if X==1..2, EA = c(c(X)+address)
    	}
    }

    public static void sendStepInformation(){
    	// Send Information to GUI
	}
}
```

### ALU

Arithmetic logic unit is implemented in the ALU.java, provide addition, minus, compareTwo, logical OR, logical AND and logical NOT.

### Halt

Multi-thread to block to main thread(in ordert to parition the process of one instuction to several parts and show the procedure on the GUI).

