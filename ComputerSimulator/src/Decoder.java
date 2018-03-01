public class Decoder {
    private String opCode;
    private String X;
    private String R;
    private String I;
    private String address;
    private String instruction;
    //Single instance for class Decoder
    private static final Decoder instance = new Decoder();
    //private constructor
    private Decoder() {
    	
    }
    // Method to get instance
    public static Decoder getInstance() {
    		return instance;
    }
    
    //get the instruction
    public void setInstruction(String instruction){
    		if(instruction.length() == 16) {
    			this.instruction=instruction;
    		}else {
    			//remain for failure process
    			this.instruction = "0000000000000000";
    		}
        
    }

    //decode instruction
    public void decode(){
    	
        String op=instruction.substring(0,6);
        R=instruction.substring(6,8);
        X=instruction.substring(8,10);
        I=instruction.substring(10,11);
        address=instruction.substring(11,16);
        
        switch (op){
            case"000001":opCode="LDR";break;
            case"000010":opCode="STR";break;
            case"000011":opCode="LDA";break;
            case"101001":opCode="LDX";break;
            case"101010":opCode="STX";break;
        }

        Addressing.getEffectiveAddress(X,I,address);

    }
    
    //search in ISA to identify the instruction.
    public void identify(){
    		ISA.execute(opCode, R, X, I, address);
    }
    
    //clear decoder
    public void clear() {
    		instruction = "0000000000000000";
    		decode();
    }
    
}
