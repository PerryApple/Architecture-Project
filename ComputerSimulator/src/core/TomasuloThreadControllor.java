package core;

import gui.controllers.UserPart4MainController;

public class TomasuloThreadControllor implements Runnable {

    public static volatile boolean flag = true;

    public void run() {
        Tomasulo.proceedTomasulo();
    }

    public static void halt() {
        flag = true;
        while (flag) {
            if (!UserPart4MainController.getOpen()) break;
        }
    }
}
