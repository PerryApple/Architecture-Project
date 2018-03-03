package core;
import gui.controllers.CenterPaneController;
//===============================================//
//            Transfer instruction               //
//===============================================//
public class TransferInstruction  extends ISA{
//    Jump If Zero:
//    If c(r) = 0, then PC <- EA
//    Else PC <- PC+1
    public static void JZ(){
        Register r = null;
        //get the Register according to R
        switch (R){
            case "00": r = cpu.getR0(); break;
            case "01": r = cpu.getR1();break;
            case "10": r = cpu.getR2();break;
            case "11": r = cpu.getR3();break;
        }
        //set PC
        if(r!=null&&r.getContent().equals("0000000000000000")){
            cpu.getPC().setContent(cpu.getIAR().getContent());
            //UI shows step information
            CenterPaneController.setStepInformation("PC<-EA",false);
            //set jump to make PC not ++
            Controler.getInstance().jump = true;
            cpu.cyclePlusOne();
            Halt.halt();
        }
    }
    //======================================//
    //*    Jump If Not Equal:               *//
    //*   If c(r) != 0, then PC <- EA       *//
    //*    Else PC <- PC + 1                *//
    //======================================//
    public static void JNE(){
        Register r = null;
        //get the Register according to R
        switch (R){
            case "00": r = cpu.getR0(); break;
            case "01": r = cpu.getR1();break;
            case "10": r = cpu.getR2();break;
            case "11": r = cpu.getR3();break;
        }
        //set PC
        if(r!=null&&!r.getContent().equals("0000000000000000")){
            cpu.getPC().setContent(cpu.getIAR().getContent());
            //UI shows step information
            CenterPaneController.setStepInformation("PC<-EA",false);
            //set jump to make PC not ++
            Controler.getInstance().jump = true;
            cpu.cyclePlusOne();
            Halt.halt();
        }
    }

//    Jump If Condition Code
//    cc replaces r for this instruction
//    cc takes values 0, 1, 2, 3 as above and specifies the bit in the Condition Code Register to check;
//    If cc bit  = 1, PC <- EA
//    Else PC <- PC + 1
    public static void JCC(){
        //get the check bit number :0 ,1 ,2 or 3
        int checkBit = Integer.valueOf(R,2);
        //check CC register
        if(cpu.getCC().getContent().charAt(checkBit)=='1'){
            cpu.getPC().setContent(cpu.getIAR().getContent());
            //UI shows step information
            CenterPaneController.setStepInformation("PC<-EA",false);
            //set jump to make PC not ++
            Controler.getInstance().jump = true;
            cpu.cyclePlusOne();
            Halt.halt();
        }
    }

//    Unconditional Jump To Address
//    PC <- EA,
//    Note: r is ignored in this instruction
    public static void JMA(){
        cpu.getPC().setContent(cpu.getIAR().getContent());
    }

//    Jump and Save Return Address:
//    R3 <- PC+1;
//    PC <- EA
//    R0 should contain pointer to arguments
//    Argument list should end with –1 (all 1s) value
    public static void JSR(){
        //R3 <- PC +1
        cpu.getR3().setContent(cpu.getPC().getContent());
        CenterPaneController.setStepInformation("R3<-PC+1",false);
        cpu.cyclePlusOne();
        Halt.halt();
        //PC <- EA
        cpu.getPC().setContent(cpu.getIAR().getContent());
        CenterPaneController.setStepInformation("PC<-EA",false);
        //set jump to make PC not ++
        Controler.getInstance().jump = true;
        cpu.cyclePlusOne();
        Halt.halt();
        //??????????????????????????????????????????????????????????????????????????????????????????????????????????
    }

//    Return From Subroutine w/ return code as Immed portion (optional) stored in the instruction’s address field.
//            R0 <-Immed; PC <- c(R3)
//    IX, I fields are ignored.
    public static void RFS(){
        //R0 <-Immed
        cpu.getR0().setContent(address);
        CenterPaneController.setStepInformation("RO<-Immed",false);
        cpu.cyclePlusOne();
        Halt.halt();
        //PC <- c(R3)
        cpu.getPC().setContent(cpu.getR3().getContent());
        CenterPaneController.setStepInformation("PC<-R3",false);
        cpu.cyclePlusOne();
        Halt.halt();
    }
//    Subtract One and Branch. R = 0..3
//    r <- c(r) – 1
//    If c(r) > 0,  PC <- EA;
//    Else PC <- PC + 1
    public static void SOB(){
        Register r = null;
        //get the Register according to R
        switch (R){
            case "00": r = cpu.getR0(); break;
            case "01": r = cpu.getR1();break;
            case "10": r = cpu.getR2();break;
            case "11": r = cpu.getR3();break;
        }
        //get the c(r)-1
        String content = Integer.toBinaryString(Integer.valueOf(r.getContent(),2)-1);
        //if length<16
        if(content.length()!=16){
            for(int i=0;i<16-content.length();i++){
                content = "0"+content;
            }
        }
        //r <- c(r) – 1
        r.setContent(content);
        CenterPaneController.setStepInformation("r<-c(r)–1",false);
        cpu.cyclePlusOne();
        Halt.halt();
        //first bit != 1 means >0
        if(r.getContent().charAt(0)!=1&&r.getContent()!="0000000000000000"){
            cpu.getPC().setContent(cpu.getIAR().getContent());
            //UI shows step information
            CenterPaneController.setStepInformation("PC<-EA",false);
            //set jump to make PC not ++
            Controler.getInstance().jump = true;
            cpu.cyclePlusOne();
            Halt.halt();
        }
    }
//    Jump Greater Than or Equal To:
//    If c(r) >= 0, then PC <- EA
//    Else PC <- PC + 1
    public static void JGE(){
        Register r = null;
        //get the Register according to R
        switch (R){
            case "00": r = cpu.getR0(); break;
            case "01": r = cpu.getR1();break;
            case "10": r = cpu.getR2();break;
            case "11": r = cpu.getR3();break;
        }
        // if c(r)>=0
        if(r.getContent().charAt(0)!=1){
            cpu.getPC().setContent(cpu.getIAR().getContent());
            //UI shows step information
            CenterPaneController.setStepInformation("PC<-EA",false);
            //set jump to make PC not ++
            Controler.getInstance().jump = true;
            cpu.cyclePlusOne();
            Halt.halt();
        }
    }
}
