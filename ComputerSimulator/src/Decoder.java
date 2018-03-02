public class Decoder {
	private String opClass;
    private String opCode;
    private String instruction;
    //data using in load and store instruction
    private String X;
    private String R;
    private String I;
    private String address;
    //Data using in arithmetic instruction
    private String RX;
    private String RY;
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
    	 	switch (op){
    	 	
    	 		//Load and Store Instructions
            case"000001":
            		opCode="LDR";
            		decodeLoadAndStore();
            		break;
            case"000010":
            		opCode="STR";
            		decodeLoadAndStore();
            		break;
            case"000011":
            		opCode="LDA";
            		decodeLoadAndStore();
            		break;
            case"100001":
            		opCode="LDX";
            		decodeLoadAndStore();
            		break;
            case"100010":
            		opCode = "STX";
            		decodeLoadAndStore();
            		break;
            		
            	//Arithmetic Instructions
            case"010000":
            		opCode = "MLT";
            		arithmetic();
            		break;
            case"010001":
            		opCode = "DVD";
            		arithmetic();
            		break;
            		
            	//Logical Instructions
            case"010010":
            		opCode = "TRR";
            		logical();
            case"010011":
            		opCode = "AND";
            		logical();
            case"010100":
            		opCode = "ORR";
            		logical();
            case"010101":
            		opCode = "NOT";
            		logical();
        }

    }
    
    //search in ISA to identify the instruction.
    public void identify(){
    		if(opClass.equals("LSI")) {
    			ISA.execute(opCode, R, X, I, address);
    		}else if(opClass.equals("AI")||opClass.equals("LI")) {
    			ISA.execute(opCode, RX, RY);
    		}
    		
    }
    
    //clear decoder
    public void clear() {
    		instruction = "0000000000000000";
    		decode();
    }
    
    //Decode instructions which belong to Load and Store
    private void decodeLoadAndStore() {
    		//LSI stands for Load and Store Instructions
    		opClass = "LSI";
    	 	R=instruction.substring(6,8);
        X=instruction.substring(8,10);
        I=instruction.substring(10,11);
        address=instruction.substring(11,16);
        Addressing.getEffectiveAddress(X,I,address);
    }
    
    private void arithmetic() {
    		//AI stands for Arithmetic Instructions
    		opClass = "AI";
    		RX = instruction.substring(6, 8);
    		RY = instruction.substring(8, 10);
    }
    
    private void logical() {
    		//LI stands for Logical Instructions
		opClass = "LI";
		RX = instruction.substring(6, 8);
		RY = instruction.substring(8, 10);
}
    
}
