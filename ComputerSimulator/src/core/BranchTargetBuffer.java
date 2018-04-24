package core;

import java.util.HashMap;
import java.util.HashSet;

public class BranchTargetBuffer {
    private HashMap<String,String> BTB = new HashMap<>();

    private static final BranchTargetBuffer branchBuffer = new BranchTargetBuffer();

    private BranchTargetBuffer(){
    }

    public static BranchTargetBuffer getInstance(){
        return branchBuffer;
    }

    //add into BTB
    public void add(String address,String target){
    		if(address.length() == 16) {
    			address = address.substring(4);
    		}
    		if(!BTB.containsKey(address)) {
    			//save in BTB
    			BTB.put(address,target);
    		}
    }

    //search
    public String getTarget(String address){
    		if(address.length() == 16) {
			address = address.substring(4);
		}
    		
        if(BTB.containsKey(address)){
            return BTB.get(address);
        }
        // cant find in BTB
        return null;
    }
    
    public HashMap getContent() {
    		return BTB;
    }

}


