
public class ISA {
    protected static CPU cpu = CPU.getInstance();
	protected static String R;
	protected static String X;
	protected static String I;
	protected static String address;
	
	public static void execute(String op, String r, String x, String i, String addr) {
		//Store R, X, I and address bits for later use.
		R = r;
		X = x;
		I = i;
		address = addr;
		// Identify the operation code and execute in specific subclass
		//Subclass Load and Store.
		//LDR
		 if(op.equals("LDR")) {
			 LoadAndStore.LDR();
		 }
		 //STR
	     else if(op.equals("STR")){
	        LoadAndStore.STR();
	     }
		 //LDA
	     else if(op.equals("LDA")){
	        LoadAndStore.LDA();
	     }
		 //LDX
	     else if(op.equals("LDX")){
	        LoadAndStore.LDX();
	     }
		 //STX
	     else if(op.equals("STX")){
	        LoadAndStore.STX();
	     }
	}
	
	
	
}
