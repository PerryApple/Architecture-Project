package gui.controllers;

import core.CPU;
import gui.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SeeRegistersPaneController implements Controller {
    @FXML private TextField PC;
    @FXML private TextField MAR;
    @FXML private TextField MBR;
    @FXML private TextField IRR;
    @FXML private TextField IAR;
    @FXML private TextField IR;
    @FXML private TextField Y;
    @FXML private TextField Z;
    @FXML private TextField X1;
    @FXML private TextField X2;
    @FXML private TextField X3;
    @FXML private TextField R0;
    @FXML private TextField R1;
    @FXML private TextField R2;
    @FXML private TextField R3;
    @FXML private TextField CC;
    @FXML private TextField MFR;
    @FXML private TextField QR;
    @FXML private TextField MLR;
    @FXML private TextField PR;
    @FXML private TextField RR;
    @FXML private TextField DR;
    @FXML private TextField SRR;

    @Override
    public void initialise(){
        updateRegisters();
    }

    public void updateRegisters() {
        PC.setText(CPU.getInstance().getPC().getContent().substring(4, 16));
        MAR.setText(CPU.getInstance().getMAR().getContent());
        MBR.setText(CPU.getInstance().getMBR().getContent());
        IRR.setText(CPU.getInstance().getIRR().getContent());
        IAR.setText(CPU.getInstance().getIAR().getContent());
        IR.setText(CPU.getInstance().getIR().getContent());
        Y.setText(CPU.getInstance().getY().getContent());
        Z.setText(CPU.getInstance().getZ().getContent());
        X1.setText(CPU.getInstance().getX1().getContent());
        X2.setText(CPU.getInstance().getX2().getContent());
        X3.setText(CPU.getInstance().getX3().getContent());
        R0.setText(CPU.getInstance().getR0().getContent());
        R1.setText(CPU.getInstance().getR1().getContent());
        R2.setText(CPU.getInstance().getR2().getContent());
        R3.setText(CPU.getInstance().getR3().getContent());
        CC.setText(CPU.getInstance().getCC().getContent());
        MFR.setText(CPU.getInstance().getMFR().getContent());
        QR.setText(CPU.getInstance().getQR().getContent());
        MLR.setText(CPU.getInstance().getMLR().getContent());
        PR.setText(CPU.getInstance().getPR().getContent());
        RR.setText(CPU.getInstance().getRR().getContent());
        DR.setText(CPU.getInstance().getDR().getContent());
        SRR.setText(CPU.getInstance().getSRR().getContent());
    }

}
