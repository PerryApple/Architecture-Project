package core;

public class FaultHandler {

    private static final FaultHandler instance = new FaultHandler();
    //private constructor
    private FaultHandler() {
    }
    // Method to get instance
    public static FaultHandler getInstance() {
        return instance;
    }

    //handle the fault
    public  void faultHandle( Fault f){
        //set MFR
        setMFR(f);
        CPU.cyclePlusOne();
        Halt.halt();
        //put PC in address(4)
        CPU.getInstance().getMAR().setContent("000000000100");
        CPU.cyclePlusOne();
        Halt.halt();
        CPU.getInstance().getMBR().setContent(CPU.getInstance().getPC().getContent());
        CPU.cyclePlusOne();
        Halt.halt();
        Cache.getInstance().writeBack(CPU.getInstance().getMAR().getContent(),CPU.getInstance().getMBR().getContent());
        CPU.cyclePlusOne();
        Halt.halt();
        //put MSR in address(5)
        CPU.getInstance().getMAR().setContent("000000000101");
        CPU.cyclePlusOne();
        Halt.halt();
        CPU.getInstance().getMBR().setContent(CPU.getInstance().getMSR().getContent());
        CPU.cyclePlusOne();
        Halt.halt();
        Cache.getInstance().writeBack(CPU.getInstance().getMAR().getContent(),CPU.getInstance().getMBR().getContent());
        CPU.cyclePlusOne();
        Halt.halt();
        //fetch the address from location 1 into PC
        CPU.getInstance().getMAR().setContent("000000000001");
        CPU.cyclePlusOne();
        Halt.halt();
        Cache.getInstance().cacheToMBR(CPU.getInstance().getMAR().getContent());
        CPU.cyclePlusOne();
        Halt.halt();
        CPU.getInstance().getPC().setContent(CPU.getInstance().getMBR().getContent());
        CPU.cyclePlusOne();
        Halt.halt();
        //set fault flag
        Controler.getInstance().fault = true;
        //set jump flag, avoid unnecessary PC++
        Controler.getInstance().jump = true;
    }

    public void faultHandleNHLT( Fault f){
        //set MFR
        setMFR(f);
        //put PC in address(4)
        CPU.getInstance().getMAR().setContent("000000000100");

        CPU.getInstance().getMBR().setContent(CPU.getInstance().getPC().getContent());

        Cache.getInstance().writeBack(CPU.getInstance().getMAR().getContent(),CPU.getInstance().getMBR().getContent());

        //put MSR in address(5)
        CPU.getInstance().getMAR().setContent("000000000101");

        CPU.getInstance().getMBR().setContent(CPU.getInstance().getMSR().getContent());

        Cache.getInstance().writeBack(CPU.getInstance().getMAR().getContent(),CPU.getInstance().getMBR().getContent());

        //fetch the address from location 1 into PC
        CPU.getInstance().getMAR().setContent("000000000001");

        Cache.getInstance().cacheToMBR(CPU.getInstance().getMAR().getContent());

        CPU.getInstance().getPC().setContent(CPU.getInstance().getMBR().getContent());

        //set fault flag
        Controler.getInstance().fault = true;
        //set jump flag, avoid unnecessary PC++
        Controler.getInstance().jump = true;
    }

    public void backAfterHandle(){
        //Move the content of memory location 4 to register PC
        CPU.getInstance().getMAR().setContent(CPU.alignment("000000000100"));
        CPU.cyclePlusOne();
        Halt.halt();
        Cache.getInstance().cacheToMBR(CPU.getInstance().getMAR().getContent());
        CPU.cyclePlusOne();
        Halt.halt();
        CPU.getInstance().getPC().setContent(CPU.getInstance().getMBR().getContent());
        CPU.cyclePlusOne();
        Halt.halt();
        Controler.getInstance().fault =false;
    }

    public void backAfterHandleNHLT(){
        //Move the content of memory location 4 to register PC
        CPU.getInstance().getMAR().setContent(CPU.alignment("000000000100"));

        Cache.getInstance().cacheToMBR(CPU.getInstance().getMAR().getContent());

        CPU.getInstance().getPC().setContent(CPU.getInstance().getMBR().getContent());

        Controler.getInstance().fault =false;
    }

    //set MFR
    public void setMFR(Fault f){
        CPU.getInstance().getMFR().setContent(CPU.alignment(Integer.toBinaryString(f.faultId)));
    }

}

class Fault{
    public int faultId;

    public String faultMessage;

    public Fault(int id,String faultMessage){
        faultId = id;
        this.faultMessage = faultMessage ;
    }

}
