package core;

public class Miscellaneousinstructions extends ISA {
    //stop the process
    public static void HLT(){
        Controler.getInstance().hlt = true;
    }
}
