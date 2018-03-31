package core;
import gui.controllers.EngineerConsoleController;
//===============================================//
//            Transfer instruction               //
//===============================================//
public class TransferInstruction  extends ISA{
	
//********************************************************************************************************************************
//    Jump If Zero:
//    If c(r) = 0, then PC <- EA
//    Else PC <- PC+1
    public static void JZ(){
        Register r = null;
        //get the Register according to R
        switch (R){
            case "00": r = cpu.getR0();	break;
            case "01": r = cpu.getR1();	break;
            case "10": r = cpu.getR2();	break;
            case "11": r = cpu.getR3();	break;
        }
        //set PC
        if(r!=null&&r.getContent().equals("0000000000000000")){
            cpu.getPC().setContent(cpu.getIAR().getContent());
            //UI shows step information
            EngineerConsoleController.setStepInformation("PC<-EA",false);
            //set jump to make PC not ++
            Controler.getInstance().jump = true;
            CPU.cyclePlusOne();
            Halt.halt();
        }
    }
    
    //JZ without single step halt
    public static void JZNHLT(){
        Register r = null;
        //get the Register according to R
        switch (R){
            case "00": r = cpu.getR0();	break;
            case "01": r = cpu.getR1();	break;
            case "10": r = cpu.getR2();	break;
            case "11": r = cpu.getR3();	break;
        }
        //set PC
        if(r!=null&&r.getContent().equals("0000000000000000")){
            cpu.getPC().setContent(cpu.getIAR().getContent());
            //set jump to make PC not ++
            Controler.getInstance().jump = true;
            CPU.cyclePlusOne();
        }
    }
  //********************************************************************************************************************************
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
            EngineerConsoleController.setStepInformation("PC<-EA",false);
            //set jump to make PC not ++
            Controler.getInstance().jump = true;
            CPU.cyclePlusOne();
            Halt.halt();
        }
    }
    
    //JNE without single step halt
    public static void JNENHLT(){
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
            //set jump to make PC not ++
            Controler.getInstance().jump = true;
            CPU.cyclePlusOne();
        }
    }

  //********************************************************************************************************************************
//    Jump If Condition Code
//    cc replaces r for this instruction
//    cc takes values 0, 1, 2, 3 as above and specifies the bit in the Condition Code Register to check;
//    If cc bit  = 1, PC <- EA
//    Else PC <- PC + 1
    public static void JCC(){

        //check CC register
        if(cpu.getCC().getContent().charAt(CC)=='1'){
            cpu.getPC().setContent(cpu.getIAR().getContent());
            //UI shows step information
            EngineerConsoleController.setStepInformation("PC<-EA",false);
            //set jump to make PC not ++
            Controler.getInstance().jump = true;
            CPU.cyclePlusOne();
            Halt.halt();
        }
    }

    //JCC without single step halt
    public static void JCCNHLT(){
        //check CC register
        if(cpu.getCC().getContent().charAt(CC)=='1'){
            cpu.getPC().setContent(cpu.getIAR().getContent());
            //set jump to make PC not ++
            Controler.getInstance().jump = true;
            CPU.cyclePlusOne();
        }
    }
    
  //********************************************************************************************************************************
//    Unconditional Jump To Address
//    PC <- EA,
//    Note: r is ignored in this instruction
    public static void JMA(){
        cpu.getPC().setContent(cpu.getIAR().getContent());
        Controler.getInstance().jump = true;
        EngineerConsoleController.setStepInformation("PC <= IAR", false);
        CPU.cyclePlusOne();
        Halt.halt();
    }

    //JMA without single step halt
    public static void JMANHLT(){
        cpu.getPC().setContent(cpu.getIAR().getContent());
        Controler.getInstance().jump = true;
        CPU.cyclePlusOne();
    }
    
  //********************************************************************************************************************************
//    Jump and Save Return Address:
//    R3 <- PC+1;
//    PC <- EA
//    R0 should contain pointer to arguments
//    Argument list should end with –1 (all 1s) value
    public static void JSR(){
        //R3 <- PC +1
    		cpu.getALU().addPC(cpu.getPC());
        cpu.getR3().setContent(cpu.getZ().getContent());
        EngineerConsoleController.setStepInformation("R3<-PC+1",false);
        CPU.cyclePlusOne();
        Halt.halt();
        //PC <- EA
        cpu.getPC().setContent(cpu.getIAR().getContent());
        EngineerConsoleController.setStepInformation("PC<-EA",false);
        //set jump to make PC not ++
        Controler.getInstance().jump = true;
        CPU.cyclePlusOne();
        Halt.halt();
    }

    //JSR without single step halt
    public static void JSRNHLT(){
        //R3 <- PC +1
		cpu.getALU().addPC(cpu.getPC());
        cpu.getR3().setContent(cpu.getZ().getContent());
        CPU.cyclePlusOne();
        //PC <- EA
        cpu.getPC().setContent(cpu.getIAR().getContent());
        //set jump to make PC not ++
        Controler.getInstance().jump = true;
        CPU.cyclePlusOne();
    }
    
  //********************************************************************************************************************************
