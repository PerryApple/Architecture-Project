package gui.controllers;

import gui.Controller;
import javafx.application.Platform;
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
    @FXML private TextArea OUTPUT;
    
    private static boolean open = false;
    private boolean loadStatus = false;
    private static String stepInformation = "";
    private static boolean memoryInformation;
    static String[]  instruction={"","","","INPUT NUMBER 1","","INPUT NUMBER 2","","INPUT NUMBER 3","","INPUT NUMBER 4","","INPUT NUMBER 5","","INPUT NUMBER 6","","INPUT NUMBER 7","","INPUT NUMBER 8","","INPUT NUMBER 9","","INPUT NUMBER 10","","INPUT NUMBER 11",
    		"","INPUT NUMBER 12","","INPUT NUMBER 13","","INPUT NUMBER 14","","INPUT NUMBER 15","","INPUT NUMBER 16","","INPUT NUMBER 17","","INPUT NUMBER 18","","INPUT NUMBER 19","","INPUT NUMBER 20","","INPUT USER NUMBER","","Find The Closest Number"};
    public static int instructionNum;
    Halt halt = new Halt();
    Thread simulator = new Thread(halt);

    @Override
    public void initialise(){}

    // button control
    public void switchOnOff() {
        if (!open) {
            // clear all data before run
            stepInformation = "";
            CPU.getInstance().clearAll();
            CPU.getInstance().getMemory().clear();
            IOmemory.getInstance().clearIOmemory();
            Cache.getInstance().clearCache();
            Decoder.getInstance().clear();
            OUTPUT.setText("");
            INPUT.setText("");
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
                    CPU.getInstance().getPC().setContent("000001111110");
                    update();
                    simulator.start();

                }catch (IOException e){
                    stepInformation="Load error";
                    update();
                    System.out.println(e.toString());
                }
            }
        }
    }

    public void nextStep() {
    		if(!Controler.getInstance().end) {
    			if(open && loadStatus) {
            		Halt.flag = false;
                while(!Halt.flag){}
                update();
            	}
    		}else{
    			Platform.exit();
    			System.exit(0);
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
        String binaryInput = CPU.alignment(Integer.toBinaryString(Integer.valueOf(tmp))); 
        IOmemory.getInstance().setContent("00000", binaryInput);
        OUTPUT.setText("Input: " + binaryInput);
        INPUT.setText("");
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
        if(!IOmemory.getInstance().getContent("00001").equals("0000000000000000")) {
        		OUTPUT.setText(Integer.valueOf(IOmemory.getInstance().getContent("00001"), 2).toString());
        		
        }
        if (memoryInformation) {
            Address.setText(CPU.getInstance().getMAR().getContent());
            Content.setText(CPU.getInstance().getMBR().getContent());
        }
        if(instructionNum<instruction.length) {
        	 	InstructionField.setText(instruction[instructionNum]);
        }
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
        OUTPUT.setText("");
        INPUT.setText("");
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
