package core;

// Arithmetic Logic Unit
public class ALU {
	//This attribute is to store result which length is 32bits
	private String thirtyTwoBitsContent;
    //Instance of ALU
    private static final ALU instance = new ALU();
    //Singleton ALU constructor
    private ALU() {
    	
    }
    //Method to get ALU instance 
    public static ALU getInstance() {
    		return instance;
    }
    
    // get complement number
    private String reversePN(String tmp){
        /*
			reverse positive to negative / negative to positive
         */
            StringBuilder res = new StringBuilder();
            int carry = 1;
            for(int i=tmp.length()-1; i>=0 ; i--){
                int cur = tmp.substring(i, i+1).equals("0") ? 1 : 0;
                res.append((cur + carry) % 2);
                carry = (cur + carry) / 2;
            }
            return res.reverse().toString();
    }

    // the result of num1 + num2(num1 and num2 is complement number)
    public String add(String num1, String num2){
        StringBuilder res = new StringBuilder();
        int carry = 0;
        for(int i = num1.length() - 1, j = num2.length() - 1; i >= 0 || j >= 0; i--, j--){
            int x = i < 0 ? 0 : num1.charAt(i) - '0';
            int y = j < 0 ? 0 : num2.charAt(j) - '0';
            res.append((x + y + carry) % 2);
            carry = (x + y + carry) / 2;
        }
        String ans = res.reverse().toString();
        // get the complement of the result
      
//        ans = ans.substring(0, 1).equals("0") ? ans : complement(ans);
        // if the result is negative number, we should transform it to true form.
        if(ans.length() == 16) {
        		CPU.cyclePlusOne();
        		CPU.getInstance().getZ().setContent(ans);
        }else if(ans.length() == 32) {
        		this.thirtyTwoBitsContent = ans;
        }
        return ans;
    }

    // the result of num1 - num2
    public String minus(String num1, String num2){
        num2 = reversePN(num2);
        return add(num1, num2);
    }
    
    //Logical operations
    //this is a compare operation, the result will change the 4th bit of CC register.
    public boolean compareTwo(String num1, String num2) {
    		for(int i = 0 ; i < num1.length(); i++) {
    			if (num1.charAt(i)!=num2.charAt(i)){
    	    			CPU.getInstance().getCC().setContent("NEQ");
    				CPU.cyclePlusOne();
    				return false;
    			}
    		}
    		CPU.getInstance().getCC().setContent("EQ");
    		CPU.cyclePlusOne();
    		return true;
    }
    //This is a Logical OR operation.
    public void or(String num1, String num2) {
    		StringBuilder result = new StringBuilder();
    		for(int i = 0 ; i < num1.length(); i++) {
    			if(num1.charAt(i)=='1'||num2.charAt(i)=='1') {
    				result.append("1");
    			}else {
    				result.append("0");
    			}
    		}
    		if(result.length() == 16) {
    			//store the result in Register Z
    			CPU.getInstance().getZ().setContent(result.toString());
    			CPU.cyclePlusOne();
    		}else {
    			CPU.getInstance().getZ().setContent("ORR Result ERROR");
    		}
    }
    //This is a Logical AND operation.
    public void and(String num1, String num2) {
		StringBuilder result = new StringBuilder();
		for(int i = 0 ; i < num1.length(); i++) {
			if(num1.charAt(i)=='0'||num2.charAt(i)=='0') {
				result.append("0");
			}else {
				result.append("1");
			}
		}
		if(result.length() == 16) {
			//store the result in Register Z
			CPU.getInstance().getZ().setContent(result.toString());
		}else {
			CPU.getInstance().getZ().setContent("AND Result ERROR");
		}
    }
    //This is a Logical NOT operation.
    public void not(String num) {
		StringBuilder result = new StringBuilder();
		for(int i = 0 ; i < num.length(); i++) {
			if(num.charAt(i)=='0') {
				result.append("1");
			}else {
				result.append("0");
			}
		}
		if(result.length() == 16) {
			//store the result in Register Z
			CPU.getInstance().getZ().setContent(result.toString());
			CPU.cyclePlusOne();
		}else {
			CPU.getInstance().getZ().setContent("NOT Result ERROR");
		}
    }
    
    public String addPC(Register pc) {
    		return add(pc.getContent(), "000000000001");
    }
    
    public String get32BitsContent() {
    		return this.thirtyTwoBitsContent;
    }   
}