//    Return From Subroutine w/ return code as Immed portion (optional) stored in the instruction’s address field.
//            R0 <-Immed; PC <- c(R3)
//    IX, I fields are ignored.
    public static void RFS(){
        //R0 <-Immed
        cpu.getR0().setContent(CPU.alignment(Immed));
        EngineerConsoleController.setStepInformation("RO<-Immed",false);
        CPU.cyclePlusOne();
        Halt.halt();
        //PC <- c(R3)
        cpu.getPC().setContent(cpu.getR3().getContent());
        EngineerConsoleController.setStepInformation("PC<-R3",false);
        CPU.cyclePlusOne();
        Halt.halt();
    }
    
    //RFS without single step halt.
    public static void RFSNHLT(){
        //R0 <-Immed
        cpu.getR0().setContent(CPU.alignment(Immed));
        CPU.cyclePlusOne();
        //PC <- c(R3)
        cpu.getPC().setContent(cpu.getR3().getContent());
        CPU.cyclePlusOne();
    }
    
  //********************************************************************************************************************************
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
        //get the c(Y) = 1, Z <= R - Y;
        cpu.getY().setContent("0000000000000001");
        cpu.getALU().minus(r.getContent(), cpu.getY().getContent());
        CPU.cyclePlusOne();
        EngineerConsoleController.setStepInformation("Y = 1, Z <= R - Y",false);
        Halt.halt();
        //r <- c(r) – 1
        r.setContent(cpu.getZ().getContent());
        EngineerConsoleController.setStepInformation("r <= Z",false);
        CPU.cyclePlusOne();
        Halt.halt();
        //first bit != 1 means >0
        if(r.getContent().charAt(0) != '1' &&r.getContent()!="0000000000000000"){
            cpu.getPC().setContent(cpu.getIAR().getContent());
            //UI shows step information
            EngineerConsoleController.setStepInformation("PC<-EA",false);
            //set jump to make PC not ++
            Controler.getInstance().jump = true;
            CPU.cyclePlusOne();
            Halt.halt();
        }
    }
    
    //SOB without single step halt
    public static void SOBNHLT(){
        Register r = null;
        //get the Register according to R
        switch (R){
            case "00": r = cpu.getR0(); break;
            case "01": r = cpu.getR1();break;
            case "10": r = cpu.getR2();break;
            case "11": r = cpu.getR3();break;
        }
        //get the c(Y) = 1, Z <= R - Y;
        cpu.getY().setContent("0000000000000001");
        cpu.getALU().minus(r.getContent(), cpu.getY().getContent());
        CPU.cyclePlusOne();
        //r <- c(r) – 1
        r.setContent(cpu.getZ().getContent());
        CPU.cyclePlusOne();
        //first bit != 1 means >0
        if(r.getContent().charAt(0) != '1' &&r.getContent()!="0000000000000000"){
            cpu.getPC().setContent(cpu.getIAR().getContent());
            //set jump to make PC not ++
            Controler.getInstance().jump = true;
            CPU.cyclePlusOne();
        }
    }
    
    
  //********************************************************************************************************************************
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
        if(r.getContent().charAt(0)!= '1'){
            cpu.getPC().setContent(cpu.getIAR().getContent());
            //UI shows step information
            EngineerConsoleController.setStepInformation("PC<-EA",false);
            //set jump to make PC not ++
            Controler.getInstance().jump = true;
            CPU.cyclePlusOne();
            Halt.halt();
        }
    }
    
    //JGE without single step halt.
    public static void JGENHLT(){
        Register r = null;
        //get the Register according to R
        switch (R){
            case "00": r = cpu.getR0(); break;
            case "01": r = cpu.getR1();break;
            case "10": r = cpu.getR2();break;
            case "11": r = cpu.getR3();break;
        }
        // if c(r)>=0
        if(r.getContent().charAt(0)!= '1'){
            cpu.getPC().setContent(cpu.getIAR().getContent());
            //set jump to make PC not ++
            Controler.getInstance().jump = true;
            CPU.cyclePlusOne();
        }
    }
}
