public class LoadAndStore extends ISA{
    private static String stepInformation;
    // record if we should update the memory context in  Monitor
    private static boolean memoryInformation;

    public static void LDR(){
        //set MAR register
        cpu.getMAR().setContent(cpu.getIAR().getContent());
        stepInformation="Execute:MAR<=IAR";
        sendStepInformation();
        Halt.halt();
        cpu.cyclePlusOne();

        //get the content in memory using address in MAR, and load it to MBR.
        String address = cpu.getMAR().getContent();
        String data = Cache.getInstance().getCacheLine(address);
        cacheToMBR(address,data);

        /*cpu.getMBR().setContent(cpu.getMemory().getContent(cpu.getMAR().getContent()));
        memoryInformation=true;
        stepInformation="Execute:MBR<=Memory[MAR]";
        sendStepInformation();
        Halt.halt();
        cpu.cyclePlusOne();*/

        //Execute the operation move data to IRR
        cpu.getIRR().setContent(cpu.getMBR().getContent());
        stepInformation="Execute:IRR<=MBR";
        sendStepInformation();
        Halt.halt();
        cpu.cyclePlusOne();

        //select the register and put the data into register
        switch (R){
            case"00":
                cpu.getR0().setContent(cpu.getIRR().getContent());
                break;
            case "01":
                cpu.getR1().setContent(cpu.getIRR().getContent());
                break;
            case "10":
                cpu.getR2().setContent(cpu.getIRR().getContent());
                break;
            case "11":
                cpu.getR3().setContent(cpu.getIRR().getContent());
        }
        stepInformation="Execute:Register<=IRR";
        sendStepInformation();
        Halt.halt();
        cpu.cyclePlusOne();
    }

    public static void STR(){
        //set MAR register
        cpu.getMAR().setContent(cpu.getIAR().getContent());
        stepInformation="Execute:MAR<=IAR";
        sendStepInformation();
        Halt.halt();
        cpu.cyclePlusOne();
        
        // select register and put data into IRR which takes one cycle
        switch (R){
            case "00":
                cpu.getIRR().setContent(cpu.getR0().getContent());
                break;
            case "01":
                cpu.getIRR().setContent(cpu.getR1().getContent());
                break;
            case "10":
                cpu.getIRR().setContent(cpu.getR2().getContent());
                break;
            case "11":
                cpu.getIRR().setContent(cpu.getR3().getContent());
                break;
        }
        stepInformation=("Execute:IRR<=Register");
        sendStepInformation();
        Halt.halt();
        cpu.cyclePlusOne();

        //move data from IRR to MBR which takes one cycle
        cpu.getMBR().setContent(cpu.getIRR().getContent());
        stepInformation="Execute:MBR<=IRR";
        sendStepInformation();
        Halt.halt();
        cpu.cyclePlusOne();

        //put data into cache and then write back to memory which takes one cycle
        String address = cpu.getMAR().getContent();
        String data = cpu.getMBR().getContent();
        Cache.getInstance().writeBack(address,data);
        memoryInformation=true;
        stepInformation=("Execute:Memory[MAR]<=Cache<=MBR");
        sendStepInformation();
        Halt.halt();
        cpu.cyclePlusOne();//??????????????? how many?????????????
    }

    public static void LDA(){
        //put EA into Register from IAR
        switch (R){
            case "00":
                cpu.getR0().setContent(cpu.getIAR().getContent());
                break;
            case "01":
                cpu.getR1().setContent(cpu.getIAR().getContent());
            case "10":
                cpu.getR2().setContent(cpu.getIAR().getContent());
            case "11":
                cpu.getR3().setContent(cpu.getIAR().getContent());
        }
        stepInformation="Execute:Register<=IAR";
        sendStepInformation();
        Halt.halt();
        cpu.cyclePlusOne();
    }

    public static void LDX(){
        //set MAR register
        cpu.getMAR().setContent(cpu.getIAR().getContent());
        stepInformation="Execute:MAR<=IAR";
        sendStepInformation();
        Halt.halt();
        cpu.cyclePlusOne();
        //fetch the data from cache according to MAR
        String address = cpu.getMAR().getContent();
        String data = Cache.getInstance().getCacheLine(address);
        cacheToMBR(address,data);

        //put data into IRR
        cpu.getIRR().setContent(cpu.getMBR().getContent());
        stepInformation="Execute:IRR<=MBR";
        sendStepInformation();
        Halt.halt();
        cpu.cyclePlusOne();
        //put the data from IRR into index register
        switch (X){
            case "01":
                cpu.getX1().setContent(cpu.getIRR().getContent());
                break;
            case "10":
                cpu.getX2().setContent(cpu.getIRR().getContent());
                break;
            case "11":
                cpu.getX3().setContent(cpu.getIRR().getContent());
        }
        stepInformation="Execute:Register<=IRR";
        sendStepInformation();
        Halt.halt();
        cpu.cyclePlusOne();
    }

    public static void STX(){
        //set MAR register
        cpu.getMAR().setContent(cpu.getIAR().getContent());
        stepInformation="Execute:MAR<=IAR";
        sendStepInformation();
        Halt.halt();
        cpu.cyclePlusOne();
        //move data from index register to IRR'
        switch (X){
            case "01":
                cpu.getIRR().setContent(cpu.getX1().getContent());
                break;
            case "10":
                cpu.getIRR().setContent(cpu.getX2().getContent());
                break;
            case "11":
                cpu.getIRR().setContent(cpu.getX3().getContent());
                break;
        }
        stepInformation="Execute:IRR<=Register";
        sendStepInformation();
        Halt.halt();
        cpu.cyclePlusOne();
        //move data from IRR to MBR
        cpu.getMBR().setContent(cpu.getIRR().getContent());
        stepInformation="Execute:MBR<=IRR";
        sendStepInformation();
        Halt.halt();
        cpu.cyclePlusOne();
        //move data from MBR to memory
        String address = cpu.getMAR().getContent();
        String data = cpu.getMBR().getContent();
        Cache.getInstance().writeBack(address,data);
        memoryInformation=true;
        stepInformation="Execute:Memory[MAR]<=Cache<=MBR";
        sendStepInformation();
        Halt.halt();
        cpu.cyclePlusOne();
    }

    public static void cacheToMBR(String address,String data){
        if(data.equals("miss")){
            data = Cache.getInstance().getIfMiss(address);
            cpu.getMBR().setContent(data);
            memoryInformation = true;
            stepInformation = "Execute:Cache miss,MBR<=Cache<=Memory[MAR]";
            sendStepInformation();
            Halt.halt();
            cpu.cyclePlusOne(); //??????????????? add how many ???????????????
        }else{
            //hit , and store the data in MBR
            cpu.getMBR().setContent(data);
            memoryInformation = true;
            stepInformation = "Execute:Cache hit, MBR<=Cache";
            sendStepInformation();
            Halt.halt();
            cpu.cyclePlusOne();//??????????????? add how many ????????????????
        }
    }

    public static void  sendStepInformation(){
        Monitor.setStepInformation(stepInformation,memoryInformation);
    }
}
