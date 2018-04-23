package core;
import gui.controllers.CenterPaneController;

public class Controler {
    //Singleton Pattern
    private static final Controler instance = new Controler();
    private  String stepInformation="";
    private Controler() {}
    //记录是否要在BTB和DP中放入新的值
    private boolean firstBranchPrediction = false;
    //if jump == true PC<-EA
    public boolean jump = false;
    //if hlt == true halt the process
    public boolean hlt = false;
    //Flag single step, if true, execute with single step
    public boolean singleStep = true;
    public boolean end = false;
    public boolean trap = false;
    public boolean fault = false;
    public static Controler getInstance() {
        return instance;
    }
    //Main process 

    public void processSingleStep(){
        Halt.halt();
        //Instruction Fetch start here
        //MAR = PC
        hlt = false;
        while(true){
            //if fault handled then junmp back
            if(fault) FaultHandler.getInstance().backAfterHandle();
            //initialize jump
            jump = false;
            stepInformation=("Instruction Fetch:MAR<=PC");
            CPU.getInstance().getCC().setContent("0000");
            CenterPaneController.instructionNum++;//Monitor shows what instruction is processing
            CPU.getInstance().getMAR().setContent(CPU.getInstance().getPC().getContent());
            sendStepInformation();
            CPU.cyclePlusOne();
            Halt.halt();

            //uses the address in the MAR to fetch a word from cache. This fetch occurs in one cycle.
            //The word fetched from cache is placed in the Memory Buffer Register (MBR).
            //if it is a miss, extract from memory and store it in cache
            Cache.getInstance().cacheToMBR(CPU.getInstance().getMAR().getContent());

            //Instruction decode start here
            //The contents of the Memory Buffer Register (MBR) are moved to the Instruction Register (IR).
            //IR = MBR
            CPU.getInstance().getIR().setContent(CPU.getInstance().getMBR().getContent());
            stepInformation=("Instruction Fetch:IR<=MBR");
            sendStepInformation();
            CPU.cyclePlusOne();
            Halt.halt();
            //Decode the instruction in IR
            //In 1 cycle  process the instruction and use it to set several flags:
            CPU.getInstance().getDecoder().setInstruction(CPU.getInstance().getIR().getContent());
            CPU.getInstance().getDecoder().decode();
            stepInformation=("Instruction Decode...");
            sendStepInformation();
            CPU.cyclePlusOne();
            Halt.halt();

            //Identify the instruction and execute it.
            //The specific steps of different instructions are handled in the ISA class and its subclasses
            //Operand Fetch, Execute and Result Store will be done in specific instruction method according to the instruction.
            CPU.getInstance().getDecoder().identify();
            //if the instruntion is HALT then break the while loop
            if(hlt) break;
            //Next Instruction
            //PC++
            //get content from pc, add one in ALU, store the result in Z temporarily and send it to pc.
            if(!jump){
                CPU.getInstance().getZ().setContent(CPU.getInstance().getALU().addPC(CPU.getInstance().getPC()));
                stepInformation=("Next Instruction: Z <= PC++ (ALU)");
                sendStepInformation();
                CPU.cyclePlusOne();
                Halt.halt();

                CPU.getInstance().getPC().setContent(CPU.getInstance().getZ().getContent());
                stepInformation=("PC <= Z");
                sendStepInformation();
                CPU.cyclePlusOne();
                Halt.halt();
            }
        }
        CenterPaneController.setStepInformation("Execute done, press next to exit", false);
        this.end = true;
        Halt.halt();
    }
    
    public void processByInstruction(){
        Halt.halt();
        //Instruction Fetch start here
        //MAR = PC
        hlt = false;
        while(true){
            //initialize jump
        		//Halt.halt();
            jump = false;
            CPU.getInstance().getCC().setContent("0000");
        		CenterPaneController.instructionNum++;//Monitor shows what instruction is processing
            CPU.getInstance().getMAR().setContent(CPU.getInstance().getPC().getContent());
            CPU.cyclePlusOne();

            //uses the address in the MAR to fetch a word from cache. This fetch occurs in one cycle.
            //The word fetched from cache is placed in the Memory Buffer Register (MBR).
            //if it is a miss, extract from memory and store it in cache
            Cache.getInstance().cacheToMBRNHLT(CPU.getInstance().getMAR().getContent());

            //Instruction decode start here
            //The contents of the Memory Buffer Register (MBR) are moved to the Instruction Register (IR).
            //IR = MBR
            CPU.getInstance().getIR().setContent(CPU.getInstance().getMBR().getContent());
            CPU.cyclePlusOne();
//======================================================================================================================
            //branch prediction
            String op=CPU.getInstance().getIR().getContent().substring(0,3);
            if(op.equals("001")){
                //transfer instruction
                String target = BranchTargetBuffer.getInstance().getTarget(CPU.getInstance().getIR().getContent());
                int isTaken = DirectionPredictor.getInstance().get(CPU.getInstance().getIR().getContent());
                if(isTaken==1){
                    //taken
                    stepInformation ="Predicted Next Address: " + target;
                }else if(isTaken ==0){
                    //not taken
                    stepInformation = "Predicted Next Address: " + CPU.getInstance().getALU().add(PC.getInstance().getContent(),"1");
                }else {
                    //第一次遇到,预测taken
                    stepInformation ="Predicted Next Address: " + CPU.getInstance().getALU().add(PC.getInstance().getContent(),"1");
                    firstBranchPrediction = true;
                }
            }
//======================================================================================================================
            //Decode the instruction in IR
            //In 1 cycle  process the instruction and use it to set several flags:
            CPU.getInstance().getDecoder().setInstruction(CPU.getInstance().getIR().getContent());
            CPU.getInstance().getDecoder().decode();;
            CPU.cyclePlusOne();

            //Identify the instruction and execute it.
            //The specific steps of different instructions are handled in the ISA class and its subclasses
            //Operand Fetch, Execute and Result Store will be done in specific instruction method according to the instruction.
            CPU.getInstance().getDecoder().identify();
            //if the instruntion is HALT then break the while loop
            if(hlt) break;
            //Next Instruction
            //PC++
            //get content from pc, add one in ALU, store the result in Z temporarily and send it to pc.
            if(!jump){
                CPU.getInstance().getZ().setContent(CPU.getInstance().getALU().addPC(CPU.getInstance().getPC()));
                CPU.cyclePlusOne();
                CPU.getInstance().getPC().setContent(CPU.getInstance().getZ().getContent());
                CPU.cyclePlusOne();
            }

            //==========================================================================================================
            //content of MAR is the branch address, put into BTB
            if(firstBranchPrediction){
                BranchTargetBuffer.getInstance().add(CPU.getInstance().getIR().getContent(),CPU.getInstance().getMAR().getContent());
                firstBranchPrediction = false;
            }
            if(jump){
                DirectionPredictor.getInstance().addOrUpdate(CPU.getInstance().getIR().getContent(),true);
            }else  DirectionPredictor.getInstance().addOrUpdate(CPU.getInstance().getIR().getContent(),false);

            //==========================================================================================================
        }
        CenterPaneController.setStepInformation("Execute done, press next to exit", false);
        this.end = true;
        Halt.halt();
    }
    public void sendStepInformation(){
        CenterPaneController.setStepInformation(stepInformation,false);
    }

}
