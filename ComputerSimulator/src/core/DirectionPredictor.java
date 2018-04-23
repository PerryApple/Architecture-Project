package core;

import java.util.HashMap;

public class DirectionPredictor {
    private HashMap<String, String> predictor = new HashMap<>();

    private static final DirectionPredictor directionPredictor = new DirectionPredictor();

    private DirectionPredictor() {
    }

    public static DirectionPredictor getInstance() {
        return directionPredictor;
    }

    //check the previous branch status
    private int get(String address) {
        if (predictor.get(address).equals("11") || predictor.get(address).equals("10")) {
            return 1;
        } else if (predictor.get(address).equals("01") || predictor.get(address).equals("00")) {
            return 0;
        } else
            return -1;
    }

    //put the recent status into predictor
    public void addOrUpdate(String address, boolean isTaken) {
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
        if (address.substring(0, 3).equals("001")) {
            if (!predictor.containsKey(address)) {
                //说明是第一次遇到,not taken
                predictor.put(address, "00");
                //返回PC++
                return CPU.getInstance().getALU().add(address, "1");
            } else {
                //之前遇到过
                if (get(address) == 1) {
                    //taken
                    return BranchTargetBuffer.getInstance().getTarget(address);
                } else if (get(address) == 0) {
                    //not taken
                    return CPU.getInstance().getALU().add(address, "1");
                }
            }
        }
        return null;
    }
}
