public class Controler {
	//Singleton Pattern
    private static final Controler instance = new Controler();
    private  String stepInformation="";
    private Controler() {}
    public static Controler getInstance() {
    		return instance;
    }

    //Main process 
    public void process(){
        Halt.halt();
        //Instruction Fetch start here
        //MAR = PC
        while(!CPU.getInstance().getMemory().getContent(CPU.getInstance().getPC().getContent()).equals("0000000000000000")){
            Monitor.instructionNum++;//Monitor shows what instruction is processing
            CPU.getInstance().getMAR().setContent(CPU.getInstance().getPC().getContent());
            stepInformation=("Instruction Fetch:MAR<=PC");
            sendStepInformation();
            Halt.halt();
            CPU.cyclePlusOne();
            //uses the address in the MAR to fetch a word from cache. This fetch occurs in one cycle.
            //The word fetched from cache is placed in the Memory Buffer Register (MBR).
            //if it is a miss, extract from memory and store it in cache
            String address = CPU.getInstance().getMAR().getContent();
            String data = Cache.getInstance().getCacheLine(address);
            //it is a miss
            if(data.equals("miss")){
                data = Cache.getInstance().getIfMiss(address);
                CPU.getInstance().getMBR().setContent(data);
                stepInformation = "Instruction Fetch:miss,MBR<=Cache<=Memory[MAR]";
                sendStepInformation();
                CPU.cyclePlusOne(); //??????????????? add how many ???????????????
            }else{
                //hit , and store the data in MBR
                CPU.getInstance().getMBR().setContent(data);
                stepInformation = "Instruction Fetch:Cache hit, MBR<=Cache";
                sendStepInformation();
                CPU.cyclePlusOne();//??????????????? add how many ????????????????
            }
            /*CPU.getInstance().getMBR().setContent(CPU.getInstance().getMemory().getContent(CPU.getInstance().getMAR().getContent()));
            stepInformation=("Instruction Fetch:MBR<=Memory[MAR]");
            sendStepInformation();
            Halt.halt();
            CPU.cyclePlusOne();*/


            //Instruction decode start here
            //The contents of the Memory Buffer Register (MBR) are moved to the Instruction Register (IR).
            //IR = MBR
            CPU.getInstance().getIR().setContent(CPU.getInstance().getMBR().getContent());
            stepInformation=("Instruction Fetch:IR<=MBR");
            sendStepInformation();
            Halt.halt();
            CPU.cyclePlusOne();
            //Decode the instruction in IR
            //In 1 cycle  process the instruction and use it to set several flags:
            CPU.getInstance().getDecoder().setInstruction(CPU.getInstance().getIR().getContent());
            CPU.getInstance().getDecoder().decode();
            stepInformation=("Instruction Decode...");
            sendStepInformation();
            Halt.halt();
            CPU.cyclePlusOne();
            //Identify the instruction and execute it.
            //The specific steps of different instructions are handled in the ISA class and its subclasses
            //Operand Fetch, Execute and Result Store will be done in specific instruction method according to the instruction.
            CPU.getInstance().getDecoder().identify();

            //Next Instruction
            //PC++
            //get content from pc, add one in ALU, store the result in Z temporarily and send it to pc.
            CPU.getInstance().getZ().setContent(CPU.getInstance().getALU().addPC(CPU.getInstance().getPC()));
            stepInformation=("Next Instruction: Z <= PC++ (ALU)");
            sendStepInformation();
            Halt.halt();
            CPU.cyclePlusOne();
            CPU.getInstance().getPC().setContent(CPU.getInstance().getZ().getContent());
            stepInformation=("PC <= Z");
            sendStepInformation();
            Halt.halt();
            CPU.cyclePlusOne();/*@@@@@@@@@ cycle?@@@@@@@@@@@@@@*/
        }
    }

    public void sendStepInformation(){
        Monitor.setStepInformation(stepInformation,false);
    }

}
