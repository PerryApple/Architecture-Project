// Arithmetic Logic Unit
public class ALU {

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
    private String complement(String tmp){
        /*
        if tmp[0] == "0", then the tmp is positive number, the complement is the true form,
        if tmp[0] == "1", then the tmp is negative number, the complement is negation of
        the true form then plus one.
         */
        if(tmp.substring(0, 1).equals("0")) {
            return tmp;
        }else{
            StringBuilder res = new StringBuilder();
            int carry = 1;
            for(int i=tmp.length()-1; i>0; i--){
                int cur = tmp.substring(i, i+1).equals("0") ? 1 : 0;
                res.append((cur + carry) % 2);
                carry = (cur + carry) / 2;
            }
            res.append(1);
            return res.reverse().toString();
        }
    }

    // the result of num1 + num2
    public String add(String num1, String num2){
        StringBuilder res = new StringBuilder();
        int carry = 0;
        num1 = complement(num1);
        num2 = complement(num2);
        for(int i = num1.length() - 1, j = num2.length() - 1; i >= 0 || j >= 0; i--, j--){
            int x = i < 0 ? 0 : num1.charAt(i) - '0';
            int y = j < 0 ? 0 : num2.charAt(j) - '0';
            res.append((x + y + carry) % 2);
            carry = (x + y + carry) / 2;
        }
        // get the complement of the result
        String ans = res.reverse().toString();
        ans = ans.substring(0, 1).equals("0") ? ans : complement(ans);
        // if the result is negative number, we should transform it to true form.
        CPU.getInstance().getZ().setContent(ans);
        return ans;
    }

    // the result of num1 - num2
    public String minus(String num1, String num2){
        // transform num1 - num2 to num1 + (-num2)
        if(num2.substring(0, 1).equals("0")){
            num2 = num2.replaceFirst("0", "1");
        }
        else if(num2.substring(0, 1).equals("1")){
            num2 = num2.replaceFirst("1", "0");
        }
        return add(num1, num2);
    }

    public String addPC(Register pc) {
    		return add(pc.getContent(), "000000000001");
    }
}
