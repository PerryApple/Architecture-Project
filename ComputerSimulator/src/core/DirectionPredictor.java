package core;

import java.util.HashMap;

public class DirectionPredictor {
    private static HashMap<String, String> predictor = new HashMap<>();

    private static final DirectionPredictor directionPredictor = new DirectionPredictor();

    private DirectionPredictor() {
    }

    public static DirectionPredictor getInstance() {
        return directionPredictor;
    }

    //check the previous branch status
    public boolean get(String address) {
    		if(address.length() == 16) {
    			address = address.substring(4);
    		}
    		if(predictor.containsKey(address)) {
    			if (predictor.get(address).equals("11") || predictor.get(address).equals("10")) {
    	            return true;
    	        } else if (predictor.get(address).equals("01") || predictor.get(address).equals("00")) {
    	            return false;
    	        }
    		}
    		return false;		
    }

    //put the recent status into predictor
    public void addOrUpdate(String address, boolean isTaken) {
    		if(address.length() == 16) {
    			address = address.substring(4);
    		}
        if (predictor.containsKey(address)) {
            //already has this instruction
            if (isTaken) {
                switch (predictor.get(address)) {
                    case "00":
                        predictor.put(address, "01");
                        break;
                    case "01":
                        predictor.put(address, "10");
                        break;
                    case "10":
                        predictor.put(address, "11");
                        break;
                }
            } else {
                switch (predictor.get(address)) {
                    case "01":
                        predictor.put(address, "00");
                        break;
                    case "10":
                        predictor.put(address, "01");
                        break;
                    case "11":
                        predictor.put(address, "10");
                        break;
                }
            }
        } else {
            if(isTaken){
                predictor.put(address,"11");
            }else predictor.put(address,"00");
        }
    }

    //整合方法
    public String predict(String address) {
    		if(address.length() == 16) {
    			address = address.substring(4);
    		}
    	
        if (!predictor.containsKey(address)) {
            //说明是第一次遇到,not taken
            //predictor.put(address, "00");
            //返回PC++
            return CPU.getInstance().getALU().add(address, "000000000001");
        } else {
            //之前遇到过
            if (get(address)) {
                //taken
                return BranchTargetBuffer.getInstance().getTarget(address);
            } else if (!get(address)) {
                //not taken
                return CPU.getInstance().getALU().add(address, "000000000001");
            }
        }
        return "BP Error";
    }
    
    //Get Hash map
    public HashMap<String, String> getContent() {
    		return predictor;
    }
}
