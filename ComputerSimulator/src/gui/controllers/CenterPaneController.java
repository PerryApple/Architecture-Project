package gui.controllers;

import gui.Controller;
import javafx.fxml.FXML;
import core.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CenterPaneController implements Controller {
    @FXML private TextArea StepInformation;
    @FXML private TextField Address;
    @FXML private TextField Content;
    @FXML private TextArea InstructionField;
    @FXML private Text cycle;

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
    @FXML private TextField INPUT;

    private static boolean open = false;
    private boolean loadStatus = false;
    private static String stepInformation = "";
    private static boolean memoryInformation;
    static String[]  instruction={"","LDR:direct, no indexing","LDR:indirect, no indexing","STR:direct, no indexing","LDA:direct, no indexing","LDX:direct, indexing",
            "LDR:indirect,indexing", "LDX:direct, indexing","LDX:direct, indexing","STX:direct, indexing"};
    public static int instructionNum;
    Halt halt = new Halt();

    @Override
    public void initialise(){}

    // button control
    public void switchOnOff() {
        if (!open) {
            // clear all data before run
            stepInformation = "";
            CPU.getInstance().clearAll();
            CPU.getInstance().getMemory().clear();
            Decoder.getInstance().clear();
            instructionNum = 0;
            loadStatus = false;
            // show the information and reverse open status
            update();
            open = !open;
        }
        else {
            shutdown();
            open = !open;
        }
    }


    // load data
    public void loadInstruction() {
        if(open){
            if(loadStatus) {
                update();
            }else {
                BufferedReader br=null;
                FileReader fr=null;
                try{
                    String txt="instruction.txt";
                    fr=new FileReader(txt);
                    br=new BufferedReader(fr);
                    String line;
                    // flag is used to mark whether the loaded line is data or instruction
                    Boolean flag = true;
                    while((line=br.readLine())!=null){
                        String test = line.substring(0,2);
                        char xx = test.charAt(0);
                        if(line.substring(0,2).equals("//")) {
                            continue;
                        }
                        // when flag is true, the loaded content is data, put it into the memory
                            String[] contents = line.split(",");
                            CPU.getInstance().getMemory().setContent(contents[0], contents[1]);
                        }
                        // when flag is false, the loaded content is instruction, put it into the memory
                    stepInformation="Load success";
                    loadStatus = true;
                    //put the beginning address of a program into PC.
                    CPU.getInstance().getPC().setContent("000010000000");
                    update();
                    new Thread(halt).start();

                }catch (IOException e){
                    stepInformation="Load error";
                    update();
                    System.out.println(e.toString());
                }
            }
        }
    }

    public void nextStep() {
        if (open && loadStatus) {
            Halt.flag = false;

            while(!Halt.flag){

            }
            update();
        }
    }

    public void viewCache() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader()
                .getResource("gui/views/SeeCachePane.fxml"));
        Pane seeCachePane = loader.load();
        Controller controller = loader.getController();
        controller.initialise();
        Stage stage = new Stage();

        stage.setTitle("Cache");
        stage.setScene(new Scene(seeCachePane));
        stage.setResizable(false);
        stage.show();
    }

    public void Input() {
        String tmp = INPUT.getText();
        int cur = Integer.valueOf(tmp);
        System.out.println(Integer.toBinaryString(cur));
    }

    public void searchMemory() {
        String searchField = Address.getText();
        Content.setText(CPU.getInstance().getMemory().getContent(searchField));
    }

    public void singleStep() {
        Controler.getInstance().singleStep = true;
    }

    public void singleInstruction() {
        Controler.getInstance().singleStep = false;
    }

    private void update() {
        // set all the TextField in the GUI
        StepInformation.setText(stepInformation);
        if (memoryInformation) {
            Address.setText(CPU.getInstance().getMAR().getContent());
            Content.setText(CPU.getInstance().getMBR().getContent());
        }
        InstructionField.setText(instruction[instructionNum]);
        cycle.setText(String.valueOf(CPU.getCycle()));
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

    // switch off the GUI
    private void shutdown() {
        StepInformation.setText("");
        Address.setText("");
        Content.setText("");
        InstructionField.setText("");

        PC.setText("");
        MAR.setText("");
        MBR.setText("");
        IRR.setText("");
        IAR.setText("");
        IR.setText("");
        Y.setText("");
        Z.setText("");
        X1.setText("");
        X2.setText("");
        X3.setText("");
        R0.setText("");
        R1.setText("");
        R2.setText("");
        R3.setText("");
        CC.setText("");
        MFR.setText("");
        QR.setText("");
        MLR.setText("");
        PR.setText("");
        RR.setText("");
        DR.setText("");
        SRR.setText("");
        Halt.flag=true;
    }

    // used by Halt Thread to detect if need to pause
    public static boolean getOpen() {
        return open;
    }

    // used by other class to set current stepInformation and memoryInformation
    public static void setStepInformation(String stepInformation, boolean memoryInformation) {
        CenterPaneController.stepInformation = stepInformation;
        CenterPaneController.memoryInformation = memoryInformation;
    }
}
