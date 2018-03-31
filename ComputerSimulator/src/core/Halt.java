package core;

import gui.controllers.EngineerConsoleController;
import gui.controllers.UserInterfaceController;
public class Halt implements Runnable {

    public static volatile boolean flag = true;

    public void run() {
        if (CPU.getInstance().getControler().singleStep) {
            CPU.getInstance().getControler().processSingleStep();
        } else {
            CPU.getInstance().getControler().processByInstruction();
        }
    }

    public static void halt() {
        flag = true;
        while (flag) {
            if (!EngineerConsoleController.getOpen() && !UserInterfaceController.getOpen()) break;
        }
    }
}
