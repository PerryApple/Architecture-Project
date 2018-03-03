package core;
import gui.controllers.CenterPaneController;
/*DEVID	Device
        0	Console Keyboard
        1	Console Printer
        2	Card Reader
        3-31	Console Registers, switches, etc*/

public class IOinstructions extends ISA {
    //Input Character To Register from Device, r = 0..3
    public static void IN(){
        Register r = null;
        //get the Register according to R
        switch (R){
            case "00": r = cpu.getR0(); break;
            case "01": r = cpu.getR1();break;
            case "10": r = cpu.getR2();break;
            case "11": r = cpu.getR3();break;
        }
        // c(r) <- IOmemory(DevID)
        r.setContent(IOmemory.getInstance().getContent(DevID));
        CenterPaneController.setStepInformation(r.getName()+"<-I/O",false);
        CPU.cyclePlusOne();
        Halt.halt();
    }
    
//    Output Character to Device from Register, r = 0..3
    public static void OUT(){
        Register r = null;
        //get the Register according to R
        switch (R){
            case "00": r = cpu.getR0(); break;
            case "01": r = cpu.getR1();break;
            case "10": r = cpu.getR2();break;
            case "11": r = cpu.getR3();break;
        }
        // IOmemory(DevID)<-c(r)
        IOmemory.getInstance().setContent(DevID,r.getContent());
        CenterPaneController.setStepInformation("I/0<-"+r.getName(),false);
        CPU.cyclePlusOne();
        Halt.halt();
    }
}
